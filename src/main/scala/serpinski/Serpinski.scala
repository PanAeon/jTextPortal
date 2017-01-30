package serpinski

/*******************************************************************************
 * Copyright 2015 Serf Productions, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/**
 * Inspired by the source code found at: "http://www.lwjgl.org/guide".
 */


import org.lwjgl._, glfw._, opengl._
import Callbacks._, GLFW._, GL11._

import org.lwjgl.system.MemoryUtil._
import utils.OpenGLApp

// VM args: -Djava.library.path=/home/vitalii/lab/jTextPortal/lib/linux
object Serpinski extends App with OpenGLApp {
  

  override val Width  = 1200
  override val Height = 800
  
  val FPS = 60

  var rtri = 34f;

  var rquad = 0f;
  
 val pyramid = Pyra(0.0f, 0.0f, 0.0f , 1.0f)
 var nextGen = pyramid::Nil
 var nextInvGen = pyramid::Nil



  
    override def initGL() {

   // glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
    
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
    glClearDepth(1.0); // Depth Buffer Setup
    glEnable(GL_DEPTH_TEST); // Enables Depth Testing
    glEnable(GL_BLEND);
    //glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_COLOR);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      
    glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing To Do

    glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
    glLoadIdentity(); // Reset The Projection Matrix

    // Calculate The Aspect Ratio Of The Window
    gluPerspective(45.0, Width.toDouble / Height.toDouble, 0.1f, 100.0f);
    glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

    // Really Nice Perspective Calculations
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    glShadeModel(GL_SMOOTH); // Enable Smooth Shading
 
       
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


  
    override def render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    

    

   glLoadIdentity(); // Reset The View
   glTranslatef(1.7f, 0.0f, -5.0f);
   glRotatef(rtri, 0.0f, 1.0f, 0.0f)
         
    nextInvGen.foreach(_.drawInverse())
    
    
    
   glLoadIdentity(); // Reset The View
   glTranslatef(-1.7f, 0.0f, -5.0f);
   glRotatef(rtri, 0.0f, 1.0f, 0.0f);
   nextGen.foreach(_.draw())
 

//    rtri += 0.6f; // Increase The Rotation Variable For The Triangle
   // rquad -= 0.15f; // Decrease The Rotation Variable For The Quad 

  }
    
  var lastMouseX = -1d
  var lastMouseY = -1d
  var mousePressed = false

  override def keyHandler (
    window: Long, key: Int, scanCode: Int, action: Int, mods: Int
  ): Unit = {
    if (action == GLFW_RELEASE) {
      key match {
        case GLFW_KEY_1 =>  updateGenerations(1)
        case GLFW_KEY_2 =>  updateGenerations(2)
        case GLFW_KEY_3 =>  updateGenerations(3)
        case GLFW_KEY_4 =>  updateGenerations(4)
        case GLFW_KEY_5 =>  updateGenerations(5)
        case GLFW_KEY_6 =>  updateGenerations(6)
        case GLFW_KEY_7 =>  updateGenerations(7)
        case GLFW_KEY_8 =>  updateGenerations(8)
        case GLFW_KEY_9 =>  updateGenerations(9)
        case GLFW_KEY_0 =>  updateGenerations(10)
        case _ =>
      }
    }
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
      glfwSetWindowShouldClose(window, true)
  }
  
  override def cursorPosHandler(window:Long, x:Double, y:Double) : Unit = {
    
    if (mousePressed) {
        rtri += (x - lastMouseX).toFloat
    }
    lastMouseX = x
    lastMouseY = y
  }
  
