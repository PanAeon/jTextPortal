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

// VM args: -Djava.library.path=/home/vitalii/lab/jTextPortal/lib/linux
object Serpinski extends App {
  import stencil.CallbackHelpers._

  private val WIDTH  = 1200
  private val HEIGHT = 800
  
  val FPS = 60


  var rtri = 34f;

  var rquad = 0f;
  
 val pyramid = Pyra(0.0f, 0.0f, 0.0f , 1.0f)
  var nextGen = pyramid::Nil
  var nextInvGen = pyramid::Nil

  def run() {
    try {
      GLFWErrorCallback.createPrint(System.err).set()

      val window = init()
      loop(window)

      glfwFreeCallbacks(window)
      glfwDestroyWindow(window)
    } finally {
      glfwTerminate() // destroys all remaining windows, cursors, etc...
      glfwSetErrorCallback(null).free()
    }
  }

  private def init(): Long = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE,   GLFW_FALSE) // hiding the window
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE) // window resizing not allowed

    val window = glfwCreateWindow(WIDTH, HEIGHT, "LWJGL in Scala", NULL, NULL)
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window")

    glfwSetKeyCallback(window, keyHandler _)

    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

    glfwSetWindowPos (
      window,
      (vidMode. width() -  WIDTH) / 2,
      (vidMode.height() - HEIGHT) / 2
    )

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    window
  }
  
    def initGL() {

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
    gluPerspective(45.0, WIDTH.toDouble / HEIGHT.toDouble, 0.1f, 100.0f);
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

  private def loop(window: Long) {
    GL.createCapabilities()

    updateGenerations(5)
    glClearColor(0f, 0f, 0f, 0f)
    initGL()
    // init

    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      render()
      //main loop
      glfwSwapBuffers(window)
      glfwPollEvents()

    }
  }
  
    def render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    

    

   glLoadIdentity(); // Reset The View
   glTranslatef(1.7f, 0.0f, -5.0f);
   glRotatef(rtri, 0.0f, 1.0f, 0.0f)
         
    nextInvGen.foreach(_.drawInverse())
    
    
    
   glLoadIdentity(); // Reset The View
   glTranslatef(-1.7f, 0.0f, -5.0f);
   glRotatef(rtri, 0.0f, 1.0f, 0.0f);
   nextGen.foreach(_.draw())
 

    rtri += 0.6f; // Increase The Rotation Variable For The Triangle
   // rquad -= 0.15f; // Decrease The Rotation Variable For The Quad 

  }
    
  var lastMouseX = -1
  var lastMouseY = -1
  var mousePressed = false

  private def keyHandler (
    window: Long, key: Int, scanCode: Int, action: Int, mods: Int
  ): Unit = {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
      glfwSetWindowShouldClose(window, true)
  }

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



//package serpinski
//
//import portal.util.TimeNow
//import org.lwjgl.opengl.Display
//import org.lwjgl.opengl.DisplayMode
//import scala.annotation.tailrec
//import org.lwjgl.input.Mouse
//import org.lwjgl.input.Keyboard
//import java.nio.FloatBuffer
//import org.lwjgl.BufferUtils
//
//object Serpinski extends TimeNow {
//
//  import org.lwjgl.opengl.GL11._

//
//  def main(args: Array[String]): Unit = {
//    Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT))
//    Display.create()
//
//    initGL()
//
//    mainloop
//  }
//

//  
//

//  
//
//
//  @tailrec
//  def mainloop {
//
//    if (!Display.isCloseRequested) {
//      updateFPS()
//
//      render()
//
//      pollInput();
//
//      Display.update()
//      Display.sync(FPS)
//
//      mainloop
//    }
//  }
//

//
//  

//  
//  def pollInput() {
//	 
//    if (lastMouseX == Mouse.getX && lastMouseY == Mouse.getY && Mouse.isButtonDown(0)) {
//      // ignore
//    } else if (Mouse.isButtonDown(0)){
//      //civilization.flip(Mouse.getX / SIZE_OF_CELL, Mouse.getY / SIZE_OF_CELL)
//      rtri += Mouse.getX - lastMouseX
//      lastMouseX = Mouse.getX
//      lastMouseY = Mouse.getY
//    }
//    
//    if (lastMouseX != -1 && !Mouse.isButtonDown(0)) {
//      lastMouseX = -1
//      lastMouseY = -1
//      
//    }
//    
//    if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
//      updateGenerations(0)
//    } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
//      updateGenerations(1)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
//      updateGenerations(2)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
//      updateGenerations(3)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
//      updateGenerations(4)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
//      updateGenerations(5)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
//      updateGenerations(6)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
//      updateGenerations(7)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_9)) {
//      updateGenerations(8)
//    }else if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
//      updateGenerations(9)
//    }
//    
//  }
//
//}



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