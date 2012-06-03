package portal.util
import org.lwjgl.Sys
import org.lwjgl.opengl.Display

trait TimeNow {
  
//	var lastFrame : Long = _
	var fps:Int = 0
    var lastFPS : Long = timeNow
    
    
	def timeNow = Sys.getTime * 1000 / Sys.getTimerResolution
	
//	def getDelta() = {
//    	val time = timeNow
//    	val delta = (time-lastFrame).toInt
//    	lastFrame = time
//    	delta
//  	}
  
  def updateFPS() = {
    if (timeNow - lastFPS > 1000) {
      Display.setTitle("FPS: " + fps)
      fps = 0;
      lastFPS += 1000;
    }
    fps+=1
  }
}