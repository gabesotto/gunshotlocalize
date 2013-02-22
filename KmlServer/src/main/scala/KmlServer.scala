package kmlserver

import akka.actor._
import spray.can.server.SprayCanHttpServerApp
import spray.routing._
import spray.http._
import MediaTypes._

import com.mongodb.casbah.Imports._

import kmlserver.generator._

object KmlServer extends App with SprayCanHttpServerApp {
  val service = system.actorOf(Props[KmlServerActor], "kml-service")
  newHttpServer(service) ! Bind(interface = "localhost", port = 8080)

  // TODO: Move elsewhere.
  // This is here because there is some sort of conflict with Spray.
  // It tries to do some implicit typeclass magic which breaks the MongoDB
  // stuff. This may be helped by limiting what is being imported from Spray.
  def getEverything: Seq[DBObject] = {
    // TODO: Get host and port from config file.
    val client = MongoClient("localhost", 27017)
    // TODO: Get db name and collection name from config file.
    val collection = client("gunshot")("detections") 
    // This is strict! If there are too many things, it will break!
    // It would be much better to do this in a lazy way.
    val stuff = collection.find().toSeq
    client.close()
    // TODO: Don't want to have to use return statement.
    return stuff
  }
}

class KmlServerActor extends Actor with KmlServerThing {
  def actorRefFactory = context

  def receive = runRoute(route)
}

trait KmlServerThing extends HttpService {
  val route = path("a" / IntNumber) { _ =>
    get {
      respondWithMediaType(`application/vnd.google-earth.kml+xml`) {
        complete {
          KmlGenerator.generateKml(KmlServer.getEverything)
        }
      }
    }
  }
}
