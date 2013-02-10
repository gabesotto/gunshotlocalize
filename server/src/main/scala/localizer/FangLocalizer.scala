package server.localizer

import server.types._
import scala.math._

// TODO: Object or class?
class FangLocalizer extends Localizer {
  type Triplet = (Detection, Detection, Detection)
  // TODO: Should I just return new Detection objects instead?
  type LocTriplet = ((Double, Double), (Double, Double), (Double, Double))

  def localize(detections: Seq[Detection]): Localization = {
    // Break into triplets.
    val triplets = for (x <- detections;
                        y <- detections;
                        z <- detections) yield (x, y, z)

    // Get the position for each pair of pairs.
    val localizations = triplets map getLocation

    // Average the positions.
    avgLocalization(localizations)
  }

  def getLocation(t: Triplet): Localization = {
    // Multiply each by the speed of sound.
    // TODO: Better way to do this? (Can't map.)
    val R = computeTODA(t) match {
      case (a: Double, b: Double) => (a*343, b*343)
    }

    val coords = getCoordinates(t)

    // TODO: Why bother computing these? They will always be zero.
    val x1 = coords._1._1
    val y1 = coords._1._2

    val x2 = coords._2._1
    // TODO: Why bother computing y? It should always be zero.
    val y2 = coords._2._2

    val x3 = coords._3._1
    val y3 = coords._3._2

    val g = (R._2 - (x2/R._1) - x3)/y3
    val h = (pow(x3,2) + pow(y3,2) - pow(R._2,2) + R._2 * (R._1*(1-(pow(x2/R._1,2)))))/(2*y3)
    val d = -((1-pow(x2/R._1,2))+g)
    val e = x2*((1-pow(x2/R._1,2)))-2*g*h
    val f = -(pow(R._1,2)/4)*pow((1-pow(x2/R._1,2)),2)-pow(h,2)

    // TODO: Need to determine whether this is plus or minus.
    val x = (sqrt(pow(e,2)-4*d*f)-e)/(2*d)
    val y = g * x + h

    // TODO: Where does time come from?
    val time = 0

    // TODO: This needs to be turned into a lat/lon.
    Localization(time, x, y)
  }

  def avgLocalization(l: Seq[Localization]): Localization = {
    val lats = l map (_.lat)
    val lons = l map (_.lon)
    Localization(0, lats(0), lons(0))
  }

  // TODO: This could probably be done in a better way.
  def getCoordinates(t: Triplet): LocTriplet = {
    val delta_x = t._1.lon
    val delta_y = t._1.lat

    val x2 = t._2.lon - delta_x
    val y2 = t._2.lat - delta_y

    val x3 = t._3.lon - delta_x
    val y3 = t._3.lat - delta_y

    // Figuring out the side of the triange would normally require
    // subtraction, but since one of the points is 0,0 ...
    val theta = atan(y2/x2)

    val x2r = x2 * cos(theta) + y2 * sin(theta)
    val y2r = -x2 * sin(theta) + y2 * cos(theta)

    val x3r = x3 * cos(theta) + y3 * sin(theta)
    val y3r = -x3 * sin(theta) + y3 * cos(theta)

    ((0, 0), (x2r, y2r), (x3r, y3r))
  }

  // TODO: Need access to the original theta and the original offsets.
  def getOrigCoordinates(t: LocTriplet): LocTriplet = {
    ???
  }

  // Compute the time difference of arrival between the sensors.
  // Return (d21, d31)
  // TODO: Code repetition.
  def computeTODA(t: Triplet): (Double, Double) = {
    val d21 = (t._1, t._2) match {
      // TODO: absolute value?
      case (d1: Detection, d2: Detection) => d1.time - d2.time
    }
    val d31 = (t._1, t._3) match {
      // TODO: absolute value?
      case (d1: Detection, d3: Detection) => d1.time - d3.time
    }

    (d21, d31)
  }

  // Calculate the distance between two (lat,lon) points. This is done
  // using the equirectangular projection because the distances are
  // likely to be small and the math is easier. This produces distance
  // in meters.
  // TODO: This comes from the simulator code. They should be combined.
  def calcDistance(src: (Double, Double), sen: (Double, Double)): Double = {
    val src_lat_rad = deg2rad(src._1)
    val src_lon_rad = deg2rad(src._2)
    val sen_lat_rad = deg2rad(sen._1)
    val sen_lon_rad = deg2rad(sen._2)
    val x = (src_lon_rad - sen_lon_rad) * cos((src_lat_rad + sen_lat_rad)/2)
    val y = src_lat_rad - sen_lat_rad
    sqrt(pow(x,2) + pow(y,2)) * 6371000
  }

  // TODO: This comes from the simulator code. They should be combined.
  // TODO: The math library provides this function.
  def deg2rad(deg: Double): Double = deg * Pi/180
}
