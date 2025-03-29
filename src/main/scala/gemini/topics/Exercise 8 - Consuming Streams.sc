/**
 * Problem Description:
 *
 * Your task is to create and consume FS2 streams using various terminal operations.
 *
 * Specific Requirements:
 *
 * Create a Stream[IO, Int] that emits the numbers 1 to 10.
 * Use .compile.toList to collect the elements of the stream into a List[Int].
 * Use .compile.toVector to collect the elements of the stream into a Vector[Int].
 * Use .compile.drain to print each element of the stream to the console.
 * Use .compile.fold to calculate the sum of the elements in the stream.
 * Use .compile.last to get the last element of the stream as an Option[Int].
 * Use .compile.count to count the number of elements in the stream.
 * Use .compile.exists to check if any element of the stream is greater than 5.
 * Use .compile.forall to check if all elements of the stream are positive.
 * Use .compile.find to find the first even number in the stream as an Option[Int].
 *
 * Completion Criteria:
 *
 * Each stream consumption should be performed correctly using the specified terminal operations.
 * The results of each stream consumption should be printed to the console.
 * Your code should be well-structured, readable, and follow Scala best practices.
 *
 * Hints:
 *
 * Use Stream.range(1, 11).covary[IO] to create the initial stream.
 * Use IO(println(...)) to print to the console.
 * Use .unsafeRunSync() to get the result of the IO actions.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

// Create a Stream[IO, Int] that emits the numbers 1 to 10.
val intStream: Stream[IO, Int] = Stream.range(1, 11).covary[IO]

val resList = intStream.compile.toList.unsafeRunSync()
val resVector = intStream.compile.toVector.unsafeRunSync()

// Use .compile.drain to print each element of the stream to the console.
val program = intStream.evalMap(IO.println)
program.compile.drain.unsafeRunSync()

val sum = intStream.compile.fold(0)(_ + _).unsafeRunSync()
val count = intStream.compile.count.unsafeRunSync()
val exist = intStream.exists(_ > 5).compile.lastOrError.unsafeRunSync()
val forall = intStream.forall(_ > 0).compile.lastOrError.unsafeRunSync()
val firstEven = intStream.find(_ % 2 == 0).compile.last.unsafeRunSync()


