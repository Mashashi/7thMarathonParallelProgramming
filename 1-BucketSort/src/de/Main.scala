package de

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import scala.collection.mutable.MutableList
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch
import java.util.NoSuchElementException
import scala.collection.mutable.SynchronizedBuffer
import scala.collection.mutable.ArrayBuffer
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.ForkJoinTaskSupport
import scala.concurrent.forkjoin.ForkJoinPool




//val buckets = new ListBuffer[String]()

/*
 * http://stackoverflow.com/questions/25759778/scala-list-comprehension-taking-two-elements-at-a-time
 **/
object Main extends App{
  
  val start = System.nanoTime();
  
  val startSetup = System.nanoTime();
  
  // START - CONFIG
    val numBuckets = 94
    //val numBuckets = 10
    //val fileName = "medium.in"
    val fileName = "small1.in"
    val numberThreads = 7
    implicit val dataSetStrLen = 7
  // END - CONFIG
  
  //val pool: ExecutorService = Executors.newFixedThreadPool(numberThreads)
  val pool: ForkJoinPool = new ForkJoinPool(numberThreads)
  
 
  val latchJob = new CountDownLatch(numBuckets);
  
  val step: Double = 1.0 / numBuckets
  
  implicit val xs = {
    var res = for { i <- step to 1 by step } yield i
    if(res.last != 1) res = res.patch(res.size-1, Seq(1.0), 1)
    res
  }
  
  implicit val buckets = new ListBuffer[Bucket]()
  for{ i <- xs } { buckets += new Bucket(latchJob, ListBuffer()) }
  
  // first line is the total number of elements
  val lines = scala.io.Source.fromFile(s"bucketsort/input/${fileName}").mkString.split("\n").drop(1) 
  
  val normalized = {
    val setupJob = new CountDownLatch(numBuckets);
    val groupSize = Math.round(lines.size.toFloat/numBuckets)
    //println(groupSize +" "+ (lines.grouped(groupSize).toList.size))
    lines.grouped(groupSize).foreach { 
      g => { pool.execute(new LineChunk(g, setupJob)) } 
    }
    setupJob.await()
    val numeric = LineChunk.tasks.foldLeft(List[(String,BigDecimal)]())(_ ++ _.result).par
    numeric.tasksupport = new ForkJoinTaskSupport(pool)
    val max = numeric.maxBy(_._2)
    numeric.map({case (x,y)=>(x,y/max._2)}).toList
  }
  
  {
    val splitJob = new CountDownLatch(numBuckets);
    val groupSize = Math.round(lines.size.toFloat/numBuckets)
    normalized.grouped(groupSize).foreach {
      g => { pool.execute(new SplitChunk(g, splitJob)) } 
    }
    splitJob.await()
  }
  println("setup - "+(System.nanoTime()-startSetup))
  
  //#####################################
  //#####################################
  //#####################################
  val startJob = System.nanoTime();
  for { b <- buckets } { pool.execute(b) }
  latchJob.await()
  //for {b <- buckets} { println(b.elems) }
  println("job - "+(System.nanoTime()-startJob))
  //#####################################
  //#####################################
  //#####################################
  
  println("total - "+(System.nanoTime()-start))
  
  
}