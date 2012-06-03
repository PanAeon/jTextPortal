package portal

import scala.swing.SimpleGUIApplication
import javax.swing.JComponent
import java.awt.Dimension


object MainApp {
  val screen = new scala.swing.Component{
    override lazy val peer = new PortalGUI
    preferredSize = new Dimension(960, 540)
    
    def updateArena = {
      peer.arenaModified = true
      peer.repaint(100)
    }

  }
}


object FieldsGUIApplication extends SimpleGUIApplication {
  import scala.swing.{MainFrame,MenuBar, Menu, MenuItem, Action,Dialog,BoxPanel,Orientation}

  def top = {
    val frame = new MainFrame {
 
    title = "Portal"
//    menuBar = new MenuBar {
//      contents += (new Menu("Game Menu") {
//        contents += (new MenuItem (Action("Clear") {
//        }))
//      })
//    }
    
    contents = new BoxPanel(Orientation.Horizontal) {
      contents += MainApp.screen
    }
    
     }
     MainApp.screen.requestFocus()
    frame
  }
}