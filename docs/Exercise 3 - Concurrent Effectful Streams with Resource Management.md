🤖  Great! Let's move on to a more complex exercise involving effectful streams. This time, we'll introduce concurrency and resource management.

### Exercise 3: Concurrent Effectful Streams with Resource Management

**Objective**: Create a stream that concurrently processes elements with a logging effect and manages resources properly.

**Instructions**:
- Create a stream that emits integers from 1 to 10.
- For each integer, simulate a resource-intensive operation (e.g., reading from a file or making an HTTP request) using a logging effect.
- Use concurrency to process multiple elements simultaneously.
- Ensure that resources are properly acquired and released.

**Test Data and Tests**:
- Verify that the logging effect is applied to each element.
- Ensure that the stream processes elements concurrently.
- Check that resources are acquired and released correctly.

**Hints**:
- Use `Stream.bracket` to manage resource acquisition and release.
- Use `.parEvalMap` or `.async` to introduce concurrency.
- Simulate a resource-intensive operation with a delay using `IO.sleep`.

Please go ahead and solve this exercise. Once you're done, submit your solution, and I'll review it and provide feedback. If you have any questions or need hints along the way, feel free to ask!

---

> I have a question. How to format like printf in Java?

---

In Scala, you can format strings similarly to how you would use `printf` in Java by using the `String.format` method or string interpolation.

### Using `String.format`

The `String.format` method in Scala works the same way as in Java. You can use it to format strings with placeholders. Here's an example:

```scala
val name = "Alice"
val age = 30
val formattedString = String.format("Name: %s, Age: %d", name, age)
println(formattedString)
```

### Using String Interpolation

Scala also provides a more concise way to format strings using string interpolation. You can embed expressions directly within string literals using the `s` prefix. Here's how you can do it:

```scala
val name = "Alice"
val age = 30
val formattedString = s"Name: $name, Age: $age"
println(formattedString)
```

### Using `f` Interpolator for Formatted Strings

For more complex formatting, you can use the `f` interpolator, which allows you to specify formats similar to `printf`:

```scala
val pi = Math.PI
val formattedString = f"Pi is approximately $pi%.2f"
println(formattedString)
```

In this example, `%.2f` specifies that the floating-point number should be formatted with two decimal places.

### Summary

- Use `String.format` for a Java-like approach.
- Use string interpolation with the `s` prefix for simple expressions.
- Use the `f` interpolator for more complex formatting needs.

These methods provide flexible and readable ways to format strings in Scala. If you have any further questions or need more examples, feel free to ask!

---

> My solution without concurrency:

```scala

import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

final case class DatabaseConnection(connection: String) extends AnyVal

def sleepUpTo(maxDuration: FiniteDuration) =
  Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

val acquire = IO {
  val conn = DatabaseConnection("test-db")
  println(s"Acquiring connection to the database: $conn")
  sleepUpTo(1.seconds)
  conn
}

val release = (conn: resource_example.DatabaseConnection) =>
  IO.println(s"Releasing connection to the database: $conn")

// Define the logging effect
def saveNumber(connection: resource_example.DatabaseConnection)(n: Int): IO[Unit] = IO({
  println(f"Saving number: $n%02d to $connection.\t Time: ${LocalTime.now()}")
  sleepUpTo(2.second)
})

// Create the stream and apply the logging effect
def numStream(connection: resource_example.DatabaseConnection) =
  Stream.range(1, 11)
    .evalMap(saveNumber(connection))

Stream.bracket(acquire)(release)
  .flatMap(connection => numStream(connection))
  .compile
  .drain
  .unsafeRunSync()
```

---

🤖 Your solution effectively demonstrates resource management and effectful operations in an FS2 stream. Let's break down what you've done and then discuss how to introduce concurrency.

### Review of Your Solution:

1. **Resource Management**:
   - You've used `Stream.bracket` to manage the acquisition and release of a `DatabaseConnection` resource. This ensures that the resource is properly released after the stream completes.

