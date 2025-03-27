## Exercise: Shared Counter with Delayed Updates

You need to implement a shared counter that multiple concurrent tasks update, but with a small delay before applying each update.

### Requirements

- [ ] Create a shared counter with an initial value of 0 using Ref[IO, Int].
- [ ] Spawn 10 concurrent tasks, each increasing the counter by 1 **after a random delay** between 100ms and 500ms.
- [ ] Ensure that all updates are applied safely without race conditions.
- [ ] Print the final counter value after all updates are applied.

### Hints

- Use `parTraverse_` to run tasks concurrently.
- Use `IO.sleep` to introduce a delay before updating the counter.
- Generate a random delay using `IO(scala.util.Random.between(100, 500)).map(_.millis)`.
- Use update on Ref[IO, Int] to modify the counter safely.
