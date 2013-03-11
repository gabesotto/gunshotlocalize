package kmlserver.generator

case class KmlData(
  data:   Seq[PointData],
  radius: Option[Double]
)

// TODO: Date representation.
// TODO: Sensor points don't have times associated with them.
case class PointData(lat: Double, lon: Double, time: Double)
