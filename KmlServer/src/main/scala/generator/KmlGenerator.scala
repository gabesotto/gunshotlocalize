package kmlserver.generator

import com.mongodb.casbah.MongoCursor
import scala.xml.Node

object KmlGenerator {
  implicit object MongoDbKmlGenerator extends KmlGenerator[MongoCursor] {
    def toKml(obj: MongoCursor): Node = {
      // Can't do XML processing instructions.
      //<?xml version="1.0" encoding="UTF-8"?>
      <kml xmlns="http://www.opengis.net/kml/2.2">
        <Placemark>
          <name>Simple placemark</name>
          <description>Attached to the ground. Intelligently places itself at the height of the underlying terrain.</description>
          <Point>
            <coordinates>40.006605,-105.263695,0</coordinates>
          </Point>
        </Placemark>
      </kml>

      /*
       * But really...
       * Need to go through each thing in the cursor and convert the
       * relevant bits into KML.
       */
    }
  }

  def generateKml[T](t: T)(implicit gen: KmlGenerator[T]) = {
    gen.toKml(t)
  }
}

trait KmlGenerator[T] {
  def toKml(t: T): Node
}
