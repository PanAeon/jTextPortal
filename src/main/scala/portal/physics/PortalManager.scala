package portal.physics

import scala.collection.mutable
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.joints.PortalJointDef
import org.jbox2d.common.Vec2
import org.jbox2d.common.MathUtils
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.collision.Manifold
import org.jbox2d.collision.WorldManifold
import MathUtils.PI

object PortalManager {
  val activePortals = mutable.ArrayBuffer[Portal]()
  
  var shadows = List[Box]()
  
  
  def isPortal(fixture : Fixture) = activePortals.exists(_.sensor == fixture)
  
  def handleContact(sensor : Fixture, fixture : Fixture) {
    // create shadow 
  //  println("Contact!")
    
    activePortals.find(_.sensor == sensor) match {
      case Some(portal) =>
        
        val companion = portal.companion.getOrElse(throw new RuntimeException("Portal is not active :("))
        
        val box = fixture.getBody().getUserData().asInstanceOf[Box]
        
        if (box.isShadow) return;
        
        // New position calculations ...
       
        // x1 = Q1 * R2_ * (x2 - g2) + g1
        val shadowPos = (companion.Q.mul(portal.R_).mul(box.body.getPosition().sub(portal.positionV))).add(companion.positionV)
        val shadowAngle = box.body.getAngle() - portal.angle + companion.angle - MathUtils.PI
        val shadowAngleAdj = if (shadowAngle > 2 * PI) shadowAngle - 2 * PI else shadowAngle
        
        val shadow = box.deepCopy((shadowPos.x, shadowPos.y), shadowAngleAdj)
        
        // new velocity calculations
        
        // v2 = Q2 * R2_ * v1
        val shadowVel = (companion.Q.mul(portal.R_)).mul(box.body.getLinearVelocity)
        val angularVelocity = box.body.getAngularVelocity()
        
        shadow.body.setLinearVelocity(shadowVel)
        shadow.body.setAngularVelocity(angularVelocity)
        
        DisplayStaff.list += shadow
        
        shadow.isShadow = true
        shadow.master = Some(box)
        box.shadow = Some(shadow)
        
        shadows = shadow :: shadows;
        
        box.portal = Some(portal)
        shadow.portal = Some(companion)
        
        // connect box and shadow
        val jointDef = new PortalJointDef()
        jointDef.initialize(box.body, shadow.body, portal.positionV, portal.angle, companion.positionV, companion.angle)
        
        val joint = box.world.createJoint(jointDef)
        
        
      case None => // strange but true.... skipping
    }
    
  }
  
  def handleDetachment(sensor : Fixture, fixture : Fixture) {
    val box = fixture.getBody().getUserData().asInstanceOf[Box]
    if (box.isShadow) {
      box.world.destroyBody(box.body)
      val idx = DisplayStaff.list.indexWhere(_ == box)
      DisplayStaff.list.remove(idx);
      
      box.master match {
        case Some(b) => b.shadow = None; b.portal = None
        case None =>
      }
      
      shadows = shadows.filter( _ != box)
      
    }
    
   
  }
  
  
  def step(world:World) {
    // query portal event queue
    val events = PortalEQ.toList
    
    events foreach{
      _ match {
        case e:BeginContactEvent => handleContact(e.a, e.b)
        case e:EndContactEvent => handleDetachment(e.a, e.b)
        case _ => throw new RuntimeException("Not implemented")
      }
    }
    
    // PLZ don't forget me
    PortalEQ.clear()
    
    // swap shadow and body if reached center
    shadows.foreach{ shadow =>
      val portal = shadow.portal.getOrElse(throw new RuntimeException("No portal for pure shadow"))
      val oldMaster = shadow.master.getOrElse(throw new RuntimeException("No master for shadow"))
      val oldMasterPortal = oldMaster.portal.getOrElse(throw new RuntimeException("no portal for master"))
      
      val y = portal.R.mul(shadow.body.getWorldCenter.sub(portal.positionV)).y;
     // val relativeCoord2 = oldMasterPortal.R.mul(oldMaster.body.getWorldCenter.sub(oldMasterPortal.positionV))
      
//      println("C1>>>" + relativeCoord1.x + "<<<>>>" + relativeCoord1.y)
//        println("C2>>>" + relativeCoord2.x + "<<<>>" + relativeCoord2.y)
      if (
          y > 0.01
         // (portal.R.mul(shadow.body.getWorldCenter.sub(portal.positionV)).y < 0.03) &&
         // (oldMasterPortal.R.mul(oldMaster.body.getWorldCenter.sub(oldMasterPortal.positionV)).y < 0.03)
         ) {
        val newShadow = shadow.master.getOrElse(throw new RuntimeException("No master for shadow"))
        val newMaster = shadow
        newShadow.master = Some(newMaster)
        newShadow.isShadow = true
        newMaster.isShadow = false
        
        shadows = shadows.filter( _ != newMaster)
        shadows = newShadow :: shadows
      }
    }
  }
  
  def applyForces() {
    // anti-gravity for shadows ... ;
    val antiGravity=new Vec2(0.0f, 10.0f);
    shadows.foreach(box => box.body.applyForce(antiGravity.mul((box.body.getMass())),box.body.getWorldCenter()))
    
  }
  
  def filterBehindPortalCollisions(contact : Contact, oldManifold : Manifold) {
    import MathUtils.abs
    
    val bodyA = contact.getFixtureA().getBody()
    val bodyB = contact.getFixtureB().getBody()
    
    var box: Box = null
    
    if (bodyA.getUserData() != null) {
      box = bodyA.getUserData().asInstanceOf[Box]
    } else if (bodyB.getUserData() != null) {
      box = bodyB.getUserData().asInstanceOf[Box]
    } else {
      return
    }
    
    if (box.portal.isEmpty)
      return
      
    //
    // if fixture is on the body of an *in* portal, && contact point is behind the 'portal' line then filter it out
    //  'on' contact line but inside contact -> filter it out; -> near the contact line but not inside portal -> accept it
    val pointCount = contact.getManifold().pointCount
    if(pointCount == 0)
      return
    
    val manifold = new WorldManifold()
    
    contact.getWorldManifold(manifold)
   // manifold.normal
   // manifold.points
    
    val threshold = if (box.isShadow) 0.5f else -0.10f
    // if each point is in 'shadow area then filter this contact out
    val portal = box.portal.getOrElse( throw new RuntimeException("No portals, suddenly"))
    
    val allPointsAreInShadow = manifold.points.slice(0,pointCount) forall { point =>
      
      val relative = portal.R.mul(point.sub(portal.positionV))
     // println(relative.x, relative.y)
      val halfLength = portal.length / 2
      (relative.y < threshold) || (relative.y < 0.1 && (abs(relative.x) < halfLength))
    }
    
    if (allPointsAreInShadow)
      contact.setEnabled(false)
    
  }
  
  
 
  
}