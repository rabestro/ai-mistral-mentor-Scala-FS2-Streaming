/**
 * Exercise 5: Error Recovery and Retries in FS2 Streams
 *
 * Objective: Create a stream that retries failed operations a specified number of times before giving up.
 *
 * Instructions:
 *
 * Create a stream that emits integers from 1 to 10.
 * Simulate an operation that fails randomly for some elements.
 * Implement a retry mechanism that retries the operation up to 3 times before giving up.
 * Log the number of retry attempts and handle the final failure gracefully.
 * Test Data and Tests:
 *
 * Verify that the retry mechanism works as expected.
 * Ensure that the stream logs the number of retry attempts.
 * Check that the stream handles the final failure gracefully and continues processing the remaining elements.
 *
 * Hints:
 *
 * Use a combination of handleErrorWith and a loop or recursion to implement retries.
 * Log the retry attempts and handle the final failure using handleErrorWith.
 */

import cats.effect.unsafe.implicits.global
Solution5.run(Nil).unsafeRunSync()