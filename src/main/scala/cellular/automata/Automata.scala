package cellular.automata
import scala.collection.mutable.ListBuffer

object Automata {

  var l = createList(70)
  
  def createList(size:Int) = {
    val builder =  ListBuffer[Boolean]()
    for ( i <- 1 to size) builder += false
    builder += true
    for ( i <- 1 to size) builder += false
    builder.toList
  }
  
  
  def main(args: Array[String]): Unit = {
    val code = 110
    val n = 70
    
    val rule = numberToRule(code)
    
    
    
    for (i <- 1 to n) {
    	display(l)
    	l = tide(l, rule)
    }
  }
  
  def display(l : List[Boolean]) {
    l.foreach( if (_) print("A") else print(" "))
    println
  }
  
  
  def tide(cells : List[Boolean], rule : (Boolean, Boolean, Boolean) => Boolean) = {

    val pf = invoke(rule,_:Seq[Boolean])
    
    false :: cells.iterator.sliding(3).withPadding(false).toList.map(pf) ::: false :: Nil
  }
  
  def invoke( rule : (Boolean, Boolean, Boolean) => Boolean, s : Seq[Boolean]) = s match {
    case Seq(a,b,c) => rule(a,b,c)
    case _ => false
  }
  
  def numberToRule(code : Int) = {
    
    val rules = Map(
    				1   -> {(l:Boolean, c:Boolean, r:Boolean) => (!l && !c && !r)},
    				2   -> {(l:Boolean, c:Boolean, r:Boolean) => (!l && !c && r)},
    				4   -> {(l:Boolean, c:Boolean, r:Boolean) => (!l && c && !r)},
    				8   -> {(l:Boolean, c:Boolean, r:Boolean) => (!l && c && r)},
    				16  -> {(l:Boolean, c:Boolean, r:Boolean) => (l && !c && !r)},
    				32  -> {(l:Boolean, c:Boolean, r:Boolean) => (l && !c && r)},
    				64  -> {(l:Boolean, c:Boolean, r:Boolean) => (l && c && !r)},
    				128 -> {(l:Boolean, c:Boolean, r:Boolean) => (l && c && r)}
    			)
    
    def extract(n:Int, r:Int) : List[Int] = {
      if (r == 0)
        Nil
      else
    	  if (n / r > 0) {
    		  r :: extract(n - r, r/2)
    	  } else {
    		  extract(n, r/2)
    	  }
    }
   
    val activeRules = extract(code, 128)
    
    val execRules = activeRules.map(rules(_))
    
    {(l:Boolean, c:Boolean, r:Boolean) =>
      execRules.exists { rule =>
        rule(l,c,r)
      }
    }
  
   
  }

}