package portal.physics

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.Fixture
import scala.util.Random
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.collision.Manifold
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.dynamics.joints.PortalJointDef
import org.jbox2d.dynamics.joints.Joint
import org.jbox2d.dynamics.joints.MyPortalJointFactory
import org.jbox2d.common.MathUtils
import org.jbox2d.callbacks.ContactFilter
import org.jbox2d.collision.WorldManifold
import scala.collection.mutable
import org.jbox2d.common.Mat22
import portal.Renderable

class Physics {
  
	
  
	val timeStep = 1f / 60.0f
	val iterations = 5
	
	val gravity = new Vec2(0.0f, -10.0f)
	
	val world = new World(gravity, true)
	world.setWarmStarting(false)
	
	
	val defaultCategory = 0x0001
	val touchingCategory = 0x0002
	val dontColideMask = 0xFFFF ^ touchingCategory
	
	
	val torque = new Vec2(5f, 5f)
	val offset = new Vec2(0.5f, 0.5f)
	def step() {
	  
	//	box.body.applyForce(torque.mul(box.body.getMass), box.body.getWorldCenter.add(offset))
	// box.body.applyForce(antiGravity.mul((box.body.getMass())),box.body.getWorldCenter());
	 //shadow.body.applyForce(antiGravity.mul((shadow.body.getMass())),shadow.body.getWorldCenter());
	  //box.body.applyAngularImpulse(1500f)
	  PortalManager.applyForces();
	
	  world.step(timeStep, 40, 40)
	  
	  world.clearForces();
	  
	  PortalManager.step(world)
	  

	}
	
	val LIGHT_GREY = (0.6f, 0.6f, 0.6f)
	val PORTAL_BLUE = (0.278f, 0.459f, 1f)
	val PORTAL_ORANGE = (1.0f, 0.64f, 0f)
	val BOX_BLUE = (0.5f, 0.5f, 1.0f)
	val POINT_RED = (0.9f, 0.1f, 0.1f)
	
	
	val b1 = new BigStaticBox(world, (-6.0f, -3.5f), (1.5f, 1.5f), LIGHT_GREY)
	
	val b2 = new BigStaticBox(world, (1.0f, -3.5f), (1.5f, 1.5f), LIGHT_GREY)
	
	val b3 = new BigStaticBox(world, (4.0f, 1.8f), (1.5f, 1.5f), LIGHT_GREY)
	
	val b4 = new BigStaticBox(world, (-2.5f, 1.0f), (1.5f, 1.5f), LIGHT_GREY)
	
	val portal1 = new Portal(world, 0f, (-6.0f, -2.0f), 3.0f, PORTAL_BLUE, None)
	val portal2 = new Portal(world, 0f, (1.0f, -2.0f), 3.0f, PORTAL_ORANGE,None ) // MathUtils.PI/4
	
	portal1.companion = Some(portal2)
	portal2.companion = Some(portal1)
	
	PortalManager.activePortals += portal1
	PortalManager.activePortals += portal2
	
	//val p1 = new MePoint(-0.5f, 1.5f, POINT_RED)
	//val p2 = new MePoint(1.5f, 0.0f, POINT_RED)
	
	var box = new Box(world, (1f, 2.0f), (0.5f, 0.5f), BOX_BLUE, 10f, MathUtils.PI / 4)
	
	box.body.applyTorque(320f)
	
	DisplayStaff.list += box
//	var shadow = new Box(world, (p2.x, p2.y), (0.5f, 0.5f), BOX_BLUE, 10f, 0f)

	Joint.portalJointFactory = MyPortalJointFactory
	
	PortalContactListener.phys = this
	world.setContactListener(PortalContactListener)
	
	
}

object DisplayStaff {
  var list = mutable.ArrayBuffer[Renderable]()
}



class MePoint(val x:Float, val y:Float, val colour : (Float, Float, Float)) {
  val p = new Vec2(x,y)
  
  def render(scale:Float, translate : ((Float, Float)) => ((Float, Float))) {
    
    import org.lwjgl.opengl.GL11._
    
    val (x, y) = translate((p.x, p.y))
    
    glColor3f(colour._1,colour._2,colour._3);
    
    
     glBegin(GL_POINTS);
	  	glVertex2f(x,y);
	 glEnd();
  }
  
}

