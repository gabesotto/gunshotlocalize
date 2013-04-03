package kmlserver.database

import kmlserver.generator._

trait DatabaseQuerier {
  def doQuery(q: GunshotDatabaseQuery): Option[KmlData]
  def doQuery(q: SensorDatabaseQuery): Option[KmlData]
}
