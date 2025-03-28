/**
 * Problem Description:
 *
 * Your task is to create and transform FS2 streams using various operators.
 *
 * Specific Requirements:
 *
 * Create a Stream[IO, Int] that emits the numbers 1 to 10.
 * Use .map to create a new stream that contains the squares of the numbers from the first stream.
 * Use .filter to create a new stream that contains only the even numbers from the squared stream.
 * Use .flatMap to create a new stream that emits the string representation of each even number,
 * prefixed with "Even: ", and its cube, prefixed with "Cube: ".
 * Use .evalMap to create a new stream that prints each string to the console and emits its length.
 * Use .scan to create a new stream that emits the running sum of the lengths from the previous stream.
 * Use .mapAccumulate to create a stream of (Int, String) tuples,
 * where the Int is the running product of the original numbers (1 to 10),
 * and the String is the original number prefixed with "Product: ".
 * Print the results of each stream transformation to the console.
 *
 * Completion Criteria:
 *
 * Each stream transformation should be applied correctly using the specified operators.
 * The results of each stream transformation should be printed to the console.
 * Your code should be well-structured, readable, and follow Scala best practices.
 *
 * Hints:
 *
 * Use Stream.range(1, 11).covary[IO] to create the initial stream.
 * Use IO(println(...)) to print to the console.
 * Use .compile.toList.unsafeRunSync() to collect the results into a list and print them.
 * Remember to use .covary[IO] if you are creating a pure stream and then want to use it in an IO context.
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

// Create a Stream[IO, Int] that emits the numbers 1 to 10.
val intStream: Stream[IO, Int] = Stream.range(1, 11).covary[IO]

val squares = intStream.map(x => x * x)
val evenSquares = squares.filter(_ % 2 == 0)
val strStream = evenSquares.flatMap(x => Stream(s"Even: $x", s"Cube: ${x * x * x}"))
val lenStream = strStream.evalMap(s => IO {
  println(s)
  s.length
})
val sumStream = lenStream.scan(0)(_ + _)

val products = intStream.mapAccumulate(1) { (acc, i) =>
  val newAcc = acc * i
  newAcc -> s"Product: $i"
}

squares.compile.toList.unsafeRunSync().foreach(println)
evenSquares.compile.toList.unsafeRunSync().foreach(println)
strStream.compile.toList.unsafeRunSync().foreach(println)
lenStream.compile.toList.unsafeRunSync().foreach(println)
sumStream.compile.toList.unsafeRunSync().foreach(println)
products.compile.toList.unsafeRunSync().foreach(println)

