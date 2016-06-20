package de.trashs



/**
 * targetTaskEffortSecs
 * Works as a timeout every time this timeout is exceed the progress is recorded and the original task is broken into brokePieces tasks
 *  
 */

/*class SplitterTask(processor:Processor, initEpoch: Int) extends Runnable{
  def run(){
    
  }
}
class Granter(var zipped: ListBuffer[(Long,Long)] = ListBuffer[(Long,Long)]())(implicit targetTaskEffortSecs: Int){
  
  val brokePieces : Int = 3
  val taskSplitter = Executors.newScheduledThreadPool(1);
  
  def get(processor: Processor) : Option[Task] = {
    
    taskSplitter.schedule(new SplitterTask(processor, processor.epoch.get()), targetTaskEffortSecs, TimeUnit.SECONDS)
    
    None
  }
  
}*/