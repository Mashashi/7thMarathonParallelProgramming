package de

import scala.annotation.tailrec
import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable.HashSet

class Processor(implicit granter:Granter, mapping: ConcurrentHashMap[(Long,Long), HashSet[Long]]) extends Runnable{
  
  @tailrec
  private def divisorsOf(num:Long, curr:Long=1, acc: List[Long]=Nil) : List[Long] = num match  {
    case x if(x==curr) => x::acc
    case x if((x.toDouble % curr)==0) => divisorsOf(num, curr+1, curr::acc)
    case _ => divisorsOf(num, curr+1, acc)
  }
  
  @tailrec
  private def gcd(x: Long, y: Long): Long = if(y==0) x else gcd(y, x % y)
  
  override def run(){
    var task = granter.get()  
    while(!task.eq(None)){
      
      for {
        n <- task.get.starts to task.get.ends
      } {
        val divs = divisorsOf(n)
        val divsSum = divs.sum
        val gcdVal = gcd(n, divsSum)
        val numerator = divsSum/gcdVal
        val denominator = n/gcdVal
        //println(n)
        val id = (numerator, denominator)
        var l : HashSet[Long] = null
        mapping.synchronized{
          l = mapping.get(id)
          if(l==null){
            l = HashSet[Long]()
            mapping.put(id, l)
          }
        }
        l.synchronized{ l.+=(n) }
      }
      
      task = granter.get(task.get)
    }
  }
  
}