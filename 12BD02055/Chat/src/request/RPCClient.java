package request;
import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.util.Scanner;
import java.util.UUID;
public class RPCClient {

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	private String replyQueueName;
	private QueueingConsumer consumer;
	
	public RPCClient() throws Exception {
	ConnectionFactory factory = new ConnectionFactory();
	factory.setHost("localhost");
	connection = factory.newConnection();
	channel = connection.createChannel();
	
	replyQueueName = channel.queueDeclare().getQueue(); 
	consumer = new QueueingConsumer(channel);
	channel.basicConsume(replyQueueName, true, consumer);
	}

	public String call(String message, String type) throws Exception {     
	String response = null;
	String corrId = UUID.randomUUID().toString();
	
	BasicProperties props = new BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();
	String str = type+message;
	channel.basicPublish("", requestQueueName, props, str.getBytes());
	
	while (true) {
	  QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	  if (delivery.getProperties().getCorrelationId().equals(corrId)) {
	    response = new String(delivery.getBody(),"UTF-8");
	    break;
	  }
	}
	
	return response; 
	}
	
	public void close() throws Exception {
	connection.close();
	}
	
	public static void main(String[] argv) {
	RPCClient client = null;
	String response = null;
	try {
	  client = new RPCClient();
	
	  Scanner in = new Scanner(System.in);
	  System.out.println("Enter the number: ");  
	  String str = in.nextLine();
	  System.out.println("Choose operation: ");  
	  String str1 = in.nextLine();
	  System.out.println(" [x] Requesting "+str);   
	  response = client.call(str, str1);
	  System.out.println(" [.] Got '" + response + "'");
	}
	catch  (Exception e) {
	  e.printStackTrace();
	}
	finally {
	  if (client!= null) {
	    try {
	    	client.close();
	    }
	    catch (Exception ignore) {}
	  }
	}
	}
}