trait MeBody {
  
}




class Box(
    val world:World,
    val initPos : (Float, Float),
    val dimm : (Float, Float),
    val colour : (Float, Float, Float),
    val density: Float,
    val angle : Float) extends Renderable {
  
  var shadow : Option[Box] = None
  var master : Option[Box] = None
  var isShadow = false
  var portal : Option[Portal] = None
  
  val (body, fixture) = {
      val fd = new FixtureDef()
	  val sd = new PolygonShape()
	  sd.setAsBox(dimm._1, dimm._2)
	  fd.shape = sd
	  fd.density = density
	  
	  val bd = new BodyDef()
	  bd.`type` = BodyType.DYNAMIC
	  fd.friction = 0.5f 
	  bd.fixedRotation = false
	  bd.position = new Vec2(initPos._1, initPos._2)
      bd.angle = angle
      //bd.angularDamping = 0.2f
      //bd.linearDamping = 0.025f
	  val myBody = world.createBody(bd)
	  
	  myBody.setUserData(this) // ugly !!!!
	  
	  val fixture = myBody.createFixture(fd)
	  (myBody, fixture)
  }
  
  
  def deepCopy(pos : (Float, Float), rotation : Float) = {
    new Box(world, pos, dimm, colour, density, rotation)
  }
  
 
  
  
   def render(scale:Float, translate : ((Float, Float)) => ((Float, Float))) {
    
    import org.lwjgl.opengl.GL11._
    import org.lwjgl.opengl.GL13._
    
    val pos = body.getPosition()
    val angle = body.getAngle()
    
    val hw = dimm._1 * scale
    val hh = dimm._2 * scale
    
    val (x, y) = translate((pos.x, pos.y))
    
    
    //if (!isShadow)
    	glColor3f(colour._1,colour._2,colour._3);
    //else 
    //   glColor4f(colour._1,colour._2,colour._3, 0.5f);
    glPushMatrix();
    
//        glEnable(GL_SCISSOR_TEST);
//
//    if (portal.isDefined) {
//        val p = portal.getOrElse(throw new RuntimeException("???"))
//        
//        glPushMatrix();
//        
//        glRotatef(-p.angle * 180 / MathUtils.PI, 0 , 0, 1)
//        val (px, py) = translate((p.pos._1, p.pos._2))
//        glTranslatef(px, py, 0)
//        
//    	glScissor(-200, 0, 400, 200);
//        glPopMatrix();
//    }
    
   
    
    glTranslatef(x, y, 0);

    //glRotatef(  angle / MathUtils.PI * 180, 0, 0, 1);
    glRotatef((angle * 180 / MathUtils.PI) , 0, 0, 1);
    
 
     
     glBegin(GL_QUADS);
	  	glVertex2f( - hw, - hh);
	  	glVertex2f( - hw, + hh);
	  	glVertex2f( + hw, + hh);
	  	glVertex2f( + hw, - hh);
	 glEnd();
	 
	 
	 glPopMatrix();
//	 glDisable(GL_SCISSOR_TEST);
  }
  
  
}



