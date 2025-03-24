/**
 * Exercise 3: Concurrency
 *
 * Task: Modify the previous exercise to handle multiple network requests concurrently. Use a concurrency level of 5, meaning up to 5 network requests can be handled in parallel.
 *
 * Instructions:
 *
 * Use Stream.range to generate the sequence.
 * Use evalMap with a concurrency level of 5 to simulate the network request and return the square of the number.
 * Collect the results into a list and print the list at the end.
 * Test Data and Tests:
 *
 * The stream should return a list of squared numbers: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100].
 * Ensure that the effects are handled concurrently and the results are collected in order.
 *
 * Hints:
 *
 * You can use evalMap with an explicit concurrency level to handle multiple effects in parallel.
 * Use compile.toList to collect the results into a list.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

import scala.concurrent.duration.DurationInt

val numbers = Stream.range(1, 11).covary[IO]

def networkRequest(number: Int): IO[Int] = {
  IO.sleep(1.second)
  IO(number * number)
}

numbers
  .parEvalMap(5)(networkRequest)
  .compile
  .toList
  .unsafeRunSync()
