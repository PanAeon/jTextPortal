package portal
import javax.swing.UIManager
import javax.swing.JFrame
import java.awt.BorderLayout
import java.awt.Dimension

object TestController {
  
  
  def main(args : Array[String])  {
    UIManager setLookAndFeel "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
    
    
    val panel = new TestPanelJ2D()
    //panel.setSize(960, 540)
    val mainFrame = new JFrame("Boxes test");
    mainFrame.setSize(960, 540)
   // mainFrame.setLayout(new BorderLayout())
    mainFrame.setVisible(true);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    mainFrame.add(panel)
   // mainFrame.pack
    mainFrame.show()
    panel.setPreferredSize(new Dimension(960, 540))
    panel.show()
    panel.setVisible(true)
    
    panel.init
    val physicsWorld = new PhysicsWorld(panel)
    physicsWorld.initDominoTest()
    val x = new X(panel, physicsWorld)
    
   // physicsWorld.world.setDebugDraw(panel.debugDraw)
   
    
    val animator = new Thread(x)
    animator.start();
    
  }

}


class X(val panel:TestPanelJ2D, val test:PhysicsWorld) extends Runnable {
  
  override def run() {
    while(true) {
    panel.render()
    test.step()
  //  panel.debugDraw
    panel.paintScreen()
   // Thread.sleep(50)
   
    }
  }
}