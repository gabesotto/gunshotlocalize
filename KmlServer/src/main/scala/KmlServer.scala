package kmlserver

import com.mongodb.casbah.Imports._
import spray.http.MediaTypes._
import spray.routing.SimpleRoutingApp

import kmlserver.generator.KmlGenerator
import kmlserver.database.MongoDbQuerier

object KmlSever extends App with SimpleRoutingApp {
  implicit val db = MongoDbQuerier

  // TODO: What does the interface argument mean?
  // TODO: Lots of code duplication here.
  startServer(interface = "localhost", port = 8080) {
    path("gunshot") {
      get {
        parameterMap { parameters =>
          respondWithMediaType(`application/vnd.google-earth.kml+xml`) {
            complete(KmlGenerator.gunshotKml(parameters))
          }
        }
      }
    } ~ path("sensor") {
      get {
        parameterMap { parameters =>
          respondWithMediaType(`application/vnd.google-earth.kml+xml`) {
            complete(KmlGenerator.sensorKml(parameters))
          }
        }
      }
    }
  }
}
