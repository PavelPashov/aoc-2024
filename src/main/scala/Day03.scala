package main.scala

import main.utils.FileUtils.parseFile

sealed trait Operation
object Operation {
  case object Do extends Operation
  case object Dont extends Operation
  case object Mul extends Operation

  def fromString(str: String): Operation = str.take(3).toUpperCase match {
    case "MUL" => Mul
    case "DON" => Dont
    case _ => Do
  }
}

object Day03 extends App {
  private val instructions = {
    parseFile("src/main/resources/day03.txt")(
      _.mkString
    ).fold(
      ex => throw new IllegalStateException(s"Failed to parse input file: ${ex.getMessage}", ex),
      identity
    )
  }

  private def multiplyStringNumbersPair(string: String) = {
    """(\d+),(\d+)""".r.findFirstMatchIn(string) match {
      case Some(m) => m.group(1).toInt * m.group(2).toInt
      case None => 0
    }
  }

  private def multiplyInstructions(instructions: Iterator[String]): Int = {
    case class State(sum: Int, instruction: Operation)

    instructions.foldLeft(State(0, Operation.Do)) {
      case (State(sum, Operation.Do), str) =>
        Operation.fromString(str) match {
          case Operation.Dont => State(sum, Operation.Dont)
          case Operation.Mul => State(sum + multiplyStringNumbersPair(str), Operation.Do)
          case Operation.Do => State(sum, Operation.Do)
        }
      case (State(sum, Operation.Dont), str) =>
        Operation.fromString(str) match {
          case Operation.Do => State(sum, Operation.Do)
          case _ => State(sum, Operation.Dont)
        }
    }.sum
  }

  private def partOneSolution(instructions: String): Unit = {
    val instructionPattern = """mul\(\d+,\d+\)""".r
    val parsedInstructions: Iterator[String] = instructionPattern.findAllIn(instructions)
    val result = multiplyInstructions(parsedInstructions)

    println(s"Part 1 result: $result")
  }

  private def partTwoSolution(instructions: String): Unit = {
    val instructionPattern = """mul\(\d+,\d+\)|do\(\)|don't\(\)""".r
    val parsedInstructions: Iterator[String] = instructionPattern.findAllIn(instructions)
    val result = multiplyInstructions(parsedInstructions)

    println(s"Part 2 result: $result")
  }

  partOneSolution(instructions)
  partTwoSolution(instructions)
}
