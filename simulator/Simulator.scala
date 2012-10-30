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
  // TODO: This is very wrong.
  def calcDistance(src: Source, sen: Sensor): Double = (src, sen) match {
    // TODO: Braces required here?
    case (Source(src_lat, src_lon, _), Sensor(sen_lat, sen_lon)) => {
      val x = (sen_lon - src_lon) * cos((sen_lat + src_lat)/2)
      val y = src_lat - sen_lat
      sqrt(pow(x,2) + pow(y,2)) * radius
    }
  }
}
