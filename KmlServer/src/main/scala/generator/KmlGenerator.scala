package kmlserver.generator

import scala.xml.Node

import kmlserver.database._

object KmlGenerator {
  def gunshotKml(p: Map[String, String])(implicit db: DatabaseQuerier) = {
    val query = GunshotDatabaseQuery(
      p.get("startDate"),
      p.get("endDate"),
      p.get("lat"),
      p.get("lon"),
      p.get("radius"),
      p.get("count")
    )
    // TODO: What if it fails?
    db.doQuery(query) match {
      case Some(kmlData) => generateKml(kmlData)
    }
  }

  def sensorKml(p: Map[String, String])(implicit db: DatabaseQuerier) = {
    val query = SensorDatabaseQuery(
      p.get("lat"),
      p.get("lon"),
      p.get("radius")
    )
    // TODO: What if it fails?
    db.doQuery(query) match {
      case Some(kmlData) => generateKml(kmlData)
    }
  }

  def generateKml(k: KmlData) = {
    // Can't do XML processing instructions.
    //<?xml version="1.0" encoding="UTF-8"?>
    // TODO: Add expiration tag.
    // TODO: Add time of event.
    <kml xmlns="http://www.opengis.net/kml/2.2">
      <Document>
        {k.data map { obj  =>
          <Placemark>
            <name>Detection</name>
            <Point>
              <coordinates>{obj.lon},{obj.lat},0</coordinates>
            </Point>
          </Placemark>
        }}
      </Document>
    </kml>
  }

  // TODO: fix feature warning, implicitConversions
  implicit def optstr2optdouble(i: Option[String]): Option[Double] = {
    i map (_.toDouble)
  }

  // TODO: fix feature warning, implicitConversions
  implicit def optstr2optint(i: Option[String]): Option[Int] = {
    i map (_.toInt)
  }

  /*
  implicit def optstr2optdate(i: Option[String]): Option[Something] = {
  }
  */
}
