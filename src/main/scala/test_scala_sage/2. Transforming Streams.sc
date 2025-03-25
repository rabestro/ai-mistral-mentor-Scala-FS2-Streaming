/**
 * Task: Create an FS2 stream that:
 *
 * Generates numbers from 1 to 20.
 * Filters out numbers divisible by 3.
 * Multiplies each remaining number by 2.
 * Converts each number to a string and adds a "Processed: " prefix.
 * Prints the results.
 * Completion Criteria:
 *
 * The stream starts with numbers 1 to 20.
 * Numbers divisible by 3 are filtered out.
 * Each remaining number is multiplied by 2.
 * Each number is converted to a string with a "Processed: " prefix.
 * The final results are printed.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

val source = Stream.range(1, 21).covary[IO]

val program = source
  .filter(_ % 3 != 0)
  .map(_ * 2)
  .map(number => s"Processed: $number")
  .evalMap(IO.println)

program.compile.drain.unsafeRunSync()

