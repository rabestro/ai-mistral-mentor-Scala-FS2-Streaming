import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.{Chunk, Stream}

val chunk: Chunk[Int] = Chunk(1, 2, 3, 4, 5)

// Accessing elements
val firstElement: Int = chunk(0) // 1

// Iterating over elements
chunk.foreach(println)

// Transforming elements
val doubledChunk: Chunk[Int] = chunk.map(_ * 2)

// Example 1: Processing Chunks Directly

val stringStream: Stream[IO, String] =
  Stream(
    "apple", "banana", "cherry",
    "date", "elderberry", "fig",
    "grape", "honeydew", "kiwi"
  )

stringStream
  .chunkN(3) // Create chunks of 3 elements
  .evalMap { chunk =>
    val uppercaseStrings = chunk.map(_.toUpperCase).toList.mkString(", ")
    IO(println(s"Chunk: $uppercaseStrings"))
  }
  .compile.drain.unsafeRunSync()

// Example 2: Chunk-Aware Transformations

val intStream: Stream[IO, Int] = Stream.range(1, 11)

intStream
  .chunks
  .scanChunks(0) { (acc, chunk) =>
    val chunkSum = chunk.foldLeft(0)((acc: Int, i) => acc + i)
    val sum = acc + chunkSum
    sum -> Chunk.singleton(sum)
  }
  .evalMap(i => IO(println(s"Running sum: $i")))
  .compile.drain.unsafeRunSync()

