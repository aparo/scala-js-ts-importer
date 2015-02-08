package org.scalajs.tools.tsimporter

import java.io.File


case class Config(tsFiles: Seq[File] = Seq(), outputDirectory: String = "", outputPackage: String = "importedjs")

object Config {
  val parser = new scopt.OptionParser[Config]("scopt") {
    head("scalajs-ts-importer")
    opt[String]("package") action {
      (x, c) => c.copy(outputPackage = x)
    } text "Package name for the output (defaults to 'importedjs')"
    opt[String]("output") action {
      (x, c) => c.copy(outputDirectory = x)
    } text "Output directory for wrote files)"
    arg[File]("<file>...") unbounded() optional() action { (x, c) =>
      c.copy(tsFiles = c.tsFiles :+ x)
    } text ("TypeScript type definition file to read")
  }

  def parse(args: Array[String]): Option[Config] = parser.parse(args, Config())

}