/**
 * Problem Description:
 *
 * Your task is to create FS2 streams from various input sources using the methods we've discussed.
 *
 * Specific Requirements:
 *
 * Create a Stream[IO, Int] that emits the numbers 10 to 15 (inclusive) using Stream.range.
 * Create a Stream[IO, String] from a List[String] containing the elements "red", "green", and "blue" using Stream.emits.
 * Create a Stream[IO, Int] from an Iterator[Int] that generates the squares of numbers from 1 to 5 using Stream.fromIterator.
 * Create a Stream[IO, Int] that emits the result of an IO[Int] computation that returns the current time in milliseconds using Stream.eval.
 * Create a Stream[IO, Int] that emits a Chunk[Int] containing the numbers 6, 7, and 8 using Stream.evalUnChunk.
 * Create a Stream[IO, String] that repeats the string "FS2" 4 times using Stream.repeat.
 * Create a Stream[IO, Int] that generates 3 random integers between 100 and 200 (inclusive) using Stream.repeatEval.
 *
 * Completion Criteria:
 *
 * Each stream should be created correctly using the specified methods and effect types.
 * The streams should emit the specified elements in the correct order.
 * The random integer stream should generate 3 unique random numbers.
 * Your code should be well-structured, readable, and follow Scala best practices.
 *
 * Hints:
 *
 * Use Stream.range(start, end + 1) for inclusive ranges.
 * Use List(...).iterator to create an Iterator.
 * Use System.currentTimeMillis() to get the current time.
 * Use Chunk(...) to create a Chunk.
 * Remember to use .covary[IO] when needed.
 * Use Random.scalaUtilRandom[IO] and .betweenInt to generate random numbers.
 */

import cats.effect.IO
import cats.effect.std.Random
import fs2.{Chunk, Stream}

import java.time.Instant
import scala.language.postfixOps

val stream1 = Stream.range(10, 16).covary[IO]

val words = List("red", "green", "blue")
val stream2 = Stream.emits(words).covary[IO]

val iterator = (1 to 5).map(x => x * x).iterator
val stream3 = Stream.fromIterator[IO](iterator, chunkSize = 1)

val stream4: Stream[IO, Long] = Stream.eval(IO(Instant.now().toEpochMilli))

val chunk = Chunk(6, 7, 8)
val stream5 = Stream.evalUnChunk(IO(chunk))

val stream6  = Stream("FS2").repeatN(4).covary[IO]

val stream7: Stream[IO, Int] = Stream
  .repeatEval(Random.scalaUtilRandom[IO])
  .evalMap(_.betweenInt(100, 201))
  .take(3)
