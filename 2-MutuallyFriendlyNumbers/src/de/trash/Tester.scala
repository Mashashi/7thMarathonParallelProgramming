package de.trash

import org.scalatest.{FlatSpec,Matchers}
import scala.collection.mutable.ListBuffer
import de.Granter

class Tester extends FlatSpec with Matchers{
  
  info("Starting...")
  
  /*it should "sing" in {
    // info("Ok")  
    true shouldEqual false
  }*/
  
  
  it should "granter test" in {
    
    implicit val targetTaskEffortSecs = 5
    val granter = new Granter(ListBuffer[(Long, Long)]((1,100)))
    
    val task1 = granter.get()
    task1.get.starts shouldEqual 1
    task1.get.ends shouldEqual 51
    
    Thread.sleep(10000)
    
    val task2 = granter.get(task1.get)
    task2.get.starts shouldEqual 52
    task2.get.ends should not be 100
    
    val task3 = granter.get(task2.get)
    task3.get.starts shouldEqual task2.get.ends+1
    task3.get.ends shouldEqual 100
    
    val task4 = granter.get(task3.get)
    task4 shouldEqual None
    
  }
  
}