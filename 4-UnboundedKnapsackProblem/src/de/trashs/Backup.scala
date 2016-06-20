package de.trashs

import java.util.concurrent.Executors


/*class Processor(implicit granter:Granter) extends Runnable{
  val interrupt = new AtomicBoolean(false) 
  val epoch = new AtomicInteger(0)
  
  override def run(){
    var task = granter.get(this)
    while(task!=null){
      while(!interrupt.get){
        
      }
      if(interrupt.get){
        synchronized{
          wait()
        }
      }
      task = granter.get(this)
      epoch.incrementAndGet()
    }
  }
  
  def interrupt(initEpoch: Int){
    synchronized{
      if(this.epoch.get(initEpoch) == )
    }
  }
  
}*/
/**
 * 
 * https://en.wikipedia.org/wiki/Knapsack_problem
 * 
 * The unbounded knapsack problem (UKP) places no upper bound on the number of copies of each kind of item and can be formulated as above except for that the only restriction on {\displaystyle x_{i}}  is that it is a non-negative integer.
 * Dynamic Programming - pág.359
 */
object Main extends App{
  
  
  val fileName = "large.in"
  val numberThreads = 1
  
  
  
  
  val start = System.nanoTime()
  //val l =List((1, 60, 10), (2, 100, 20), (3, 120, 30))
  
  type triple = (Int,Int,Int)
  val pool = Executors.newFixedThreadPool(numberThreads)
  
  val (lines, max) ={
    val read = scala.io.Source.fromFile(s"knapsack/input/${fileName}").mkString.split("\r\n")
    val lines = read.drop(1).collect({case x=>val s = x.split(" ");(s(0),s(1));})
    val zipped = (1 to lines.size).iterator.toList.zip(lines).collect({case x => (x._1,x._2._1.toInt,x._2._2.toInt)}).toList
    (zipped, read(0).split(" ")(1).toInt)
  }
  //val lines = List((1, 60, 10), (2, 100, 20), (3, 120, 30))
  println(lines)
  println(max)
  
  def solve(l: List[triple], max: Int, acc: (Long, Long, List[triple])=(0,0,Nil), n:Option[triple]=None): (Long,Long,List[triple]) = {
    val newAcc = {
      val newAccList = n match { case None => acc._3 ; case Some(x) => x :: acc._3 }
      var v = newAccList.foldLeft(0l)((x,y)=>x+y._2)
      var w = newAccList.foldLeft(0l)((x,y)=>x+y._3)
      (v, w, newAccList)
    }
    
    
    //println(newAcc)
    if(newAcc._2>max) {
      return acc
    } else {
      val x1 = l.collect({ case i => solve(l, max, newAcc, Some(i)) })
      val x2 = x1.maxBy(_._1)
      x2
    }
    
  }
  val s = solve(lines, max)
  println(s)
  println("Time: "+((System.nanoTime()-start)/1000000000)+" s")
}