package com.ebills.func

object HighFunc {
  val func:Int=>Int = x=>x*x
  def m1(x:Int) = x*x
  def m2(x:Int)(y:Int) = x*y
  def m3(x:Int)= (y:Int) => x*y
  def m4() = (x:Int)=>x*x

  def main(args: Array[String]): Unit = {
    val arr = Array(1,2,3,4,5)
    println(arr.map(m1).toBuffer)
  }
}
