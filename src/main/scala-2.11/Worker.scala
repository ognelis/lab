import java.io.{FileOutputStream, OutputStreamWriter, BufferedWriter, PrintWriter}
import java.net.URI
import java.nio.file.{Paths, Files}
import java.util.Calendar
import java.util.concurrent.ForkJoinPool
import scala.collection.mutable.Queue
import scala.io.Source
import scala.util.Random

/**
 * Created by Admin on 15.11.2015.
 */
object Worker {
  private var poolRequests: Queue[(JobidURL, Int)] = Queue[(JobidURL,Int)]()
  def isPoolEmpty: Boolean = poolRequests.isEmpty
  def isPoolNonEmpty: Boolean = !isPoolEmpty
  def addRequest(request: (JobidURL, Int)) = poolRequests enqueue request
  def recieveRequest(jobidurl: JobidURL, concurrency: Int): Unit = {
    println(s"Worker - ${Thread.currentThread.getName()}")
    println(s"   ${Calendar.getInstance().getTime()} : getting request ${jobidurl.id}")
    println(s"   ${Calendar.getInstance().getTime()} : Changing job ${jobidurl.id} state to ${Queued.value}")
    addRequest((jobidurl, concurrency))
    process
  }
  def downloadFile(url: String): Source = Source.fromURL(url, "UTF-8")
  def getUriFragment(uri: String): String = URI.create(uri).getFragment
  def getUriFromReqest(request: (JobidURL, Int)) = request._1.url
  def isFileExist(name: String): Boolean = new java.io.File(name).exists
//  def newFileName(name: String): String = {
//   var i       = 2;
//   var newName = name + "_1"
//       while (!isFileExist(name)) {
//        newName = s"${name}_${i.toString}"
//        i = i + 1
//       }
//    newName
//  }
  def saveFile(file: Source, name: String): String = {
    val out  = new PrintWriter(name)
        val iter = file.getLines()
        while (iter.hasNext) {
          out.print(iter.next())
        }
    out.close()
    name
  }

  def readFile(file: String) : Array[Int] = {
    Source.fromFile(file).getLines().map(line => line.split(",").map(x => x.toInt)).toArray.flatten
  }
  def process: Unit = {
    while (isPoolNonEmpty) {
      println(s"   ${Calendar.getInstance().getTime()} : Processing request")
      val request     = poolRequests.dequeue()
      val url         = getUriFromReqest(request)
      val idJob       = request._1.id
      val name        = idJob.toString
      //val urlFragment = getUriFragment(url)
      //println(urlFragment)
      println(s"   ${Calendar.getInstance().getTime()} : Downloading file: ${url}")
      val file = downloadFile(url)

      println(s"   ${Calendar.getInstance().getTime()} : file ${name} saved")
      val fileName = saveFile(file, name)


      val dataFile = readFile(fileName)
      val dataLength = dataFile.length


      val numberOfThreads = request._2
      val pool : ForkJoinPool = new ForkJoinPool(numberOfThreads)
      val throught : Int = dataLength / numberOfThreads
      val dataSort = new MergeParallel(dataFile,0,dataFile.length)
      dataSort.setTroughput(throught)


      SimpleHttpServer.httpServerExecutor.execute(new Runnable {
        println(s"   ${Calendar.getInstance().getTime()} : Changing job ${idJob} state to ${Progress.value}")
        def run(): Unit = PoolJob.changeJobState(idJob,Progress)
      })
      println(s"   ${Calendar.getInstance().getTime()} : Sorting file: ${idJob}")
      pool.invoke(dataSort)


      val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)))
      for (data <- dataSort.result) {
        writer.write(s"${data}\n")
      }
      writer.close()
      println(s"   ${Calendar.getInstance().getTime()} : data saved to file ${name}")


      SimpleHttpServer.httpServerExecutor.execute(new Runnable {
        def run(): Unit = {
          println(s"   ${Calendar.getInstance().getTime()} : Changing job ${idJob} state to ${Ready.value}")
          PoolJob.changeJobState(idJob,Ready)
        }
      })
      println(s"   ${Calendar.getInstance().getTime()} : Calling Garbage Collector")
      System.gc()
      println(s"   ${Calendar.getInstance().getTime()} : Processing done\n")
    }
  }
}
