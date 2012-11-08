import akka.actor._

import java.net.InetSocketAddress

object Server extends App {
  val system = ActorSystem("Server")
  val tcpServer = system.actorOf(Props(new TCPServer(8080)))
}

class TCPServer(port: Int) extends Actor {
  import IO._

  override def preStart {
    IOManager(context.system).listen(new InetSocketAddress(port))
  }

  def receive = {
    case NewClient(server) =>
      server.accept()
    case Read(handle, bytes) =>
      val buf = bytes.toByteBuffer.asDoubleBuffer()
      val lat = buf.get()
      val lon = buf.get()
      val time = buf.get()

      println(lat)
      println(lon)
      println(time)
      println("")
  }
}
