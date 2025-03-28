/**
 * 🏆 **Hands-on Exercise: Creating and Using Pipes**
 *
 * **Problem Description:**
 *
 * Your task is to create and use FS2 pipes to perform various stream transformations.
 *
 * **Specific Requirements:**
 *
 * 1. Create a `Pipe[IO, String, String]` that converts all strings in a stream to uppercase.
 * 2. Create a `Pipe[IO, Int, Int]` that filters out odd numbers from a stream.
 * 3. Create a `Pipe[IO, Int, String]` that converts each integer in a stream to its string representation, prefixed with "Number: ".
 * 4. Create a `Pipe[IO, String, Int]` that converts strings to their lengths.
 * 5. Create a pipeline that reads lines from a string, splits them into words, converts the words to uppercase, and then prints each uppercase word to the console.
 *
 * **Completion Criteria:**
 *
 * - Each pipe should be created correctly using the appropriate FS2 methods and effect types.
 * - The pipes should perform the specified transformations on the stream elements.
 * - The pipeline should correctly process the input string and print the uppercase words to the console.
 *
 * **Hints:**
 *
 * - Use `.map` to transform stream elements.
 * - Use `.filter` to filter stream elements.
 * - Use `.through` to apply pipes to streams.
 * - Use `IO(println(...))` to print to the console.
 */

import cats.effect.IO
import cats.effect.std.Random
import cats.effect.unsafe.implicits.global
import fs2.{Pipe, Stream, text}

val inputString =
  """hello world
    |fs2 is awesome
    |scala rocks""".stripMargin

val randomIntStream: Stream[IO, Int] = Stream
  .eval(Random.scalaUtilRandom[IO])
  .map(_.betweenInt(0, 100))
  .flatMap(Stream.repeatEval)
  .take(10)

// 1. Create a `Pipe[IO, String, String]` that converts all strings in a stream to uppercase.
val toUpperCase: Pipe[IO, String, String] = _.map(_.toUpperCase)

// 2. Create a `Pipe[IO, Int, Int]` that filters out odd numbers from a stream.
val evenNumbers: Pipe[IO, Int, Int] = _.filter(_ % 2 == 0)

// 3. Create a `Pipe[IO, Int, String]` that converts each integer in a stream
// to its string representation, prefixed with "Number: ".
val numbersToString: Pipe[IO, Int, String] = _.map("Number: " + _)

randomIntStream
  .through(numbersToString)
  .evalMap(IO.println)
  .compile.drain.unsafeRunSync()

// 4. Create a `Pipe[IO, String, Int]` that converts strings to their lengths.
val stringToLength: Pipe[IO, String, Int] = _.map(_.length)

// 5. Create a pipeline that reads lines from a string, splits them into words,
// converts the words to uppercase, and then prints each uppercase word to the console.

Stream.emit(inputString)
  .through(text.lines)
  .through(_.flatMap(s => Stream.emits(s.split("\\s+"))))
  .through(toUpperCase)
  .evalMap(IO.println)
  .compile
  .drain
  .unsafeRunSync()



