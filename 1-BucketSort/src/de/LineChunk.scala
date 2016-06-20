package de

import scala.collection.mutable.ListBuffer
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.CountDownLatch

object LineChunk{
  val asciiStart = '!'.toByte
  val asciiNewLine = '\n'.toByte
  val asciiCarriageReturn = '\r'.toByte
  val base : BigDecimal = 256
  //val progress = new AtomicInteger(0) 
  val tasks = ListBuffer[LineChunk]()
}

class LineChunk(val chunk: Array[String], val setupJob: CountDownLatch)(implicit dataSetStrLen: Int) extends Runnable{
  var result: List[(String, BigDecimal)] = null
  LineChunk.tasks += this
  override def run(){
    val tmp =
      for {
        g <- {
              for {
                line <- chunk
                i = (dataSetStrLen-1 to 0 by -1).iterator
                c <- line
                if c != LineChunk.asciiCarriageReturn
              } yield (c, (LineChunk.base.pow(i.next())) * (c.toByte-LineChunk.asciiStart)) 
            }.grouped(dataSetStrLen)
      } yield (g.foldLeft("")(_+_._1), g.foldLeft(BigDecimal(0))(_+_._2))
    result = tmp.toList
    //println("done "+LineChunk.progress.incrementAndGet())
    setupJob.countDown()
  }
}