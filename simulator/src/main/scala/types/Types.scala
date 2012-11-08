package simulator.types

case class Sensor(lat: Double, lon: Double)
case class Source(lat: Double, lon: Double, amp: Double)
case class SensorResult(distance: Double, delay: Double, amp: Double)
