import scala.collection.immutable.Vector
import scala.math._

// TODO: Is this really the way it works?
abstract class Point(lat: Double, lon: Double)
case class Sensor(lat: Double, lon: Double) extends Point(lat, lon)
case class Source(lat: Double, lon: Double, amp: Double) extends Point(lat, lon)

case class SensorResult(distance: Double, delay: Double, amplitude: Double)

object Simulator extends App {
  // TODO: Do IO stuff to get the information.

  // Speed of Sound (m/s)
  val speedOfSound = 343.2

  // Radius of the Earth (m)
  // TODO: Check value.
  val radius = 6371000

  // lat, lon, amplitude
  val source = Source(0, 0, 0)

  // To execute in parallel, add '.par' to the end of the line.
  val sensors = Vector(Sensor(0, 0))

  val results = sensors.map(doCalculation _)
  println(results)

  def doCalculation(sensor: Sensor): SensorResult = {
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
