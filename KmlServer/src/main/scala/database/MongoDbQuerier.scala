package kmlserver.database

import com.mongodb.casbah.Imports._

import kmlserver.generator._

object MongoDbQuerier extends DatabaseQuerier {
  // TODO: Figure out radius units.
  // TODO: Deal with Options more idiomatically.
  def doQuery(q: GunshotDatabaseQuery): Option[KmlData] = {
    if (q.lat.isDefined && q.lon.isDefined) {
      val collection = connect("gunshot", "localizations")
      val builder = MongoDBObject.newBuilder
      if (q.radius.isDefined) {
        builder += "loc" -> MongoDBObject(
          "$within" -> MongoDBObject(
            "$center" -> MongoDBList(
              // Expecting distances in km.
              // Divide by 111.12 in order to get degrees?
              MongoDBList(q.lon.get, q.lat.get), (q.radius.get/111.12)
            )
          )
        )
      } else {
        builder += "loc" -> MongoDBObject(
          "$near" -> MongoDBList(q.lon.get, q.lat.get)
        )
      }
      // TODO: Which side (start or end) is inclusive?
      if (q.startDate.isDefined) {
        if (q.endDate.isDefined) {
          builder += "time" -> MongoDBObject(
            "$gte" -> q.startDate.get,
            "$lt" -> q.endDate.get
          )
        } else {
          builder += "time" -> MongoDBObject(
            "$gte" -> q.startDate.get
          )
        }
      } else {
        if (q.endDate.isDefined) {
          builder += "time" -> MongoDBObject(
            "$lt" -> q.endDate.get
          )
        }
      }
      
      val obj = builder.result
      // Ugh.
      var cursor: Seq[DBObject] = null
      if (q.count.isDefined) {
        cursor = collection.find(obj).limit(q.count.get).toSeq
      } else {
        cursor = collection.find(obj).toSeq
      }
      val something = cursor map { c =>
        val loc = c.get("loc").asInstanceOf[DBObject]
        PointData(
          loc.get("lat").asInstanceOf[Double],
          loc.get("lon").asInstanceOf[Double],
          c.get("time").asInstanceOf[Double]
        )
      }
      val data = KmlData(something, None)
      return Some(data)
    }
    return None
  }

  // TODO: Deal with Options more idiomatically.
  def doQuery(q: SensorDatabaseQuery): Option[KmlData] = {
    if (q.lat.isDefined && q.lon.isDefined) {
      val collection = connect("gunshot", "sensors")
      val builder = MongoDBObject.newBuilder
      if (q.radius.isDefined) {
        builder += "loc" -> MongoDBObject(
          "$within" -> MongoDBObject(
            "$center" -> MongoDBList(
              // Expecting distances in km.
              // Divide by 111.12 in order to get degrees?
              MongoDBList(q.lon.get, q.lat.get), (q.radius.get/111.12)
            )
          )
        )
      } else {
        builder += "loc" -> MongoDBObject(
          "$near" -> MongoDBList(q.lon.get, q.lat.get)
        )
      }
      val cursor = collection.find(builder.result).toSeq
      val something = cursor map { c =>
        val loc = c.get("loc").asInstanceOf[DBObject]
        PointData(
          loc.get("lat").asInstanceOf[Double],
          loc.get("lon").asInstanceOf[Double],
          0
        )
      }
      val data = KmlData(something, q.radius)
      return Some(data)
    }
    return None
  }

  // TODO: Need a way to disconnect.
  def connect(db: String, coll: String) = {
    // TODO: Get host and port from config.
    MongoClient("localhost", 27017)(db)(coll)
  }
}
