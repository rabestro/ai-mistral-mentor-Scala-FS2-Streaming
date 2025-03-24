import CommonFunctions.sleepUpTo
import DatabaseConnection.{acquire, release}
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.time.LocalTime
import scala.concurrent.duration.DurationInt
import scala.util.Random

/**
 * Exercise 5: Error Recovery and Retries in FS2 Streams
 *
 * Objective: Create a stream that retries failed operations a specified number of times before giving up.
 *
 * Instructions:
 *
 * Create a stream that emits integers from 1 to 10.
 * Simulate an operation that fails randomly for some elements.
 * Implement a retry mechanism that retries the operation up to 3 times before giving up.
 * Log the number of retry attempts and handle the final failure gracefully.
 * Test Data and Tests:
 *
 * Verify that the retry mechanism works as expected.
 * Ensure that the stream logs the number of retry attempts.
 * Check that the stream handles the final failure gracefully and continues processing the remaining elements.
 *
 * Hints:
 *
 * Use a combination of handleErrorWith and a loop or recursion to implement retries.
 * Log the retry attempts and handle the final failure using handleErrorWith.
 */

object Solution5 extends IOApp {
  // Define the effect with 50% error rate
  private def saveNumber(connection: DatabaseConnection)(number: Int): IO[Unit] = IO({
    println(f"Saving number: $number%02d to $connection.\t Time: ${LocalTime.now()}")
    sleepUpTo(4.second)
    // Introducing an error in the effect (50% rate)
    if (Random.nextBoolean())
      throw new RuntimeException(s"Error processing the number $number")
    println(s"✅Successfully processed number $number")
  })

  private def saveWithRetry(connection: DatabaseConnection)(number: Int, attempts: Int = 3): IO[Unit] = {
    saveNumber(connection)(number).handleErrorWith {
      case _: RuntimeException if attempts > 1 =>
        IO.println(s"⚠️Retrying ($attempts left) for number $number...") *>
          saveWithRetry(connection)(number, attempts - 1)
      case e: RuntimeException =>
        IO.println(s"⛔Error encountered: ${e.getMessage}")
    }
  }

  // Create the stream and apply the logging effect with concurrency and error handling
  private def numStream(connection: DatabaseConnection): Stream[IO, Unit] =
    Stream.range(1, 11)
      .covary[IO]
      .parEvalMap(5)(number => saveWithRetry(connection)(number)) // Process up to 5 elements concurrently

  override def run(args: List[String]): IO[ExitCode] =
    Stream.bracket(acquire)(release)
      .flatMap(connection => numStream(connection))
      .compile
      .drain
      .as(ExitCode.Success)
}
