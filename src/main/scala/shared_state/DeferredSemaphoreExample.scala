package shared_state

import cats.effect.std.Semaphore
import cats.effect.{Deferred, IO, IOApp}
import cats.implicits._

import scala.concurrent.duration.DurationInt

object DeferredSemaphoreExample extends IOApp.Simple {

  def program: IO[Unit] = for {
    deferred <- Deferred[IO, String] // Create Deferred once
    semaphore <- Semaphore[IO](2) // Limit concurrency to 2 tasks

    taskA = for {
      _ <- IO.println(s"🕒 [${Thread.currentThread.getName}] Task A waiting for semaphore...")
      _ <- semaphore.acquire // Acquire a permit
      _ <- IO.println(s"✅ [${Thread.currentThread.getName}] Task A acquired semaphore")
      message <- deferred.get
      _ <- IO.println(s"📩 [${Thread.currentThread.getName}] Task A received message: '$message'")
      _ <- semaphore.release // Release the permit
      _ <- IO.println(s"🔓 [${Thread.currentThread.getName}] Task A released semaphore")
    } yield ()

    taskB = for {
      _ <- IO.println("🚀 [Task B] Started, waiting 3 seconds before completing Deferred...")
      _ <- IO.sleep(3.seconds)
      _ <- deferred.complete("Hello World!")
      _ <- IO.println("🎉 [Task B] Completed! Deferred is now fulfilled.")
    } yield ()

    _ <- List.fill(5)(taskA).appended(taskB).parSequence_
  } yield ()

  override def run: IO[Unit] = program
}

