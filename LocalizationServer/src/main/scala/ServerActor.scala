package server

import server.config._

import scala.collection.mutable.Map
import akka.actor._
import akka.event.Logging

import server.localizer.LocalizationActor
import server.types._
import server.config._

// For database access.
import server.database.MongoDBActor
import server.database.Messages._

// TODO: How do you signal the actor to stop?
class ServerActor extends Actor {
  // Get a handle on the logging system.
  val log = Logging(context.system, this)

  // Spawn the TCP server.
  val tcpServer = context.actorOf(Props(new TcpServer(8080)))

  /*
   * This is a map from sensor IDs to an optional detection. If a sensor
   * has not heard anything, the value for that sensor's ID is Nothing.
   * If it has heard something, the value for that sensor's ID is a
   * detection. (The detection is wrapped in a Some constructor.)
   *
   * TODO: Map has a method "default" that could be overridden by
   * a subclass such that it returns a promise if you ask it for
   * a key that doesn't exist.
   */
  val detections: Map[Int, Option[Detection]] = Map()

  override def preStart = {
    // Initialize the detections map.
    initMap()
  }

  def receive = {
    case DetectionData(bytes) =>
      // Read data from the connection.
      // TODO: The protocol has changed.
      val buf = bytes.toByteBuffer

      val mid = buf.get()
      val sid = buf.getInt()
      val lat = buf.getDouble()
      val lon = buf.getDouble()
      val time = buf.getDouble()

      // If the message ID is 0, the sensor has heard something.
      if (mid == 0) {
        val detection = Detection(sid, lat, lon, time)

        // TODO: Should this be here, or should this be started once
        // when this actor starts?
        // TODO: Get host/port from config file.
        val db = context.actorOf(Props(new MongoDBActor("localhost", 27017)))
        db ! WriteDetection(detection)
        // TODO: Is it safe to stop the actor right after sending a message?
        context.stop(db)

        detections += (detection.id -> Some(detection))

        // Check whether everything in the map is defined.
        if (detections forall {case (_,v) => v.nonEmpty}) {
          // Spawn a child actor to do the localization.
          val localizer = context.actorOf(Props[LocalizationActor])
          val d = detections.values map {x => x.get}
          // TODO: Why do I have to convert this to a Seq?
          localizer ! Detections(d.toSeq)

          // Reset  the detections.
          initMap()
        }
      }
  }

  def initMap() = {
    detections.clear()
    Config.sensors map {x => detections += (x -> None)}
  }
}
