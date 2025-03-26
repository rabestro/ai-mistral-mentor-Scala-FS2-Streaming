/**
 * Exercise 1: Basic Stream Creation and Transformation
 *
 * Objective: Create a simple FS2 stream that emits a sequence of integers,
 * transforms them by multiplying each by 2, and then collects the results into a list.
 *
 * Instructions:
 *
 * Create a stream that emits integers from 1 to 10.
 * Transform the stream by multiplying each integer by 2.
 * Collect the transformed integers into a list.
 *
 * Test Data:
 *
 * Input: Stream of integers from 1 to 10.
 * Expected Output: List of integers [2, 4, 6, 8, 10, 12, 14, 16, 18, 20].
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

val source = Stream.emits(1 to 10).covary[IO]
val transformation = source.map(_ * 2)

val result = transformation.compile.toList.unsafeRunSync()
