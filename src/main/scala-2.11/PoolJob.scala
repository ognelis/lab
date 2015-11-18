import java.util.Calendar

import scala.collection.mutable.{Seq, Queue}
import scala.util.Random

/**
 * Created by Admin on 17.11.2015.
 */

object PoolJob {
  private var poolJobId: Seq[Jobid] = Seq[Jobid]()
  private val idGeneretor = new Random(32)
  def id = idGeneretor.nextInt()
  def addJob(job: Jobid) = {poolJobId = poolJobId :+ job}
  def findJobByID(jobId: Int) :  Option[Jobid] =  poolJobId.find(x => x.id equals jobId)
  def changeJobState(jobId: Int, state: State) = findJobByID(jobId).foreach(x => x.state = state.value)
  def fillJobData(jobId: Int, data: Array[Int]) =  findJobByID(jobId).foreach(x => x.data = data)

  def recieveRequest(url: String, concurrency: Int) : JobidURL = {
    println(s"PoolJob : ${Thread.currentThread.getName()}")
    println(s"   ${Calendar.getInstance().getTime()} : getting Request\n")

    val idJob = id
    val jobId = new Jobid(idJob,Queued.value,Array[Int]())
    addJob(jobId)
    val jobIdUrl = JobidURL(idJob,url)
    SimpleHttpServer.workerExecutor.execute(new Runnable {
      def run(): Unit = Worker.recieveRequest(jobIdUrl, concurrency)
    })
    //println(test)
    jobIdUrl
  }
}
