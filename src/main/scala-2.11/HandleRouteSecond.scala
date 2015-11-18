import java.util.Calendar

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

import scala.io.Source

/**
 * Created by Admin on 15.11.2015.
 */
object HandleRouteSecond extends HttpHandler {
  var paramsJobid = Seq[(String,String)]()

  def handle(t: HttpExchange): Unit = {
    sendResponse(t)
  }

  def isFileExist(name: String): Boolean = new java.io.File(name).exists
  def readFile(file: String) : Array[Int] = {
    Source.fromFile(file).getLines().map(line => line.split("\n").map(x => x.toInt)).toArray.flatten
  }

  private def generateResponse(id: Int): Option[Jobid] ={
    PoolJob.findJobByID(id)
  }

  import org.json4s._
  import org.json4s.jackson.JsonMethods._
  import org.json4s.JsonDSL._
  val errorMessage =  ("state" -> Eexists.value) ~
    ("data" -> "[ ]")

  private def sendResponse(t: HttpExchange) {
    import org.json4s.{NoTypeHints}
    import org.json4s.jackson.JsonMethods._
    import org.json4s.jackson.Serialization
    import org.json4s.Extraction
    println (s"$HandleRouteSecond -- ${Thread.currentThread.getName()}")

    implicit val formats = Serialization.formats(NoTypeHints)

    val id = Option(paramsJobid.head._2.toInt)
    val answer: String = id match {
      case Some(x) => {
        val jobid = generateResponse(x)
        jobid match {
          case Some(job) => {
            val fileName = job.id.toString
            if (isFileExist(fileName)) {
              val data = readFile(fileName)
              if (job.state equals Ready.value) {
                job.data = data.toSeq
              }
            }
            println (s"   ${Calendar.getInstance().getTime()} : data is OK")
            pretty(render(Extraction.decompose(job)))
          }
          case None => {
            println (s"   ${Calendar.getInstance().getTime()} : wrong Job, data is not found")
            pretty(render(errorMessage))
          }
        }
      }
      case _ => {
        println (s"   ${Calendar.getInstance().getTime()} : bad params")
        pretty(render(errorMessage))
      }
    }
    println ()


    val responseBody = answer
    t.sendResponseHeaders(Ok.code, responseBody.length())
    val os = t.getResponseBody
    os.write(responseBody.getBytes)
    os.close()
  }
}
