package de.trash

import scala.concurrent.forkjoin.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport
import scala.util.control.Breaks._
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicBoolean
import scala.BigInt
import scala.math.BigInt.int2bigInt

object Main4 extends App {
  
  //val clauses = List[List[Short]](List(3,3,3),List(2,1,-1),List(-3,-2,-3),List(2,1,2))
  //val nVar : Int = 3
  //println(clauses.transpose)
  
  //val fileName = "medium.in"
  val fileName = "small3.in"
  val numberThreads = 1
  
  
  val lines = scala.io.Source.fromFile(s"3sat/input/${fileName}").mkString.split("\r\n")
  //println(lines(0).split(" ")(1))
  val nVar : Int = lines(0).split(" ")(1).toInt
	val clauses = lines.drop(1).map ( _ split(" ") map(_.toInt) )
  
  var i : Int =0;
  val iVar = (for {i <- 0 until nVar} yield Math.pow(2, i).toLong)
  val maxNumber = BigInt(Math.pow(2, nVar).toLong)
  var b = maxNumber/10737418;if(b<1){b=BigInt(1)}
  println(b)
  var number: AtomicLong = new AtomicLong(0)
	var end: AtomicBoolean = new AtomicBoolean(false)
  
	val fjpool = new ForkJoinPool(numberThreads)
  val customTaskSupport = new ForkJoinTaskSupport(fjpool)
  
  //println("aqui "+nVar+" "+maxNumber)
  
  val s : Double = System.nanoTime()
  val t = (BigInt(0) to BigInt(maxNumber+"") by b).par
  t.tasksupport = customTaskSupport
	
	println("start")
	t.foreach({ x => {
	  println(":"+x)
	  if(!end.get()){
  	  val p = (x to x+b).par
  	  p.tasksupport = customTaskSupport
  	  p.foreach({ x=>{
          if({
            clauses.forall({ c => {
              !(0 to 2).map(c(_)).forall({ v => {
        	      !(v > 0 && (x.toLong & iVar(v - 1)) > 0) && !(v < 0 && (x.toLong & iVar(-v - 1)) == 0)
        	    } })
            } })
          }) {
            end.set(true)
            number.set(x.toLong)
          }
  	  }})
	  }
	}})
	println(number)
  println((System.nanoTime()-s)/1000000000)
}