/**
 * Exercise 4: Resource Management
 *
 * Task: Modify the previous exercise to include resource management.
 * Simulate a resource that needs to be acquired before making network requests
 * and released afterward. Use a concurrency level of 5.
 *
 * Instructions:
 *
 * Use Stream.range to generate the sequence.
 * Use bracket to acquire and release the resource.
 * Use parEvalMap with a concurrency level of 5 to simulate the network request and return the square of the number.
 * Collect the results into a list and print the list at the end.
 * Test Data and Tests:
 *
 * The stream should return a list of squared numbers: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100].
 * Ensure that the effects are handled concurrently, the resource is acquired and released correctly, and the results are collected in order.
 *
 * Hints:
 *
 * You can use bracket to acquire and release the resource.
 * Use parEvalMap with an explicit concurrency level to handle multiple effects in parallel.
 * Use compile.toList to collect the results into a list.
 */

import DatabaseConnection.{acquire, release}
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

import scala.concurrent.duration.DurationInt

def networkRequest(connection: DatabaseConnection)(number: Int): IO[Int] =
  IO.println(s"Requesting $connection for number $number") *>
    IO.sleep(1.second) *>
    IO(number * number) <*
    IO.println(s"✅Successfully processed number $number")

def dataStream(connection: DatabaseConnection) =
  Stream.range(1, 11)
    .covary[IO]
    .parEvalMap(5)(networkRequest(connection))

Stream.bracket(acquire)(release)
  .flatMap(connection => dataStream(connection))
  .compile
  .toList
  .unsafeRunSync()
