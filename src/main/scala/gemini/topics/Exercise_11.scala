package gemini.topics

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.io.{BufferedReader, FileReader, FileWriter, PrintWriter}

/**
 * Problem Description:
 *
 * Your task is to create an FS2 stream that reads data from a file, processes it,
 * and writes the processed data to another file, ensuring that both files are
 * properly closed using Stream.bracket.
 *
 * Specific Requirements:
 *
 * Create a file named input.txt with the following content:
 *
 * Line 1
 * Line 2
 * Line 3
 *
 * Create an FS2 stream that reads the lines from input.txt using Stream.bracket to manage the FileReader.
 *
 * Process each line by converting it to uppercase.
 *
 * Write the processed lines to a new file named output.txt using Stream.bracket to manage the FileWriter.
 *
 * Ensure that both FileReader and FileWriter are properly closed using the release part of Stream.bracket.
 *
 * Print a message to the console after each file is successfully closed.
 *
 * Hints:
 *
 * Use java.io.FileReader and java.io.FileWriter to read from and write to files.
 * Use java.io.BufferedReader to read lines from the FileReader.
 * Use scala.io.Source.fromReader(reader).getLines().to(Stream) to create a stream from the BufferedReader.
 * Use Stream.eval(IO(writer.write(line + "\n"))) to write lines to the FileWriter.
 * Use IO(println(...)) to print messages to the console.
 *
 * Completion Criteria:
 *
 * The input.txt file should be read correctly.
 * The lines should be processed (converted to uppercase) correctly.
 * The processed lines should be written to output.txt correctly.
 * Both FileReader and FileWriter should be properly closed using Stream.bracket.
 * The console should display messages indicating successful file closure.
 */
object Exercise_11 extends IOApp.Simple {
  val inputFile = "./data/input.txt"
  val outputFile = "./data/output.txt"

  def acquireReader(file: String): IO[BufferedReader] =
    IO.println(s"Opening file: $file") >>
      IO(new BufferedReader(new FileReader(file)))

  def releaseReader(reader: BufferedReader): IO[Unit] =
    IO.println(s"Closing file: ${reader}") >> IO(reader.close())

  def acquireWriter(file: String): IO[PrintWriter] =
    IO.println(s"Creating output file: $file") >>
      IO(new PrintWriter(new FileWriter(file)))

  def releaseWriter(writer: PrintWriter): IO[Unit] =
    IO.println(s"Closing output file: ${writer}") >> IO(writer.close())

  def inFileStream(file: String): Stream[IO, BufferedReader] =
    Stream.bracket(acquireReader(file))(releaseReader)

  def outFileStream(file: String): Stream[IO, PrintWriter] =
    Stream.bracket(acquireWriter(file))(releaseWriter)

  private val readLines: BufferedReader => IO[Option[(String, BufferedReader)]] =
    reader => IO(Option(reader.readLine())).map(_.map((_, reader)))

  def writeLines(writer: PrintWriter): String => IO[Unit] = line =>
    IO.println(s"Processing: $line") >> IO(writer.println(line.toUpperCase))

  val program: Stream[IO, Unit] = for {
    reader <- inFileStream(inputFile)
    writer <- outFileStream(outputFile)
    _ <- Stream
      .unfoldEval(reader)(readLines)
      .evalMap(writeLines(writer))
  } yield ()

  override def run: IO[Unit] = program
    .compile
    .drain
    .as(ExitCode.Success)
}
