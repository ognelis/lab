/**
 * Created by Admin on 15.11.2015.
 */
sealed abstract class HttpHeaders {
  def header: String = this match {
    case Allow       => "Allow"
    case ContentType => "Content-Type"
    case _       => ""
  }

}

object Allow extends HttpHeaders
object ContentType extends HttpHeaders

