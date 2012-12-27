package simulator.types

import java.nio._

case class Sensor(lat: Double, lon: Double)
case class Source(lat: Double, lon: Double, amp: Double)

case class SensorData(lat: Double, lon: Double, time: Double) {
  def toBytes(): Array[Byte] = {
    // Allocate space for all the bytes at once
    // Use double buffer
    val lat_buf = ByteBuffer.allocate(8)
    lat_buf.putDouble(lat)
    val lon_buf = ByteBuffer.allocate(8)
    lon_buf.putDouble(lon)
    val time_buf = ByteBuffer.allocate(8)
    time_buf.putDouble(time)
    Array(lat_buf.array(), lon_buf.array(), time_buf.array()).flatten
  }
}
