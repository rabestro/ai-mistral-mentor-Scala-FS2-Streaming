package gemini.topics

import cats.effect.{IO, IOApp}
import fs2.{Stream, text}
import fs2.io.file.{Files, Path}

object MergeFileStreams extends IOApp.Simple {
  private def readFile(path: String): Stream[IO, String] =
    Files[IO]
      .readAll(Path(s"./data/$path")) // ✅ Using fs2.io.file.Path
      .through(text.utf8.decode)
      .through(text.lines)

  private val fileStream1 = readFile("file1.txt").map(line => s"📂 File1: $line")
  private val fileStream2 = readFile("file2.txt").map(line => s"📂 File2: $line")

  val mergedStream: Stream[IO, String] = fileStream1 merge fileStream2

  def run: IO[Unit] = mergedStream.evalMap(IO.println).compile.drain
}