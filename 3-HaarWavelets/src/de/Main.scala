package de

import java.io.FileInputStream
import java.io.IOException
import scala.collection.concurrent.Map
import scala.collection.mutable.Map
import scala.concurrent.forkjoin.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport

object Main extends App{
  
  // Can we read the image from the generated file? 
  //Yes that is the point.
  // The first 32 bit of the generated file don't seem to have the width & height
  // Its not the first 32 bit it is the first 64 or 8 bytes
  var in : Option[FileInputStream] = None 
  try {
    
    in = Some(new FileInputStream("input41"))
    
    def readBytes(count: Int): List[Int] = {
      var c = 0
      var res : List[Int] = Nil
      var read = 0
      while ({c = in.get.read; (c != -1 && read != count)}) {
        res =  res ++ List(c)
        read += 1
      }
      res
    }
    
    //println(Bits.getBigInt(0x0A :: 0x00 :: Nil))
    /*println(Bits.fromByteList( 1 :: 3 :: Nil))
    println(Bits.getBigInt(0 :: 3 :: Nil))
    println(Bits.getBigInt(readBytes(8)))*/
    //println(Bits.getBits(1,8))
    //println(Bits.getBits(1,8))
    //println(Bits.fromLongLong(rep))
    /*val a = readBytes(8)
    println(Bits.fromByteList(a))
    println(a)*/
    
    val size = Bits.getBigInt(readBytes(2))
    println(size)
    val elems = (size.*(size)).toLong
    val struct = new Struct()
    
    var i : Long = 0
    var read : Long = 0;
    var num = 0;
    while(read+num<size)
    {
      read = i * Int.MaxValue
      num = if(read<elems) Int.MaxValue else (elems-((i-1) * Int.MaxValue)).toInt
      println(read+" "+elems)
      struct.add(readBytes(num))
      i+=1 
    }
    
    /*println( struct.get(0) )
    println( struct.get(1) )
    println( struct.get(2) )
    println( struct.get(3) )
    println( struct.get(12544) )
    println( struct.pixel(111, 111) )
    println( struct.pixel(112, 111) )
    println( struct.size() )*/
    
    val sqrt = Math.sqrt(2)
    
    // There is no point into paralellize the outer loop because the next iteration can't be done until the previous is finished
    // For the same reason the two inner loops are not parallelized between them
    
    
    val fjpool = new ForkJoinPool(7)
    val customTaskSupport = new ForkJoinTaskSupport(fjpool)
    
    
    val start = System.nanoTime();
    
    //case class Task
    var p : BigInt = size-1
    while(p>1){
      
      val mid : BigInt = p / 2
      
      val s = (BigInt(0) to BigInt(mid+"")).par
      s.tasksupport = customTaskSupport
      s.foreach({y => {
        val s = (BigInt(0) to BigInt(mid+""))
        s.foreach({ x => { 
          var a = struct.pixel(x.toLong, y.toLong)
          a = ((a+struct.pixel((mid+x).toLong, y.toLong)).toDouble/(sqrt)).toInt
          var d = struct.pixel(x.toLong, y.toLong)
          d = ((d-struct.pixel((mid+x).toLong,y.toLong))/sqrt).toInt
          struct.set(x.toLong, y.toLong, a)
          struct.set((mid+x).toLong, y.toLong, d) 
        }})
      }})
      
      s.par.foreach({y => {
        val s = (BigInt(0) to BigInt(mid+"")).par
        s.tasksupport = customTaskSupport
        s.foreach({ x => { 
          var a = struct.pixel(x.toLong, y.toLong)
          a = ((a+struct.pixel(x.toLong, (mid+y).toLong)).toDouble/(sqrt)).toInt
          var d = struct.pixel(x.toLong, y.toLong)
          d = ((d-struct.pixel(x.toLong,(mid+y).toLong))/sqrt).toInt
          struct.set(x.toLong, y.toLong, a)
          struct.set(x.toLong, (mid+y).toLong, d) 
        }})
      }})
      
      p /= 2
      
    }
    
    System.out.println((System.nanoTime()-start).toDouble/1000000000);
    
    // TODO: Write the final code to write it to a file
    
  } catch {
    case e: IOException => e.printStackTrace
  } finally {
    if (in.isDefined) in.get.close
  }
  
  
  
}