package main.scala

import main.utils.FileUtils.parseFile

object Day02 extends App {
  // Parse the input file into an array of integer arrays
  // Each line is split on spaces and converted to integers
  private val parsedRows =
    parseFile("src/main/resources/day02.txt")(
      // For each line in the file:
      //   - split the line on spaces into Array[String]
      //   - map each string to Int
      //   - convert final collection of Int arrays to an Array[Array[Int]]
      _.map(_.split(' ').map(_.toInt)).toArray
      // Handle Success/Failure cases using fold:
      //   - First argument handles Failure case (throws exception)
      //   - Second argument (identity) just returns the value in Success case
    ).fold(
      ex => throw new IllegalStateException(s"Failed to parse input file: ${ex.getMessage}", ex),
      identity
    )

  /**
   * Checks if a sequence of numbers follows a valid pattern:
   * - Each adjacent difference must be between 1-3 (inclusive)
   * - All differences must maintain the same direction (all increasing or all decreasing)
   */
  private def isRowSafe(row: Array[Int]): Boolean = {
    // Tracks both whether sequence is valid and the direction of changes
    case class State(isSafe: Boolean, direction: Option[Int])

    row.zip(row.tail) // Pair each number with its next number
      .map { case (prev, current) => prev - current } // Calculate differences
      .foldLeft(State(isSafe = true, direction = None)) {
        // Short-circuit if we've already determined sequence is invalid
        case (State(false, _), _) =>
          State(isSafe = false, None)

        // First difference sets the direction
        case (State(true, None), level) =>
          val absLevel = math.abs(level)
          if (absLevel >= 1 && absLevel <= 3) {
            State(isSafe = true, Some(level.sign))
          } else {
            State(isSafe = false, None)
          }

        // Subsequent differences must match direction and magnitude constraints
        case (State(true, Some(prevDirection)), level) =>
          val absLevel = math.abs(level)
          val newDirection = level.sign
          State(
            isSafe = absLevel >= 1 && absLevel <= 3 && newDirection == prevDirection,
            direction = Some(newDirection)
          )
      }.isSafe
  }

  /**
   * Part 1: Count how many rows follow the safe pattern
   */
  private def partOneSolution(rows: Array[Array[Int]]): Unit = {
    val result = rows
      .map {
        case (row) => {
          if (isRowSafe(row)) 1 else 0
        }
      }.sum
    println(s"Part 1 result: $result")
  }

  /**
   * Part 2: For each row, count if removing any single number
   * can create a sequence that follows the safe pattern
   */
  private def partTwoSolution(rows: Array[Array[Int]]): Unit = {
    val result = rows
      .map {
        case (row) => {
          // Generate all possible sequences by removing one number at a time
          val possibleRows = row.indices.map(i => row.take(i) ++ row.drop(i + 1)).toArray
          // Check if any of the possible sequences are safe
          if (possibleRows.exists(isRowSafe)) 1 else 0
        }
      }.sum

    println(s"Part 2 result: $result")
  }

  // Run solutions
  partOneSolution(parsedRows)
  partTwoSolution(parsedRows)
}