2. **Effectful Operations**:
   - The `saveNumber` function simulates a resource-intensive operation by logging a message and sleeping for a random duration.

3. **Stream Creation**:
   - You've created a stream that emits integers from 1 to 10 and applies the `saveNumber` effect to each integer.

### Introducing Concurrency:

To introduce concurrency, you can use `parEvalMap` instead of `evalMap`. This will allow multiple elements to be processed simultaneously, which can improve performance for I/O-bound or CPU-bound operations.

### Modified Solution with Concurrency:

Here's how you can modify your solution to introduce concurrency:

```scala
import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

final case class DatabaseConnection(connection: String) extends AnyVal

def sleepUpTo(maxDuration: FiniteDuration) =
  Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

val acquire = IO {
  val conn = DatabaseConnection("test-db")
  println(s"Acquiring connection to the database: $conn")
  sleepUpTo(1.seconds)
  conn
}

val release = (conn: resource_example.DatabaseConnection) =>
  IO.println(s"Releasing connection to the database: $conn")

// Define the logging effect
def saveNumber(connection: resource_example.DatabaseConnection)(n: Int): IO[Unit] = IO({
  println(f"Saving number: $n%02d to $connection.\t Time: ${LocalTime.now()}")
  sleepUpTo(2.second)
})

// Create the stream and apply the logging effect with concurrency
def numStream(connection: resource_example.DatabaseConnection) =
  Stream.range(1, 11)
    .parEvalMap(5)(saveNumber(connection)) // Process up to 5 elements concurrently

Stream.bracket(acquire)(release)
  .flatMap(connection => numStream(connection))
  .compile
  .drain
  .unsafeRunSync()
```

### Explanation of Changes:

- **`parEvalMap`**: This operator is used instead of `evalMap` to process elements concurrently. The parameter `5` specifies the maximum number of concurrent operations.

### Next Steps:

- Try running the modified solution to see the concurrent processing in action.
- Experiment with different concurrency levels by changing the parameter in `parEvalMap`.

If you have any questions or need further assistance, feel free to ask!

---

I faced some compilation errors:

> [!ERROR]
> ```
> /Users/jcemisovs/IdeaProjects/Bootcamp/FS2 Streaming Mastery Journey/src/main/scala/Exercise 3: Concurrent Effectful Streams with Resource Management.sc:68:19
could not find implicit value for parameter F: cats.effect.Concurrent[[x]fs2.Pure[x]]
.parEvalMap(5)(saveNumber(connection))
> ```

and 

> [!ERROR]
> ```
> /Users/jcemisovs/IdeaProjects/Bootcamp/FS2 Streaming Mastery Journey/src/main/scala/Exercise 3: Concurrent Effectful Streams with Resource Management.sc:68:19
could not find implicit value for parameter F: cats.effect.Concurrent[[x]fs2.Pure[x]]
.parEvalMap(5)(saveNumber(connection))
> ```

---

The errors you're encountering are related to the type mismatch and missing implicit values for concurrent operations in FS2. Let's address these issues step by step.

### Issue 1: Type Mismatch

The error `type mismatch; found: cats.effect.IO[Unit] required: fs2.Pure[?]` indicates that the stream is expecting a pure computation, but you're providing an effectful one (`IO`).

### Issue 2: Missing Implicit Value

The error `could not find implicit value for parameter F: cats.effect.Concurrent[[x]fs2.Pure[x]]` indicates that the stream is not configured to handle concurrent effects.

### Solution:

To resolve these issues, you need to ensure that your stream is configured to handle effectful computations with `IO`. This involves using the correct type for the stream and providing the necessary implicit values.

Here's the corrected version of your code:

