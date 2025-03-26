import cats.effect.unsafe.IORuntime
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Random

/**
 * Exercise 6: Advanced Stream Operations with Complex Scenarios
 *
 * Task: Create a stream that generates a sequence of numbers from 1 to 100. Simulate network requests for each number with variable latency and random failures. Collect the results into a list, handling errors gracefully and ensuring that the stream continues processing even if some requests fail. Additionally, introduce a retry mechanism for failed requests, with a maximum of 3 retries.
 *
 * Instructions:
 *
 * Use Stream.range to generate the sequence.
 * Use evalMap with a concurrency level of 5 to simulate the network request with variable latency and random failures.
 * Introduce a retry mechanism for failed requests, with a maximum of 3 retries.
 * Collect the results into a list and print the list at the end.
 * Handle errors gracefully and ensure that the stream continues processing even if some requests fail.
 *
 * Test Data and Tests:
 *
 * The stream should return a list of results, where each result is either a squared number or an error message.
 * Ensure that the effects are handled concurrently, errors are handled gracefully, and the results are collected in order.
 *
 * Hints:
 *
 * Use an attempt to handle errors gracefully.
 * Use evalMap with an explicit concurrency level to handle multiple effects in parallel.
 * Use a recursive function or a loop to implement the retry mechanism.
 * Use compile.toList to collect the results into a list.
 */

object Solution6 extends IOApp {
  val MaxRetryAttempts = 3
  val ConcurrencyLevel = 5
  val MaxNumber = 20

  def randomDuration(min: FiniteDuration, max: FiniteDuration): FiniteDuration =
    FiniteDuration(min.toNanos + Random.nextLong((max - min).toNanos), TimeUnit.NANOSECONDS)

  def randomLatency(): IO[Unit] = IO.sleep(randomDuration(min = 1.second, max = 4.seconds))

  final case class ServerConnection(connection: String) extends AnyVal

  def acquire(server: String): IO[ServerConnection] =
    IO.println(s"Acquiring connection to $server") *> randomLatency() *> IO.pure(ServerConnection(server))

  val release: ServerConnection => IO[Unit] = (connection: ServerConnection) =>
    IO.println(s"Releasing connection to the server: $connection")

  def responseOrError(number: Int): IO[Int] =
    if (Random.nextBoolean())
      IO.raiseError(new RuntimeException(s"Error processing the number $number"))
    else
      IO.pure(number * number)

  def requestServer(connection: ServerConnection)(number: Int): IO[Int] =
    IO.println(s"...requesting $connection for number $number") *>
      randomLatency() *>
      responseOrError(number) <*
      IO.println(s"✅Successfully processed number $number")

  def requestWithRetry(connection: ServerConnection)(number: Int, attempts: Int = MaxRetryAttempts): IO[Int] =
    requestServer(connection)(number)
      .handleErrorWith {
        case _: RuntimeException if attempts > 1 =>
          IO.println(s"⚠️Retrying ($attempts left) for number $number...") *>
            requestWithRetry(connection)(number, attempts - 1)
        case e: RuntimeException =>
          IO.println(s"⛔Error encountered: ${e.getMessage}") *>
            IO.pure(0)
      }

  val source: Stream[IO, Int] = Stream.emits(1 to MaxNumber).covary[IO]

  def sourceParStream(connection: ServerConnection): Stream[IO, Int] =
    source.parEvalMap(ConcurrencyLevel)(number => requestWithRetry(connection)(number))

  override def run(args: List[String]): IO[ExitCode] = {

    val result = Stream.bracket(acquire("square-sever"))(release)
      .flatMap(connection => sourceParStream(connection))
      .compile
      .toList
      .unsafeRunSync()(IORuntime.global)

    println(result)

    IO.pure(ExitCode.Success)
  }
}
