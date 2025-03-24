/**
 * Exercise 1: Basic Stream Creation
 *
 * Task: Create a simple FS2 stream that generates a sequence of numbers
 * from 1 to 10 and prints each number to the console.
 *
 * Instructions:
 *
 * Use Stream.range to generate the sequence.
 * Use evalMap to print each number to the console.
 * Test Data and Tests:
 *
 * The stream should print numbers from 1 to 10.
 * Ensure that the numbers are printed in order.
 *
 * Hints:
 *
 * You can use Stream.range(1, 10) to create the stream.
 * Use evalMap to apply an effect to each element of the stream.
 */

import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

val numbers = Stream.range(1, 11).covary[IO]

numbers
  .evalMap(number => IO(println(number)))
  .compile
  .drain
  .unsafeRunSync()

