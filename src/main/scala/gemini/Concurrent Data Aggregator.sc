import cats.effect.std.Random
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref}
import fs2.{Pipe, Stream}

val minInclusive = 1
val maxExclusive = 101

val randomIntStream: Stream[IO, Int] =
  Stream.eval(Random.scalaUtilRandom[IO])
    .map(_.betweenInt(minInclusive, maxExclusive))
    .flatMap(Stream.repeatEval)

type Accumulator = (Int, Int)

def runningAverage: Pipe[IO, Int, Double] = input =>
  Stream.eval(Ref.of[IO, Accumulator]((0, 0))).flatMap {
    acc: Ref[IO, Accumulator] =>
      input.evalMap { number: Int =>
        acc.modify { case (sum, count) =>
          val newSum = sum + number
          val newCount = count + 1
          val average = newSum / newCount.toDouble
          (newSum, newCount) -> average
        }
      }
  }

val program: Stream[IO, Double] =
  randomIntStream
    .through(runningAverage)
    .take(10)

// To run the program and print the results:
program.compile
  .toList
  .flatMap(IO.println)
  .unsafeRunSync()