class Portal(
    val world:World, 
    val angle : Float, 
    val pos : (Float, Float), 
    val length : Float, 
    val colour : (Float, Float, Float), 
    var companion : Option[Portal]) {
  
  val thikness = 0.22f
  val (body, sensor) = {
    
     val bd = new BodyDef
	 bd.`type` = BodyType.STATIC
	 bd.angle = angle
	 
	 bd.position = new Vec2(pos._1, pos._2)
    
    val shape = new PolygonShape();
   
    shape.setAsBox(length/2, thikness)
    
    val fd = new FixtureDef()
    fd.shape = shape;
    
    fd.isSensor = true;
    
    val myBody = world.createBody(bd)
    val myFixture = myBody.createFixture(fd)
    (myBody, myFixture)
  }
  
  import MathUtils.{sin, cos, PI}
  // world -> portal CCW
  val R = new Mat22(cos(angle), -sin(angle), sin(angle), cos(angle))
  val R_ = new Mat22(cos(angle + PI), -sin(angle + PI), sin(angle + PI), cos(angle + PI))
  // portal -> world CW
  val Q = new Mat22(cos(angle), sin(angle), -sin(angle), cos(angle))
  val Q_ = new Mat22(cos(angle + PI), sin(angle + PI), -sin(angle + PI), cos(angle + PI))
  
  def positionV() = new Vec2(pos._1, pos._2)
  
  def render(scale:Float,translate : ((Float, Float)) => ((Float, Float))) {
    
     import org.lwjgl.opengl.GL11._
     
     val (x, y) = translate((pos._1, pos._2))
     val hw = length * scale / 2
     val hh = thikness * scale
     
     glColor4f(colour._1,colour._2,colour._3, 0.5f);
     
    glPushMatrix();
    
    glTranslatef(x, y, 0);

    glRotatef((-angle * 180 / MathUtils.PI) , 0, 0, 1);
     
    
     glBegin(GL_QUADS);
	  	glVertex2f( - hw, - hh);
	  	glVertex2f( - hw, + hh);
	  	glVertex2f( + hw, + hh);
	  	glVertex2f( + hw, - hh);
	 glEnd();
	 
	 glColor3f(1f, 0f, 0f);
	 glBegin(GL_LINES);
	 	glVertex2f(0,0)
	 	glVertex2f(0f, 0.25f * scale);
	 glEnd;
	 
	 glColor3f(0f, 1f, 0f);
	 glBegin(GL_LINES);
	 	glVertex2f(0,0)
	 	glVertex2f(0.25f * scale, 0f);
	 glEnd;
	 
	 glPopMatrix();
  
  }
}





object PortalEQ {
  
  val buffer = mutable.ListBuffer[ContactEvent]()
  
  def add(e : ContactEvent) {
    buffer += e
  }
  
  
  def toList() = {
    buffer.toList
  }
  
  def clear() {
     buffer.clear()
  }
  
  
}

trait ContactEvent
case class BeginContactEvent(a : Fixture, b : Fixture) extends ContactEvent
case class EndContactEvent(a : Fixture, b : Fixture) extends ContactEvent

object PortalContactListener extends ContactListener {
  
  var phys:Physics = _
  
  override def beginContact(contact:Contact) {
    val a = contact.getFixtureA()
    val b = contact.getFixtureB()
    
    if (PortalManager.isPortal(a)) 
      PortalEQ add BeginContactEvent(a, b)
    
   if (PortalManager.isPortal(b))
      PortalEQ add BeginContactEvent(b, a)
  }
  
  override def endContact(contact:Contact) {
    
    val a = contact.getFixtureA()
    val b = contact.getFixtureB()
    
    if (PortalManager.isPortal(a)) 
    	PortalEQ add EndContactEvent(a, b)
    
    if (PortalManager.isPortal(b)) 
	   PortalEQ add EndContactEvent(b, a)  
  }
  
  // inspect contact before it goes to the solver,
  // can modify contact manifold (disable contact)
  override def preSolve(contact : Contact, oldManifold : Manifold) {
    
    PortalManager.filterBehindPortalCollisions(contact, oldManifold)
    
  }
  
  override def postSolve(contact : Contact, impulse : ContactImpulse ) {
    
  }
}


//	  if (box.p1_contact_begin) {
//	    val fdata1 = box.fixture.getFilterData()
//	    fdata1.categoryBits = touchingCategory
//	    fdata1.maskBits = dontColideMask
//	    box.fixture.setFilterData(fdata1)
//	    
//	    val fdata2 = b1.fixture.getFilterData()
//	    	fdata2.categoryBits = touchingCategory
//	    	fdata2.maskBits = dontColideMask
//	    	b1.fixture.setFilterData(fdata2)
//	  }
//	  
//	  if(box.p1_contact_end) {
//	    box.fixture.getFilterData().groupIndex = 0
//	    b1.fixture.getFilterData().groupIndex = 0
//	  }
	  // manual adj.