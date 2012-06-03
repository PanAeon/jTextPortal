package org.jbox2d.dynamics.joints

import org.jbox2d.dynamics.World

object MyPortalJointFactory extends PortalJointFactory {

  // this is a bit fucked-up
  //Joint.portalJointFactory = this
  
  def createPortalJoint(world: World, jointDef: JointDef): Joint = {
    val jointDefinition = jointDef.asInstanceOf[PortalJointDef]
    new PortalJoint(world.getPool, jointDefinition)
  }

}