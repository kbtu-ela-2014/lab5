package rab;




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sun.net.httpserver.HttpExchange;

public class Read {
	  public static String readFromFile(String fileName) throws IOException{
		  	String response = null;
		  	BufferedReader br = new BufferedReader(new FileReader(fileName));
		  	String line = br.readLine();
		  		    	while (line!=null){
		  		    		response+=line+"\n";
		  		    		line=br.readLine();
		  		    	}
		  		    	br.close();
		  		    	return response;
			}
	  public static Vector<String> readUserInput(HttpExchange t) throws IOException {
			 InputStream is = t.getRequestBody();
			 BufferedReader br = new BufferedReader(new InputStreamReader(is));
			 
			 Vector<String> data = new Vector<>();
			 String s;
			 s = br.readLine();
			 
			 s = URLDecoder.decode(s);
			// System.out.println(s);
			 StringTokenizer st = new StringTokenizer(s, "=&");
		    while (st.hasMoreTokens()) {
		   	 data.add(st.nextToken());
		    }
		    return data;
			}

public static int convertToInt(String s){
  char a[];
  int result = 0;
  int h=1;
  a = s.toCharArray();
  int n = s.length();
  for(int i=n-1; i>=0; i--){
	   result += (a[i]-48)*h;
	   h*=10;
  }
  return result;
}




}
