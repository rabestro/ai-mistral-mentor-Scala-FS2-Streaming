/**
 * Exercise 2: Effectful Streams
 *
 * Objective: Create a stream that emits a side effect (e.g., logging) for each element.
 *
 * Instructions:
 *
 * Modify the previous stream to log each number using a logging effect instead of printing directly to the console.
 * Use a simple logging effect that prints to the console with a prefix (e.g., "Logging number: ").
 * Test Data and Tests:
 *
 * Ensure the log messages are correct and include the prefix.
 * Verify the order of logging is correct.
 *
 * Hints:
 *
 * You can define a simple logging effect using a function.
 * Use .evalMap to apply the logging effect to each element in the stream.
 */

import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

// Define the logging effect
def logNumber(n: Int): IO[Unit] = IO(println(s"Logging number: $n"))

// Create the stream and apply the logging effect
val loggedStream = Stream.range(1, 11).evalMap(logNumber)

// Run the stream
loggedStream.compile.drain.unsafeRunSync()

