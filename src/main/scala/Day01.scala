package main.scala

import main.utils.FileUtils.parseFile

import scala.util.{Failure, Success}

object Day01 extends App {
  private val lineNumberRegex = """(\d+)\s+(\d+)""".r

  private val parsedColumns = parseFile("src/main/resources/day01.txt") { lines =>
    lines.foldLeft((List.empty[Int], List.empty[Int])) {
      case ((leftColumn, rightColumn), line) =>
        lineNumberRegex.findFirstMatchIn(line) match {
          case Some(m) => (m.group(1).toInt :: leftColumn, m.group(2).toInt :: rightColumn)
          case None => (leftColumn, rightColumn)
        }
    }
  }

  private def partOneSolution(leftColumn: List[Int], rightColumn: List[Int]): Unit = {
    val result = leftColumn.sorted.zip(rightColumn.sorted)
      .map { case (id1, id2) => Math.abs(id1 - id2) }
      .sum

    println(s"Part 1 result: $result")
  }

  private def partTwoSolution(leftColumn: List[Int], rightColumn: List[Int]): Unit = {
    val countRightMap = rightColumn.groupBy(identity).view.mapValues(_.length).toMap
    val result = leftColumn
      .map {
        case (id) => id * countRightMap.getOrElse(id, 0)
      }.sum

    println(s"Part 2 result: $result")
  }

  parsedColumns
  match {
    case Success((leftColumn, rightColumn)) =>
      partOneSolution(leftColumn, rightColumn)
      partTwoSolution(leftColumn, rightColumn)
    case Failure(ex) =>
      println(s"Failed to process file: ${ex.getMessage}")
  }
}
