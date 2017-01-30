//package lwjgl.examples
//import org.lwjgl.opengl.Display
//import org.lwjgl.opengl.DisplayMode
//import org.lwjgl.input.Keyboard
//import org.lwjgl.input.Mouse
//import org.lwjgl.opengl.GL11._
//import org.lwjgl.Sys
//
//object DisplayExample {
//  
//  var lastFrame : Long = _
//  var fps:Int = 0
//  var lastFPS : Long = _
//  var x = 400f
//  var y = 300f
//  var rotation = 0f
//
//  def main(args: Array[String]): Unit = {
//    start()
//    
//  }
//  
//  def start() {
//    
//    
//    Display.setDisplayMode(new DisplayMode(800, 600))
//    Display.create()
//    
//    // init OpenGL
////	glMatrixMode(GL_PROJECTION);
////	glLoadIdentity();
////	glOrtho(0, 800, 0, 600, 1, -1);
////	glMatrixMode(GL_MODELVIEW);
//    
//    glMatrixMode(GL_PROJECTION);
//	glLoadIdentity();
//	glOrtho(0, 800, 0, 600, 1, -1);
//	glMatrixMode(GL_MODELVIEW);
//    
//	getDelta() 
//	lastFPS = timeNow
//	
//    while( ! Display.isCloseRequested()) {
//       val delta = getDelta()
//       update(delta)
//      renderGL()
//       // Clear the screen and depth buffer
////	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
////		
////	    // set the color of the quad (R,G,B,A)
////	    glColor3f(0.5f,0.5f,1.0f);
////	    	
////	    // draw quad
////	    glBegin(GL_QUADS);
////	        glVertex2f(0,0);
////		glVertex2f(0+700,0);
////		glVertex2f(0+700,0+500);
////		glVertex2f(0,0+500);
////	    glEnd();
//      
//      pollInput()
//      
//      Display.update()
//      Display.sync(60)
//      
//    }
//    Display.destroy()
//  }
//  
//  def pollInput() {
//     if (Mouse.isButtonDown(0)) {
//	    val (x,y) = (Mouse.getX,Mouse.getY) 
//	    println("MOUSE DOWN @ X: " + x + " Y: " + y);
//	}
//		
//	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//	    println("SPACE KEY IS DOWN");
//	}
//		
//	while (Keyboard.next()) {
//	    if (Keyboard.getEventKeyState()) {
//	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
//		    println("A Key Pressed");
//		}
//		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
//		    println("S Key Pressed");
//		}
//		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
//		    println("D Key Pressed");
//		}
//	    } else {
//	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
//	        	println("A Key Released");
//	        }
//	    	if (Keyboard.getEventKey() == Keyboard.KEY_S) {
//	    		println("S Key Released");
//		}
//		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
//		    println("D Key Released");
//		}
//	    }
//	}
//  }
//  
//  def timeNow = Sys.getTime * 1000 / Sys.getTimerResolution
//  
//  def getDelta() = {
//    val time = timeNow
//    val delta = (time-lastFrame).toInt
//    lastFrame = time
//    delta
//  }
//  
//  def updateFPS() {
//    if (timeNow - lastFPS > 1000) {
//      Display.setTitle("FPS: " + fps)
//      fps = 0;
//      lastFPS += 1000;
//    }
//    fps+=1
//  }
//  
//  def renderGL() {
//    // Clear The Screen And The Depth Buffer
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//		// R,G,B,A Set The Color To Blue One Time Only
//		glColor3f(0.5f, 0.5f, 1.0f);
//
//		// draw quad
//		glPushMatrix();
//			glTranslatef(x, y, 0);
//			glRotatef(rotation, 0f, 0f, 1f);
//			glTranslatef(-x, -y, 0);
//			
//			glBegin(GL_QUADS);
//				glVertex2f(x - 50, y - 50);
//				glVertex2f(x + 50, y - 50);
//				glVertex2f(x + 50, y + 50);
//				glVertex2f(x - 50, y + 50);
//			glEnd();
//		glPopMatrix();
//  }
//  
//  def update( delta : Int) {
//		// rotate quad
//		rotation += 0.15f * delta;
//		
//		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
//		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;
//		
//		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
//		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 0.35f * delta;
//		
//		// keep quad on the screen
//		if (x < 0) x = 0;
//		if (x > 800) x = 800;
//		if (y < 0) y = 0;
//		if (y > 600) y = 600;
//		
//		updateFPS(); // update FPS Counter
//	}
//
//}