import scala.collection.immutable.Vector
import scala.collection.JavaConverters._
import scala.math._
import java.lang.Integer

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigList

case class Sensor(lat: Double, lon: Double)
case class Source(lat: Double, lon: Double, amp: Double)
case class SensorResult(distance: Double, delay: Double, amp: Double)

object Simulation extends App {
  val config = ConfigFactory.load("simulation.conf")

  // Speed of Sound (m/s)
  // TODO: Get from config.
  val speedOfSound = 343.2

  // Radius of the Earth (m)
  // TODO: Get from config.
  val radius = 6371000

  // Should multiple sources be allowed?
  val sources = config.getList("sources").asScala.toList map {x =>
    val y = x.unwrapped().asInstanceOf[java.util.HashMap[String, Integer]]
    val lat = y.get("lat").toDouble
    val lon = y.get("lon").toDouble
    val amp = y.get("amp").toDouble
    Source(lat, lon, amp)
  }
  val sensors = config.getList("sensors").asScala.toList map {x =>
    val y = x.unwrapped().asInstanceOf[java.util.HashMap[String, Integer]]
    val lat = y.get("lat").toDouble
    val lon = y.get("lon").toDouble
    Sensor(lat, lon)
  }

  // This will allow for multiple sensors, but probably not in the
  // correct way. In order for multiple sources to be modeled corectly,
  // we must introduce the notion of when the source occurs.
  // TODO: Would this look better if translated into map/flapMap calls?
  val results = for (source <- sources;
                     sensor <- sensors) yield doCalculation(source, sensor)
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
