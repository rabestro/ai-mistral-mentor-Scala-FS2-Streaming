package shared_state

import cats.effect._
import cats.implicits._

object DataRaceExample extends IOApp.Simple {
  private var counter: Int = 0

  private def increment(): Unit =
    (1 to 5000).foreach { _ => counter += 1 }

  override def run: IO[Unit] =
    for {
      _ <- IO.blocking(increment()).both(IO.blocking(increment()))
      _ <- IO.println(s"Final counter value: $counter")
    } yield ()
}