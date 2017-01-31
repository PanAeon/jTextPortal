package portal

import scala.annotation.tailrec

import org.lwjgl.opengl.GL11._
import org.jbox2d.collision.WorldManifold
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.common.Mat22
import org.jbox2d.common.MathUtils
import org.jbox2d.common.Vec2

import portal.physics.Physics
import portal.physics.DisplayStaff
import utils.OpenGLApp


//-Djava.library.path=/home/vitalii/lab/jTextPortal/lib/linux
object Portal2D extends OpenGLApp {

  val FPS = 60
  override val Width = 1200  
  override val Height = 800
  
  val world : Physics = new Physics

  def main(args: Array[String]): Unit = {
   
   
  

    run()
  }

      
  def gluPerspective(fovy: Double, aspect: Double, zNear: Double, zFar: Double) {
    import scala.math.{ tan, Pi }

    var xmin, xmax, ymin, ymax = 0.0;

    ymax = zNear * tan(fovy * Pi / 360.0);
    ymin = -ymax;
    xmin = ymin * aspect;
    xmax = ymax * aspect;

    glFrustum(xmin, xmax, ymin, ymax, zNear, zFar);
  }
  
 override def initGL() {
   
   
   // glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
    

    glMatrixMode(GL_PROJECTION);
	  glLoadIdentity();
	  glOrtho(0, 960, 0, 540, 1, -1);
  	glMatrixMode(GL_MODELVIEW);
  	glShadeModel(GL_SMOOTH);
	  glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);   
	  glEnable(GL_BLEND);     
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
  }
  
  

  
  override def render() {
      world.step
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
  
//  var f : WorldManifold = _
  
 
   val scale = 25.0f;
   
   def translate(p:(Float,Float)) = {
     
     val center_x = Width.toFloat / 2;
     val center_y = Height.toFloat / 2;
    
     (center_x + p._1 * scale, center_y + p._2 * scale)
   }

}

