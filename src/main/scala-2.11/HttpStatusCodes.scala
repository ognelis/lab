/**
 * Created by Admin on 15.11.2015.
 */

sealed abstract class HttpStatusCodes {
  def code: Int = this match {
    //2xx: Success
    case Ok          => 200
    case Created     => 201
    case Accepted    => 202
    //4xx: Client Error
    case NotFound    => 404
    //501: Not Implemented
    case _           => 501
  }

}

//2xx: Success
object Ok extends HttpStatusCodes
object Created extends HttpStatusCodes
object Accepted extends HttpStatusCodes

//4xx: Client Error
object NotFound extends HttpStatusCodes

