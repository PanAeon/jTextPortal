package portal

import scala.collection.mutable.ArrayBuffer
import org.jbox2d.dynamics.Body
import org.jbox2d.collision.AABB
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.common.Vec2
import org.jbox2d.callbacks.QueryCallback
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.callbacks.DebugDraw
import java.awt.Color

class PhysicsWorld(val panel : TestPanelJ2D) {
	//val targetFPS = 60
	//val timeStep = 1000 / targetFPS
	val timeStep = 1f / 60.0f
	val iterations = 5
	


	// init world
	
	val gravity = new Vec2(0.0f, -10.0f)
	val world = new World(gravity, true)
	
	//val ps = new PolygonShape();
   // ps.setAsBox(96.0f,54.0f);
//         
   // val fd = new FixtureDef();
  //  fd.shape = ps;
// 
    val bd = new BodyDef();
  //  bd.position= new Vec2(0.0f,-10f);
// 
    world.createBody(bd)//.createFixture(fd);
	
	// val bodyDef = new BodyDef()

	//val groundBody = world createBody bodyDef

	
	def step() {
	  
	  	
		
	 // world.setWarmStarting(true);
	  world.step(timeStep, 5, 3)
	  world.drawDebugData()
	  //println("done step")
	}
	
	def initDominoTest() {
	  world.setDebugDraw(panel.debugDraw);
	  // Floor
	  {
	  val fd = new FixtureDef()
	  val sd = new PolygonShape()
	  sd.setAsBox(20.0f, 2.0f)
	  fd.shape = sd
	  
	  val bd = new BodyDef()
	  bd.position = new Vec2(0.0f, -10.0f)
	  
	  
	  world.createBody(bd).createFixture(fd)
	  }
	  // Platforms
	  for( i <- List.range(1,5) ) {
	    val fd = new FixtureDef()
	    val sd = new PolygonShape()
	    sd.setAsBox(15.0f, 0.2f)
	    fd.shape = sd
	    
	    val bd = new BodyDef()
	    bd.position = new Vec2(0.0f, 5f + 5f * i)
	    world.createBody(bd).createFixture(fd)
	  }
	  
	  // Dominos!!
	  {
	    val fd = new FixtureDef()
	    val sd = new PolygonShape()
	    sd.setAsBox(0.125f, 2f)
	    fd.shape = sd
	    fd.density = 25.0f
	    
	    val bd = new BodyDef()
	    bd.`type` = BodyType.DYNAMIC
	    val friction = 0.5f
	    val numPerRow = 25
	    for ( i <- List.range(1,5); j <- List.range(0, numPerRow)){
	      fd.friction = friction;
	      bd.position = new Vec2(-14.75f + j * (29.5f / (numPerRow - 1)), 7.3f + 5f * i);
	      if ( i == 2 && j == 0) {
	        bd.angle = -0.1f;
	        bd.position.x += .1f
	      } else if (i == 3 && j == numPerRow - 1) {
	        bd.angle = 0.2f
	        bd.position.x -= 0.2f
	      } else 
	        bd.angle = 0f
	      val myBody = world.createBody(bd)
	      myBody.createFixture(fd)
	    }
	  }
	}
	
	
}