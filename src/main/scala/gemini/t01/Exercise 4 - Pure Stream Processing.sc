/**
 * Problem Description:
 *
 * Your task is to create and process pure FS2 streams to perform various data transformations.
 *
 * Specific Requirements:
 *
 * Create a Stream[Pure, Int] that emits the numbers 1 to 10.
 * Create a Pipe[Pure, Int, Int] that squares each number in the stream.
 * Create a Pipe[Pure, Int, String] that converts each number in the stream to its string representation, prefixed with "Squared: ".
 * Apply the pipes to the initial stream, and collect the results into a Vector[String].
 *
 * Create a Stream[Pure, String] that emits the words "apple", "banana", "cherry", "date".
 * Create a Pipe[Pure, String, Int] that calculates the length of each word.
 * Create a Pipe[Pure, Int, Int] that filters out word lengths less than 6.
 * Apply the pipes to the initial stream, and collect the results into an Option[Int] representing the length of the last word that has length greater than 6.
 *
 * Completion Criteria:
 *
 * The first stream should be correctly created and transformed using pure pipes.
 * The results of the first stream processing should be collected into a Vector[String].
 * The second stream should be correctly created and transformed using pure pipes.
 * The results of the second stream processing should be collected into an Option[Int].
 * Your code should be well-structured, readable, and follow Scala best practices.
 * Hints:
 *
 * Use Stream.range(1, 11) to create the first stream.
 * Use .map to square the numbers and convert them to strings.
 * Use .through to apply the pipes.
 * Use compile.toVector to collect the results into a vector.
 * Use Stream(...) to create the second stream.
 * Use .map to calculate the word lengths.
 * Use .filter to filter the word lengths.
 * Use compile.last to get the last element of a stream.
 */

import fs2.{Pipe, Pure, Stream}

// TASK 1

// Create a Stream[Pure, Int] that emits the numbers 1 to 10.
val intStream = Stream.range(1, 21)

// Create a Pipe[Pure, Int, Int] that squares each number in the stream.
val squarePipe: Pipe[Pure, Int, Int] = _.map(number => number * number)

// Create a Pipe[Pure, Int, String] that converts each number in the stream to its string representation
val stringPipe: Pipe[Pure, Int, String] = _.map("Squared: " + _)

// Apply the pipes to the initial stream, and collect the results into a Vector[String]
val result = intStream.through(squarePipe).through(stringPipe).toVector

// TASK 2

// Create a Stream[Pure, String]
val words = Stream("apple", "banana", "cherry", "date")

// Create a Pipe[Pure, String, Int] that calculates the length of each word.
val lengthPipe: Pipe[Pure, String, Int] = _.map(_.length)

// Create a Pipe[Pure, Int, Int] that filters out word lengths less than 6.
val lessThanSixPipe: Pipe[Pure, Int, Int] = _.filter(_ >= 6)

// Apply the pipes to the initial stream, and collect the results into an Option[Int]
// representing the length of the last word that has length greater than 6.
val lastWordLengthOpt = words
  .through(lengthPipe)
  .through(lessThanSixPipe)
  .compile
  .last

println(lastWordLengthOpt)
