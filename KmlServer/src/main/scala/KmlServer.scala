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
  def getEverything: MongoCursor = {
    // TODO: Get host and port from config file.
    val client = MongoClient("localhost", 27017)
    // TODO: Get db name and collection name from config file.
    val collection = client("gunshot")("detections") 
    val cursor = collection.find()
    client.close()
    return cursor
  }
}

class KmlServerActor extends Actor with KmlServerThing {
  def actorRefFactory = context

  def receive = runRoute(route)
}

trait KmlServerThing extends HttpService {
  val route = path("") {
    get {
      respondWithMediaType(`application/vnd.google-earth.kml+xml`) {
        complete {
          KmlGenerator.generateKml(KmlServer.getEverything)
        }
      }
    }
  }
}
