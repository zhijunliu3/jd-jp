package com.ebills.implic

class MissRight[T] {
  def choose(first: T, second: T)(implicit ord: T => Ordered[T]): T ={
    if(first > second) first else second
  }

  def select(first: T, second: T)(implicit ord: Ordering[T]): T = {
    if(ord.gt(first, second)) first else second
  }

  def random(first: T, second: T)(implicit ord : Ordering[T]): T ={
    import Ordered.orderingToOrdered
    if(first > second) first else second
  }
}

object  MissRight {
  def main(args: Array[String]): Unit = {
    val select = new MissRight[Boy]
    val first = new Boy("zhangsan",20)
    val second = new Boy("lisi",18)
    import MyPreDef._
    //println(select.choose(first, second).name)
    println(select.select(first, second).name)
  }
}
