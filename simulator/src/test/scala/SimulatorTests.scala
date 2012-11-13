import simulator._
import simulator.types._
import org.scalatest.FunSuite

class SimulatorTests extends FunSuite {
  test("A source and sensor at the same spot have zero distance.") {
    // TODO: This isn't ideal, because we have to care about the other
    // irrelevant aspects of the source and sensor.
    val source = Source(1, 2, 3)
    val sensor = Sensor(1, 2)
    val distance = Simulation.calcDistance(source, sensor)
    assert(distance === 0)
  }
}
