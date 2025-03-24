### Exercise 2: Effectful Streams  
  
**Objective**: Create a stream that emits a side effect (e.g., logging) for each element.  
  
**Instructions**:  
- Modify the previous stream to log each number using a logging effect instead of printing directly to the console.  
- Use a simple logging effect that prints to the console with a prefix (e.g., `"Logging number: "`).  
  
**Test Data and Tests**:  
- Ensure the log messages are correct and include the prefix.  
- Verify the order of logging is correct.  
  
**Hints**:  
- You can define a simple logging effect using a function.  
- Use `.evalMap` to apply the logging effect to each element in the stream.  
  
Please go ahead and solve this exercise. Once you're done, submit your solution, and I'll review it and provide feedback. If you have any questions or need hints along the way, feel free to ask!

---

> 👨🏻‍🎓 I'm stuck with this exercise. Please explain the theory so I can solve it practically.

---

Sure! Let's dive into the theory behind effectful streams in FS2 and how you can apply effects to each element in a stream.

### Theory: Effectful Streams in FS2

In FS2, streams can be more than just a sequence of values; they can also represent sequences of effects. An effect in this context is any computation that has a side effect, such as logging, interacting with I/O, or modifying state.

#### Key Concepts:

1. **Pure vs. Effectful Streams**:
   - **Pure Streams**: These are streams that produce values without any side effects. They are deterministic and can be reasoned about purely in terms of their input and output.
   - **Effectful Streams**: These streams produce values and also perform side effects. They are useful for interacting with the outside world, such as reading from a file or logging to the console.

2. **Evaluating Effects**:
   - To apply an effect to each element in a stream, you can use the `evalMap` operator. This operator takes a function that returns an effect (e.g., `F[B]`) and applies it to each element in the stream.

3. **Effects in Scala**:
   - In Scala, effects are often represented using types like `IO`, `Task`, or `Future`. For simplicity, we'll use `IO` from the `cats.effect` library, which is commonly used with FS2.

#### Example: Logging Effect

Let's consider a simple logging effect. We want to log each number in the stream with a prefix. Here's how you can define and use such an effect:

- **Define the Logging Effect**: Create a function that takes an integer and returns an `IO` effect that logs the number.
- **Apply the Effect**: Use `evalMap` to apply the logging effect to each element in the stream.

### Steps to Solve the Exercise:

1. **Import Necessary Libraries**: Make sure you have the necessary imports for FS2 and `cats.effect.IO`.

2. **Define the Logging Effect**: Create a function that logs a number using `IO`.

3. **Apply the Effect**: Use `evalMap` to apply the logging effect to each element in the stream.

4. **Run the Stream**: Compile and run the stream to see the logging output.

### Example Code Structure:

```scala
import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

// Define the logging effect
def logNumber(n: Int): IO[Unit] = IO(println(s"Logging number: $n"))

// Create the stream and apply the logging effect
val loggedStream = Stream.range(1, 11).evalMap(logNumber)

// Run the stream
loggedStream.compile.drain.unsafeRunSync()
```

### Explanation:

- **`logNumber` Function**: This function takes an integer and returns an `IO` effect that prints the number with a prefix.
- **`evalMap`**: This operator is used to apply the `logNumber` effect to each element in the stream.
- **`compile.drain`**: This runs the stream for its side effects (in this case, logging).
- **`unsafeRunSync`**: This is used to run the `IO` effect and block until it completes.

With this understanding, you should be able to solve the exercise. If you have any further questions or need more clarification, feel free to ask!

---

> I use your solution:
```
import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

// Define the logging effect
def logNumber(n: Int): IO[Unit] = IO(println(s"Logging number: $n"))

// Create the stream and apply the logging effect
val loggedStream = Stream.range(1, 11).evalMap(logNumber)

// Run the stream
loggedStream.compile.drain.unsafeRunSync()

```

> It seems I need one more exercise for this topic. May be a more complex. 

---

[[Exercise 3 - Concurrent Effectful Streams with Resource Management]]