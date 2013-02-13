package simulator

import scala.math._
import java.net._
import java.io.IOException
import simulator.config.Config
import simulator.types._

object Simulation extends App {
  // Calculate the time of arrival for each sensor.
  val results = Config.sensors map (doCalculation(Config.source, _))

  // Send the time of arrival calculations to the server.
  results map (sendData(_))

  /*
   * Given the source, calculate the time it will take for sound to travel
   * from the source to the sensor.
   */
  def doCalculation(source: Source, sensor: Sensor): SensorData = {
    val distance = calcDistance(source, sensor);
    val time = distance / Config.speedOfSound
    SensorData(sensor.id, sensor.lat, sensor.lon, time)
  }

  /*
   * Calculate the distance between a source and a sensor.
   */
  def calcDistance(src: Source, sen: Sensor): Double = (src, sen) match {
    case (Source(src_lat, src_lon, _), Sensor(_, sen_lat, sen_lon)) =>
      calcDistance((src_lat, src_lon), (sen_lat, sen_lon))
  }

  // Where the first element is latitude, second in longitude.
  type GeoPos = (Double, Double)

  /*
   * Calculate the distance between two (lat,lon) points. This is done
   * using the equirectangular projection because the distances are
   * likely to be small and the math is easier. This produces distance
   * in meters.
   */
  def calcDistance(src: GeoPos, sen: GeoPos): Double = {
    val src_lat_rad = toRadians(src._1)
    val src_lon_rad = toRadians(src._2)
    val sen_lat_rad = toRadians(sen._1)
    val sen_lon_rad = toRadians(sen._2)
    val x = (src_lon_rad - sen_lon_rad) * cos((src_lat_rad + sen_lat_rad)/2)
    val y = src_lat_rad - sen_lat_rad
    sqrt(pow(x,2) + pow(y,2)) * Config.radius
  }

  /*
   * Serialize the sensor data and send to the server over TCP.
   */
  def sendData(res: SensorData): Unit = {
    try {
      val socket = new Socket("localhost", 8080)
      socket.getOutputStream().write(res.toBytes)
      socket.close()
    } catch {
      case _: IOException =>
        println("Problem communicating with server.")
    }
  }
}
