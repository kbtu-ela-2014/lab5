package Server;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import ionic.Msmq.Queue;
import ionic.Msmq.Message;
import ionic.Msmq.TransactionType;
import ionic.Msmq.MessageQueueException;

public class Channel {

	Queue queue = null;
	String qname;

	String ipAddr = " [ip:?]";

	public Channel(String name) {
		qname = name;
		try {
			java.net.InetAddress thisIp = java.net.InetAddress.getLocalHost();
			ipAddr = " [ip:" + thisIp.getHostAddress() + "]";
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

	}

	public boolean isTrans() {
		return queue.isTransactional();
	}

	public Message receive() throws java.io.UnsupportedEncodingException,
			MessageQueueException {
		Message msg = null;
		try {
			checkOpen();			
			msg = queue.receive(10); // timeout= 2000 ms
			System.out.println("receive");
			System.out.println(" ==> message: " + msg.getBodyAsString());
			System.out.println("     label:   " + msg.getLabel());
			System.out.println("     id:   " + msg.getCorrelationIdAsString());
		} catch (MessageQueueException e) {
			if (e.hresult == -1072824293) 
				return null;
			throw new MessageQueueException(e.hresult);
		}
		return msg;
	}

	public Message peek() throws java.io.UnsupportedEncodingException,
			MessageQueueException {
		Message msg = null;

		try {
			checkOpen();
			msg = queue.peek(); // timeout= 2000 ms
			System.out.println("peek");
			System.out.println(" ==> message: " + msg.getBodyAsString());
			System.out.println("     label:   " + msg.getLabel());
		} catch (MessageQueueException e) {
			return null;
		}
		return msg;

	}

	public void close() {
		try {
			checkOpen();
			System.out.println("close");
			queue.close();
			queue = null;
			System.out.println("close: OK.");
		} catch (MessageQueueException ex1) {
			System.out.println("close failure: " + ex1);
		}
	}

	public void send(Message msg) throws UnsupportedEncodingException {
		try {
			checkOpen();
			queue.send(msg);
			System.out.println("Send successful: " + msg);
		} catch (MessageQueueException ex1) {
			System.out.println("Put failure: " + ex1);
		}
	}

	public void create() {
		try {
			String fullname = ".\\private$\\" + qname;
			System.out.println("create (" + fullname + ")");
			String qLabel = "Created by " + this.getClass().getName() + ".java";
			boolean transactional = false; // should the queue be transactional
			queue = Queue.create(fullname, qLabel, transactional);
			System.out.println("Create: OK.");
		} catch (MessageQueueException ex1) {
			System.out.println("Queue creation failure: " + ex1);
		}
	}

	public void delete() {
		try {
			String fullname = getQueueFullName(".", qname);
			System.out.println("delete (" + fullname + ")");
			ionic.Msmq.Queue.delete(fullname);
			System.out.println("delete: OK.");
		} catch (MessageQueueException ex1) {
			System.out.println("Queue deletion failure: " + ex1);
		}
	}

	public void open() {
		try {
			if (queue != null) {
				queue.close();
				queue = null;
			}
			// String qname= "queue1";
			String hostname = ".";
			String fullname = getQueueFullName(hostname, qname);
			System.out.println("open (" + fullname + ")");
			queue = new Queue(fullname);
			System.out.println("open: OK.");
		} catch (MessageQueueException ex1) {
			System.out.println("Queue open failure: " + ex1);
		}
	}

	public String getQueueFullName(String queueShortName) {
		return getQueueFullName(".", queueShortName);
	}

	private String getQueueFullName(String hostname, String queueShortName) {
		String h1 = hostname;
		String a1 = "OS";
		if ((h1 == null) || h1.equals(""))
			h1 = ".";
		char[] c = h1.toCharArray();
		if ((c[0] >= '1') && (c[0] <= '9'))
			a1 = "TCP";

		return "DIRECT=" + a1 + ":" + h1 + "\\private$\\" + queueShortName;
	}

	
	private void checkOpen() throws MessageQueueException {
		if (queue == null)
			throw new MessageQueueException("open a queue first!\n", -1);
	}
}
