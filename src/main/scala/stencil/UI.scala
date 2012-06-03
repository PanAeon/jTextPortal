package stencil
import portal.util.TimeNow
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import scala.annotation.tailrec
import org.lwjgl.input.Mouse
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.PixelFormat


object UI extends TimeNow {
  
  import org.lwjgl.opengl.GL11._
  import Math.{Pi,sin, cos, toDegrees, toRadians}
  
  val FPS = 60
  val SIZE_OF_CELL = 10
  val GRID_SIZE = 50
  val SIZE_X = SIZE_OF_CELL * GRID_SIZE
  val SIZE_Y = SIZE_OF_CELL * GRID_SIZE
  
  
  
  // 
  
  val viewPoint = Point(20., 0.)
  val portalA = Portal(0., 0., 50., Pi / 2.0  , MeColor(0.2, 0.2, 0.9))
  val portalB = Portal(150.0, 0.0, 50.0, Pi, MeColor(0.8, 0.8, 0.0))
  
 
  
 
  
  def main(args: Array[String]): Unit = {
   
    Display.setDisplayMode(new DisplayMode(SIZE_X, SIZE_Y))
    Display.create(new PixelFormat(0, 24, 8))
    
    initGL()

    mainloop
  }

  def initGL() {
    glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, SIZE_X, 0, SIZE_Y, 1, -1);
	glMatrixMode(GL_MODELVIEW);
	glShadeModel(GL_SMOOTH);
	glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);   
	glEnable(GL_BLEND);                         
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
    glClearStencil(0);    
  }
  
  var frame = 0
  
  @tailrec
  def mainloop {

    if (!Display.isCloseRequested ) {
    	updateFPS()
    	
    	
    	
    	render()
    	

    	pollInput();

    	Display.update()
    	Display.sync(FPS)
    	
    	
    	mainloop
    }
  }
  
  
  def render() {
      
	  // main scene:
    
    
    
	  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	  
	  
	  glClearStencil(0);
	  
	  
	  
	  glLoadIdentity();
	  glTranslatef(SIZE_X.toFloat /2, SIZE_Y.toFloat / 2, 0.f)
	  
	  drawScene()
	  
	  
	  val drawPortalA = portalA.checkViewPoint(viewPoint.x, viewPoint.y)
	  val drawPortalB = portalB.checkViewPoint(viewPoint.x, viewPoint.y)

    // **************
    // portalA:
    // **************
    if (drawPortalA) {

      glEnable(GL_STENCIL_TEST);
      glStencilFunc(GL_ALWAYS, 1, 1);
      glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

      // mask

      portalA.drawAreaAsSeenFrom(viewPoint.x, viewPoint.y)

      glStencilFunc(GL_EQUAL, 1, 1); // We Draw Only Where The Stencil Is 1
      // (I.E. Where The Floor Was Drawn)
      glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP); // Don't Change The Stencil Buffer

      // picture
      glColor3f(1.0f, 0f, 0f)
      // glRecti(0,0, 400, 400);
      glScaled(1.0, -1.0, 1.0)
      glTranslated(portalA.x, portalA.y, 0.f)
      glRotated(Math.toDegrees(Pi + portalA.angle - portalB.angle), 0, 0, 1);
      glTranslated(-portalB.x, -portalB.y, 0.f)
     
      drawScene()

      glDisable(GL_STENCIL_TEST);

    }
	  
	 // ************
	 // now portal B
	 // *************
	if (drawPortalB) {
	  glClear(GL_STENCIL_BUFFER_BIT);
	  
	  glClearStencil(0);

	  glLoadIdentity();
	  glTranslatef(SIZE_X.toFloat /2, SIZE_Y.toFloat / 2, 0.f)
	  
	  
	  
	  glEnable(GL_STENCIL_TEST);                    
	  glStencilFunc(GL_ALWAYS, 1, 1);                    
	  glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
	  
	  portalB.drawAreaAsSeenFrom(viewPoint.x, viewPoint.y)
	  
	    glStencilFunc(GL_EQUAL, 1, 1);                      
                                    						
	  glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);  
	  glColor3f(1.0f, 0f, 0f)
	  
	  glScaled(1.0, -1.0, 1.0)
	  glTranslated(portalB.x, portalB.y, 0.f)
	  glRotated(Math.toDegrees(portalB.angle - portalA.angle) , 0, 0, 1);
	  glTranslated(-portalA.x, -portalA.y, 0.f)
	  //glTranslatef(0.0f, -portalB.x.toFloat, 0.f)
	 drawScene()
	  
	  glDisable(GL_STENCIL_TEST); 
	}
	  // *****************
	  // Now boundaries
	  // *****************
	  
	  glClearStencil(0);
	  
	  glLoadIdentity();
	  glTranslatef(SIZE_X.toFloat /2, SIZE_Y.toFloat / 2, 0.f)
	  
	  if (drawPortalA) {
		  portalA.drawTransparentAreaAsSeenFrom(viewPoint.x, viewPoint.y)
		   
		  portalA.drawBoundaryAsSeenFrom(viewPoint.x, viewPoint.y)
	  }
	  
	  if (drawPortalB) {
	  portalB.drawTransparentAreaAsSeenFrom(viewPoint.x, viewPoint.y)
	  portalB.drawBoundaryAsSeenFrom(viewPoint.x, viewPoint.y)
	  }
	  
	  
	  
	  
  }
  
  
  def drawScene() {
    
    import Math.{sin, cos}
    glColor3d(0.2, 0.2, 02);
    glRecti(-220, -30, -60, 30)
    
    glColor3d(0.7, 0.8, 0.05);
    glRecti(-200, -20, -180, 20)
    
    glRecti(-160, -20, -140, 20)
    
    glRecti(-120, -20, -100, 20)
    
    glColor3d(0.2, 0.2, 02);
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
  
  var lastMouseX = -1
  var lastMouseY = -1
  var spacePressed = false
  
  def pollInput() {
    if (lastMouseX == Mouse.getX && lastMouseY == Mouse.getY && Mouse.isButtonDown(0)) {
      // ignore
    } else if (Mouse.isButtonDown(0)){
      viewPoint.x =  Mouse.getX - SIZE_X / 2
      viewPoint.y = Mouse.getY - SIZE_Y / 2
      lastMouseX = Mouse.getX
      lastMouseY = Mouse.getY
    }
    
    if (lastMouseX != -1 && !Mouse.isButtonDown(0) && !gameStarted) {
     // civilization.flip(lastMouseX / SIZE_OF_CELL, lastMouseY / SIZE_OF_CELL)
      lastMouseX = -1
      lastMouseY = -1
      
    }
  }

}

