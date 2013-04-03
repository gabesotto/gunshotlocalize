package server.localizer

import akka.actor._
import akka.event.Logging

import server.types._

// For database access.
import server.database.MongoDBActor
import server.database.Messages._

class LocalizationActor extends Actor {
  // Get a handle on the logging system.
  val log = Logging(context.system, this)

  def receive = {
    case Detections(s) =>
      // TODO: Class best here?
      val localizer = new Localizer()
      val localization = localizer.localize(s)
      println(localization)

      // TODO: Get host/port from config file.
      val db = context.actorOf(Props(new MongoDBActor("localhost", 27017)))
      db ! WriteLocalization(localization)
      // TODO: Is it safe to stop the actor right after sending a message?
      context.stop(db)

      // Stop the actor because it is no longer needed.
      context.stop(self)
  }
}
