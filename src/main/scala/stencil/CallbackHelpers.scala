package stencil

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



import org.lwjgl.glfw.{
  GLFWCharCallbackI,
  GLFWCharModsCallbackI,
  GLFWCursorEnterCallbackI,
  GLFWCursorPosCallbackI,
  GLFWDropCallbackI,
  GLFWErrorCallbackI,
  GLFWFramebufferSizeCallbackI,
  GLFWKeyCallbackI,
  GLFWMonitorCallbackI,
  GLFWMouseButtonCallbackI,
  GLFWScrollCallbackI,
  GLFWWindowCloseCallbackI,
  GLFWWindowFocusCallbackI,
  GLFWWindowIconifyCallbackI,
  GLFWWindowPosCallbackI,
  GLFWWindowRefreshCallbackI,
  GLFWWindowSizeCallbackI
}

object CallbackHelpers {
  import scala.language.implicitConversions

  /** Called when a Unicode character is input */
  implicit def f2CharCB(f: (Long, Int) => Unit)
  : GLFWCharCallbackI =
    new GLFWCharCallbackI() {
      override def invoke(window: Long, codePoint: Int)
      : Unit = f(window, codePoint)
    }

  /** Called when a Unicode character is input */
  implicit def f2CharModsCB(f: (Long, Int, Int) => Unit)
  : GLFWCharModsCallbackI =
    new GLFWCharModsCallbackI() {
      override def invoke(window: Long, codePoint: Int, mods: Int)
      : Unit = f(window, codePoint, mods)
    }

  /** Called when the cursor enters or leaves the client area of the window */
  implicit def f2CursorEnterCB(f: (Long, Boolean) => Unit)
  : GLFWCursorEnterCallbackI =
    new GLFWCursorEnterCallbackI() {
      override def invoke(window: Long, entered: Boolean)
      : Unit = f(window, entered)
    }

  /** Called when the cursor is moved. */
  implicit def f2CursorPosCB(f: (Long, Double, Double) => Unit)
  : GLFWCursorPosCallbackI =
    new GLFWCursorPosCallbackI() {
      override def invoke(window: Long, xPos: Double, yPos: Double)
      : Unit = f(window, xPos, yPos)
    }

  /** Called when one or more dragged files are dropped on the window. */
  implicit def f2DropCB(f: (Long, Int, Long) => Unit)
  : GLFWDropCallbackI =
    new GLFWDropCallbackI() {
      override def invoke(window: Long, count: Int, names: Long)
      : Unit = f(window, count, names)
    }

  /** Called with an error code and description when a GLFW error occurs. */
  implicit def f2ErrorCB(f: (Int, Long) => Unit)
  : GLFWErrorCallbackI =
    new GLFWErrorCallbackI() {
      override def invoke(error: Int, desc: Long)
      : Unit = f(error, desc)
    }

  /** Called when the framebuffer of the specified window is resized. */
  implicit def f2FramebufferSizeCB(f: (Long, Int, Int) => Unit)
  : GLFWFramebufferSizeCallbackI =
    new GLFWFramebufferSizeCallbackI() {
      override def invoke(window: Long, width: Int, height: Int)
      : Unit = f(window, width, height)
    }

  /** Called when a key is pressed repeated or released. */
  implicit def f2KeyCB(f: (Long, Int, Int, Int, Int) => Unit)
  : GLFWKeyCallbackI =
    new GLFWKeyCallbackI() {
      override def invoke (
        window: Long, key: Int, scanCode: Int, action: Int, mods: Int
      ): Unit = f(window, key, scanCode, action, mods)
    }

  /** Called when a monitor is connected to or disconnected from the system */
  implicit def f2MonitorCB(f: (Long, Int) => Unit)
  : GLFWMonitorCallbackI =
    new GLFWMonitorCallbackI() {
      override def invoke (monitor: Long, event: Int)
      : Unit = f(monitor, event)
    }

  /** Called when a mouse button is pressed or released */
  implicit def f2MouseButtonCB(f: (Long, Int, Int, Int) => Unit)
  : GLFWMouseButtonCallbackI =
    new GLFWMouseButtonCallbackI() {
      override def invoke (window: Long, button: Int, action: Int, mods: Int)
      : Unit = f(window, button, action, mods)
    }

  /** Called when a monitor is connected to or disconnected from the system */
  implicit def f2ScrollCB(f: (Long, Double, Double) => Unit)
  : GLFWScrollCallbackI =
    new GLFWScrollCallbackI() {
      override def invoke (window: Long, xOffset: Double, yOffset: Double)
      : Unit = f(window, xOffset, yOffset)
    }

  /** Called when the user attempts to close the specified window. */
  implicit def f2WindowCloseCB(f: (Long) => Unit)
  : GLFWWindowCloseCallbackI =
    new GLFWWindowCloseCallbackI() {
      override def invoke (window: Long)
      : Unit = f(window)
    }

  /** Called when the window gains/loses focus. */
  implicit def f2WindowFocusCB(f: (Long, Boolean) => Unit)
  : GLFWWindowFocusCallbackI =
    new GLFWWindowFocusCallbackI() {
      override def invoke (window: Long, focused: Boolean)
      : Unit = f(window, focused)
    }

  /** Called when the window is Un/Iconified. */
  implicit def f2WindowIconifyCB(f: (Long, Boolean) => Unit)
  : GLFWWindowIconifyCallbackI =
    new GLFWWindowIconifyCallbackI() {
      override def invoke (window: Long, iconified: Boolean)
      : Unit = f(window, iconified)
    }

  /** Called when the window moves. */
  implicit def f2WindowPosCB(f: (Long, Int, Int) => Unit)
  : GLFWWindowPosCallbackI =
    new GLFWWindowPosCallbackI() {
      override def invoke (window: Long, xPos: Int, yPos: Int)
      : Unit = f(window, xPos, yPos)
    }

  /** Called when the window needs to be redrawn. */
  implicit def f2WindowRefreshCB(f: (Long) => Unit)
  : GLFWWindowRefreshCallbackI =
    new GLFWWindowRefreshCallbackI() {
      override def invoke (window: Long)
      : Unit = f(window)
    }

  /** Called when the window is resized. */
  implicit def f2WindowSizeCB(f: (Long, Int, Int) => Unit)
  : GLFWWindowSizeCallbackI =
    new GLFWWindowSizeCallbackI() {
      override def invoke (window: Long, width: Int, height: Int)
      : Unit = f(window, width, height)
    }
}
