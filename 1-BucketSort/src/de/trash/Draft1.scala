package de

/*import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import scala.collection.mutable.MutableList
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch
import java.util.NoSuchElementException
import scala.collection.mutable.SynchronizedBuffer
import scala.collection.mutable.ArrayBuffer


class Bucket(val latch: CountDownLatch, var elems: ListBuffer[(String, BigDecimal)]) extends Runnable{
    
  override def run(){
    elems = elems.sorted
    latch.countDown()
  }
  
  //var sorted : List[T] = Nil
  /*def bubbleSort(a: List[T]) : List[T] = {
    println("lol")
    a match {
      case e1::e2::t if (e1 > e2) => e2 :: bubbleSort(e1::t)
      case e1::e2::t if (e1 <= e2) => e1 :: bubbleSort(e2::t)
      case Nil => Nil
    }
  }*/
  
}

//val buckets = new ListBuffer[String]()

/*
 * http://stackoverflow.com/questions/25759778/scala-list-comprehension-taking-two-elements-at-a-time
 * TODO:
 * + Parallelize setup code section to reduce sequential code
 **/
object Main extends App{
  
  var start = System.nanoTime();
  
  // START - CONFIG
    //val numBuckets = 94
    val numBuckets = 10
    val fileName = "medium.in"
    val numberThreads = 1
    val dataSetStrLen = 7
  // END - CONFIG
  
  val pool: ExecutorService = Executors.newFixedThreadPool(numberThreads)
  
  val setupJob = new CountDownLatch(numBuckets);
  val latchJob = new CountDownLatch(numBuckets);
  
  val step: Double = 1.0 / numBuckets
  
  val xs = {
    var res = {
      for {
        i <- step to 1 by step
      } yield i 
    }
    if(res.last != 1) res = res.patch(res.size-1, Seq(1.0), 1)
    res
  }
  
  val buckets = new ListBuffer[Bucket]()
  
  for{ i <- xs } { buckets += new Bucket(latchJob, ListBuffer()) }
  
  val lines = scala.io.Source.fromFile(s"c_sequential/bucketsort/input/${fileName}").mkString.split("\n").drop(1) // first line is the total number of elements
  
  // The actual computation will happen when next is called on the iterator
  // So the sequential section can potentially be reduced by calculating the resultant 
  // array in a thread pool
  val normalized = { 
    val numeric = {
      val asciiStart = '!'.toByte
      val asciiNewLine = '\n'.toByte
      val asciiCarriageReturn = '\r'.toByte
      val base : BigDecimal = 256
      //s"(${c.toByte}-${asciiStart}) * ${base}^${i.next()}"
      //(base.pow(i.next())) * (c.toByte-asciiStart)  
      for{
        g <- {
              for {
                line <- lines
                i = (dataSetStrLen-1 to 0 by -1).iterator
                c <- line
                if c != asciiCarriageReturn
              } yield (c, (base.pow(i.next())) * (c.toByte-asciiStart)) 
            }.grouped(dataSetStrLen)
      } yield (g.foldLeft("")(_+_._1), g.foldLeft(BigDecimal(0))(_+_._2))
    }.toList
    
    val max = numeric.maxBy(_._2)
    numeric.map({case (x,y)=>(x,y/max._2)})
  }
  
  /*
  println(numeric.next())
  println(numeric.next())
  */
  //println(s"${numeric(0)} + ${numeric(1)} + ${numeric(2)} + ${numeric(3)} + ${numeric(4)} + ${numeric(5)} + ${numeric(6)}")
  //println(s"${numeric(7)} + ${numeric(8)} + ${numeric(9)} + ${numeric(10)} + ${numeric(11)} + ${numeric(12)} + ${numeric(13)}")
  //println(normalized(1))
  //println(xs)
  
  for {
    i <- normalized
    p = xs.map(x=>(x,(x-i._2))).filter({case (_,diff)=>diff>=0})(0)
  } {
    buckets(xs.indexOf(p._1)).elems += i
  } 
  
  println("setup - "+(System.nanoTime()-start))
  
  start = System.nanoTime();
  for { b <- buckets } { pool.execute(b) }
  latchJob.await()
  //for {b <- buckets} { println(b.elems) }
  println("job - "+(System.nanoTime()-start))
  
  
  
}*/