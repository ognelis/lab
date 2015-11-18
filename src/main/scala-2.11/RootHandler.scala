import java.io.{OutputStream, InputStream}

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

/**
 * Created by Admin on 17.11.2015.
 */
object RootHandler extends HttpHandler {

  def handle(t: HttpExchange) {
    chooseHandler(t)
    //displayPayload(t.getRequestBody)
    //sendResponse(t)
  }



  private def chooseHandler(t: HttpExchange): Unit = {
    val uri    = t.getRequestURI
    val params = UriParse.uriQueryParse(uri.getQuery)
    val path   = uri.getPath

    val route1Params = UriParse.uriQueryParse(RouteFirst.query)
    val route2Params = UriParse.uriQueryParse(RouteSecond.query)

    if (path.equals(RouteFirst.path) && UriParse.compareQueryParams(params,route1Params)) {
      HandleRouteFirst.paramsJobidUrl = params
      HandleRouteFirst.handle(t)
    } else if (path.equals(RouteSecond.path) && UriParse.compareQueryParams(params,route2Params)) {
      HandleRouteSecond.paramsJobid = params
      HandleRouteSecond.handle(t)
    } else sendResponse(t)
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

  private def sendResponse(t: HttpExchange) {
    val response = "Ack!"
    t.sendResponseHeaders(Ok.code, response.length())
    val os = t.getResponseBody
    os.write(response.getBytes)
    os.close()
  }

}