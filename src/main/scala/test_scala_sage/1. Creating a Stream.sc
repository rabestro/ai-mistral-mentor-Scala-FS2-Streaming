/**
 * Task: Create an FS2 stream that:
 * Generates numbers from 1 to 10.
 *
 * Filters out even numbers.
 * Multiplies each remaining number by 3.
 * Prints the results.
 * Completion Criteria:
 *
 * The stream starts with numbers 1 to 10.
 * Only odd numbers are processed.
 * Each odd number is multiplied by 3.
 * The final results are printed.
 */
import fs2.Stream

Stream.range(1, 11)
  .filter(_ % 2 == 1)
  .map(_ * 3)
  .compile
  .toList
  .foreach(println)

/**
 * Your solution is almost correct, but there are a couple of issues:
 *
 * Effect Handling: FS2 streams need to be run within an effect type, typically IO from Cats Effect.
 * Printing Results: Instead of .foreach(println), you should use .evalMap to print each element as it is processed.
 */

import cats.effect.IO

val stream = Stream.range(1, 11)
  .filter(_ % 2 == 1)
  .map(_ * 3)
  .evalMap(num => IO(println(num)))

val program: IO[Unit] = stream.compile.drain

// Run the program
import cats.effect.unsafe.implicits.global
program.unsafeRunSync()
