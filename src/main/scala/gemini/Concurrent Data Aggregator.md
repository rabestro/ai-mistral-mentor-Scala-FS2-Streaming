# 🏆Hands-on Exercise: Concurrent Data Aggregator with `Ref`

## Problem Description

Imagine you have a stream of events representing data points from various sources. Your task is to build a concurrent data aggregator that calculates the running average of these data points.

## Specific Requirements

- [ ] Create an FS2 stream that generates a sequence of random integers between 1 and 100.
- [ ] Implement a function that takes this stream and calculates the running average of the integers.
- [ ] Use `Ref` to store and update the running sum and count of the integers.
- [ ] Ensure that the aggregation is thread-safe and can handle concurrent updates.
- [ ] The output stream should emit the current running average after each new data point is processed.

## Completion Criteria

- Your solution should correctly calculate the running average for all data points in the stream.
- The aggregation logic must be thread-safe, preventing race conditions and ensuring accurate results.
- The output stream should emit the running average after each data point is processed.
- Your code should be well-structured, readable, and follow Scala best practices.

## Hints

- Use a case class or a tuple to store the running sum and count in the `Ref`.
- Use `ref.update` to atomically update the running sum and count.
- Use `ref.get` to retrieve the current running sum and count.
- Remember to handle potential division by zero errors.

## Example

If the input stream is `[10, 20, 30]`, the output stream should be `[10.0, 15.0, 20.0]`.