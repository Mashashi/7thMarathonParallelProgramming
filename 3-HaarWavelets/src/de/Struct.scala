package de

import java.util.concurrent.ConcurrentHashMap
import java.util.Map
import java.lang.Integer

class Struct {
  
  var v = List[List[Int]]()
  val map: Map[Long, Integer] = new ConcurrentHashMap()
  
  def add(p:List[Int]){
    v = v ++ List(p)
  }
  def size() = {
    len() / 4
  }
  private def len() = {
    v.collect({case x: List[Int] => x.length}).sum
  }
  def pixel(x: Long, y:Long) : Int = {
    val lim = Math.floor(Math.sqrt(size()))
    require(x<lim, y<lim)
    require(x>=0, y>=0)
    //println(s"((${y})*${lim.toLong-1})+(${x})")
    get(((y)*lim.toLong)+(x))
  }
  def set(x:Long, y:Long, v:Int) = {
    val lim = Math.floor(Math.sqrt(size()))
    map.put(((y)*lim.toLong)+(x), v)
  }
  def get(i:Long):Int={
    // 4 - 32 bits
    require(i<size())
    require(i>=0)
    var e = map.get(i);
    if(e == null){
      
      val b = {
        var fin = List[Int]()
        val start = i*4
        var st = start
        while(st<start+4){
          fin = fin ++ List( v( (st / Int.MaxValue).toInt )((st % Int.MaxValue).toInt) )
          st = st + 1
          //println(st)
        }
        fin
      }
      e = Bits.getInt(b)
      map.put(i, e)
    }
    e
  }
}