package com.ebills

import java.io.File

import scala.io.Source

object MyPredef{
  implicit def m(f:File) = new RichFile(f)
}

class RichFile(val f:File){
  def read() = Source.fromFile(f).mkString
}

object RichFile {
  def main(args: Array[String]): Unit = {
    val f = new File("E://aaa.txt")
    import MyPredef._
    println(f.read())
  }
}