```scala
import fs2.Stream
import cats.effect.{IO, IOApp}
import cats.effect.unsafe.implicits.global

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

final case class DatabaseConnection(connection: String) extends AnyVal

def sleepUpTo(maxDuration: FiniteDuration) =
  Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

val acquire = IO {
  val conn = DatabaseConnection("test-db")
  println(s"Acquiring connection to the database: $conn")
  sleepUpTo(1.seconds)
  conn
}

val release = (conn: resource_example.DatabaseConnection) =>
  IO.println(s"Releasing connection to the database: $conn")

// Define the logging effect
def saveNumber(connection: resource_example.DatabaseConnection)(n: Int): IO[Unit] = IO({
  println(f"Saving number: $n%02d to $connection.\t Time: ${LocalTime.now()}")
  sleepUpTo(2.second)
})

// Create the stream and apply the logging effect with concurrency
def numStream(connection: resource_example.DatabaseConnection): Stream[IO, Unit] =
  Stream.range(1, 11)
    .covary[IO]
    .parEvalMap(5)(saveNumber(connection)) // Process up to 5 elements concurrently

Stream.bracket(acquire)(release)
  .flatMap(connection => numStream(connection))
  .compile
  .drain
  .unsafeRunSync()
```

### Explanation of Changes:

1. **Stream Type**: The stream is now explicitly typed as `Stream[IO, Unit]`, indicating that it handles effectful computations with `IO`.

2. **`covary[IO]`**: This method is used to change the effect type of the stream to `IO`.

3. **Implicit Values**: By using `IOApp` and importing `cats.effect.unsafe.implicits.global`, you provide the necessary implicit values for concurrent operations.

Try running this corrected version, and it should resolve the compilation errors. If you have any further questions or need additional assistance, feel free to ask!

---

> Yes, it works! Should it always print numbers in order?
The output is:
```
Acquiring connection to the database: DatabaseConnection(test-db)
Saving number: 01 to DatabaseConnection(test-db).	 Time: 13:35:04.855320
Saving number: 02 to DatabaseConnection(test-db).	 Time: 13:35:04.855445
Saving number: 03 to DatabaseConnection(test-db).	 Time: 13:35:04.855602
Saving number: 04 to DatabaseConnection(test-db).	 Time: 13:35:04.855769
Saving number: 05 to DatabaseConnection(test-db).	 Time: 13:35:04.855915
Saving number: 06 to DatabaseConnection(test-db).	 Time: 13:35:06.443113
Saving number: 07 to DatabaseConnection(test-db).	 Time: 13:35:06.443915
Saving number: 08 to DatabaseConnection(test-db).	 Time: 13:35:06.444655
Saving number: 09 to DatabaseConnection(test-db).	 Time: 13:35:06.778728
Saving number: 10 to DatabaseConnection(test-db).	 Time: 13:35:06.779926
Releasing connection to the database: DatabaseConnection(test-db)
```

---

The output you're seeing is a result of concurrent processing. When you use `parEvalMap`, the stream processes multiple elements simultaneously, which can lead to out-of-order execution depending on how long each operation takes.

### Explanation:

- **Concurrency**: The `parEvalMap` operator allows multiple elements to be processed concurrently. In your case, up to 5 elements can be processed at the same time.

- **Out-of-Order Execution**: Since each `saveNumber` operation includes a random sleep duration (`sleepUpTo`), some operations may finish earlier than others, leading to out-of-order logging.

- **Order of Logging**: The order in which the log messages appear depends on when each concurrent operation completes. This is expected behavior in concurrent processing.

### Controlling Order:

If you need the operations to complete in a specific order while still benefiting from concurrency, you would need to introduce additional synchronization mechanisms. However, this would typically go against the benefits of concurrency, which is to improve throughput by processing elements independently.

### Summary:

- The current behavior is expected due to concurrent processing.
- If strict ordering is required, concurrency might not be the best approach, or additional mechanisms would be needed to enforce order.

If you have any further questions or need additional modifications, feel free to ask!

---

