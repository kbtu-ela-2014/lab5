package Server;
import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class Worker {
	public static Channel msgCh;
	public static Channel resCh;
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Worker working");
		msgCh = new Channel("msg_queue"); msgCh.open();
		resCh = new Channel("res_queue"); resCh.open();
		while (true) {
			Message msg = null;
			try { 
				msg = msgCh.receive();
				if (msg == null) {
					Thread.sleep(200);
					continue;
				}
				resCh.send(new Message(process(msg.getBodyAsString(), Integer.parseInt(msg.getLabel())), "", msg.getCorrelationId()));
				System.out.println("body = " + msg.getBodyAsString());
				System.out.println("label = " + msg.getLabel());
				System.out.println("id = " + msg.getCorrelationIdAsString());
			} 
			catch (UnsupportedEncodingException | MessageQueueException e) { e.printStackTrace(); }
			Thread.sleep(200);
		}		
	}
	
	private static String process(String nums, int type) throws NumberFormatException, UnsupportedEncodingException {
		if (type == 1) return factorization(nums);
		if (type == 2) return primeTest(nums);
		return exponentation(nums);
	}

	private static String factorization(String s) {
		Long x = new Long(s);
		boolean first = true;
		StringBuilder res = new StringBuilder();
		for (Long i = 2l; i * i <= x; i++) {
			if (x % i == 0) {
				int pow = 0;
				while (x % i == 0) {
					x /= i; 
					pow++;
				}
				if (!first) res.append(" * ");
				res.append(i);
				if (pow > 1) res.append("^" + pow);					
				first = false;
			}
		}
		if (x > 1) {
			if (!first) res.append(" * ");   
			res.append(x);
		}
		return res.toString();
	}
	private static String primeTest(String s) {
		Long x = new Long(s);
		for (Long i = 2l; i * i <= x; i++) {
			if (x % i == 0) return "NO";
		}
		return "YES";
	}
	private static String exponentation(String s) {
		StringTokenizer st = new StringTokenizer(s, "+ ");
		BigInteger a = new BigInteger(st.nextToken());
		Integer b = new Integer(st.nextToken());
		BigInteger res = new BigInteger("1");
		while (b-- != 0) {
			res = res.multiply(a);
		}
		return res.toString();			
	}
}
