package de.trash

import scala.annotation.tailrec

object Main extends App{
  @tailrec
  private def divisorsOf(num:Long, curr:Long=1, acc: List[Long]=Nil) : List[Long] = num match  {
    case x if(x==curr) => x::acc
    case x if((x.toDouble % curr)==0) => divisorsOf(num, curr+1, curr::acc)
    case _ => divisorsOf(num, curr+1, acc)
  }
  
  @tailrec
  private def gcd(x: Long, y: Long): Long = if(y==0) x else gcd(y, x % y)
  val n=200
  val divs = divisorsOf(n)
  val divsSum = divs.sum
  val gcdVal = gcd(n, divsSum)
  val numerator = divsSum/gcdVal
  val denominator = n/gcdVal
  println(numerator+"/"+denominator)
}