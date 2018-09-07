package com.ebills.implic

import scala.math.Ordering

object MyPreDef {
  implicit def boy2Ordered(boy: Boy) = new Ordered[Boy]{
    override def compare(that: Boy): Int = {
      boy.age - that.age
    }
  }

  trait BoyOrdering extends Ordering[Boy] {
    def compare(x: Boy, y: Boy) =
      x.age - y.age
  }
  implicit object Boy extends BoyOrdering


}
