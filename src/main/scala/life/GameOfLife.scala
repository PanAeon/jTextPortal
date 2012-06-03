package life

import scala.collection.mutable

object GameOfLife {
  
    def newLife(size : Int, cells : Seq[(Int, Int)]) = {
      val civilization = new Civilization(size)
      cells.foreach(c => civilization.set(c._1, c._2, true))
      civilization
    }
  
	def tide(civilization:Civilization) = {
	  civilization.mapWithNeighbours { (c, neighbours) =>
	    neighbours match {
	      case n if n < 2         => false
	      case n if n > 3         => false
	      case n if c && (n == 2) => true
	      case n if n == 3        => true
	      case _ => false//throw new IllegalArgumentException("wrong # of neighbours: " + neighbours)
	    }
	  }
	}

}

// assuming square grid
class Civilization(size:Int) {
  
  //val grid = mutable.BitSet()
  val grid = mutable.Set[Cell]()
  
  def foreachWithCoords( f : (Integer, Integer, Boolean) => Unit) {
     for (x <- 0 until size; y <- 0 until size)
       f(x,y, grid.contains(Cell(x,y)))
  }
  
  
  def  mapWithNeighbours(f: (Boolean, Integer) => Boolean) : Civilization = {
    val newCivilization = new Civilization(size)
    for (x <- 0 until size; y <- 0 until size) {
      val alive = grid.contains( Cell(x,y))
      newCivilization.set(x,y, f(alive, neighbours(x,y).size))
    }
    newCivilization
  }
  
  // private
  private[life] def set(x:Int, y:Int, value : Boolean) = value match {
      case true => grid += Cell(x,y)
      case false => grid -= Cell(x, y)
  }
  
   def flip(x:Int, y:Int){
     grid.contains(Cell(x,y)) match {
      case true => grid -= Cell(x,y)
      case false => grid += Cell(x, y)
     }
  }
  
  def neighbours(x : Int, y : Int) = {
    for (
        i <- x - 1 to x + 1;
        j <- y - 1 to y + 1;
        if (!(i ==x && j == y));
        if (grid.contains(Cell(wrap(i), wrap(j))))
    ) yield(x,y)
  }
  
  def wrap(value : Int) = if ( value < 0) size + value else if (value >= size) value - size else value
 
}

case class Cell(val x:Int, val y:Int)
  
//}
//
//class Civilization(seed: List[(Int, Int)], size:Int) {
//  private val STAY_ALIVE = 2
//  private val BE_BORN = 3
//  private val grid = new Array[Array[Boolean]](size, size)
//  seed foreach (cell => containsCellAt(cell._1, cell._2))
//
//  def tick = {
//    val nextGeneration = new Civilization(Nil, size)
//    for (row <- 0 until size; col <- 0 until size)
//      if (isCellAt((row, col)) && numberOfNeighboursFor(row, col) == STAY_ALIVE
//          || numberOfNeighboursFor(row, col) == BE_BORN)
//        nextGeneration.containsCellAt(row, col)
//    nextGeneration
//  }
//
//  private def numberOfNeighboursFor(row: Int, col: Int): Int =
//    areaAround(row, col) map wrap filter isCellAt length
//
//  private def areaAround(row: Int, col: Int): List[(Int, Int)] =
//    List((row-1, col-1), (row,col-1), (row+1,col-1), (row-1,col), (row+1,col), (row-1,col+1), (row,col+1), (row+1,col+1))
//
//  private def wrap(cell: (Int, Int)): (Int, Int) =
//    (((cell._1 + size) % size), ((cell._2 + size) % size))
//
//  private def isCellAt(cell: (Int, Int)): Boolean =
//    grid(cell._1)(cell._2)
//
//  private def containsCellAt(row: Int, col: Int) =
//    grid(row)(col) = true
//
//  def printGrid = {
//    println
//    grid foreach {
//      row => row foreach {
//        cell => if (cell) print("*") else print(".")
//      }
//      println
//    }
//  }
//}