> I run several times but the order is same. I've increased sleep time for 4 seconds. The last output:
```
Acquiring connection to the database: DatabaseConnection(test-db)
Saving number: 01 to DatabaseConnection(test-db).	 Time: 13:36:02.334309
Saving number: 02 to DatabaseConnection(test-db).	 Time: 13:36:02.334458
Saving number: 03 to DatabaseConnection(test-db).	 Time: 13:36:02.334654
Saving number: 04 to DatabaseConnection(test-db).	 Time: 13:36:02.334847
Saving number: 05 to DatabaseConnection(test-db).	 Time: 13:36:02.335037
Saving number: 06 to DatabaseConnection(test-db).	 Time: 13:36:04.293198
Saving number: 07 to DatabaseConnection(test-db).	 Time: 13:36:05.972449
Saving number: 08 to DatabaseConnection(test-db).	 Time: 13:36:05.973478
Saving number: 09 to DatabaseConnection(test-db).	 Time: 13:36:05.974024
Saving number: 10 to DatabaseConnection(test-db).	 Time: 13:36:05.974632
Releasing connection to the database: DatabaseConnection(test-db)
```

---

The consistent order you're observing, despite increasing the sleep duration, suggests that the concurrency might not be fully utilized as expected. Let's investigate potential reasons and solutions:

### Potential Reasons:

1. **Concurrency Level**: The concurrency level (`5` in `parEvalMap(5)`) might be too high relative to the number of elements (`10`), leading to all elements being processed in the order they are emitted.

2. **Sleep Duration**: If the sleep duration is consistent and short, the operations might complete in the order they start, even with concurrency.

3. **Environment**: The execution environment (e.g., JVM settings, available CPU cores) can affect how concurrency is handled.

### Solutions to Verify Concurrency:

1. **Reduce Concurrency Level**: Try reducing the concurrency level to see if it affects the order. For example, use `parEvalMap(2)` to limit the number of concurrent operations.

2. **Vary Sleep Duration**: Introduce more variability in the sleep duration to simulate different processing times for each element.

3. **Check Execution Environment**: Ensure that your environment supports concurrent execution (e.g., sufficient CPU cores).

### Modified Code with Reduced Concurrency Level:

```scala
import fs2.Stream
import cats.effect.{IO, IOApp}
import cats.effect.unsafe.implicits.global

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

final case class DatabaseConnection(connection: String) extends AnyVal

def sleepUpTo(maxDuration: FiniteDuration) =
  Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

val acquire = IO {
  val conn = DatabaseConnection("test-db")
  println(s"Acquiring connection to the database: $conn")
  sleepUpTo(1.seconds)
  conn
}

val release = (conn: resource_example.DatabaseConnection) =>
  IO.println(s"Releasing connection to the database: $conn")

// Define the logging effect with varied sleep duration
def saveNumber(connection: resource_example.DatabaseConnection)(n: Int): IO[Unit] = IO({
  println(f"Saving number: $n%02d to $connection.\t Time: ${LocalTime.now()}")
  sleepUpTo(4.seconds) // Increased sleep duration for more variability
})

// Create the stream and apply the logging effect with reduced concurrency
def numStream(connection: resource_example.DatabaseConnection): Stream[IO, Unit] =
  Stream.range(1, 11)
    .covary[IO]
    .parEvalMap(2)(saveNumber(connection)) // Reduced concurrency level

Stream.bracket(acquire)(release)
  .flatMap(connection => numStream(connection))
  .compile
  .drain
  .unsafeRunSync()
```

### Explanation of Changes:

- **Concurrency Level**: Reduced to `2` to see if it affects the order of execution.
- **Sleep Duration**: Increased variability in sleep duration to simulate different processing times.

Try running this modified version and observe the output. If you still encounter issues or need further assistance, feel free to ask!

---

> I have recreate the solution as an app and now the output is random:

