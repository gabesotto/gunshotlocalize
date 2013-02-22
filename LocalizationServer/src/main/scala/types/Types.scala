package server.types

case class Detection(id: Int, lat: Double, lon: Double, time: Double)

// TODO: Does this need to be wrapped?
case class Detections(d: Seq[Detection])

// TODO: How should time be represented?
case class Localization(time: Double, lat: Double, lon: Double)

// TODO: Move elsewhere?
// TODO: Trait or something else?
/*
 * Container for information about coordinate transformation.
 * Used with a Reader monad.
 */
trait CoordInfo {
}
