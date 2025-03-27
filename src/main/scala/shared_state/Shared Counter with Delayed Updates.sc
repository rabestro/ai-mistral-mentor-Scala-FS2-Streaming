/**
 * Shared Counter with Delayed Updates
 *
 * Goal: Implement a shared counter that multiple concurrent tasks update,
 * but with a small delay before applying each update.
 *
 * Requirements:
 *
 * 1️⃣ Create a shared counter with an initial value of 0 using Ref[IO, Int].
 * 2️⃣ Spawns 10 concurrent tasks, each increasing the counter by 1 after a random delay between 100ms and 500ms.
 * 3️⃣ Ensure all updates are applied safely without race conditions.
 * 4️⃣ Print the final counter-value after all updates are applied.
 */

import cats.effect.{IO, Ref}
import cats.implicits.catsSyntaxParallelTraverse_

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}
import scala.util.Random

val randomDuration: IO[FiniteDuration] =
  IO(Random.between(100, 500)).map(_.millis)

def increment(ref: Ref[IO, Int]): IO[Unit] =
  ref.update(_ + 1)

def task(ref: Ref[IO, Int]): IO[Unit] = for {
  delay <- randomDuration
  _ <- IO.sleep(delay)
  _ <- increment(ref)
  _ <- IO.println(s"Counter increased after ${delay.toMillis}ms delay!")
} yield ()

def concurrentTasks(concurrentLevel: Int)(task: IO[Unit]): IO[Unit] =
  (1 to concurrentLevel).toList.parTraverse_(_ => task)

val program: IO[Unit] = for {
  counter <- Ref[IO].of(0)
  _ <- IO.println("Counter initialized")
  _ <- concurrentTasks(10)(task(counter))
  value <- counter.get
  _ <- IO.println(s"Final counter value: $value")
} yield ()

import cats.effect.unsafe.implicits.global

program.timeout(Duration.Inf).unsafeRunSync()

