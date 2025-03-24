import DatabaseConnection.{acquire, release, sleepUpTo}
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.time.LocalTime
import scala.concurrent.duration.DurationInt

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

object Solution3 extends IOApp {
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

  override def run(args: List[String]): IO[ExitCode] =
    Stream.bracket(acquire)(release)
      .flatMap(connection => numStream(connection))
      .compile
      .drain
      .as(ExitCode.Success)
}
