package de.trashs

import java.util.concurrent.Executors
import java.util.concurrent.Callable
import scala.collection.mutable.ListBuffer

object Main2 extends App{
  /*val s = Stream(1,2,3)
  val r = s.filter(x=>{println(x);true})
  for{
    i<- r
  }{
    
  }*/
  
  /*val pool = Executors.newFixedThreadPool(1)
  
  val f = pool.submit(new Callable[Int]{
    def call():Int={
      2
    }
  })
  println(f.get)*/
  println("-->")
  val a = new ListBuffer[Int]()
  println("-->"+a.remove(0))
}