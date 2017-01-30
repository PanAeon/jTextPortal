package utils

import org.lwjgl._, glfw._, opengl._
import Callbacks._, GLFW._, GL11._

import org.lwjgl.system.MemoryUtil._

trait OpenGLApp {
  import CallbackHelpers._
  
  def Width: Int
  def Height: Int

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

    val window = glfwCreateWindow(Width, Height, "LWJGL in Scala", NULL, NULL)
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window")

    glfwSetKeyCallback(window, keyHandler _)
    glfwSetCursorPosCallback(window, cursorPosHandler _)
    glfwSetMouseButtonCallback(window, mouseButtonHandler _)

    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

    glfwSetWindowPos (
      window,
      (vidMode. width() -  Width) / 2,
      (vidMode.height() - Height) / 2
    )

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    window
  }

  private def loop(window: Long) {
    GL.createCapabilities()

    glClearColor(0f, 0f, 0f, 0f)
    // init
    initGL()

    while (!glfwWindowShouldClose(window)) {
      // update fps ... updateFPS()
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      //main loop
      render()
      glfwSwapBuffers(window)
      glfwPollEvents()

    }
  }
  
  type KeyHandler = Function5[Long, Int, Int, Int, Int, Unit]
  
  def keyHandler (window: Long, key: Int, scanCode: Int, action: Int, mods: Int): Unit = {
    // do nothing
  }
  
  // fantastic ...
  def cursorPosHandler(window:Long, x:Double, y:Double) : Unit = {
    
  }
  
  def mouseButtonHandler(window:Long, button:Int, action:Int, mods:Int): Unit = {
    
  }
  
  def initGL():Unit = {}
  
  def render(): Unit
  

//  private def keyHandler (
//    window: Long, key: Int, scanCode: Int, action: Int, mods: Int
//  ): Unit = {
//    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
//      glfwSetWindowShouldClose(window, true)
//  }
}