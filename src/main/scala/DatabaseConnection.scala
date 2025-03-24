import CommonFunctions.sleepUpTo
import cats.effect.IO

import scala.concurrent.duration.DurationInt

final case class DatabaseConnection(connection: String) extends AnyVal

object DatabaseConnection {
  val acquire: IO[DatabaseConnection] = IO {
    val conn = DatabaseConnection("test-db")
    println(s"Acquiring connection to the database: $conn")
    sleepUpTo(1.seconds)
    conn
  }

  val release: DatabaseConnection => IO[Unit] = (conn: DatabaseConnection) =>
    IO.println(s"Releasing connection to the database: $conn")

}