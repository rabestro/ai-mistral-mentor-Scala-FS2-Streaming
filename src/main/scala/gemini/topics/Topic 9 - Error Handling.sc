import cats.effect.IO
import fs2.Stream
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeError

val errorStream: Stream[IO, Int] = Stream(1, 2, 0, 4, 5).map {
  case 0 => throw new ArithmeticException("Division by zero")
  case i => 10 / i
}.covary[IO]

// handleErrorWith
val handledStream: Stream[IO, Int] = errorStream.handleErrorWith {
  case _: ArithmeticException => Stream(-1, -2, -3)
  case e => Stream.raiseError[IO](e) //rethrow other errors.
}

// attempt
val attemptedStream: Stream[IO, Either[Throwable, Int]] = errorStream.attempt

// onError
errorStream.onError {
  case e => Stream.eval(IO(println(s"Error occurred: ${e.getMessage}")))
}.compile.drain.unsafeRunSync()

// Compile to list
val result = handledStream.compile.toList.unsafeRunSync()
val attemptResult = attemptedStream.compile.toList.unsafeRunSync()

println(s"Handled stream: $result")
println(s"Attempted stream: $attemptResult")