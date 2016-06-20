package de

import scala.collection.parallel.ParSeq
import java.util.concurrent.Executors
import scala.collection.mutable.ListBuffer
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.Random
import scala.util.control.Breaks._

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
  
  
  val fileName = "costum.in"
  val numberThreads = 5
  
  
  
  
  val start = System.nanoTime()
  //val l =List((1, 60, 10), (2, 100, 20), (3, 120, 30))
  
  type triple = (Int,Int,Int)
  type rank = (Long,Long,List[triple])
  
  val (lines, max) ={
    val read = scala.io.Source.fromFile(s"knapsack/input/${fileName}").mkString.split("\r\n")
    val lines = read.drop(1).collect({case x=>val s = x.split(" ");(s(0),s(1));})
    val zipped = (1 to lines.size).zip(lines).collect({case x => (x._1,x._2._1.toInt,x._2._2.toInt)}).toList
    (zipped, read(0).split(" ")(1).toInt)
  }
  //val lines = List((1, 60, 10), (2, 100, 20), (3, 120, 30))
  //println(lines)
  //println(max)
  
  val pool = Executors.newFixedThreadPool(numberThreads)
  
  def solve(l: List[triple], max: Int, acc: rank=(0,0,Nil), n:Option[triple]=None): rank = {
    val newAcc = {
      val newAccList = n match { case None => acc._3 ; case Some(x) => x :: acc._3 }
      var v = newAccList.foldLeft(0l)((x,y)=>x+y._2)
      var w = newAccList.foldLeft(0l)((x,y)=>x+y._3)
      (v, w, newAccList)
    }
    
    trait Taskable {
      def getTasks() : ListBuffer[List[triple]]
    }
    //println(newAcc)
    if(newAcc._2>max) {
      return acc
    } else {
      //println(active.get+" "+numberThreads)
      if(n==None){
        
        val itemsTask = Math.ceil(l.size.toDouble/numberThreads).toInt
        //val itemsTask = 1
        val tasks = l.grouped(itemsTask).toList
        val futures = new ListBuffer[Future[rank]]()
        val callables = new ListBuffer[Callable[rank] with Taskable]()
        for{ i<-tasks }
        {
          class Div extends Callable[rank] with Taskable {
            var tasks = ListBuffer[List[triple]]()
            override def getTasks() : ListBuffer[List[triple]] = { tasks }
            override def call():rank={
              //println("starting")
              tasks ++= i.grouped(1).toList
              
              var x1 = List[rank]()
              breakable{
                while(true){
                  
                  breakable{
                    while(true){
                      x1 ++= tasks.synchronized
                      {
                        val t = this.tasks; 
                        if(t.size==0) 
                          break
                        t.remove(0)
                      }
                      .collect({ case i => solve(l, max, newAcc, Some(i)) })
                    }
                  }
                  
                  var r : Option[List[triple]] = None
                  while(r==None){
                    val working = callables.find(p=>p.getTasks.size!=0)
                    r = working match{
                      case Some(x) => x.getTasks.synchronized{
                         val t = x.getTasks()
                         if(t.size==0) None else Some(t.remove(0))
                      }
                      case None => break
                    }
                  }
                  tasks.synchronized{ tasks += r.get }
                  
                }
              }
              val x2 = x1.maxBy(_._1)
              println("finnish "+x1.size)
              return x2
          }
        }
          callables += new Div
          futures += pool.submit(callables.last)
        }
        return futures.collect({case x => x.get}).maxBy(_._1)
      }
      
      if(newAcc._2>max) {
        return acc
      } else {
        val x1 = l.collect({ case i => solve(l, max, newAcc, Some(i)) })
        val x2 = x1.maxBy(_._1)
        x2
      }
      
    }
    
  }
  
  // Shuffle array for
  val s = solve(util.Random.shuffle(lines), max)
  println(s)
  println("Time: "+((System.nanoTime()-start)/1000000000)+" s")
  
}