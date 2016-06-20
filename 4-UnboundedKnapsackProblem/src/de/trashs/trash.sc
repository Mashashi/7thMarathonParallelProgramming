package de

object trash {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  //val x1 = l.collect({ case t => (t._1*t._2, t) })
  //val x2 = x1.sortBy(_._2)
  /*val next = l.maxBy({(x: triple)=>{
	  val out = x._1*x._2+res
	  if(out>max) -1 else out
	  }
	})*/
  /*
  futures += pool.submit(new Callable[rank](){
            override def call():rank={
              active.incrementAndGet
              println("starting")
              var x3 : rank = (-1,-1,List())
              for {
                x <- i.grouped((i.size*0.1).toInt)
              } {
                val x1 = i.collect({ case i => solve(l, max, newAcc, Some(i)) })
                val x2 = x1.maxBy(_._1)
                x3 = if (x2._1>x3._1) x2 else x3
              }
              active.decrementAndGet
              //println("finnish ")
              return x3
            }
          })
        }
  
  */
}