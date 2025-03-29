package resource_example

import cats.effect.{IO, IOApp, Resource}
import fs2.io.file.Path
import resource_example.ExerciseResources.{program, randomDelay}

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Random

// Task 1: Implement DatabaseConnection

final case class DatabaseConnection(db: String) extends Product with Serializable {
  def queryData: IO[Unit] =
    IO.println("Querying data...") >> randomDelay
}

object DatabaseConnection {
  def open(db: String): IO[DatabaseConnection] =
    IO.println("DB connected") >> randomDelay >> IO.pure(new DatabaseConnection(db))

  def close(connection: DatabaseConnection): IO[Unit] =
    IO.println("DB closed") >> randomDelay
}

object ExerciseResources {
  def randomDuration: IO[FiniteDuration] =
    IO(Random.between(100, 500)).map(_.millis)

  val randomDelay: IO[Unit] = randomDuration.flatMap(IO.sleep)

  def dbResource: Resource[IO, DatabaseConnection] =
    Resource.make(DatabaseConnection.open("test-db"))(DatabaseConnection.close)

  // Task 2: Implement File Reader

  def acquireFile(fileName: String): IO[Path] =
    IO.println(s"Opening file: $fileName") >> IO(Path(fileName))

  def releaseFile(file: Path): IO[Unit] =
    IO.println(s"Releasing file: ${file.fileName}")

  def fileResource: Resource[IO, Path] =
    Resource.make(acquireFile("input.txt"))(releaseFile)

  private val combinedResource: Resource[IO, (DatabaseConnection, Path)] = for {
    db <- ExerciseResources.dbResource
    in <- ExerciseResources.fileResource
  } yield (db, in)

  // Task 3: Compose both resources
  val program: IO[Unit] = combinedResource.use {
    case (conn, file) =>
      IO.println(s"Using file ${file.fileName} with connection: ${conn.db}") >>
        conn.queryData
  }
}

// Run
object ResourceExample extends IOApp.Simple {
  def run: IO[Unit] = program
}
