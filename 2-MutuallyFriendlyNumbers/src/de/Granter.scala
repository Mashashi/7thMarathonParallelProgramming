package de

import scala.collection.mutable.ListBuffer

class Granter(var zipped: ListBuffer[(Long,Long)] = ListBuffer[(Long,Long)]())(implicit targetTaskEffortSecs: Int){
  
  val initialSize : Int = 50
  
  def get(last:Task=null) : Option[Task] = {
    var to : Long = initialSize
    if(last!=null){
      val timeTaken = (System.currentTimeMillis()-last.startTimestamp)/1000
      val count = last.ends - last.starts
      println(Thread.currentThread().getId+" "+timeTaken+" "+count+" "+last.ends)
      to = {
        if(timeTaken==0)
          Math.pow(to, 2).toLong
        else
          ((count*targetTaskEffortSecs.toInt)/timeTaken).toLong
      }
    }
    var p : (Long, Long) = this.synchronized{ 
      // The impact of the synchronized shouldn't be relevant because each thread will take in average at least targetTaskEffortSecs
      // so there is not contention
      zipped.indexWhere { (p: (Long, Long)) => p._1+to<p._2 } match {
        case -1 => { 
          val t = zipped.indexWhere { (p: (Long, Long)) => p._1!=p._2 }
          if(t == -1)
             return None
          val z = zipped(t)
          val n = (z._2, z._2)
          zipped = zipped.patch(t, List(n), 1)
          z
        }
        case v => {  
          val z = zipped(v)
          val n = (z._1+to+1, z._2)
          zipped = zipped.patch(v, List(n), 1)
          (z._1, z._1+to)
        }
      }
    }
    if(p==null) println("lols")
    if(p.equals(None)) None else Some(Task(System.currentTimeMillis(), p._1, p._2))
    
  }
  
}