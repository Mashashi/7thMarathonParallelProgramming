package de

import java.util.concurrent.CountDownLatch
import scala.collection.mutable.ListBuffer

class SplitChunk(val chunk: List[(String, BigDecimal)], val setupJob: CountDownLatch)(implicit buckets: ListBuffer[Bucket], xs: IndexedSeq[Double]) extends Runnable{
  override def run(){
    for {
      i <- chunk
      p = xs.map(x=>(x,(x-i._2))).filter({case (_,diff)=>diff>=0})(0)
    }
    { 
      val l = buckets(xs.indexOf(p._1)).elems
      l.synchronized{
        l+=i
      } 
    }
    setupJob.countDown()
  }
}