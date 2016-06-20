package de

object trash {
  println("Welcome to the Scala worksheet")
  
	/*
	c.foldLeft(BigInt(0))(
		(x,y)=>{
			println(x+" "+y)
			x +y
		}
	)
	*/
	
	/*
  pool.shutdown()
  pool.awaitTermination(365, TimeUnit.DAYS)
  */
  
  /*
  for {b <- buckets} { println(b.elems) }
  */
  
  /*
    val numeric : List[BigDecimal] = List()
    for {
      i <- 1 to numeric.size/numBuckets
    } {
      pool.execute(new Runnable(){
        def run(){
          try{
            for {i<-1 to numBuckets} { numeric ++ List(numericIte.next()) }
          } catch {
            case e:NoSuchElementException => Nil
          }
          println("finnish")
          setupJob.countDown()
        }
      })
    }
    setupJob.await()
    */
    
    //class Bucket[T <% Ordered[T]]
    
    /*
    for {
    i <- normalized
    p = xs.map(x=>(x,(x-i._2))).filter({case (_,diff)=>diff>=0})(0)
	  }
	  { buckets(xs.indexOf(p._1)).elems += i }
    */
}