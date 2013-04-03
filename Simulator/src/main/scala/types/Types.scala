package simulator.types

import java.nio._

case class Sensor(id: Int, lat: Double, lon: Double)
case class Source(lat: Double, lon: Double, amp: Double)

case class SensorData(id: Int, lat: Double, lon: Double, time: Double) {
  def toBytes(): Array[Byte] = {
    /*
     * Message layout:
     * - message ID (byte)
     * - sensor ID  (int)
     * - latitude   (double)
     * - longitude  (double)
     * - time       (double)
     * Total size is 29 bytes.
     */
    val message = ByteBuffer.allocate(29)

    // Message ID is 0 for gunshot. The other values are unused.
    message.put(0.toByte)
    message.putInt(id)
    message.putDouble(lat)
    message.putDouble(lon)
    message.putDouble(time)
    message.array()
  }
}
