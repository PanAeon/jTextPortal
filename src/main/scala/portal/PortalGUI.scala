package portal

import javax.swing.JComponent
import java.awt.AWTEvent
import java.awt.event._
import java.awt.image.{BufferedImage}
import java.awt.{Graphics2D, Graphics, Color, BasicStroke, Dimension, Composite, RenderingHints, CompositeContext, GridBagConstraints}

case class Point(val x:Int, val y:Int)

class PortalGUI extends JComponent {
  
    var currentSelection = Point(-1,-1)
    var dragStart = Point(-1,-1)
    var hoverPoint = Point(-1,-1)
    
     val bgColor               = new Color(5, 5, 5)
     val gridColor             = new Color(25, 25, 25)


      val thinStroke    = new BasicStroke(1)
  val normStroke    = new BasicStroke(2)
  val mediumStroke  = new BasicStroke(3)
  val thickStroke   = new BasicStroke(6)
    
  val white = new Color(255, 255, 255)
  val red = new Color(255, 0, 0)
  val green = new Color(0, 255, 0)
  val blue = new Color(0, 0, 255)
  val yellow = new Color(255, 255, 0)
  val teal = new Color(0, 255, 255)
  val purple = new Color(255, 0, 255)



  
	var arenaModified = true
	
	
  enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK )

    
   final private def drawLine(g : Graphics2D, p1 : Point, p2 : Point) = {
      g.drawLine(p1.x, p1.y, p2.x, p2.y)
  }
    
  override def processMouseMotionEvent (e : MouseEvent ) = {
    if ((dragStart.x != -1) && (dragStart.y != -1)) {
      val d = getSize()
     // val (_, hscale, vscale) = calcScale(d)
   //   val newHoverPoint = Point((e.getPoint.getX / hscale).toInt, (e.getPoint.getY / vscale).toInt)
   //   if (newHoverPoint != hoverPoint) {
      //  hoverPoint = newHoverPoint
//        if (FieldsModel.goal != hoverPoint && FieldsModel.start != hoverPoint && (hoverPoint.x < 60) && (hoverPoint.y < 60)
//            && (hoverPoint.y >= 0) && (hoverPoint.x >= 0))
//        	FieldsModel.flipObstacle(hoverPoint);
        this.repaint(300)
    //  }
    }
  }
  
   override def processMouseEvent (e : MouseEvent ) = {
      if ((e.getID == MouseEvent.MOUSE_PRESSED)) {
          val d = getSize()
      //    val (_, hscale, vscale) = calcScale(d)
      //    val clickPoint = Point((e.getPoint.getX / hscale).toInt, (e.getPoint.getY / vscale).toInt)
          
//          if ( e.isShiftDown()) {
//            FieldsModel.obstacles(clickPoint.x)(clickPoint.y) = false
//            FieldsModel.start = clickPoint;
//            this.repaint(100);
//          } else if ( e.isControlDown()) {
//            FieldsModel.obstacles(clickPoint.x)(clickPoint.y) = false
//            FieldsModel.goal = clickPoint;
//            this.repaint(100);
//            FieldsModel.calculatePotential()
//          } else {
//              if (FieldsModel.goal != clickPoint && FieldsModel.start != clickPoint)
//            	  FieldsModel.flipObstacle(clickPoint);
//        	  dragStart = clickPoint
//        	  FieldsModel.calculatePotential()
//        	  this.repaint(100)
//          }

      } else if ((e.getID == MouseEvent.MOUSE_RELEASED)) {
          val d = getSize()
         
          
       //   val releasePoint = Point((e.getPoint.getX / hscale).toInt, (e.getPoint.getY / vscale).toInt)
          dragStart = Point(-1, -1)
        //  FieldsModel.calculatePotential()
          this.repaint(100)
      }
  }

  override def processKeyEvent (e : KeyEvent ) = {
      if (e.getID == KeyEvent.KEY_PRESSED) {
          e.getKeyCode match {
              case KeyEvent.VK_SPACE => {
//                  arena.turnCCW(currentSelection)
//                  arenaModified = true
               //   FieldsModel.calculatePotential()
                  this.repaint(100)
              }
              case KeyEvent.VK_ENTER => {
            	//  FieldsModel.move(this)
              }
             
              case _ => null // println ("Key not supported: " + e.getKeyCode)
          }
          // println ("Key : " + KeyEvent.getKeyText(e.getKeyCode))
      }
  }


   var offscreen = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB)

   
   
  override def paint(g: Graphics):Unit = {
      val d = getSize()
      if ((d.width != offscreen.getWidth) || (d.height != offscreen.getHeight)) {
          offscreen = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB)
      }
      val bufferedGraphics = offscreen.getGraphics
      // bufferedGraphics.setColor (bgColor)
      // dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);
      paintHouse(bufferedGraphics)
      g.drawImage(offscreen, 0, 0, this)
  }
   
    final private def paintHouse(oldg: Graphics):Unit = {
      val g = oldg.asInstanceOf[Graphics2D]
      val dim = getSize()
     // val (minLength, hscale, vscale) = calcScale(dim)
      
     // g.setColor(bgColor)
    //  g.fillRect(0,0, minLength, minLength)

    //  g.setClip(0,0,arena.bounds.width * hscale - 1,arena.bounds.height * vscale - 1)

      g.setColor (green)
      g.setStroke(thinStroke)
      
      g.drawRect(50, 50, 60, 120)
      
      
      Game.walls.foreach(_.paint(g))
//      0.until(arena.bounds.width).foreach (i => {
//                  g.drawLine(i * hscale, 0, i * hscale, minLength)
//              })
//      0.until(arena.bounds.height).foreach (i => {
//                  g.drawLine(0, i * vscale, minLength, i * vscale)
//              })
              
      // potentials
      
    //  for (x <- Array.range(0, 60); y <- Array.range(0,60)){
       // g.setColor(new Color(0, 0, getIntensity(FieldsModel.getPotential(Point(x, y)))))
      //  g.fillRect(x * hscale, y * vscale, hscale, vscale)
   //   }
      //  g.setStroke(normStroke)
      //  g.setColor (red)
      
   //  for (x <- Array.range(0, 60); y <- Array.range(0,60)){
       //if (FieldsModel.obstacles(x)(y)) {
       
    	   //g.drawRect(x * hscale, y * vscale, hscale, vscale)
    	//   g.fillRect(x * hscale, y * vscale, hscale, vscale)
      // }
   //  }
      
   //  g.setColor(green);
    // g.fillRect(FieldsModel.goal.x * hscale, FieldsModel.goal.y * vscale, hscale, vscale)
     
    // g.setColor(yellow);
    // g.fillRect(FieldsModel.start.x * hscale, FieldsModel.start.y * vscale, hscale, vscale)
     
      

   



    }
    
   // def getIntensity( value : Int) : Int = {
     //  7200 / 255 = 28
     // scala.Math.min(255, (value / 14))
   // }
    
    



