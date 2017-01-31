package stencil


import scala.annotation.tailrec
import org.lwjgl._, glfw._, opengl._
import Callbacks._, GLFW._, GL11._

import org.lwjgl.system.MemoryUtil._

 import math.{Pi,sin, abs, cos, toDegrees, toRadians}
import breeze.linalg._
import breeze.math._
import breeze.numerics._
import utils._


object UI extends OpenGLApp {
  

  
  val FPS = 60
  override val Width  = 600
  override val Height =  600
  
  
  
  // 
  
  val viewPoint = Point(20.0, 0.0)
  val portalA = Portal(0.0, 0.0, 50.0, Pi / 2.0  , MeColor(0.2, 0.2, 0.9), MeColor(0.8, 0.8, 0.0))
  val portalB = Portal(75.0, 0.0, 50.0, -Pi / 2.0  , MeColor(0.8, 0.8, 0.0), MeColor(0.2, 0.2, 0.9))
  
 
  
 
  
  def main(args: Array[String]): Unit = {
   
   run()
  }

  override def initGL() {
    glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, Width, 0, Height, 1, -1);
	glMatrixMode(GL_MODELVIEW);
	glShadeModel(GL_SMOOTH);
	glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);   
	glEnable(GL_BLEND);                         
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
    glClearStencil(0);    
  }
  
  var frame = 0
  

  
  
  override def render() {
      
	  // main scene:
    
    
    
	  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	  
	  
	  glClearStencil(0);
	  
	  
	  
	  glLoadIdentity();
	  glTranslatef(Width.toFloat /2, Height.toFloat / 2, 0.0f)
	  
	  drawScene()
	  
	  
	  val drawPortalA = portalA.checkViewPoint(viewPoint.x, viewPoint.y)
	  val drawPortalB = portalB.checkViewPoint(viewPoint.x, viewPoint.y)

	  drawPortal(portalA, portalB, viewPoint, 1)
	  drawPortal(portalB, portalA, viewPoint, 1)
  }
  
  
  def portalTransform(portalA:Portal, portalB : Portal) {
   // glScaled(1.0, -1.0, 1.0)
    glTranslated(portalA.x, portalA.y, 0.0f)
      glRotated(Math.toDegrees(Pi + portalA.angle - portalB.angle), 0, 0, 1);
      glTranslated(-portalB.x, -portalB.y, 0.0f)
  }
  
  def calculateNewViewPoint(portalA : Portal,portalB : Portal, viewPoint : Point) : Point = {
   // val newX = 2 * portalB.x - viewPoint.x;
   // val newY = 2 * portalB.y - viewPoint.y;
   // Point(newX, newY)
    viewPoint
  }

  def drawPortal(portalA: Portal, portalB: Portal, viewPoint: Point, order: Int): Unit = {

    if (order < 7) {
      val drawPortalA = portalA.checkViewPoint(viewPoint.x, viewPoint.y)

      if (drawPortalA) {

        glPushMatrix();

        if (order == 1) {
        glClear(GL_STENCIL_BUFFER_BIT);
        glClearStencil(0);
        

        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, 1, 1);
        //glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        
        }
        glStencilOp(GL_KEEP, GL_KEEP, GL_INCR);
        // mask

        portalA.drawAreaAsSeenFrom(viewPoint.x, viewPoint.y)

        glStencilFunc(GL_EQUAL, order, order); // We Draw Only Where The Stencil Is 1

        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP); // Don't Change The Stencil Buffer

        // picture
        glColor3f(1.0f, 0f, 0f)

        portalTransform(portalA, portalB);

        drawScene()
        val newViewPoint = calculateNewViewPoint(portalA,portalB, viewPoint)
        drawPortal(portalA, portalB, newViewPoint, order + 1);
        if (order == 1) {
           glDisable(GL_STENCIL_TEST);
        }
       

        glPopMatrix();
        
          // probably draw parent boundary && area should be here ...
     //   val isInverseColor = order % 2 == 0;
       if (order == 1)
        portalA.drawTransparentAreaAsSeenFrom(viewPoint.x, viewPoint.y)
        portalA.drawBoundaryAsSeenFrom(viewPoint.x, viewPoint.y)

      }
    }
  }
  
  
  def drawScene() {
    
    import Math.{sin, cos}
    glColor3d(0.2, 0.2, 0.2);
    glRecti(-220, -30, -60, 30)
    
    glColor3d(0.7, 0.8, 0.05);
    glRecti(-200, -20, -180, 20)
    
    glRecti(-160, -20, -140, 20)
    
    glRecti(-120, -20, -100, 20)
    
    glColor3d(0.2, 0.2, 0.2);
    glRecti(40, -120, 80, 120)
    
    
   // glColor3d(0.7, 0.4, 0.4)
   // glRecti(0, 0, 250, 250)
    
    glColor3d(0.7, 0.8, 0.05);
    glBegin(GL_TRIANGLES)
    	glVertex2d(45.0, 60.0);
    	glVertex2d(75.0, 60.0);
    	glVertex2d(60.0, 90.0);
    glEnd()
    
    glBegin(GL_TRIANGLES)
    	glVertex2d(45.0, 0.0);
    	glVertex2d(75.0, 0.0);
    	glVertex2d(60.0, 30.0);
    glEnd()
    
    glBegin(GL_TRIANGLES)
    	glVertex2d(45.0, -60.0);
    	glVertex2d(75.0, -60.0);
    	glVertex2d(60.0, -30.0);
    glEnd()
    
    glColor3d(0.1, 0.9, 0.1)
    glBegin(GL_TRIANGLES)
    	glVertex2d(200.0, 200.0);
    	glVertex2d(220.0, 240.0);
    	glVertex2d(240.0, 200.0);
    glEnd()
    
    glColor3d(0.5, 0.5, 0.5)
    glBegin(GL_TRIANGLES)
    	glVertex2d(-200.0, 200.0);
    	glVertex2d(-220.0, 240.0);
    	glVertex2d(-240.0, 200.0);
    glEnd()
    
    glColor3d(0.3, 0.3, 0.9)
    glBegin(GL_TRIANGLES)
    	glVertex2d(200.0, -240.0);
    	glVertex2d(220.0, -200.0);
    	glVertex2d(240.0, -240.0);
    glEnd()
    
    glColor3d(0.95, 0.1, 0.1)
    glBegin(GL_TRIANGLES)
    	glVertex2d(-200.0, -240.0);
    	glVertex2d(-220.0, -200.0);
    	glVertex2d(-240.0, -240.0);
    glEnd()
    
    
    val x1,y1 = 0.0
    val radius = 15.0
    glColor3d(0.9, 0.12, 0.1);
   glBegin(GL_TRIANGLE_FAN)
    	glVertex2d(x1, y1);
   		for(angle <- 0.0 to 360.0 by 5.0)
   			glVertex2d(x1 + sin(angle) * radius, y1 + cos(angle) * radius)
    glEnd()
    
      
  }
  
  var gameStarted = false
  
  var lastMouseX = -1d
  var lastMouseY = -1d
  var spacePressed = false
  
  var mousePressed = false
  
  override def cursorPosHandler(window:Long, x:Double, y:Double) : Unit = {
    
    if (mousePressed) {
       println(s"${viewPoint.x} - ${viewPoint.y}")
         println(s"mx: $x, my: $y")
         viewPoint.x =  x - Width / 2.0
         viewPoint.y =  -(y - Height / 2.0)
    }
    lastMouseX = x
    lastMouseY = y
  }
  
  override def mouseButtonHandler(window:Long, button:Int, action:Int, mods:Int): Unit = {
    if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
      mousePressed = true
    } else if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
      mousePressed = false
    }
  }
