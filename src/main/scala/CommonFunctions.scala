import scala.concurrent.duration.FiniteDuration
import scala.math.abs
import scala.util.Random

object CommonFunctions {
  def sleepUpTo(maxDuration: FiniteDuration): Unit =
    Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

}
