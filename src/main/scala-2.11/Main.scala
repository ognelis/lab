/**
 * Created by Admin on 14.11.2015.
 */
import java.io.{InputStream, OutputStream}
import java.net.InetSocketAddress

import org.json4s.Extraction
import org.json4s.jackson.JsonMethods._

import java.util.concurrent.Executors
import scala.collection.mutable.Seq
import scala.concurrent._

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import scala.io.Source
import scala.reflect.macros._
import scala.util.Random

object SimpleHttpServer extends App {
    val httpServerExecutor = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
    val workerExecutor     = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
    val httpServerPort : Int  = 8888
    val ipSocetAddress : InetSocketAddress = new InetSocketAddress(httpServerPort)
    //This is the maximum number of queued incoming connections to allow on the listening socket.
    val maximumBackLog : Int  = 0
    val server = HttpServer.create(ipSocetAddress, maximumBackLog)
    val path = server.createContext("/",  RootHandler)
    server.setExecutor(httpServerExecutor)
    server.start()

    println("************************")
    println("*Hit any key to exit...*")
    println("************************")
    System.in.read()
    server.stop(0)
    def message  = (Thread.currentThread.getName() + "\n")
}
