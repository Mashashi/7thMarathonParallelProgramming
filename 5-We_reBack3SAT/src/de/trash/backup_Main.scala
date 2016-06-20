package de.trash

import scala.concurrent.forkjoin.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport
import scala.util.control.Breaks._
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicBoolean
import scala.BigInt

object Main2 extends App {
  
  val clauses = List[List[Short]](List(3,3,3),List(2,1,-1),List(-3,-2,-3),List(2,1,2))
  //println(clauses.transpose)
  
  val nVar : Int = 3
  
  
  var i : Int =0;
  val iVar = (for {i <- 1 to nVar} yield Math.pow(2, i).toLong)
  val maxNumber = BigInt(Math.pow(2, nVar).toLong)
  var number: AtomicLong = new AtomicLong(0)
	var end: AtomicBoolean = new AtomicBoolean(false)
  
	val fjpool = new ForkJoinPool(1)
  val customTaskSupport = new ForkJoinTaskSupport(fjpool)
  
	val t = (BigInt(0) to BigInt(maxNumber+"")).par
	t.tasksupport = customTaskSupport
	
	t.foreach({ x => {
	  
    if(!end.get()){
      var isTrue = false
      for{
    	  c <- clauses
      } {
        if({
    	    (0 to 2).forall({ x => {
    	      val v : Int = c(x)
    	      var ret = false
    	      //!(v > 0 && (x.toLong & iVar(v - 1)) > 0) && !(v < 0 && (x.toLong & iVar(-v - 1)) == 0)
    	      if(!(v > 0 && (x.toLong & iVar(v - 1)) > 0)){
    	        //println(s"${v} < 0 && (${x.toLong} & ${iVar(-v - 1)}) == 0")
    	        ret = true
    	      }else if(!(v < 0 && (x.toLong & iVar(-v - 1)) == 0)){
    	        //println(s"${v} > 0 && (${x.toLong} & ${iVar(v - 1)}) > 0")
    	        ret = true
    	      }
    	      ret
    	    }})
        }){ 
          end.set(true); 
          isTrue = true 
        }
      }
      if(isTrue) number.set(x.toLong)  
    }
	  
	}})
	print(number)
  /*
long solveClauses(short **clauses, int nClauses, int nVar) {

	

	for (number = 0; number < maxNumber; number++) {

		for (c = 0; c < nClauses; c++) {


			var = clauses[1][c];
			if (var > 0 && (number & iVar[var - 1]) > 0)
				continue; // clause is true
			else if (var < 0 && (number & iVar[-var - 1]) == 0)
				continue; // clause is true

			var = clauses[2][c];
			if (var > 0 && (number & iVar[var - 1]) > 0)
				continue; // clause is true
			else if (var < 0 && (number & iVar[-var - 1]) == 0)
				continue; // clause is true

			break; // clause is false
		}

		if (c == nClauses)
			return number;

	}
	return -1;

} 
   */
  
  
}