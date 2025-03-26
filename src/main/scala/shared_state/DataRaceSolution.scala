package shared_state

import cats.effect.{IO, IOApp, Ref}
import cats.implicits.{catsSyntaxParallelTraverse_, toFoldableOps}

object DataRaceSolution extends IOApp.Simple {

  private val counter = Ref[IO].of(0)

  private def increment(ref: Ref[IO, Int]): IO[Unit] =
    (1 to 500).toList.traverse_(_ => ref.update(_ + 1))

  def run: IO[Unit] = for {
    ref <- counter
    _ <- IO.println("Counter initialized")
    _ <- (1 to 10).toList.parTraverse_(_ => increment(ref))
    _ <- increment(ref).both(increment(ref))
    value <- ref.get
    _ <- IO.println(s"Final counter value: $value")
  } yield ()
}
