package server.localizer

import server.types._

abstract class Localizer {
  def localize(detections: Seq[Detection]): Localization
}
