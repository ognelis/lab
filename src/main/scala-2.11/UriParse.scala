/**
 * Created by Admin on 15.11.2015.
 */

import java.util._
import java.util.concurrent.ForkJoinPool

object UriParse {
  def uriQueryParse(queries: String): Seq[(String,String)] = {
    var params = Seq[(String,String)]()
    for {querie <- queries.split("&")} {
      val pair : Array[String] = querie.split("=")
      val param = if  (pair.length > 1)
        (pair(0),pair(1))
      else
        (pair(0),"")
      params = params :+ param
    }
    params
  }

  def compareQueryParams(params: Seq[(String,String)],routeQueryParams: Seq[(String,String)]): Boolean = {
    val result = if (routeQueryParams.length == params.length) {
      routeQueryParams.zip(params).forall(x => x._1._1 equals  x._2._1)
    } else false
    result
  }

}
