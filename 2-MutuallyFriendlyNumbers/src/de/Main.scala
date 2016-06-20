package de

import scala.annotation.tailrec
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable.SetBuilder
import scala.collection.mutable.HashSet





object Main extends App{
  
  val starts = System.nanoTime()
  
  
  val numberThreads = 5
  val fileName = "large.in"
  val lines = scala.io.Source.fromFile(s"friendly/input/${fileName}").mkString.split("\r\n")
  
  
  // So that when we increase the number of threads we don't get a race condition
  // And when we finnish we don't get to much stranglers 
  implicit val targetTaskEffortSecs = numberThreads/2 
  val pool = Executors.newFixedThreadPool(numberThreads)
  implicit val granter = new Granter()
  implicit val mapping = new ConcurrentHashMap[(Long, Long), HashSet[Long]]()
  
  for
  { i <- lines } 
  { 
    val s = i.split(" ")
    granter.zipped += ((s(0).toLong, s(1).toLong))
  }
  
  for{
    i <- 1 to numberThreads
  } {
    pool.execute(new Processor())
  }
  
  pool.shutdown()
  pool.awaitTermination(365, TimeUnit.DAYS)
  
  println("Took: "+(System.nanoTime()-starts)+" ns")
  
  /*
  println(mapping.size())
  val vals = mapping.values().iterator()
  while(vals.hasNext()){
    val n = vals.next()
    if(n.size!=1){
      println(n)
    }
  }
  */
}
