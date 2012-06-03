package stencil
import scala.io.Source
import scala.collection.mutable.ListBuffer

object QuickSortMain {
  
  var comparisonCount = 0;
  
  def push(n : Int) {
    comparisonCount += n;
  }

  def main(args: Array[String]): Unit = {
    val numbers = (for(line <- Source.fromFile("src/main/resources/QuickSort.txt").getLines()) yield {
      line.toInt
    }).toArray
    
    quicksort(numbers, 0, numbers.size - 1)
    
//    val sorted = quicksort(numbers)
    
    numbers.foreach(println)
     println("-" * 20)
     println(comparisonCount)
    
  }
  
  
//   def quicksort(list : List[Int]) : List[Int] = list match {
//    case head::tail =>
//     
//      val (left, right) =   partition(_ < tail.last, list.dropRight(1))
//    
//      push(list.size - 1)
//      //val (left, right) = partition(_ < head, tail.reverse)
//      quicksort(left) ::: list.last :: quicksort(right)
//      
//      
//    case l => l
//  }
//  
  
//  def quicksort(list : List[Int]) : List[Int] = list match {
//    case head::tail =>
//      // determine median
//      val median = (head :: list.last :: list(list.size / 2) :: Nil).sortWith(_ > _)(1)
//      val (left,right) = if (  head == median) {
//        partition(_ < head, tail)
//      } else if ( list.last == median) {
//        val b = list.dropRight(1)
//        partition(_ < tail.last, b)
//      } else {
//        val (b,c) = list.splitAt(list.size / 2)
//        partition(_ < median, (b ::: c.tail))
//      }
//     
//      push(list.size - 1)
//      //val (left, right) = partition(_ < head, tail.reverse)
//      quicksort(left) ::: median :: quicksort(right)
//      
//      
//    case l => l
//  }
  
  // again procedure
   def quicksort(A : Array[Int], l : Int, r : Int) {
     if (l < r) {
       push(r - l)
       val median = (A(l) :: A(r) :: A( (l+r) / 2) :: Nil).sortWith(_ > _)(1)
       if (A(r) == median)
    	   swap(A, l, r)
       if (A( (l + r)/2) == median)
           swap(A, l, (l+r) / 2)
       val p = partition(A, l, r)
       quicksort(A, l, p-1)
       quicksort(A, p+1, r)
     } 
   }
  
  
  // procedure
  def partition(A : Array[Int], l:Int, r:Int) = {
    val p = A(l)
    var i = l + 1  
    for(j <- l+1 to r) {
      if (A(j) < p) { 
        swap(A, i, j)
        i += 1;
      }
    }
   // println("l:" + l + "i: " + i)
    if (i > 0)
    swap(A, l, i-1)
    i - 1
  }
  
  // procedure!
  def swap(A : Array[Int], i : Int, j: Int) {
   // println("i:" + i + "j: " + j)
    val tmp = A(i)
    A(i) = A(j)
    A(j) = tmp
  }

}