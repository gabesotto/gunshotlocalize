package server.localizer

import server.types._
import breeze.linalg._
import scala.math._

/*
 * 1 deg lat = pi/180 * R_e
 * 1 deg lon = pi/180 * R_e * cos(lat)
 */

// TODO: Better name.
case class Something(x: Double, y: Double, t: Double)

class MyLocalizer {
  // TODO: Stolen from simulator.
  type GeoPos = (Double, Double)

  // TODO: Store elsewhere.
  val sos = 343.2

  def localize(detections: Seq[Detection]): Localization = {
    /*
    // TODO: toSeq forces this to be strict.
    val fourtuples = detections.combinations(4).toSeq
    val localizations = fourtuples map { localizeFourTuple(_) }
    localizations.head
    averageLocalizations(localizations)
    */
    localizeFourTuple(detections)
  }

  def localizeFourTuple(d: Seq[Detection]): Localization = d match {
    case Seq(d0, d1, d2, d3) =>
      val d1_norm = normalize(d0, d1)
      val d2_norm = normalize(d0, d2)
      val d3_norm = normalize(d0, d3)
      val A = DenseMatrix((Am(d1_norm, d2_norm), Bm(d1_norm, d2_norm)), (Am(d1_norm, d3_norm), Bm(d1_norm, d3_norm)))
      val B = DenseVector(-Dm(d1_norm, d2_norm), -Dm(d1_norm, d3_norm))
      val sol = A \ B
      println("sol = " + sol)
      val (lat, lon) = unnormalize(d0, sol(0), sol(1))
      Localization(0, lat, lon)
  }

  def averageLocalizations(l: Seq[Localization]): Localization = {
    ???
  }

  // TODO: Better name?
  // TODO: How do we avoid subtracting numbers that are very close
  // together? (lat/lon, time)
  // TODO: Change the 10e7 value?
  def normalize(d0: Detection, dm: Detection): Something = {
    val x = cos(dm.lat)*(dm.lon - d0.lon)*(10e7/90)
    val y = (dm.lat - d0.lat)*(10e7/90)
    Something(x, y, dm.time - d0.time)
  }

  // TODO: Better name?
  // TODO: Change the 10e7 value?
  def unnormalize(d0: Detection, x: Double, y: Double): GeoPos = {
    val lat = (90 * y)/10e7 + d0.lat
    val lon = (90 * x)/(10e7 * cos(lat)) + d0.lon
    (lat, lon)
  }

  def Am(d1: Something, dm: Something): Double = {
    ((2*dm.x)/(sos*dm.t)) - ((2*d1.x)/(sos*d1.t))
  }

  def Bm(d1: Something, dm: Something): Double = {
    ((2*dm.y)/(sos*dm.t)) - ((2*d1.y)/(sos*d1.t))
  }

  def Dm(d1: Something, dm: Something): Double = {
    sos*dm.t - sos*d1.t - ((pow(dm.x,2)+pow(dm.y,2))/(sos*dm.t)) + ((pow(d1.x,2)+pow(d1.y,2))/(sos*d1.t))
  }
}
