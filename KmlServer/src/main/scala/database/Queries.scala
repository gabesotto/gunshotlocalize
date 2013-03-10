package kmlserver.database

case class GunshotDatabaseQuery(
  // TODO: date representation
  startDate: Option[Double],
  endDate:   Option[Double],
  lat:       Option[Double],
  lon:       Option[Double],
  radius:    Option[Double],
  count:     Option[Int]
)

case class SensorDatabaseQuery(
  lat:    Option[Double],
  lon:    Option[Double],
  radius: Option[Double]
)
