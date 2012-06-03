package portal
import org.jbox2d.callbacks.DebugDraw
import org.jbox2d.common.OBBViewportTransform
import org.jbox2d.pooling.arrays.Vec2Array
import org.jbox2d.common.Vec2
import org.jbox2d.common.Color3f
import scala.collection.mutable.ArrayBuffer
import java.math
import org.jbox2d.collision.AABB
import org.jbox2d.common.Transform
import org.jbox2d.pooling.arrays.IntArray

object DebugDrawJ2D {
  val circlePoints = 13
}

class DebugDrawJ2D(val panel : TestPanelJ2D) extends DebugDraw(new OBBViewportTransform()) {
	viewportTransform.setYFlip(true)
	
	val vec2Array = new Vec2Array()
	val sp1 = new Vec2()
	val sp2 = new Vec2()
	val saxis = new Vec2()
	val temp = new Vec2()
	val temp2 = new Vec2()
	
	val xIntsPool = new IntArray()
	val yIntsPool = new IntArray()
	
	
	
	override def drawCircle(center:Vec2, radius:Float, color:Color3f){
	  val vecs = vec2Array.get(DebugDrawJ2D.circlePoints)
	  generateCircle(center, radius, vecs, DebugDrawJ2D.circlePoints)
	  drawPolygon(vecs, DebugDrawJ2D.circlePoints, color)
	}
	
	override def drawPoint(point:Vec2, radiusOnScreen:Float, color:Color3f) {
	  getWorldToScreenToOut(point, sp1);
	  val c = ColourPool.getColour(color.x, color.y, color.z)
	  panel.getDBGraphics().setColor(c)
	  sp1.x -= radiusOnScreen
	  sp1.y -= radiusOnScreen
	  panel.getDBGraphics().fillOval(sp1.x.toInt, sp1.y.toInt, radiusOnScreen.toInt * 2, radiusOnScreen.toInt * 2)
	}
	
	override def drawSegment(p1:Vec2, p2:Vec2, color:Color3f) {
	  getWorldToScreenToOut(p1, sp1)
	  getWorldToScreenToOut(p2, sp2)
	  
	  val c = ColourPool.getColour(color.x, color.y, color.z)
	  panel.getDBGraphics().setColor(c)
	  
	  panel.getDBGraphics().drawLine(sp1.x.toInt, sp1.y.toInt, sp2.x.toInt, sp2.y.toInt)
	}
	
	def drawAABB(aabb:AABB, color:Color3f) {
	  val vecs = vec2Array.get(4)
	  aabb.getVertices(vecs)
	  drawPolygon(vecs, 4, color)
	}
	
	override def drawSolidCircle(center : Vec2, radius:Float, axis : Vec2, color : Color3f) {
	  val vecs = vec2Array.get(DebugDrawJ2D.circlePoints)
	  generateCircle(center,radius, vecs, DebugDrawJ2D.circlePoints)
	  drawSolidPolygon(vecs, DebugDrawJ2D.circlePoints, color);
	  if (axis != null) {
	    saxis.set(axis).mulLocal(radius).addLocal(center)
	    drawSegment(center, saxis, color)
	  }
	  
	}
	
	override def drawSolidPolygon(vertices: Array[Vec2], vertexCount : Int, color : Color3f) {
	  val xInts = xIntsPool.get(vertexCount)
	  val yInts = yIntsPool.get(vertexCount)
	  
	  for ( i <- List.range(0, vertexCount)) {
	    getWorldToScreenToOut(vertices(i), temp);
	    xInts(i) = temp.x.toInt;
	    yInts(i) = temp.y.toInt;
	  }
	  
	   val c = ColourPool.getColour(color.x, color.y, color.z, .4f)
	   panel.getDBGraphics().setColor(c)
	   
	   panel.getDBGraphics().fillPolygon(xInts, yInts, vertexCount)
	   
	   drawPolygon(vertices, vertexCount, color)
	  
	  
	}
	
	override def drawString(x:Float, y:Float, s : String, color : Color3f) {
	  val c = ColourPool.getColour(1, 0, 0)
	  panel.getDBGraphics().setColor(c)
	  panel.getDBGraphics().drawString(s,x,y)
	}
	
	override def drawTransform(xf : Transform) {
	  getWorldToScreenToOut(xf.position, temp)
	  temp2.setZero()
	  val k_axisScale = 0.4f
	  
	  var c = ColourPool.getColour(1, 0, 0)
	  panel.getDBGraphics().setColor(c)
	  
	  temp2.x = xf.position.x + k_axisScale * xf.R.col1.x;
	  temp2.y = xf.position.y + k_axisScale * xf.R.col1.y;
	  getWorldToScreenToOut(temp2, temp2);
	  
	  panel.getDBGraphics().drawLine(temp.x.toInt, temp.y.toInt,  temp2.x.toInt, temp2.y.toInt);
	  
	  c = ColourPool.getColour(0, 1, 0)
	  panel.getDBGraphics().setColor(c);
      temp2.x = xf.position.x + k_axisScale * xf.R.col2.x;
      temp2.y = xf.position.y + k_axisScale * xf.R.col2.y;
      getWorldToScreenToOut(temp2, temp2);
      panel.getDBGraphics().drawLine(temp.x.toInt,  temp.y.toInt,  temp2.x.toInt, temp2.y.toInt);
	}
	
	val PI = 3.14159265358979323846f;
	
	def generateCircle(center:Vec2, radius:Float, points:Array[Vec2], numPoints:Int) {
	  val inc =  PI * 2 / numPoints;
	  for (i <- List.range(0, numPoints)) {
	    points(i).x =  center.x + Math.cos(inc * i).toFloat * radius
	    points(i).y =  center.y + Math.sin(inc * i).toFloat * radius
	  }
	}
}