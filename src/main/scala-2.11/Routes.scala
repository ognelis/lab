import java.net.URI

/**
 * Created by Admin on 15.11.2015.
 */
sealed case class Routes(path : String, query : String) {}

object RouteFirst extends Routes("/","concurrency=N&sort=<url>")
object RouteSecond extends Routes("/","get=<jobid>")
object RouteDevNull extends  Routes("/","")



