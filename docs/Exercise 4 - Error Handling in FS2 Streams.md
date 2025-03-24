Great! Let's move on to a more advanced topic in FS2 streaming: handling errors in streams. This is a crucial aspect of working with effectful streams, as it allows you to manage failures gracefully.

### Exercise 4: Error Handling in FS2 Streams

**Objective**: Create a stream that processes elements and handles errors gracefully.

**Instructions**:
- Create a stream that emits integers from 1 to 10.
- Introduce a simulated error for one of the elements (e.g., throw an exception for a specific number).
- Handle the error using FS2's error-handling mechanisms.
- Ensure that the stream continues processing the remaining elements after encountering an error.

**Test Data and Tests**:
- Verify that the error is handled and logged appropriately.
- Ensure that the stream continues processing after the error.
- Check that all elements, except the one with the error, are processed successfully.

**Hints**:
- Use `Stream.eval` or `Stream.emit` to introduce errors.
- Use `handleErrorWith` or `attempt` to handle errors in the stream.
- Log the error message and continue processing the remaining elements.

Please go ahead and solve this exercise. Once you're done, submit your solution, and I'll review it and provide feedback. If you have any questions or need hints along the way, feel free to ask!

---

> I've introduce the error in the method:
```scala
// Define the effect
def saveNumber(connection: DatabaseConnection)(number: Int): IO[Unit] = IO({
  println(f"Saving number: $number%02d to $connection.\t Time: ${LocalTime.now()}")
  sleepUpTo(4.second)
  // Introducing an error in the effect
  if (number % 3 == 0) throw new RuntimeException(s"Error processing the number $number")
  else println(s"Successfully processed number $number")
})
```

> Please explain me how to handle errors properly. I use the last code (with concurrency)

---

Handling errors in FS2 streams involves using operators that allow you to recover from failures or log them without stopping the entire stream. Let's go through the steps to handle errors properly in your stream.

### Steps to Handle Errors in FS2 Streams:

1. **Use `attempt` or `handleErrorWith`**:
   - `attempt`: This operator converts a failed effect into a successful one by wrapping the result in `Either`. This allows you to handle errors downstream.
   - `handleErrorWith`: This operator allows you to specify a recovery action that will be executed if an error occurs.

2. **Log the Error**:
   - You can log the error message and continue processing the remaining elements.

3. **Continue Processing**:
   - Ensure that the stream continues to process elements even after encountering an error.

### Modified Code with Error Handling:

Here's how you can modify your code to handle errors using `handleErrorWith`:

```scala
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

final case class DatabaseConnection(connection: String) extends AnyVal

object Solution4 extends IOApp {
  def sleepUpTo(maxDuration: FiniteDuration): Unit =
    Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

  val acquire: IO[DatabaseConnection] = IO {
    val conn = DatabaseConnection("test-db")
    println(s"Acquiring connection to the database: $conn")
    sleepUpTo(1.seconds)
    conn
  }

  val release: DatabaseConnection => IO[Unit] = (conn: DatabaseConnection) =>
    IO.println(s"Releasing connection to the database: $conn")

  // Define the effect with error handling
  def saveNumber(connection: DatabaseConnection)(number: Int): IO[Unit] = IO({
    println(f"Saving number: $number%02d to $connection.\t Time: ${LocalTime.now()}")
    sleepUpTo(4.second)
    // Introducing an error in the effect
    if (number % 3 == 0) throw new RuntimeException(s"Error processing the number $number")
    else println(s"Successfully processed number $number")
  }).handleErrorWith { case e: RuntimeException =>
    IO.println(s"Error encountered: ${e.getMessage}")
  }

  // Create the stream and apply the logging effect with concurrency and error handling
  def numStream(connection: DatabaseConnection): Stream[IO, Unit] =
    Stream.range(1, 11)
      .covary[IO]
      .parEvalMap(5)(saveNumber(connection)) // Process up to 5 elements concurrently

  override def run(args: List[String]): IO[ExitCode] =
    Stream.bracket(acquire)(release)
      .flatMap(connection => numStream(connection))
      .compile
      .drain
      .as(ExitCode.Success)
}
```

### Explanation of Changes:

- **`handleErrorWith`**: This operator is used to catch the `RuntimeException` and log an error message. This allows the stream to continue processing the remaining elements.

- **Error Logging**: The error message is logged using `IO.println`, and the stream continues executing the next elements.

### Next Steps:

- Try running this modified version and observe how errors are handled.
- Experiment with different error-handling strategies, such as retrying the operation or collecting errors for later processing.

If you have any questions or need further assistance, feel free to ask!

---

> With your help I understand how works `handleErrorWith`. I hope that I understand. May be one more exercise (a different one) for this topic to stronger my understanding. 

---

[[Exercise 5 - Error Recovery and Retries in FS2 Streams]]