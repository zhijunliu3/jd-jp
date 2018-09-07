package com.ebills.implic

import MyPreDef._

/*class Select[T <% Ordered[T]] {
  def choose(first:T, second:T): T ={
    if(first>second) first else second
  }
}*/

class Select[T : Ordering] {
  def choose(first:T, second:T): T ={
    val ord = implicitly[Ordering[T]]
    if(ord.gt(first,second)) first else second
  }
}

object Select{
  def main(args: Array[String]): Unit = {
    val select = new Select[Boy]
    val first = new Boy("zhangsan",20)
    val second = new Boy("lisi",18)
    println(select.choose(first, second).name)
  }
}