import cats.effect.IO
import cats.effect.std.Random
import fs2.{Chunk, Stream}
import cats.effect.unsafe.implicits.global

import scala.language.postfixOps

// Stream from a sequence of elements
val stream1: Stream[IO, Int] = Stream(1, 2, 3, 4, 5).covary[IO]

// Stream from a list
val stream2: Stream[IO, String] = Stream.emits(List("apple", "banana", "cherry"))

// Stream from an iterator
val stream3: Stream[IO, Int] = Stream.fromIterator[IO](1 to 5 iterator, chunkSize = 1)

// Stream from an effectful computation
val stream4: Stream[IO, Int] = Stream.eval(IO(42))

// Stream from an effectful chunk
val stream5: Stream[IO, Int] = Stream.evalUnChunk(IO(Chunk(1, 2, 3)))

// Stream from a range
val stream6: Stream[IO, Int] = Stream.range(1, 6).covary[IO]

// Stream that repeats an element
val stream7: Stream[IO, String] = Stream("hello").repeat.take(3)

// Stream that repeats an effectful computation
val stream8: Stream[IO, Int] = Stream
  .repeatEval(Random.scalaUtilRandom[IO])
  .evalMap(_.betweenInt(1, 10))
  .take(3)

stream1.compile.toList.unsafeRunSync().foreach(println)
stream2.compile.toList.unsafeRunSync().foreach(println)
stream3.compile.toList.unsafeRunSync().foreach(println)
stream4.compile.toList.unsafeRunSync().foreach(println)
stream5.compile.toList.unsafeRunSync().foreach(println)
stream6.compile.toList.unsafeRunSync().foreach(println)
stream7.compile.toList.unsafeRunSync().foreach(println)
stream8.compile.toList.unsafeRunSync().foreach(println)