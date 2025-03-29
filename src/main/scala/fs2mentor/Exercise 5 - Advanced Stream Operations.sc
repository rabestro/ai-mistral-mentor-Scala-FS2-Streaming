/**
 * Exercise 5: Advanced Stream Operations
 *
 * Task: Create a stream that generates a sequence of numbers from 1 to 100,
 * but instead of printing them, return a stream of IO effects that simulate a network request for each number.
 * Each network request should return the square of the number.
 * Additionally, introduce a random delay for each network request to simulate variability in network latency.
 *
 * Instructions:
 *
 * Use Stream.range to generate the sequence.
 * Use evalMap to simulate the network request with a random delay and return the square of the number.
 * Collect the results into a list and print the list at the end.
 * Handle errors gracefully and ensure that the stream continues processing even if some network requests fail.
 *
 * Test Data and Tests:
 *
 * The stream should return a list of squared numbers: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100, ..., 10000].
 * Ensure that the effects are handled correctly, and the results are collected in order.
 * Simulate network request failures for some numbers and handle them gracefully.
 *
 * Hints:
 *
 * You can use IO.sleep with a random duration to simulate variable network latency.
 * Use evalMap to apply the effect to each element of the stream.
 * Use compile.toList to collect the results into a list.
 * Use attempt to handle errors gracefully.
 */

import resource_example.DatabaseConnection.{acquire, release}
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Random

def randomDuration(min: FiniteDuration, max: FiniteDuration): FiniteDuration =
  FiniteDuration(min.toNanos + Random.nextLong((max - min).toNanos), TimeUnit.NANOSECONDS)

def networkRequest(connection: resource_example.DatabaseConnection)(number: Int): IO[Int] = for {
  _ <- IO.println(s"Requesting $connection for number $number")
  _ <- IO.sleep(randomDuration(min = 1.second, max = 4.seconds))
  square =
    if (Random.nextBoolean())
      throw new RuntimeException(s"Error processing the number $number")
    else number * number
  _ <- IO.println(s"✅Successfully processed number $number")
} yield (square)

def requestWithRetry(connection: resource_example.DatabaseConnection)(number: Int): IO[Int] = {
  networkRequest(connection)(number).attempt.map {
    case Right(value) => value
    case Left(e) => {
      println(s"⛔Error encountered: ${e.getMessage}")
      -1
    }
  }
}

def dataStream(connection: resource_example.DatabaseConnection) =
  Stream.range(1, 11)
    .covary[IO]
    .parEvalMap(5)(requestWithRetry(connection))

Stream.bracket(acquire)(release)
  .flatMap(connection => dataStream(connection))
  .compile
  .toList
  .unsafeRunSync()
