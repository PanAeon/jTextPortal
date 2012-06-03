package portal.physics
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.common.Vec2

class BigStaticBox(val world:World, val initPos :(Float,Float), val dimm : (Float, Float), val colour : (Float, Float, Float)) {
  
  val (body, fixture) = {
     val fd = new FixtureDef()
	 val sd = new PolygonShape()
     
	 sd.setAsBox(dimm._1, dimm._2)
	 fd.shape = sd
	  
	 val bd = new BodyDef
	 bd.position = new Vec2(initPos._1, initPos._2)
	 val myBody = world.createBody(bd)
	 val myFixture = myBody.createFixture(fd)
	 (myBody,myFixture)
  }

  def render(scale:Float, translate : ((Float, Float)) => ((Float, Float))) {
    
    import org.lwjgl.opengl.GL11._
    
    val pos = body.getPosition()
    val hw = dimm._1 * scale
    val hh = dimm._2 * scale
    
    val (x, y) = translate((pos.x, pos.y))
    
    glColor3f(colour._1,colour._2,colour._3);
    
    
     glBegin(GL_QUADS);
	  	glVertex2f(x - hw,y - hh);
	  	glVertex2f(x - hw,y + hh);
	  	glVertex2f(x + hw,y + hh);
	  	glVertex2f(x + hw,y - hh);
	 glEnd();
  }
  
}