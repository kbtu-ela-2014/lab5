package Server;


import ionic.Msmq.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.TreeMap;
import java.util.StringTokenizer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {
	private static Channel msgCh, resCh;
	private static Integer idCounter;
	private static TreeMap<Integer, String> results; 
	public static void main(String[] args) throws IOException {
		System.out.println("Srever starts");
		idCounter = new Integer(0);
		results = new TreeMap<Integer, String>();
		msgCh = new Channel("msg_queue"); msgCh.open();
		resCh = new Channel("res_queue"); resCh.open();
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8800), 0);
		server.createContext("/menu", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}
	
	static class MyHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String vars = getVars(t);
			if (vars != null) {
				StringTokenizer st = new StringTokenizer(vars, "&=");				 
				st.nextToken(); String op = st.nextToken();
				st.nextToken(); String nums = st.nextToken();
				
				// Sending request
				Integer id = ++idCounter;
				String curId = id.toString();
				
				msgCh.send(new Message(nums, op, curId));
				
				System.out.println("Sended (" + nums + ", " + op + ", " + curId + ")");
				goSleep(1000);
				
				while (true) {
					if (results.containsKey(id)) { // found
						returnResult(t, results.get(id));
						return;
					}
					
					System.out.println("waiting");
					try {
						Message msg = resCh.receive();
						if (msg == null) {
							goSleep(1000);
							continue;
						}
						Integer newId = Integer.parseInt(convertToInt(msg.getCorrelationIdAsString()));
						results.put(newId, msg.getBodyAsString());
					}
					catch (Exception e) { e.printStackTrace(); }
					goSleep(1000);
				}
			}
			returnResult(t, "");
		}		
	}
	
	private static void returnResult(HttpExchange t, String res) throws IOException { 
		String response = readFromFile("menu").replace("#result#", res);
		t.getResponseHeaders().add("Content-type", "text/html; charset=UTF-8");
		t.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private static String convertToInt(String s) {
		StringBuilder res = new StringBuilder();
		for(int i = 0; i < s.length(); i++) 
			if ('0' <= s.charAt(i) && s.charAt(i) <= '9')
				res.append(s.charAt(i));
		return res.toString();
	}
	private static String getVars(HttpExchange t) throws IOException {
		String vars = t.getRequestURI().toString();
		if (vars.length() < 6) return null;
		return vars.substring(6);
	}
	
	private static String readFromFile(String from) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("Lab5/" + from));
		String s;
		StringBuilder res = new StringBuilder();
		while (true) {
			s = br.readLine();
			if (s == null) 
				break;
			res.append(s + "\n");
		}
		br.close();
		return res.toString();
	} 
	static void goSleep(int x) {
		try { Thread.sleep(x); } 
		catch (Exception e) { e.printStackTrace(); }
	}
}
