package server

import java.net.InetSocketAddress
import akka.actor._
import akka.event.Logging

import server.types._

class TcpServer(port: Int) extends Actor {
  import IO._

  // Get a handle on the logging system.
  val log = Logging(context.system, this)

  override def preStart {
    // Start listening for incoming TCP connections.
    IOManager(context.system).listen(new InetSocketAddress(port))
  }

  def receive = {
    case NewClient(server) =>
      // Someone has connected to the server.
      server.accept()
    case Read(handle, bytes) =>
      // A sensor has sent us some data.
      context.parent ! DetectionData(bytes)
  }
}
