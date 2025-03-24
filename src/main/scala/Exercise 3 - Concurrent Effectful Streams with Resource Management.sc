/**
 * Exercise 3: Concurrent Effectful Streams with Resource Management
 *
 * Objective: Create a stream that concurrently processes elements with a logging effect and manages resources properly.
 *
 * Instructions:
 *
 * Create a stream that emits integers from 1 to 10.
 * For each integer, simulate a resource-intensive operation (e.g., reading from a file or making an HTTP request) using a logging effect.
 * Use concurrency to process multiple elements simultaneously.
 * Ensure that resources are properly acquired and released.
 * Test Data and Tests:
 *
 * Verify that the logging effect is applied to each element.
 * Ensure that the stream processes elements concurrently.
 * Check that resources are acquired and released correctly.
 *
 * Hints:
 *
 * Use Stream.bracket to manage resource acquisition and release.
 * Use .parEvalMap or .async to introduce concurrency.
 * Simulate a resource-intensive operation with a delay using IO.sleep.
 */

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
  sleepUpTo(2.second)
})

// Create the stream and apply the logging effect
def numStream(connection: DatabaseConnection) =
  Stream.range(1, 11)
    .evalMap(saveNumber(connection))

Stream.bracket(acquire)(release)
  .flatMap(connection => numStream(connection))
  .compile
  .drain
  .unsafeRunSync()