case class Point(var x : Double, var y : Double)
case class MeColor(val r : Double, val g : Double, val b : Double)

case class Portal(val x : Double, val y : Double, val length : Double, val angle : Double, val color : MeColor) {
  import Math.{Pi,sin, cos, toDegrees, toRadians}
  import scalala.scalar._;
   import scalala.tensor.::;
   import scalala.tensor.mutable;
   import scalala.tensor.dense._;
   import scalala.tensor.sparse._;
   import scalala.library.Library._;
   import scalala.library.LinearAlgebra._;
   import scalala.library.Statistics._;
   import scalala.library.Plotting._;
  
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
    
    
    
   
    // now we need take into account the boundary
    // we'll add additional points at the boundary
    

    // now return points in proper order
  // val foo0 = (xA, yA) :: (xA1, yA1) :: Nil
  //  val foo1 = (xB1, yB1) :: (xB, yB) :: Nil
  //  foo0 ::: getCorners(x0,y0,xA,yA,xB, yB, xA1, yA1, xB1, yB1) ::: foo1
   // foo0 ::: foo1
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
    val summ = mutable.Vector(0.0, 0.0)
    for (p <- points) {
      summ += p
    }
    summ / points.size
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
      val pointBetween = (a + b) / 2
      val angleToA = getAngleSign(center, a, pointBetween)
      val angleToB = getAngleSign(center, b, pointBetween)
      val aToIt = getAngleSign(center, a, it)
      val bToIt = getAngleSign(center, b, it)
      
      (angleToA == aToIt) && (angleToB == bToIt) && (getAngleSign(a, b, center) != getAngleSign(a, b, it))
  //     (isRightAngle(center, b, it) && isRightAngle(center, it, a))// ||
 //      (isRightAngle(it,b, a) && !isRightAngle(it, a, b))
 //      val a1 = getAngle(a-center, it-center)
 ////      val a2 = getAngle(it-center, b-center)
  //    if (abs(alpha - Pi) < 0.1)
   //     false
  //      else abs(a1 + a2 - alpha) < 0.1
    } 
    
    val foo = DenseVector(xA, yA) :: DenseVector(xA1, yA1) :: Nil
    
    val bar = DenseVector(xB1, yB1) :: DenseVector(xB, yB) :: Nil
    
    val t = foo ::: myCorners ::: bar
    
//    println("->>>")
//    for (too <- t) {
//      print(too)
//      println(";")
//    }
//    println("-<<<<")
    
   // z =  foo ::: myCorners.sorted(new DistanceOrdering(foo(1))) ::: bar
   // val z = t.head :: arrangeHull(t.head, t.tail) 
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
  
  def drawTransparentAreaAsSeenFrom(x0: Double, y0:Double) {
    import org.lwjgl.opengl.GL11._
    
    glColor4d(color.r, color.g, color.b, 0.3);
    
     glBegin(GL_POLYGON)
    	for (p <- boundary(x0, y0))
    	  glVertex2d(p._1, p._2);
    glEnd()
  }
  
  def drawBoundaryAsSeenFrom(x0 : Double, y0 : Double) {
    import org.lwjgl.opengl.GL11._
    
    glColor3d(color.r, color.g, color.b);
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