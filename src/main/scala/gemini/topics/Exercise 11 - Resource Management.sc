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

import cats.effect.IO

import java.io.{BufferedReader, FileReader}
import fs2.{Stream, text}

def acquireFile: IO[java.io.File] =
  IO(new java.io.File("input.txt"))

def releaseFile(file: java.io.File): IO[Unit] =
  IO(println(s"Releasing file: ${file.getName}"))

val filePath = "./data/input.txt"
val acquire: IO[BufferedReader] =
  IO.println(s"Acquiring connection to the file: $filePath") >>
    IO(new BufferedReader(new FileReader(filePath)))

val release = (reader: BufferedReader) => IO(reader.close()) >>
  IO.println(s"Releasing connection to the file: $filePath")

Stream.bracket(acquire)(release)
