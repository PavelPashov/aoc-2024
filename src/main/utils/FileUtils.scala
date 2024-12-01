package main.utils

import scala.io.Source
import scala.util.{Using, Try}

object FileUtils {
  /**
   * Generic file parser that returns a Try of any type
   */
  def parseFile[A](filePath: String)(process: Iterator[String] => A): Try[A] = {
    Using(Source.fromFile(filePath)) { source =>
      process(source.getLines())
    }
  }
}

