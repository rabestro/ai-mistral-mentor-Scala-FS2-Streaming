/**
 * Hands-on Exercise: Chunk Processing
 *
 * Problem Description:
 *
 * Your task is to create and use FS2 streams and chunk-aware operators to process a stream of strings.
 *
 * Specific Requirements:
 *
 * Create a Stream[IO, String] that emits the following strings: "apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew", "kiwi", "lemon", "mango", "nectarine".
 * Convert the stream into a Stream[IO, Chunk[String]] with chunks of size 4.
 * Create a Pipe[IO, Chunk[String], String] that takes a chunk of strings, converts them to uppercase, and then concatenates them into a single string, separated by commas.
 * Apply the pipe to the stream of chunks.
 * Print the resulting concatenated strings to the console.
 *
 * Create a Stream[IO, Int] that emits the integers 1 to 20.
 * Convert the stream into a Stream[IO, Chunk[Int]] with chunks of size 5.
 * Create a Pipe[IO, Chunk[Int], Int] that calculates the sum of each chunk.
 * Apply the pipe to the stream of integer chunks.
 * Print the resulting sums to the console.
 *
 * Completion Criteria:
 *
 * The stream of strings should be correctly chunked into groups of 4.
 * The pipe should correctly convert the strings to uppercase and concatenate them.
 * The resulting concatenated strings should be printed to the console.
 * The stream of integers should be correctly chunked into groups of 5.
 * The pipe should correctly calculate the sum of each chunk.
 * The resulting sums should be printed to the console.
 * Your code should be well-structured, readable, and follow Scala best practices.
 *
 * Hints:
 *
 * Use Stream(...) to create the initial streams.
 * Use chunkN(4) and chunkN(5) to create chunks of the desired sizes.
 * Use chunk.map(_.toUpperCase).toList.mkString(", ") to process the string chunks.
 * Use chunk.foldLeft(0)(_ + _) to calculate the sum of the integer chunks.
 * Use evalMap(IO(println(...))) to print to the console.
 * Use .through to apply the pipes.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.{Chunk, Pipe, Stream}

// TASK 1

// Create a Stream
val inputStream: Stream[IO, String] = Stream(
  "apple", "banana", "cherry", "date",
  "elderberry", "fig", "grape", "honeydew",
  "kiwi", "lemon", "mango", "nectarine"
)

// Convert the stream
val chunkStream: Stream[IO, Chunk[String]] = inputStream.chunkN(4)

// Create a Pipe
val toUpperCaseJoinedPipe: Pipe[IO, Chunk[String], String] = input => input.map {
  chunk => chunk.map(_.toUpperCase).toList.mkString(", ")
}

// Apply the pipe to the stream of chunks.
// Print the resulting concatenated strings to the console.
chunkStream
  .through(toUpperCaseJoinedPipe)
  .evalMap(IO.println)
  .compile.drain.unsafeRunSync()

// TASK 2

// Create a Stream[IO, Int] that emits the integers 1 to 20
val intStream = Stream.range(1, 21).covary[IO]

// Convert the stream
val byFiveChunks = intStream.chunkN(5)

// Create a Pipe
val chunkSumPipe: Pipe[IO, Chunk[Int], Int] = input => input.map {
  chunk => chunk.foldLeft(0)(_ + _)
}

// Apply the pipe to the stream of integer chunks.
// Print the resulting sums to the console.
byFiveChunks
  .through(chunkSumPipe)
  .evalMap(IO.println)
  .compile.drain.unsafeRunSync()

