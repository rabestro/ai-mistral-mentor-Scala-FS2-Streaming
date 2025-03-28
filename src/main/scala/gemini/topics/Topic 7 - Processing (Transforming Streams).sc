import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

val numberStream: Stream[IO, Int] = Stream(1, 2, 3, 4, 5).covary[IO]

// Map
val doubledStream: Stream[IO, Int] = numberStream.map(_ * 2)

// Filter
val evenStream: Stream[IO, Int] = numberStream.filter(_ % 2 == 0)

// FlatMap
val stringStream: Stream[IO, String] = numberStream.flatMap(i => Stream(s"Number: $i", s"Squared: ${i * i}"))

// EvalMap
val effectfulStream: Stream[IO, String] = numberStream.evalMap(i => IO(s"Effectful: $i"))

// EvalTap
numberStream.evalTap(i => IO(println(s"Side effect: $i"))).compile.drain.unsafeRunSync()

// Scan
val runningSumStream: Stream[IO, Int] = numberStream.scan(0)(_ + _)

// MapAccumulate
val mappedAccumulatedStream: Stream[IO, (Int, (Int, String))] = numberStream
  .mapAccumulate(0) { (acc, i) =>
    val newAcc = acc + i
    newAcc -> (i, s"Sum: $newAcc")
  }

// Print results
doubledStream.compile.toList.unsafeRunSync().foreach(println)
evenStream.compile.toList.unsafeRunSync().foreach(println)
stringStream.compile.toList.unsafeRunSync().foreach(println)
effectfulStream.compile.toList.unsafeRunSync().foreach(println)
runningSumStream.compile.toList.unsafeRunSync().foreach(println)
mappedAccumulatedStream.compile.toList.unsafeRunSync().foreach(println)