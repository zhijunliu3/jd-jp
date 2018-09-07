package com.ebills.implic

object Context {
  implicit val name:String = "ljj"
}

object ImplicitValue {
  def m()(implicit name:String = "lzj"): Unit ={
    println(s"say Hi! $name")
  }

  import Context._
  def main(args: Array[String]): Unit = {
    m()
  }
}
