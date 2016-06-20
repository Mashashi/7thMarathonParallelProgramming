package de

object Bits{
      val a = 0x0001
      val two = BigInt(2)
      def fromInt(p:Int){ getBits(p, 2*8) }
      def fromByteList(p:List[Int]) = {
        /*
        var r = List[List[Boolean]]()
        for { i <- p } {  r = r ++ List(getBits(i, 8)) }
        r.flatten.toList
        */
        (for { i <- p } yield getBits(i, 8)).flatten
      }
      def fromByte(rep:Int){
        getBits(rep, 8)
      }
      def getInt(p:List[Int]) = {
        var result : Int = 0
        var i = 0
        for { e <- fromByteList(p) } 
        {
          if(e) result = result + (i*i)
          i += 1
        }
        result
      }
      def getBigInt(p:List[Int]) = {
        var result : BigInt = BigInt(0)
        var i = 0
        for { e <- fromByteList(p) } 
        {
          if(e) result = result + two.pow(i)
          i += 1
        }
        result
      }
      def getBits(p: Int, size : Int) : List[Boolean] = {
        if({size == 0}) 
          return Nil
        val b = p & a
        (b == 1) :: getBits(p >> 1, size-1) 
      }
    }