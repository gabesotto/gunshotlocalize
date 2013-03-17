package server

import akka.actor._

object LocalizationServer extends App {
  val system = ActorSystem("LocalizationServer")
  system.actorOf(Props[ServerActor])
}

