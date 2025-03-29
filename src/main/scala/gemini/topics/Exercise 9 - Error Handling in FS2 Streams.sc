/**
 * Problem Description:
 *
 * Your task is to create and process an FS2 stream that may encounter errors, and then handle those errors using the provided operators.
 *
 * Specific Requirements:
 *
 * 1. Create a `Stream[IO, Int]` that emits the numbers 1 to 5. However, if the number 3 is encountered, throw an `ArithmeticException("Division by zero")`.
 * 2. Use `.handleErrorWith` to handle the `ArithmeticException`. If the exception occurs, emit the numbers -1, -2, and -3 instead.
 * 3. Use `.attempt` to convert the stream into a `Stream[IO, Either[Throwable, Int]]`.
 * 4. Use `.onError` to print the error message to the console if any error occurs during stream processing.
 * 5. Print the results of each stream transformation to the console.
 *
 * **Completion Criteria:**
 *
 * - Each stream transformation should be applied correctly using the specified error handling operators.
 * - The results of each stream transformation should be printed to the console.
 * - Your code should be well-structured, readable, and follow Scala best practices.
 *
 * **Hints:**
 *
 * - Use `Stream(...)` and `.map` to create the initial stream with the potential error.
 * - Use `IO(println(...))` to print to the console.
 * - Use `.compile.toList.unsafeRunSync()` to collect the results into a list and print them.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeError
import fs2.Stream

val errorStream: Stream[IO, Int] = Stream.range(1, 6).map {
  case 3 => throw new ArithmeticException("Division by zero")
  case i => i
}.covary[IO]

val handledStream = errorStream.handleErrorWith{
  case _: ArithmeticException => Stream(-1, -2, -3)
}

val attemptedStream = errorStream.attempt

handledStream.compile.toList.unsafeRunSync()
attemptedStream.compile.toList.unsafeRunSync()

errorStream
  .onError(e => Stream.eval(IO.println("⛔Error: " + e.getMessage)))
  .compile.toList.unsafeRunSync()


