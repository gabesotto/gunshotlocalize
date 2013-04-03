package server.types

case class Detection(id: Int, lat: Double, lon: Double, time: Double)

// TODO: Does this need to be wrapped?
case class Detections(d: Seq[Detection])

// TODO: How should time be represented?
case class Localization(time: Double, lat: Double, lon: Double)

case class DetectionData(b: akka.util.ByteString)