// FIXME:
//  def pollInput() {
//    if (lastMouseX == Mouse.getX && lastMouseY == Mouse.getY && Mouse.isButtonDown(0)) {
//      // ignore
//    } else if (Mouse.isButtonDown(0)){
//      viewPoint.x =  Mouse.getX - SIZE_X / 2
//      viewPoint.y = Mouse.getY - SIZE_Y / 2
//      lastMouseX = Mouse.getX
//      lastMouseY = Mouse.getY
//    }
//    
//    if (lastMouseX != -1 && !Mouse.isButtonDown(0) && !gameStarted) {
//     // civilization.flip(lastMouseX / SIZE_OF_CELL, lastMouseY / SIZE_OF_CELL)
//      lastMouseX = -1
//      lastMouseY = -1
//      
//    }
//  }

}

case class Point(var x : Double, var y : Double)
case class MeColor(val r : Double, val g : Double, val b : Double)

case class Portal(
    val x : Double,
    val y : Double,
    val length : Double,
    val angle : Double,
    val color : MeColor,
    val companionColor : MeColor) {
 
  
//  import scalala.tensor.dense.DenseVector
  
  val HW = 250.0 // half width
  val HH = 250.0 // half height
  
 // val a = DenseVector[Double](x + (length / 2.0) * cos(angle), y + (length / 2.0) * sin(angle))
 // val b = DenseVector[Double](x - (length / 2.0) * cos(angle), y - (length/ 2.0) * sin(angle))

  
  val xA = x + (length / 2.0) * cos(angle)
  val yA = y + (length / 2.0) * sin(angle)
  
  val xB = x - (length / 2.0) * cos(angle)
  val yB = y - (length/ 2.0) * sin(angle)

  def boundary(x0: Double, y0: Double) = {
    //  boundary line equations
    val boundaryA =
      if (xA != x0)
        (x: Double) => ((yA - y0) / (xA - x0)) * (x - x0) + y0
      else
        (x: Double) => y0
    val boundaryB = if (xB != y0)
      (x: Double) => ((yB - y0) / (xB - x0)) * (x - x0) + y0
      else
        (x:Double) => y0
    
    // now find out sign
    val signXa = Math.signum(xA - x0)
    val signYa = Math.signum(yA - y0)
    
    val signXb = Math.signum(xB - x0)
    val signYb = Math.signum(yB - y0)
    
    // now find coordinates of other side points
    val xA1 = xA + signXa*HW*2
    val yA1 = if (xA != x0) boundaryA(xA1) else HH * signYa
    
    val xB1 = xB + signXb * HW * 2
    val yB1 = if (xB != x0) boundaryB(xB1) else HH * signYb
    
    getCorners(x0,y0,xA,yA,xB, yB, xA1, yA1, xB1, yB1)
  }
  
 
  class PolarAngleOrdering(val o : DenseVector[Double])  extends Ordering[DenseVector[Double]] {
    
    def compare(x:DenseVector[Double], y:DenseVector[Double]) : Int = {
        //val foo = cross((x-o).asRow,(y-o).asCol)
       val a = x - o
       val b = y - o
        
     //   val z = cross(a.asRow,b)
        val crossProduct = a(0) * b(1) - a(1) * b(0)
        if (crossProduct > 0.00) // max
        	1
        else -1
    }
  }
  
    class InvertOrdering(val o : DenseVector[Double])  extends Ordering[DenseVector[Double]] {
    
    def compare(x:DenseVector[Double], y:DenseVector[Double]) : Int = {
        //val foo = cross((x-o).asRow,(y-o).asCol)
       val a = x - o
       val b = y - o
        
     //   val z = cross(a.asRow,b)
        val crossProduct = a(0) * b(1) - a(1) * b(0)
        if (crossProduct > 0.00) // max
        	-1
        else 1
    }
  }
  
  class DistanceOrdering(val o : DenseVector[Double]) extends Ordering[DenseVector[Double]] {
    def compare(x: DenseVector[Double], y : DenseVector[Double]) : Int = {
      //Math.signum( (x - o).norm(2.0) - (y - o).norm(2.0) )
      import scala.math.signum
      signum(((x - o).norm(2.0) - (y - o).norm(2.0)).toInt)
    }
  }
   
  def arrangeHull(a : DenseVector[Double], points:List[DenseVector[Double]]) : List[DenseVector[Double]]  = points match {
    case List(f, _*) => val next  = points.max(new PolarAngleOrdering(a))
                        val rest = points.filterNot(_.equals(next))
    				  next :: arrangeHull(next, rest) 
    case _ => Nil
  }
  
  
  def fuckingConvexHull(input : List[DenseVector[Double]]) : List[DenseVector[Double]] = {
    // find lowest point p0
    // compute right chain until you find highest vertex (pk)
    // continue from pk to choose vertexes with smallest polar value, but now
    // from negative x-axis
    // continue until we come to p0
    // simple huh?
    if (allPointsAreOnTheSamePlane(input)) {
      return input
    }
    val buff = scala.collection.mutable.ListBuffer[DenseVector[Double]]()
    
    // lowerst point
    val p0 = input.minBy(_(1))
    val pk = input.maxBy(_(1))
    
    buff += p0
    
    // constructing right chain:
    var current = p0
    while(current != pk) {
      val foo = input.filterNot(_ == current)
      val rez = foo.min(new PolarAngleOrdering(current))
      buff += rez
      current = rez
    }
    
    while(current != p0) {
       val foo = input.filterNot(_ == current)
      val rez = foo.min(new PolarAngleOrdering(current))
      buff += rez
      current = rez
    }
    
    return buff.toList
    
  }

  def allPointsAreOnTheSamePlane(points: List[DenseVector[Double]]) = {
    assert(points.size > 3)
    val a = points(0)
    val b = points(1)

    val firstX = a(0)
    if (points.forall(_(0) == a(0)))
      true
    else {
      val l =
        if (a(0) != b(0))
          (x: Double) => ((b(1) - a(1)) / (b(0) - a(0))) * (x - a(0)) + a(1)
        else
          (x: Double) => a(1)
      points.forall( p => abs(l(p(0)) - p(1)) < 0.01)
    }
  }
  
  
   def getAngleSign(o: DenseVector[Double], x:DenseVector[Double], y:DenseVector[Double]) : Int = {
        //val foo = cross((x-o).asRow,(y-o).asCol)
        val a = x - o
        val b  = y - o
        val crossProduct = a(0) * b(1) - a(1) * b(0)
        if (crossProduct > 0.0)
        	1
        else -1
    }
  
 
  
  def getCorners(x0:Double, y0:Double,
      xA : Double, yA : Double,
      xB:Double, yB : Double,
      xA1 : Double, yA1 : Double,
      xB1 : Double, yB1 : Double) = {
   

   def getAngle(a: DenseVector[Double], b : DenseVector[Double]) = {
	   val c_alpha = (a dot b) / (a.norm(2) * b.norm(2))
	   Math.acos(c_alpha)
   }
   
  
   
   def getCenter(points:List[DenseVector[Double]]) = {
    val summ = DenseVector(0.0, 0.0)
    for (p <- points) {
      summ += p
    }
    
    
    summ :/ points.size.toDouble
   }
   

   
  
   
   
    
    val a = DenseVector(xA, yA)
    val b = DenseVector(xB, yB)
    
    val foo0 = DenseVector(-HW, HH)
    val foo1 = DenseVector( -HW, -HH)
    val foo2 = DenseVector( HW, -HH)
    val foo3 = DenseVector( HW, HH)
    
    
    
   
    val center = DenseVector(x0, y0)
    
    val alpha = getAngle(a-center,b-center)
    
   
    
    val corners = foo0::foo1::foo2::foo3::Nil
    val myCorners = corners.filter{ it =>
      val pointBetween = (a + b) :/ 2d
      val angleToA = getAngleSign(center, a, pointBetween)
      val angleToB = getAngleSign(center, b, pointBetween)
      val aToIt = getAngleSign(center, a, it)
      val bToIt = getAngleSign(center, b, it)
      
      (angleToA == aToIt) && (angleToB == bToIt) && (getAngleSign(a, b, center) != getAngleSign(a, b, it))
    } 
    
    val foo = DenseVector(xA, yA) :: DenseVector(xA1, yA1) :: Nil
    
    val bar = DenseVector(xB1, yB1) :: DenseVector(xB, yB) :: Nil
    
    val t = foo ::: myCorners ::: bar
    
    val z = fuckingConvexHull(t)
    z.map(c => (c(0) , c(1)))
   
  }
  
  
  
  def drawAreaAsSeenFrom(x0: Double, y0:Double) {
    import org.lwjgl.opengl.GL11._
    
    glColor3d(0.0f, 0.0f, 0.0f);
    
     glBegin(GL_POLYGON)
    	for (p <- boundary(x0, y0))
    	  glVertex2d(p._1, p._2);
    glEnd()
  }
  
  def drawTransparentAreaAsSeenFrom(x0: Double, y0:Double, inverseColor:Boolean = false) {
    import org.lwjgl.opengl.GL11._
    
    if (inverseColor) {
       glColor4d(companionColor.r, companionColor.g, companionColor.b, 0.3);
    } else {
       glColor4d(color.r, color.g, color.b, 0.3);
    }
   
    
     glBegin(GL_POLYGON)
    	for (p <- boundary(x0, y0))
    	  glVertex2d(p._1, p._2);
    glEnd()
  }
  
  def drawBoundaryAsSeenFrom(x0 : Double, y0 : Double, inverseColor:Boolean = false) {
    import org.lwjgl.opengl.GL11._
    
   if (inverseColor) {
       glColor4d(companionColor.r, companionColor.g, companionColor.b, 0.3);
    } else {
       glColor4d(color.r, color.g, color.b, 0.3);
    }
     glLineWidth(3.0f)
     glBegin(GL_LINE_STRIP)
    	for (p <- boundary(x0, y0))
    	  glVertex2d(p._1, p._2);
    glEnd()
     glLineWidth(1.0f)
  }
  
  def checkViewPoint(x0 : Double, y0 : Double) = {
    val a = DenseVector(xA, yA)
    val b = DenseVector(xB, yB)
    val p = DenseVector(x + 10 * cos(angle - Pi / 2.0),y + 10 * sin(angle - Pi / 2.0))
    val viewPoint = DenseVector(x0, y0)
    getAngleSign(a,b, p) == getAngleSign(a, b, viewPoint)
  }
}