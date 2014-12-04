package commandmessage;
import java.io.IOException;

import com.rabbitmq.client.*;

public class MessageReceiver{

	private static final String TASK_QUEUE_NAME = "task_queue";

	  public static void main(String[] argv) throws Exception {

	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    
	    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
	    System.out.println(" [*] Waiting for messages");
	    
	    channel.basicQos(1);
	    
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
	    
	    while (true) {
	      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	      String message = new String(delivery.getBody());
	      
	      System.out.println(" [x] Received '" + message + "'");
	      String str = doWork(message);
	      System.out.println("");
	      System.out.println(str);

	      System.out.println(" [x] Done");

	      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	    }         
	  }
	  
	  private static String doWork(String task) throws InterruptedException {
		  Integer n = Integer.parseInt(task);
		  Integer ans=1;
	    for (int i=0; i<5; i++) {
	    	System.out.print("..");
	    	ans*=n;
	       Thread.sleep(1000);
	       
	    }
	    return ans.toString();
	  }

}
