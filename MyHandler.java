package rab;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Vector;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyHandler implements HttpHandler {
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		{
			HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);
			server.createContext("/", new MyHandler());
			server.setExecutor(null); // creates a default executor
			server.start();
		}
	}

	
		
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
    	String path = uri.toString();
        String response = new String();
        

        String templ = Read.readFromFile("fas.txt");
        
        
        if(path.equals("/calc")){
        	 Vector<String> data = Read.readUserInput(t);
        	 try {
				response = templ.replace("%content%",NewTask1.getMessage(data.get(1)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }     	  
	    
        else if(path.equals("/")){
        	response=templ.replace("%content%","Main Page ");
        }
                      
                  
		Headers header = t.getResponseHeaders();	//adding header
		header.add("Content-Type", "text/html; charset=ANSI");
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
}