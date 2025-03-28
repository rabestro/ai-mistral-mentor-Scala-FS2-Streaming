import fs2.{Pipe, Stream}
import cats.effect.IO

// A pipe that doubles each integer in a stream
def doublePipe[F[_]]: Pipe[F, Int, Int] = _.map(_ * 2)

val inputStream: Stream[IO, Int] = Stream(1, 2, 3, 4, 5)

// Apply the pipe to the input stream
val doubledStream: Stream[IO, Int] = inputStream.through(doublePipe)

// Run the stream and print the doubled numbers
import cats.effect.unsafe.implicits.global
doubledStream.compile.toList.unsafeRunSync()
  .foreach(println)