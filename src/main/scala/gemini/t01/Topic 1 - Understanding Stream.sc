import cats.effect.IO
import fs2.{Pure, Stream}

// A stream of integers with IO effects
val myStream: Stream[IO, Int] = Stream(1, 2, 3, 4, 5)

// A pure stream of strings
val pureStream: Stream[Pure, String] = Stream("apple", "banana", "cherry")


