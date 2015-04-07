/* TypeScript importer for Scala.js
 * Copyright 2013-2014 LAMP/EPFL
 * @author  Sébastien Doeraene
 */

package org.scalajs.tools.tsimporter

import java.io.{Console => _, Reader => _, _}

import scala.collection.immutable.PagedSeq

import Trees._

import scala.util.parsing.input._
import parser.TSDefParser

/** Entry point for the TypeScript importer of Scala.js */
object Main {
  def main(args: Array[String]) {
    Config.parse(args).foreach {
      config =>
        config.tsFiles.foreach {
          inputTsFile =>
            val outputFileName = Seq(config.outputDirectory, inputTsFile.getName.replace(".d.ts", ".scala")).mkString(File.separator)

            val definitions = parseDefinitions(readerForFile(inputTsFile))
            println(definitions)

            val output = new PrintWriter(new BufferedWriter(
              new FileWriter(outputFileName)))
            try {
              process(definitions, output, config.outputPackage)
            } finally {
              output.close()
            }
        }
    }
  }

  private def process(definitions: List[DeclTree], output: PrintWriter,
                      outputPackage: String) {
    new Importer(output)(definitions, outputPackage)
  }

  private def parseDefinitions(reader: Reader[Char]): List[DeclTree] = {
    val parser = new TSDefParser
    parser.parseDefinitions(reader) match {
      case parser.Success(rawCode, _) =>
        rawCode

      case parser.NoSuccess(msg, next) =>
        Console.err.println(
          "Parse error at %s\n".format(next.pos.toString) +
            msg + "\n" +
            next.pos.longString)
        sys.exit(2)
    }
  }

  /** Builds a [[scala.util.parsing.input.PagedSeqReader]] for a file
    *
    * @param tsFile name of the file to be read
    */
  private def readerForFile(tsFile: File) = {
    new PagedSeqReader(PagedSeq.fromReader(
      new BufferedReader(new FileReader(tsFile))))
  }
}
