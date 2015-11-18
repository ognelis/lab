/**
 * Created by Admin on 15.11.2015.
 */

sealed abstract class HttpRequestMethods
{
  def method: String = this match {
    case Get     => "GET"
    case Options => "OPTIONS"
    case Head    => "HEAD"
    case Post    => "POST"
    case _       => ""
  }
}


case object Get extends HttpRequestMethods
case object Options extends HttpRequestMethods
case object Post extends  HttpRequestMethods
case object Head extends HttpRequestMethods



