# Exercise: Accumulating State in a Stream

## Task

Create an FS2 stream that processes a list of integers and accumulates their sum as state. The stream should output each intermediate state along with the current integer being processed.

## Completion Criteria

- [ ] Use the evalScan operator to accumulate the sum of integers.
- [ ] Ensure that the state update function returns an effectful computation (e.g., IO).
- [ ] Output each intermediate state along with the current integer being processed.

### Example Input

```scala worksheet
val input = List(1, 2, 3, 4, 5)
```

### Expected Output

```scala worksheet
List((1, 1), (3, 2), (6, 3), (10, 4), (15, 5))
```

## Hints

- Use Stream.emits to create a stream from the input list.
- Use evalScan to accumulate the sum.
- The state update function should return an IO effect.