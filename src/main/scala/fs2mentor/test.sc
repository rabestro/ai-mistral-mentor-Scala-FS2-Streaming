import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{DurationInt, DurationLong, FiniteDuration}
import scala.util.Random

val x = 1.second.toNanos + Random.nextLong(2.second.toNanos)

val y = FiniteDuration(1.second.toNanos + Random.nextLong(2.second.toNanos), TimeUnit.NANOSECONDS)

