package gemini.topics

import cats.effect.{IO, IOApp}

import scala.concurrent.duration._

object RaceExample extends IOApp.Simple {
  private val fast = IO.sleep(1.second) *> IO("Fast Stream Wins!")
  private val slow = IO.sleep(3.seconds) *> IO("Slow Stream Loses!")

  def run: IO[Unit] = IO.race(fast, slow).flatMap {
    case Left(winner) => IO.println(s"🏆 Winner: $winner")
    case Right(winner) => IO.println(s"🏆 Winner: $winner")
  }
}