```scala
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import java.time.LocalTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.math.abs
import scala.util.Random

/**
 * Exercise 3: Concurrent Effectful Streams with Resource Management
 *
 * Objective: Create a stream that concurrently processes elements with a logging effect and manages resources properly.
 *
 * Instructions:
 *
 * Create a stream that emits integers from 1 to 10.
 * For each integer, simulate a resource-intensive operation (e.g., reading from a file or making an HTTP request) using a logging effect.
 * Use concurrency to process multiple elements simultaneously.
 * Ensure that resources are properly acquired and released.
 * Test Data and Tests:
 *
 * Verify that the logging effect is applied to each element.
 * Ensure that the stream processes elements concurrently.
 * Check that resources are acquired and released correctly.
 *
 * Hints:
 *
 * Use Stream.bracket to manage resource acquisition and release.
 * Use .parEvalMap or .async to introduce concurrency.
 * Simulate a resource-intensive operation with a delay using IO.sleep.
 */
final case class DatabaseConnection(connection: String) extends AnyVal

object Solution3 extends IOApp {
  def sleepUpTo(maxDuration: FiniteDuration): Unit =
    Thread.sleep(abs(Random.nextLong()) % maxDuration.toMillis)

  val acquire: IO[resource_example.DatabaseConnection] = IO {
    val conn = DatabaseConnection("test-db")
    println(s"Acquiring connection to the database: $conn")
    sleepUpTo(1.seconds)
    conn
  }

  val release: resource_example.DatabaseConnection => IO[Unit] = (conn: resource_example.DatabaseConnection) =>
    IO.println(s"Releasing connection to the database: $conn")

  // Define the logging effect
  def saveNumber(connection: resource_example.DatabaseConnection)(n: Int): IO[Unit] = IO({
    println(f"Saving number: $n%02d to $connection.\t Time: ${LocalTime.now()}")
    sleepUpTo(4.second)
  })

  // Create the stream and apply the logging effect with concurrency
  def numStream(connection: resource_example.DatabaseConnection): Stream[IO, Unit] =
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

> The last output is
```
Acquiring connection to the database: DatabaseConnection(test-db)
Saving number: 01 to DatabaseConnection(test-db).	 Time: 13:43:49.529970
Saving number: 03 to DatabaseConnection(test-db).	 Time: 13:43:49.530434
Saving number: 04 to DatabaseConnection(test-db).	 Time: 13:43:49.530691
Saving number: 05 to DatabaseConnection(test-db).	 Time: 13:43:49.530972
Saving number: 02 to DatabaseConnection(test-db).	 Time: 13:43:49.530195
Saving number: 06 to DatabaseConnection(test-db).	 Time: 13:43:52.032106
Saving number: 07 to DatabaseConnection(test-db).	 Time: 13:43:52.034848
Saving number: 08 to DatabaseConnection(test-db).	 Time: 13:43:52.035502
Saving number: 09 to DatabaseConnection(test-db).	 Time: 13:43:53.093849
Saving number: 10 to DatabaseConnection(test-db).	 Time: 13:43:53.094680
Releasing connection to the database: DatabaseConnection(test-db)
```

---

Great! The random order of the output indicates that concurrency is now working as expected. The elements are being processed simultaneously, and the order of completion depends on the random sleep duration for each operation.

### Key Observations:

1. **Concurrency**: The use of `parEvalMap(5)` allows up to 5 elements to be processed concurrently. This is evident from the interleaved log messages.

2. **Resource Management**: The `Stream.bracket` ensures that the `DatabaseConnection` resource is properly acquired and released, even with concurrent processing.

3. **Random Sleep Duration**: The `sleepUpTo` function introduces variability in processing times, leading to the random order of log messages.

### Conclusion:

Your implementation successfully demonstrates concurrent processing with proper resource management in FS2. The random order of log messages confirms that elements are being processed concurrently, which is the expected behavior.

### Next Steps:

- If you're satisfied with this exercise, we can move on to more advanced topics in FS2, such as handling errors in streams, working with different stream types, or exploring more complex concurrency patterns.
- If you have any further questions or need additional modifications, feel free to ask!

---

> Let's move to the next topic

---

[[Exercise 4 - Error Handling in FS2 Streams]]