package portal
import java.awt.Color

import scala.collection.mutable.HashMap

object ColourPool {
  
    val cache = HashMap[ColourKey, Color]()
  
	def getColour(r:Float, g:Float, b:Float, alpha:Float) : Color = {
      cache.getOrElseUpdate(ColourKey(r,g,b,alpha), new Color(r,g,b,alpha))
       
	}
	
	def getColour(r:Float, g:Float, b:Float) : Color = getColour(r,g,b,1)

}

case class ColourKey(val r : Float, val g:Float,val b:Float,val a:Float)