object arena {
    val dimmensions = Point(640, 360); 
}



}


case class Wall(val position:Point, box: Point) {
    // pour !
	def paint(g: Graphics){
	  g.setColor (Game.gray)
	  g.fillRect(position.x, position.y, box.x, box.y)
	}
}

object Game {
  val gray = new Color(140, 140, 140)
  val walls = Wall(Point(20, 20),Point(920, 20)) :: Wall(Point(20, 500),Point(920, 20)) :: Nil
}


//import java.awt.{CompositeContext, RenderingHints}
//import java.awt.image.{ColorModel, ComponentColorModel, Raster, WritableRaster, IndexColorModel, PackedColorModel}
//
//object AddComposite extends java.awt.Composite {
//    class AddCompositePackedContext extends CompositeContext {
//        def compose (src : Raster, dst : Raster, w : WritableRaster) = {
//            val minX = Math.max (src.getMinX, dst.getMinX)
//            val maxX = minX + Math.min(src.getWidth, dst.getWidth)
//            val minY = Math.max (src.getMinY, dst.getMinY)
//            val maxY = minY + Math.min(src.getHeight, dst.getHeight)
//            for (i <- minX until maxX; j <- minY until maxY; band <- 0 until Math.min(src.getNumBands, dst.getNumBands)) {
//                val value = Math.min(src.getSample(i,j,band) + dst.getSample(i,j,band), 255)
//                w.setSample(i, j, band, value)
//            }
//        }
//        def dispose = {}
//    }
//
//    def createContext (cm : ColorModel, cm2 : ColorModel, rh : RenderingHints) : CompositeContext = new AddCompositePackedContext
//}


