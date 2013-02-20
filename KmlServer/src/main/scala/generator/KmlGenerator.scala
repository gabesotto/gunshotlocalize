package kmlserver.generator

import com.mongodb.casbah.Imports.DBObject
import scala.xml.Node

object KmlGenerator {
  implicit object MongoDbKmlGenerator extends KmlGenerator[Seq[DBObject]] {
    def toKml(stuff: Seq[DBObject]): Node = {
      // Can't do XML processing instructions.
      //<?xml version="1.0" encoding="UTF-8"?>
      // TODO: Add expiration tag.
      <kml xmlns="http://www.opengis.net/kml/2.2">
        {stuff map { obj  =>
          <Placemark>
            <name>Detection</name>
            <Point>
              <coordinates>{obj.get("lat")},{obj.get("lon")},0</coordinates>
            </Point>
          </Placemark>
        }}
      </kml>
    }
  }

  def generateKml[T](t: T)(implicit gen: KmlGenerator[T]) = {
    gen.toKml(t)
  }
}

trait KmlGenerator[T] {
  def toKml(t: T): Node
}
