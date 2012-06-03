package org.jbox2d.dynamics.joints
import org.jbox2d.dynamics.Body
import org.jbox2d.common.Vec2

class PortalJointDef extends JointDef {
  
  this.`type` = JointType.PORTAL
  collideConnected = false
  var ground1 = new Vec2(0f, 0f)
  var ground2 = new Vec2(0f, 0f)
  var ground1Angle = 0f;
  var ground2Angle = 0f;
  
  
  def initialize(b1 : Body, b2 : Body, ground1 : Vec2, groundAngle1 : Float, ground2: Vec2, groundAngle2 : Float) {
    bodyA = b1
    bodyB = b2
    this.ground1 = ground1
    this.ground2 = ground2
    this.ground1Angle = groundAngle1
    this.ground2Angle = groundAngle2
  }

}