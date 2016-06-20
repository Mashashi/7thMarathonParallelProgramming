package de

import java.util.concurrent.CountDownLatch
import scala.collection.mutable.ListBuffer

class Bucket(val latch: CountDownLatch, var elems: ListBuffer[(String, BigDecimal)]) extends Runnable{ 
  override def run(){
    elems = elems.sorted
    latch.countDown()
  }
}