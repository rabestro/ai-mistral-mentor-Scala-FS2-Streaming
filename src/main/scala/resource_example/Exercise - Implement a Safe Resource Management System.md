🏆 Exercise - Implement a Safe Resource Management System


**Objective:**

You will create a **resource-safe** system that simulates opening and closing a **database connection** and a **file reader** using Resource in Cats Effect.

---

**Task 1: Implement a Safe Database Connection**

1. Create a **DatabaseConnection** class that simulates:

- [ ] Opening a connection (println("DB connected"))
- [ ] Closing the connection (println("DB closed"))
- [ ] Querying data (IO(println("Querying data...")))

2. Use **Resource.make** to ensure the connection is **acquired and released safely**.

---

**Task 2: Implement a Safe File Reader**

- [ ] Use **Resource** to safely open and close a file.
- [ ] Read lines from the file inside Resource.use.

---

**Task 3: Compose Resources**

- [ ] Open both the database and file **together** using for-comprehension.
- [ ] Print "Processing data from DB and File..." after acquiring both.

---

**Requirements:**

✅ Use Resource.make for proper cleanup.
✅ Ensure the file and DB close **even if an error occurs**.
✅ Combine both resources in for-comprehension.

---

**Starter Template:**

```scala
import cats.effect.{IO, IOApp, Resource}
import resource_example.DatabaseConnection

import scala.io.Source

// Task 1: Implement DatabaseConnection
class DatabaseConnection {
  def queryData: IO[Unit] = IO(println("Querying data..."))
}

def dbResource: Resource[IO, DatabaseConnection] = ???

// Task 2: Implement File Reader
def fileResource(filePath: String): Resource[IO, Source] = ???

// Task 3: Compose both resources
val program: IO[Unit] = ???

// Run
object ResourceExample extends IOApp.Simple {
  def run: IO[Unit] = program
}
```



---

💡 **Bonus:** Try adding a delay (IO.sleep(2.seconds)) to simulate real-world latency!

