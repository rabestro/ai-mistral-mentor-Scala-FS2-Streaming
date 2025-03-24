import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

/**
 * Exercise 4: Error Handling in FS2 Streams
 *
 * Objective: Create a stream that processes elements and handles errors gracefully.
 *
 * Instructions:
 *
 * Create a stream that emits integers from 1 to 10.
 * Introduce a simulated error for one of the elements (e.g., throw an exception for a specific number).
 * Handle the error using FS2's error-handling mechanisms.
 * Ensure that the stream continues processing the remaining elements after encountering an error.
 * Test Data and Tests:
 *
 * Verify that the error is handled and logged appropriately.
 * Ensure that the stream continues processing after the error.
 * Check that all elements, except the one with the error, are processed successfully.
 * Hints:
 *
 * Use Stream.eval or Stream.emit to introduce errors.
 * Use handleErrorWith or attempt to handle errors in the stream.
 * Log the error message and continue processing the remaining elements.
 */
object Solution4 extends IOApp {
  def sleepUpTo(maxDuration: FiniteDuration): Unit =
    Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

  val acquire: IO[DatabaseConnection] = IO {
    val conn = DatabaseConnection("test-db")
    println(s"Acquiring connection to the database: $conn")
    sleepUpTo(1.seconds)
    conn
  }

  val release: DatabaseConnection => IO[Unit] = (conn: DatabaseConnection) =>
    IO.println(s"Releasing connection to the database: $conn")

  // Define the effect with error handling
  def saveNumber(connection: DatabaseConnection)(number: Int): IO[Unit] = IO({
    println(f"Saving number: $number%02d to $connection.\t Time: ${LocalTime.now()}")
    sleepUpTo(4.second)
    // Introducing an error in the effect
    if (number % 3 == 0) throw new RuntimeException(s"Error processing the number $number")
    else println(s"Successfully processed number $number")
  }).handleErrorWith { case e: RuntimeException =>
    IO.println(s"Error encountered: ${e.getMessage}")
  }

  // Create the stream and apply the logging effect with concurrency and error handling
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
