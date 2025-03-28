/**
 * 🏆 **Hands-on Exercise: Creating Basic Streams**
 *
 * **Problem Description:**
 *
 * Your task is to create several basic FS2 streams using different methods and effect types.
 *
 * **Specific Requirements:**
 *
 * 1. Create a `Stream[IO, Int]` that emits the numbers 1 to 5.
 * 2. Create a `Stream[Pure, String]` that emits the words "apple", "banana", and "cherry".
 * 3. Create a `Stream[IO, String]` from a `List[String]` containing the elements "one", "two", and "three".
 * 4. Create a `Stream[IO, Int]` that generates a sequence of 10 random integers between 1 and 100 (inclusive).
 * 5. Create a `Stream[IO, Unit]` that prints "Hello, FS2!" to the console 3 times.
 *
 * **Completion Criteria:**
 *
 * - Each stream should be created correctly using the appropriate FS2 methods and effect types.
 * - The streams should emit the specified elements in the correct order.
 * - The random integer stream should generate 10 unique random numbers.
 * - The printing stream should print "Hello, FS2!" to the console 3 times.
 *
 * **Hints:**
 *
 * - Use `Stream(...)` to create streams from a sequence of elements.
 * - Use `Stream.eval(IO(...))` to create streams with side effects.
 * - Use `Stream.iterable(...)` to create streams from collections.
 * - Use `Random.scalaUtilRandom[IO]` and `Stream.repeatEval` to generate random numbers.
 * - Use `IO(println(...))` to print to the console.
 * - Use `.take(...)` to limit the number of elements in a stream.
 */

import cats.effect.IO
import cats.effect.std.Random
import fs2.{Pure, Stream}

// 1. Create a `Stream[IO, Int]` that emits the numbers 1 to 5.
val ioStream: Stream[IO, Int] = Stream.range(1, 6).covary[IO]

// 2. Create a `Stream[Pure, String]` that emits the words "apple", "banana", and "cherry".
val pureStream: Stream[Pure, String] = Stream("apple", "banana", "cherry")

// 3. Create a `Stream[IO, String]` from a `List[String]` containing the elements "one", "two", and "three".
val words = List("apple", "banana", "cherry")
val stream: Stream[IO, String] = Stream.emits(words).covary[IO]

// 4. Create a `Stream[IO, Int]` that generates a sequence
// of 10 random integers between 1 and 100 (inclusive).
val minInclusive = 1
val maxInclusive = 100
val numberCount = 10

val randomIntStream: Stream[IO, Int] = Stream
  .eval(Random.scalaUtilRandom[IO])
  .map(_.betweenInt(minInclusive, maxInclusive + 1))
  .flatMap(Stream.repeatEval)
  .take(numberCount)

// 5. Create a `Stream[IO, Unit]` that prints "Hello, FS2!" to the console 3 times.
import cats.effect.unsafe.implicits.global

Stream
  .emit("Hello, FS2!")
  .evalMap(IO.println)
  .repeatN(3)
  .compile
  .drain
  .unsafeRunSync()

Stream
  .eval(Random.scalaUtilRandom[IO])
  .map(_.betweenInt(minInclusive, maxInclusive + 1))
  .flatMap(Stream.repeatEval)
  .take(5)
//  .map(IO.println)
//  .flatMap(Stream.eval)
  .evalMap(IO.println)
  .compile
  .drain
  .unsafeRunSync()