package portal

import scala.annotation.tailrec
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11._
import org.jbox2d.collision.WorldManifold
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.common.Mat22
import org.jbox2d.common.MathUtils
import org.jbox2d.common.Vec2
import portal.util.TimeNow
import portal.physics.Physics
import portal.physics.DisplayStaff



object Portal2D extends TimeNow {

  val FPS = 60
  val SIZE_X = 960
  val SIZE_Y = 540
  
  val world : Physics = new Physics

  def main(args: Array[String]): Unit = {
   
    Display.setDisplayMode(new DisplayMode(SIZE_X, SIZE_Y))
    Display.create()
    
    initGL()

    mainloop
  }

  def initGL() {
    glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, 960, 0, 540, 1, -1);
	glMatrixMode(GL_MODELVIEW);
	glShadeModel(GL_SMOOTH);
	glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);   
	glEnable(GL_BLEND);     
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
  }
  
  
  
  @tailrec
  def mainloop {
    if (!Display.isCloseRequested ) {
    	updateFPS()
    	
    	world.step
    	render()
    	
    	Display.update()
    	Display.sync(FPS)
    	mainloop
    }
  }
  
  
  def render() {
      
	  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	  
	  
	  
	  // draw b1
	
	 
	 DisplayStaff.list.foreach(_.render(scale, translate))
	 
	 world.b1.render(scale, translate)
	 world.b2.render(scale, translate)
	 world.b3.render(scale, translate)
	 world.b4.render(scale, translate)

	 world.portal1.render(scale, translate)
	 world.portal2.render(scale, translate)
	 
	// world.p1.render(scale, translate)
	 //world.p2.render(scale, translate)
	 
	 
	  
//	  var ce = world.box.body.getContactList()
//	  while (ce != null) {
//	    if ( ce.contact.isTouching()) {
//	    	world.box.body.destroyFixture(world.box.fixture)
//	    	
//	    	world.box = Box.create(world.world)
//	      
//	    }
//	    ce = ce.next
//	  }
	  //world.world.m_contactManager.ge
	  
  }
  
  var f : WorldManifold = _
  
 
   val scale = 25.0f;
   
   def translate(p:(Float,Float)) = {
     
     val center_x = SIZE_X.toFloat / 2;
     val center_y = SIZE_Y.toFloat / 2;
    
     (center_x + p._1 * scale, center_y + p._2 * scale)
   }

}

