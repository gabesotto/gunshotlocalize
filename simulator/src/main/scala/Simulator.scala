package simulator

import scala.math._
import java.net._
import java.io.IOException
import simulator.config.Config
import simulator.types._

object Simulation extends App {
  val results = Config.sensors map (doCalculation(Config.source, _))
  // TODO: In the future, we would want to send data to the server as
  // the sensors hear it. This could perhaps be done in two passes.
  // In the first pass, the time delta is calculated. In the second
  // pass, the sensors send their data to the server in order of their
  // times.
  results map (sendData(_))

  def doCalculation(source: Source, sensor: Sensor): SensorData = {
    val distance = calcDistance(source, sensor);
    val time = distance / Config.speedOfSound
    SensorData(sensor.lat, sensor.lon, time)
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
      sqrt(pow(x,2) + pow(y,2)) * Config.radius
  }

  def deg2rad(deg: Double): Double = deg * Pi/180

  def sendData(res: SensorData): Unit = {
    try {
      val socket = new Socket("localhost", 8080)
      socket.getOutputStream().write(SensorData.toBytes(res))
      socket.close()
    } catch {
      case _: IOException =>
        println("Problem communicating with server.")
    }
  }
}
