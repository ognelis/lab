/**
 * Created by Admin on 15.11.2015.
 */

import java.io.{OutputStream, InputStream}
import java.util.Calendar
import com.sun.net.httpserver.{HttpExchange, HttpHandler}

import scala.util.Random

object HandleRouteFirst extends HttpHandler {
  var paramsJobidUrl = Seq[(String,String)]()

  def handle(t: HttpExchange): Unit = {
    sendResponse(t)
  }

  private def displayPayload(body: InputStream): Unit ={
    println()
    println("******************** REQUEST START ********************")
    println()
    copyStream(body, System.out)
    println()
    println("********************* REQUEST END *********************")
    println()
  }

  private def copyStream(in: InputStream, out: OutputStream) {
    Iterator
      .continually(in.read)
      .takeWhile(-1 !=)
      .foreach(out.write)
  }

  def isRequestMethodLegal(t: HttpExchange) = {
    val method = t.getRequestMethod.toUpperCase
    if (method.equals(Post.method))
      true
    else
      false
  }

  private def sendResponse(t: HttpExchange) {
    import org.json4s.{NoTypeHints}
    import org.json4s.jackson.JsonMethods._
    import org.json4s.jackson.Serialization
    import org.json4s.Extraction
    println(s"HandleRouteFirst -- ${Thread.currentThread.getName()}")

    implicit val formats = Serialization.formats(NoTypeHints)

    val url           = paramsJobidUrl.last._2
    val concurrency   = paramsJobidUrl.head._2.toInt

    //////////////////////////////////////////////////
    println(s"   ${Calendar.getInstance().getTime()} : Sending Request to PoolJob\n")
    val jobidUrl = PoolJob.recieveRequest(url, concurrency)
    //////////////////////////////////////////////////
    val json = pretty(render(Extraction.decompose(jobidUrl)))


    val responseBody = json

    t.sendResponseHeaders(Ok.code, responseBody.length())
    val os = t.getResponseBody
    os.write(responseBody.getBytes)
    os.close()
  }
}
