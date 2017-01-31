package life

import scala.annotation.tailrec

import utils.OpenGLApp
import org.lwjgl.glfw.GLFW

object UI extends OpenGLApp {
  
  import org.lwjgl.opengl.GL11._
  
  val FPS = 60
  val SIZE_OF_CELL = 10
  val GRID_SIZE = 50
  val SIZE_X = SIZE_OF_CELL * GRID_SIZE
  val SIZE_Y = SIZE_OF_CELL * GRID_SIZE
  override val Width = SIZE_X
  override val Height = SIZE_Y
  var GenenerationPeriod = 1 // 0-59 .. 0 means 60fps, 59 means 1fps
  val TOTAL_SIZE = 100
  var viewPosX = 11.0
  var viewPosY = 11.0
  
  var civilization = GameOfLife.newLife(TOTAL_SIZE,  (10,10) :: (10,9) :: (10,8) :: (9,8) :: (8,9):: Nil)
  
  def main(args: Array[String]): Unit = {
   
  
   run()
  }

  override def initGL() {
    glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, SIZE_X, 0, SIZE_Y, 1, -1);
	glMatrixMode(GL_MODELVIEW);
	glShadeModel(GL_SMOOTH);
	glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);   
	glEnable(GL_BLEND);                         
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
  }
  

    
  var frame = 0
  
  override def render() {
      
	  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	  
	  glColor3f(0.8f,0.8f,0.8f);
	  glPushMatrix();
	  glTranslated(-viewPosX, -viewPosY, 1.0);
	  civilization.foreachWithCoords { (x, y, alive) =>
	    if (alive) {
	      if ( x * SIZE_OF_CELL >= viewPosX && x < viewPosX + GRID_SIZE * SIZE_OF_CELL && 
	           y * SIZE_OF_CELL >= viewPosY && y < viewPosY + GRID_SIZE * SIZE_OF_CELL) {
	        
	        
	    	glBegin(GL_QUADS);
	  			glVertex2f(x * SIZE_OF_CELL, y * SIZE_OF_CELL);
	  			glVertex2f(x * SIZE_OF_CELL, y * SIZE_OF_CELL + SIZE_OF_CELL);
	  			glVertex2f(x * SIZE_OF_CELL + SIZE_OF_CELL,y * SIZE_OF_CELL +  SIZE_OF_CELL);
	  			glVertex2f(x * SIZE_OF_CELL + SIZE_OF_CELL,y * SIZE_OF_CELL);
	  		glEnd();
	  	    
	      }
	    }
	  }
	  
	 
	  glPopMatrix();
	  
	   glPushMatrix();
	   glTranslated(-(viewPosX % SIZE_OF_CELL), -(viewPosY % SIZE_OF_CELL), 1.0);
	  // draw grid
	  glColor3f(0.2f,0.2f,0.2f);
	  for ( x <- List.range(0, SIZE_X, SIZE_OF_CELL)) {
	    glBegin(GL_LINES);
	    glVertex2f(x, 0);
	    glVertex2f(x, SIZE_Y);
	    glEnd();
	  }
	  
	  for ( y <- List.range(0, SIZE_Y, SIZE_OF_CELL)) {
	    glBegin(GL_LINES);
	    glVertex2f(0, y);
	    glVertex2f(SIZE_X, y);
	    glEnd();
	  }
	  
	   glPopMatrix();
	   
	   // draw boundary
	  glPushMatrix();    
	   glTranslated(-viewPosX, -viewPosY, 1.0);
	    glColor3f(0.9f, 0.1f, 0.1f);
	   glBegin(GL_LINE_STRIP);
	    glVertex2f(0, 0);
	    glVertex2f(0, TOTAL_SIZE * SIZE_OF_CELL);
	    glVertex2f(TOTAL_SIZE * SIZE_OF_CELL, TOTAL_SIZE * SIZE_OF_CELL);
	    glVertex2f(TOTAL_SIZE * SIZE_OF_CELL, 0);
	    glVertex2f(0, 0);
	    glEnd();
	  glPopMatrix()
	   
	  
	  if(gameStarted) {
    	    frame += 1
    	    if (frame > GenenerationPeriod) {
    	    	frame = 0
    	    	civilization = GameOfLife.tide(civilization)
    	    }
    	}
	  
  }
  
  var gameStarted = false
  
  var lastMouseX = -1d
  var lastMouseY = -1d
  var spacePressed = false
  var lastPMouseX = -1d 
  var lastPMouseY = -1d
  
  
    override def keyHandler (
    window: Long, key: Int, scanCode: Int, action: Int, mods: Int
  ): Unit = {
    if (action == GLFW.GLFW_RELEASE) {
      key match {
        case GLFW.GLFW_KEY_SPACE =>  
          gameStarted = !gameStarted
        case _ =>
      }
    }
    if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
      GLFW.glfwSetWindowShouldClose(window, true)
  }
  
  override def cursorPosHandler(window:Long, x:Double, y:Double) : Unit = {
    

    lastMouseX = x
    lastMouseY = y
  }
  
  override def mouseButtonHandler(window:Long, button:Int, action:Int, mods:Int): Unit = {
    val arr1 = new Array[Double](2)
    val arr2 = new Array[Double](2)
    GLFW.glfwGetCursorPos(window, arr1, arr2)
    
     if (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_RELEASE) {
      val x = ((arr1(0) + viewPosX) / SIZE_OF_CELL).toInt
      val y = ((Height - arr2(0) + viewPosY ) / SIZE_OF_CELL).toInt
      civilization.flip(x, y)
    }
  }
  
  
//  def pollInput() {
//    

//    
//    // panning
//    if (lastPMouseX == Mouse.getX && lastPMouseX == Mouse.getY && Mouse.isButtonDown(2)) {
//      // ignore
//    } else if (Mouse.isButtonDown(2)) {
//      if (lastPMouseX != -1) {
//    	  viewPosX -= (Mouse.getX - lastPMouseX) 
//    	  viewPosY -= (Mouse.getY - lastPMouseY)
//      }
//      lastPMouseX = Mouse.getX
//      lastPMouseY = Mouse.getY
//    }
//    
//     if (lastPMouseX != -1 && !Mouse.isButtonDown(2)) {
//      lastPMouseX = -1
//      lastPMouseY = -1
//      
//    }
//    
//    
//    
//    
//    if (!spacePressed && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//    	gameStarted = !gameStarted
//    	spacePressed = true
//    }
//    
//     if (spacePressed && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//    	spacePressed = false
//    }
//    
//     if (Keyboard.isKeyDown(Keyboard.KEY_2) && GenenerationPeriod > 0) {
//    	GenenerationPeriod -= 1
//    }
//     
//     if (Keyboard.isKeyDown(Keyboard.KEY_1) && GenenerationPeriod < 59) {
//    	GenenerationPeriod += 1
//    }
//    
//  }

}