public class ReformatBuffer {

    public String outputString;
    static int cutoffASCII = 10; // ASCII code of the character used for cut-off between received messages
    static String bufferReadToString = ""; // empty, but not null
    static String data="";
    public static void parseByteArray(byte[] readBuffer) {
        String s = new String(readBuffer);
        System.out.println("GGGGG "+s);

//        
//      bufferReadToString = bufferReadToString.concat(s);
//      System.out.println(bufferReadToString);
//      if((bufferReadToString.indexOf(cutoffASCII) + 1) > 0)
//      {
//      String outputString = bufferReadToString.substring(0, bufferReadToString.indexOf(cutoffASCII) + 1);
//      bufferReadToString = bufferReadToString.substring(bufferReadToString.indexOf(cutoffASCII) + 1); // adjust as needed to accommodate the CRLF convention ("\n\r"), ASCII 10 & 13
//      Main m=new Main();
//      System.out.print("FFFF : "+outputString);
//      m.recieved_message(outputString);
//      }
        
        if(s.contains("*")){ //\\*
        	System.out.println("inside if mayura 1 " + s);
        	
            //Main m=new Main();
           // m.recieved_message(bufferReadToString); //output variable
            bufferReadToString="";
        }
        else
        {
        	 System.out.println("elseelse "+s);
           bufferReadToString = bufferReadToString.concat(s);
            //System.out.println("Svakruth11 = "+bufferReadToString);
        }   
    }
    public void set_msg_to_null() {
    	bufferReadToString=null;   	
    }
}
    
   