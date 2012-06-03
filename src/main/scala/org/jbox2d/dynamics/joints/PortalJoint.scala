package org.jbox2d.dynamics.joints

import org.jbox2d.pooling.IWorldPool
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.TimeStep
import scala.collection.mutable.ArrayBuffer
import org.jbox2d.common.Vec3
import org.jbox2d.common.Mat22
import scala.collection.mutable.ArrayBuilder
import org.jbox2d.common.MathUtils
import org.jbox2d.common.Settings

class PortalJoint(worldPool: IWorldPool, definition : PortalJointDef) extends Joint(worldPool, definition) {
  import MathUtils.{sin, cos, PI}
  // before solving
  val b1 = m_bodyA;
  val b2 = m_bodyB;
  val ground1 = definition.ground1.clone();
  val ground2 = definition.ground2.clone();
  
  val t1 = definition.ground1Angle
  val t2 = definition.ground2Angle
 
 // world -> portal CCW
 val R1 = new Mat22(cos(t1), -sin(t1), sin(t1), cos(t1))
 val R2 = new Mat22(cos(t2 + PI), -sin(t2 + PI), sin(t2 + PI), cos(t2 + PI))//new Mat22(cos(t2), -sin(t2), sin(t2), cos(t2))
// val R2_ = new Mat22(cos(t2 + PI), -sin(t2 + PI), sin(t2 + PI), cos(t2 + PI))
 // portal -> world CW
 val Q1 = new Mat22(cos(t1), sin(t1), -sin(t1), cos(t1))
 val Q2 = new Mat22(cos(t2 + PI), sin(t2 + PI), -sin(t2 + PI), cos(t2 + PI))//new Mat22(cos(t2), sin(t2), -sin(t2), cos(t2))
 //val Q2_ = new Mat22(cos(t2 + PI), sin(t2 + PI), -sin(t2 + PI), cos(t2 + PI))
  


 
  
  
  val effectiveMass = Array.ofDim[Float](3,3) // Effective constraint mass
   
  // state after solving
  // Accumulated  lambdas
  val lambda = Array(0f, 0f, 0f)
 
  
  // arbitrary
  override def getAnchorA(out: Vec2): Unit = {val c = m_bodyA.getWorldCenter(); out.x =c.x; out.y = c.y; }
  // arbitrary
  override def getAnchorB(out: Vec2): Unit = { val c = m_bodyB.getWorldCenter(); out.x =c.x; out.y = c.y;  }

  
  override def getReactionForce(inv_dt: Float, argOut: Vec2): Unit = {
    argOut.x =  lambda(0) * inv_dt;
    argOut.y =  lambda(1) * inv_dt;
  }

  override def getReactionTorque(inv_dt: Float): Float = { 
	 inv_dt * lambda(2)
  }

  override def initVelocityConstraints(step: TimeStep): Unit = { 
    
    // compute effective mass
    effectiveMass(0)(0) = 1 / (b1.m_invMass + b2.m_invMass) //inverse
    effectiveMass(1)(1) = 1 / (b1.m_invMass + b2.m_invMass) // inverse
    effectiveMass(2)(2) = 1 / (b1.m_invI + b2.m_invI)      // inverse

    assert (b1.m_invI + b2.m_invI != 0, "Inertia must be set for this to work correctly")
    
    lambda(0) = 0f
    lambda(1) = 0f
    lambda(2) = 0f
  }

  override def solveVelocityConstraints(step: TimeStep): Unit = { 

    val v1 = b1.m_linearVelocity
    val v2 = b2.m_linearVelocity // world coordinates
   
    val v1_ = new Vec2();
    Mat22.mulToOut(R1, v1, v1_);
    val v2_ = new Vec2();
    Mat22.mulToOut(R2, v2, v2_);  // portal coordinates
    
    // calculate Cdot:
    val cdot = Array(
     v2_.x - v1_.x,
     v2_.y - v1_.y,
     b2.m_angularVelocity - b1.m_angularVelocity
     )
    
    // calculate lambda
     val lambda = Array(0f, 0f, 0f)
     for ( i <- 0 until 3) {
       lambda(i) =  -(effectiveMass(i)(0) * cdot(0) + effectiveMass(i)(1) * cdot(1) +  effectiveMass(i)(2) * cdot(2))
     }
    
  
     
    
    val P1 = new Vec2(lambda(0), lambda(1))
    Q1.mulToOut(P1, P1)
    
    val P2 = new Vec2(lambda(0), lambda(1))
    Q2.mulToOut(P2, P2); // now in world coordinates 
    
    // apply impulse
    b1.m_linearVelocity.x -= b1.m_invMass * P1.x
    b1.m_linearVelocity.y -= b1.m_invMass * P1.y
    b1.m_angularVelocity -= b1.m_invI * lambda(2)
    
    b2.m_linearVelocity.x += b2.m_invMass  * P2.x
    b2.m_linearVelocity.y += b2.m_invMass  * P2.y
    b2.m_angularVelocity += b2.m_invI * lambda(2)
    
   // println("VELOCITY:::\t\t"  + Math.abs(cdot(2)))

     this.lambda(0) += P2.x
     this.lambda(1) += P2.y
    // this.lambda(2) += lambda(2)
  }
  

  override def solvePositionConstraints(baumgarte: Float): Boolean = {
   
    val diff1 = R1.mul( b1.m_sweep.c.sub(ground1))
    val diff2 = R2.mul( b2.m_sweep.c.sub(ground2))
 
     val C = Array(
    		diff2.x - diff1.x,
    		diff2.y - diff1.y,
    		(b2.getAngle  - b1.getAngle - t2 + t1 + PI)
     )

    // Calculate lambda
	val lambda = Array(0f, 0f, 0f)
     for ( r <- 0 until 3) {
       lambda(r) =  -(effectiveMass(r)(0) * C(0) + effectiveMass(r)(1) * C(1) + effectiveMass(r)(2) * C(2))
     }
	
     
    val P1 = new Vec2(lambda(0), lambda(1))
    Q1.mulToOut(P1, P1)
    
    val P2 = new Vec2(lambda(0), lambda(1))
    Q2.mulToOut(P2, P2);

	// Apply impulse
    b1.m_sweep.c.x -= b1.m_invMass * P1.x
    b1.m_sweep.c.y -= b1.m_invMass * P1.y
    b1.m_sweep.a   -= b1.m_invI * lambda(2)
    
    b2.m_sweep.c.x += b2.m_invMass * P2.x
    b2.m_sweep.c.y += b2.m_invMass * P2.y
    b2.m_sweep.a   += b2.m_invI * lambda(2)

	// Push the changes to the transforms
	b1.synchronizeTransform()
	b2.synchronizeTransform()

	//println("POSITION:::\t\t"  + Math.abs(C(2)))
	// Constraint is satisfied if all constraint equations are nearly zero
	(Math.abs(C(0)) < Settings.linearSlop) && (Math.abs(C(1)) < Settings.linearSlop) && (Math.abs(C(2)) < Settings.angularSlop)

     
  }
  
  


}