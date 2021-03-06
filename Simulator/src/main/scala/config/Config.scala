package simulator.config

import simulator.types._

object Config {
  // Speed of Sound (m/s)
  val speedOfSound = 343.2

  // Radius of the Earth (m)
  val radius = 6371000

  // Position of the source.
  val source = Source(40.007268, -105.264153, 160)

  // IDs and positions of the sensors.
  val sensors = Vector(
    Sensor(1, 40.007968, -105.264178),
    Sensor(2, 40.006838, -105.261126),
    Sensor(3, 40.002687, -105.261958),
    Sensor(4, 40.007361, -105.272091)
  )
}
