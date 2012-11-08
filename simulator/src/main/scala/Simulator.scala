package simulator

import scala.math._

import simulator.config.Config
import simulator.types._

object Simulation extends App {
  val speedOfSound = Config.speedOfSound
  val radius = Config.radius

  val results = Config.sensors map (doCalculation(Config.source, _))
  println(results)

  def doCalculation(source: Source, sensor: Sensor): SensorResult = {
    val distance = calcDistance(source, sensor);
    val time = distance / speedOfSound
    val amp = source.amp * exp(-1.32 * distance)
    SensorResult(distance, time, amp)
  }

  // Calculate the distance between two (lat,lon) points. This is done
  // using the equirectangular projection because the distances are
  // likely to be small and the math is easier. This produces distance
  // in meters.
  def calcDistance(src: Source, sen: Sensor): Double = (src, sen) match {
    case (Source(src_lat, src_lon, _), Sensor(sen_lat, sen_lon)) =>
      val src_lat_rad = deg2rad(src_lat)
      val src_lon_rad = deg2rad(src_lon)
      val sen_lat_rad = deg2rad(sen_lat)
      val sen_lon_rad = deg2rad(sen_lon)
      val x = (src_lon_rad - sen_lon_rad) * cos((src_lat_rad + sen_lat_rad)/2)
      val y = src_lat_rad - sen_lat_rad
      sqrt(pow(x,2) + pow(y,2)) * radius
  }

  def deg2rad(deg: Double): Double = deg * Pi/180
}
