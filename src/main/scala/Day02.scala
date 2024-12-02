package main.scala

import main.utils.FileUtils.parseFile
import scala.util.{Failure, Success}


object Day02 extends App {
  private val parsedRows =
    parseFile("src/main/resources/day02.txt") { lines =>
      lines.map { line =>
        line.split(' ').map(_.toInt)
      }.toArray
    }
    match {
      case Success(value) =>
        value
      case Failure(ex) =>
        throw new RuntimeException("File processing failed", ex)
    }

  private def isRowSafe(row: Array[Int]): Boolean = {
    val (isSafe, _) = row.zip(row.tail).foldLeft(true, 99) {
      case ((isSafe, direction), (prev, current)) =>
        val level = prev - current
        val absLevel = Math.abs(level)
        val isLevel = (absLevel >= 1 && absLevel <= 3)
        val newDirection = if (level == 0) 0 else if (level > 0) 1 else -1

        if (direction == 99) {
          (isLevel, newDirection)
        } else {
          if (isSafe && direction == newDirection && isLevel) (isSafe, newDirection) else (false, newDirection)
        }
    }
    isSafe
  }

  private def partOneSolution(rows: Array[Array[Int]]): Unit = {
    val result = rows.foldLeft(0) {
      case (sum, row) =>
        val isSafe = isRowSafe(row)
        if (isSafe) sum + 1 else sum
    }
    println(s"Part 1 result: $result")
  }

  private def partTwoSolution(rows: Array[Array[Int]]): Unit = {
    val result = rows.foldLeft(0) {
      case (sum, row) =>
        val possibleRows = row.indices.map(i => row.take(i) ++ row.drop(i + 1)).toArray
        val safeCount = possibleRows.foldLeft(0) {
          case (safeCount, row) =>
            if (isRowSafe(row)) safeCount + 1 else safeCount
        }
        if (safeCount > 0) sum + 1 else sum
    }
    println(s"Part 2 result: $result")
  }

  partOneSolution(parsedRows)
  partTwoSolution(parsedRows)
}
