
/**
 * Problem Description:
 *
 * Your task is to create and process FS2 streams concurrently using parEvalMap, parEvalMapUnordered, merge, and concurrently.
 *
 * Specific Requirements:
 *
 * Create a Stream[IO, Int] that emits the numbers 1 to 5.
 * Create a function process(i: Int): IO[String] that simulates an I/O-bound operation
 * by sleeping for 100 milliseconds and returning a string like "Processed: $i".
 *
 * - Use parEvalMap to process the numbers from the stream concurrently
 * with a maximum of 2 concurrent evaluations. Print the results to the console.
 * - Use parEvalMapUnordered to process the numbers from the stream concurrently
 * with a maximum of 2 concurrent evaluations. Print the results to the console.
 * - Create a Stream[IO, String] that emits the letters "A", "B", and "C".
 * - Use merge to merge the stream of letters with the stream of processed numbers from step 3. Print the merged stream to the console.
 * - Use concurrently to run the stream of letters and the stream of processed numbers from step 3 concurrently. Print the results to the console.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

import scala.concurrent.duration.DurationInt

val intStream: Stream[IO, Int] = Stream.range(1, 6).covary[IO]
val strStream: Stream[IO, String] = Stream.emits('A' to 'C').map(_.toString).covary[IO]

def process(i: Int): IO[String] =
  IO.println(s"🔄 $i") >> IO.sleep(100.millis) >> IO(s"✅ Processed: $i")

val processedStream = intStream.parEvalMap(2)(process)

processedStream
  .evalMap(IO.println)
  .compile.drain.unsafeRunSync()

intStream
  .parEvalMapUnordered(2)(process)
  .evalMap(IO.println)
  .compile.drain.unsafeRunSync()

val mergedStream = processedStream merge strStream

mergedStream
  .evalMap(IO.println)
  .compile.drain.unsafeRunSync()

val concurrentStream = strStream concurrently processedStream

concurrentStream.evalMap(IO.println).compile.drain.unsafeRunSync()

