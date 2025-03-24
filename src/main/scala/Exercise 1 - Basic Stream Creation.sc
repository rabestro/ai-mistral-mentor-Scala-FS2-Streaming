/**
 * Exercise 1: Basic Stream Creation
 *
 * Objective: Create a simple FS2 stream that emits the numbers from 1 to 10.
 *
 * Instructions:
 *
 * Use the FS2 library to create a stream.
 * The stream should emit integers from 1 to 10.
 * Print each number to the console.
 * Test Data and Tests:
 *
 * Ensure the stream emits exactly the numbers from 1 to 10.
 * Verify the order of emission is correct.
 * Hints:
 *
 * You can use Stream.emits or Stream.range to create the stream.
 * Use .compile.drain to run the stream and see the output.
 */

import fs2.Stream

val intStream = Stream.range(1, 11)

intStream.map(println).compile.drain