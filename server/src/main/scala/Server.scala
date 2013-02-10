package server

import scala.collection.mutable.Map
import akka.actor._
import java.net.InetSocketAddress
import server.config._
import server.types._
//import server.localizer._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

object Server extends App {
  val system = ActorSystem("Server")
  val tcpServer = system.actorOf(Props(new TCPServer(8080)))
}

// TODO: I'd like to separate the IOManager part from the part that
// keeps track of the detections.
class TCPServer(port: Int) extends Actor {
  import IO._

  // TODO: Map has a method "default" that could be overridden by
  // a subclass such that it returns a promise if you ask it for
  // a key that doesn't exist.
  // TODO: Should the detections map be kept by a different actor?
  val detections: Map[Int, Option[Detection]] = Map()

  override def preStart {
    IOManager(context.system).listen(new InetSocketAddress(port))
    initMap()
  }

  def receive = {
    case NewClient(server) =>
      server.accept()
    case Read(handle, bytes) =>
      // TODO: Fire off a new actor to handle the data?
      // The new actor would need a way to access the detections map.
      val buf = bytes.toByteBuffer

      val mid = buf.get()
      val sid = buf.getInt()
      val lat = buf.getDouble()
      val lon = buf.getDouble()
      val time = buf.getDouble()

      // If the message ID is 0, the sensor has heard something.
      if (mid == 0) {

        // Record the detection to the database.
        // TODO: Do this better.
        val connection = MongoConnection("localhost", 27017)
        val collection = connection("gunshot")("detections")
        // TODO: Add indexes.
        // TODO: Add the server time.
        val builder = MongoDBObject.newBuilder
        builder += "id" -> sid
        builder += "lat" -> lat
        builder += "lon" -> lon
        builder += "time" -> time
        collection += builder.result

        // TODO: Need to change the simulator to send integers!
        val detection = Detection(sid, lat, lon, time)
        detections += (detection.id -> Some(detection))
        println(detections)

        // Check whether everything in the map is defined.
        /*
        if (detections forall {case (_,v) => v.nonEmpty}) {
          // Spawn a child actor to do the localization.
          val localizer = context.actorOf(Props[Localizer])
          localizer ! (detections.values map {x => x.get})

          // Reset  the detections.
          initMap()
        }
        */
      }
  }

  def initMap() = {
    detections.clear()
    Config.sensors map {x => detections += (x -> None)}
  }
}

/*
class Localizer extends Actor {
  def receive = {
    case Detections(s) =>
      // TODO: Should this be a class or object?
      val localizer = new FangLocalizer()
      val localization = localizer.localize(s)
      println(localization)

      // Write to database.
      // Can focus on getting this down well, then worry about the details
      // of localizing.
      // TODO: Do this with another actor?
      // TODO: What about the time of the event?
      // TODO: Get the host and port from a config file.
      val host = "localhost"
      val port = 27017
      val connection = MongoConnection(host, port)
      val collection = connection("test")("test")
      // TODO: How do you the geospatial indexing with Scala?
      collection.ensureIndex("loc")
      val builder = MongoDBObject.newBuilder
      builder += "time" -> localization.time
      builder += "loc" -> MongoDBObject("lat" -> localization.lat,
                                        "lon" -> localization.lon)
      collection += builder.result
  }
}
*/
