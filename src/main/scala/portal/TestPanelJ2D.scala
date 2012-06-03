package portal
import javax.swing.JPanel
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.Image
import org.jbox2d.callbacks.DebugDraw

class TestPanelJ2D extends JPanel{
    val panelWidth = 960
    val panelHeight = 540
    
	var dbImage : Image = _
    var dbg : Graphics2D = _
    var debugDraw:DebugDrawJ2D = _
    
    def init() {
      dbImage = createImage(panelWidth, panelHeight);
      dbg = dbImage.getGraphics().asInstanceOf[Graphics2D]
      debugDraw = new DebugDrawJ2D(this)
      debugDraw.getViewportTranform().setExtents(panelWidth.toFloat / 2.0f , panelHeight.toFloat / 2.0f)
    //  debugDraw.getViewportTranform().setCenter(panelWidth / 2.0f, panelHeight / 2.0f)
      debugDraw.getViewportTranform().setCamera(0.0f , 5.0f ,10.0f)
      
      var flags = 0;
		//panel.debugDraw
		//flags +=  DebugDraw.e_shapeBit;
		
		//flags += DebugDraw.e_centerOfMassBit;
		flags += DebugDraw.e_shapeBit
		
		debugDraw.setFlags(flags);
    }
    
    
    
   
    
	def getDBGraphics() = dbg;
    

    setBackground(Color.black)

    val dimm = new Dimension(panelWidth, panelHeight)
    setMaximumSize(dimm)
    setMinimumSize(dimm)
    
    def render() {
      dbImage = createImage(panelWidth, panelHeight);
      dbg = dbImage.getGraphics().asInstanceOf[Graphics2D]
      dbg.setColor(Color.black)
      dbg.fillRect(0, 0, panelWidth, panelHeight)
      init()
    }
    
    def paintScreen() {
      this.synchronized {

      val g = this.getGraphics
      g.drawImage(dbImage, 0, 0, null)
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
      }
    }
    
    
    
}