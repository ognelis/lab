/**
 * Created by Admin on 15.11.2015.
 */

import java.net.URI

import org.json4s.FullTypeHints
import org.json4s.jackson.Serialization

case class Jobid(id: Int, var state: String,var data: Seq[Int] ){
}

case class JobidURL(id : Int, url: String){}

abstract sealed class State {
  def value: String = this match {
    case Ready       => "ready"
    case Queued      => "queued"
    case Progress    => "progress"
    case Eexists     => "eexists"
    case _           =>  Eexists.value
  }
}

case object Ready extends State
case object Queued extends State
case object Progress extends State
case object Eexists extends State
