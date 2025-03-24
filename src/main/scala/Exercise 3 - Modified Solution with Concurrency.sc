import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

final case class DatabaseConnection(connection: String) extends AnyVal

def sleepUpTo(maxDuration: FiniteDuration): Unit =
  Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

val acquire = IO {
  val conn = DatabaseConnection("test-db")
  println(s"Acquiring connection to the database: $conn")
  sleepUpTo(1.seconds)
  conn
}

val release = (conn: DatabaseConnection) =>
  IO.println(s"Releasing connection to the database: $conn")

// Define the logging effect
def saveNumber(connection: DatabaseConnection)(n: Int): IO[Unit] = IO({
  println(f"Saving number: $n%02d to $connection.\t Time: ${LocalTime.now()}")
  sleepUpTo(4.second)
})

// Create the stream and apply the logging effect with concurrency
def numStream(connection: DatabaseConnection): Stream[IO, Unit] =
  Stream.range(1, 11)
    .covary[IO]
    .parEvalMap(5)(saveNumber(connection)) // Process up to 5 elements concurrently

Stream.bracket(acquire)(release)
  .flatMap(connection => numStream(connection))
  .compile
  .drain
  .unsafeRunSync()