  override def mouseButtonHandler(window:Long, button:Int, action:Int, mods:Int): Unit = {
    if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
      mousePressed = true
    } else if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
      mousePressed = false
    }
  }

  updateGenerations(3)
  run()
  

  
 
  
  
  
  
  def updateGenerations(numberOfGenerations:Int) {
    var gen = pyramid::Nil
    var invGen = pyramid::Nil
    for (i <- 1 to numberOfGenerations) {
      gen = nextIteration(gen)
      invGen = nextInverseIteration(invGen)
     
    }
    
    nextGen = gen
    nextInvGen = invGen
  }
  

  
 
  
  def nextIteration(pyramids : List[Pyra]) = pyramids.flatMap(_.nextGen)
  
  def nextInverseIteration(pyramids : List[Pyra]) = pyramids ::: pyramids.flatMap(_.nextGen)
}










case class Pyra(x:Float, y:Float, z:Float, length:Float) {
  def top = Pyra(x,y + length/2,z,length/2)
  def frontLeft = Pyra(x - length/2, y - length/2, z + length/2, length/ 2)
  def frontRight = Pyra(x + length/2, y - length/2, z + length/2, length/ 2)
  def bottomLeft = Pyra(x - length/2, y - length/2, z - length/2, length/ 2)
  def bottomRight = Pyra(x + length/2, y - length/2, z - length/2, length/ 2)
  
  lazy val nextGen = top :: frontLeft :: frontRight :: bottomLeft :: bottomRight :: Nil
  
  
  import org.lwjgl.opengl.GL11._
  
  def drawInverse() {
    val l = length
    
    glPushMatrix();
	glTranslatef(x, y, z);
	
	
	
	//glColor3f(0.1f, 0.4f, 0.9f); 
    glBegin(GL_TRIANGLES); 

    glColor3f(0.1f, 0.4f, 0.9f);  
    glVertex3f(0.0f, -l, 0.0f); 
    glVertex3f(-l/2, 0, l/2); 
    glVertex3f(l/2, 0, l/2);

    glColor3f(0.4f, 0.4f, 0.4f); 
    glVertex3f(0.0f, -l, 0.0f); 
    glVertex3f(l/2, 0, l/2); 
    glVertex3f(l/2, 0, -l/2); 

    glColor3f(0.1f, 0.4f, 0.9f);  
    glVertex3f(0.0f, -l, 0.0f); 
    glVertex3f(l/2, 0, -l/2);
    glVertex3f(-l/2, 0, -l/2); 

    glColor3f(0.4f, 0.4f, 0.4f);
    glVertex3f(0.0f, -l, 0.0f); 
    glVertex3f(-l/2, 0, -l/2); 
    glVertex3f(-l/2, 0, l/2); 
    glEnd(); 
    
    glColor3f(0.1f, 0.4f, 0.9f);  
    glBegin(GL_QUADS);
    	glVertex3f(-l/2, 0, -l/2); 
    	glVertex3f( l/2, 0, -l/2); 
    	glVertex3f( l/2, 0,  l/2); 
    	glVertex3f(-l/2, 0,  l/2); 
    glEnd();
    
    glPopMatrix();
  }
  
   def draw() {
	
	
	val l = length
	
	//glLoadIdentity(); 
	
	//
	glPushMatrix();
	glTranslatef(x, y, z);
	
	
	
	
    glBegin(GL_TRIANGLES); 

    glColor3f(0.9f, 0.4f, 0.1f); 
    glVertex3f(0.0f, l, 0.0f); 
    glVertex3f(-l, -l, l); 
    glVertex3f(l, -l, l);

    glColor3f(0.4f, 0.4f, 0.4f);
    glVertex3f(0.0f, l, 0.0f); 
    glVertex3f(l, -l, l); 
    glVertex3f(l, -l, -l); 

    glColor3f(0.9f, 0.4f, 0.1f); 
    glVertex3f(0.0f, l, 0.0f);
    glVertex3f(l, -l, -l);
    glVertex3f(-l, -l, -l); 

    glColor3f(0.4f, 0.4f, 0.4f);
    glVertex3f(0.0f, l, 0.0f); 
    glVertex3f(-l, -l, -l); 
    glVertex3f(-l, -l, l); 
    glEnd(); 
    glPopMatrix();
  }
}