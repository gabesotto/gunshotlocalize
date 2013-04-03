package server.database

import akka.actor._
import akka.event.Logging

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

import server.types._
import server.database.Messages._

// TODO: What about failures? (DB connection, writes.)
// This class assumes that the application uses only one MongoDB instance.
class MongoDBActor(host: String, port: Int) extends Actor {
  // Get a handle on the logging system.
  val log = Logging(context.system, this)

  // Connect to the database.
  val connection = MongoConnection(host, port)

  def receive = {
    // TODO: Store sensor statistics?
    case WriteDetection(d: Detection) =>
      // TODO: Get from configuration file.
      val db_name = "gunshot"
      val coll_name = "detections"
      val collection = connection(db_name)(coll_name)

      // TODO: Add indexes.
      // TODO: Add the server time.
      val builder = MongoDBObject.newBuilder
      // TODO: ID is being replaced w/ MAC address.
      builder += "id" -> d.id
      builder += "lat" -> d.lat
      builder += "lon" -> d.lon
      builder += "time" -> d.time
      collection += builder.result

    case WriteLocalization(l: Localization) =>
      // TODO: Get from configuration file.
      val db_name = "gunshot"
      val coll_name = "localizations"
      val collection = connection(db_name)(coll_name)

      // Geospatial indexing must start with longitude first.
      collection.ensureIndex(MongoDBObject("loc" -> "2d"))

      val builder = MongoDBObject.newBuilder
      builder += "time" -> l.time
      builder += "loc" -> MongoDBObject("lon" -> l.lon, "lat" -> l.lat)
      collection += builder.result
  }

  override def postStop = {
    // Close the mongodb connection.
    connection.close()
  }
}

object Messages {
  case class WriteDetection(d: Detection)
  case class WriteLocalization(l: Localization)
}
