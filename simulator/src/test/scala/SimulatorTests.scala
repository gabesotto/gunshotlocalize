import simulator._
import org.scalatest.FunSuite

/*
 * Things to test:
 * - works for positive and negative degree values
 * - works for some minimum distances
 * - works for some maximum distance
 */

class SimulatorTests extends FunSuite {
  test("A two things at the same spot have zero distance.") {
    val distance = Simulation.calcDistance((1.0,1.0), (1.0,1.0))
    assert(distance === 0)
  }
}
