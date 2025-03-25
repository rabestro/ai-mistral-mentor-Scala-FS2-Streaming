/**
 * Exercise 2: Handling Effects
 *
 * Task: Create a stream that generates a sequence of numbers from 1 to 10, but instead of printing them, return a stream of IO effects that simulate a network request for each number. Each network request should return the square of the number.
 *
 * Instructions:
 *
 * Use Stream.range to generate the sequence.
 * Use evalMap to simulate the network request and return the square of the number.
 * Collect the results into a list and print the list at the end.
 * Test Data and Tests:
 *
 * The stream should return a list of squared numbers: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100].
 * Ensure that the effects are handled correctly and the results are collected in order.
 * Hints:
 *
 * You can use IO.sleep to simulate a network delay.
 * Use evalMap to apply the effect to each element of the stream.
 * Use compile.toList to collect the results into a list.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

import scala.concurrent.duration.DurationInt

val numbers = Stream.range(1, 11).covary[IO]

def networkRequest(number: Int): IO[Int] =
  IO.sleep(5.second) *> IO(number * number)


numbers
  .evalMap(networkRequest)
  .compile
  .toList
  .unsafeRunSync()
