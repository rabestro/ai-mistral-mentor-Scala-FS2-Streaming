import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeError
import fs2.Stream

val errorStream: Stream[IO, Int] = Stream(1, 2, 0, 4, 5).map {
  case 0 => throw new ArithmeticException("Division by zero")
  case i => 10 / i
}.covary[IO]

// Handle error with
val handledStream: Stream[IO, Int] = errorStream.handleErrorWith {
  case _: ArithmeticException => Stream(-1, -2, -3)
  case e => Stream.raiseError[IO](e)
}

// Attempt
val attemptedStream: Stream[IO, Either[Throwable, Int]] = errorStream.attempt

// Compile to list
val result = handledStream.compile.toList.unsafeRunSync()
val attemptResult = attemptedStream.compile.toList.unsafeRunSync()

println(s"Handled stream: $result")
println(s"Attempted stream: $attemptResult")

// On error
errorStream.onError {
  case e => Stream.eval(IO(println(s"Error occurred: ${e.getMessage}")))
}.compile.drain.unsafeRunSync()
