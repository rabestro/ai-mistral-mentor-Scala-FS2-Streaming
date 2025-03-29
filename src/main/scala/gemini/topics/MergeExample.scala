package gemini.topics

import cats.effect.{IO, IOApp}
import fs2.Stream
import scala.concurrent.duration._

/**
 * The merge method combines two streams,
 * emitting elements as they arrive.
 *
 * Both streams run simultaneously.
 */
object MergeExample extends IOApp.Simple {
  private val streamA = Stream.awakeEvery[IO](1.second).map(d => s"A: $d")
  private val streamB = Stream.awakeEvery[IO](500.millis).map(d => s"B: $d")

  val mergedStream: Stream[IO, String] = streamA.merge(streamB).take(10)

  def run: IO[Unit] = mergedStream.evalMap(IO.println).compile.drain
}
