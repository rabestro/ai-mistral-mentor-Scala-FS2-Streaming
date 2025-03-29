import cats.effect.IO
import fs2.Stream
import cats.effect.unsafe.implicits.global

def process(i: Int): IO[String] = IO {
  println(s"Processing $i")
  Thread.sleep(100) // Simulate some processing time
  s"Processed $i"
}

val stream1: Stream[IO, Int] = Stream.range(1, 6).covary[IO]
val stream2: Stream[IO, String] = Stream("A", "B", "C").covary[IO]

// parEvalMap
val parMappedStream: Stream[IO, String] = stream1.parEvalMap(2)(process)

// parEvalMapUnordered
val parUnorderedStream: Stream[IO, String] = stream1.parEvalMapUnordered(2)(process)

// merge
val mergedStream: Stream[IO, String] = stream2.merge(parMappedStream)

// concurrently
val concurrentStream: Stream[IO, String] = stream2 concurrently parMappedStream

parMappedStream.compile.toList.unsafeRunSync().foreach(println)
println("---")
parUnorderedStream.compile.toList.unsafeRunSync().foreach(println)
println("---")
mergedStream.compile.toList.unsafeRunSync().foreach(println)
println("---")
concurrentStream.compile.toList.unsafeRunSync().foreach(println)