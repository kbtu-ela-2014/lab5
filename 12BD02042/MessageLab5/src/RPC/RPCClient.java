package RPC;

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
  
  public String call(String message) throws Exception {     
    String response = null;
    String corrId = UUID.randomUUID().toString();
    
    BasicProperties props = new BasicProperties
                                .Builder()
                                .correlationId(corrId)
                                .replyTo(replyQueueName)
                                .build();
    
    channel.basicPublish("", requestQueueName, props, message.getBytes());
    
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
    
	  Scanner in = new Scanner(System.in);
	  System.out.println("Enter a number:");
	    String number = in.next();
	    
	    System.out.println("Choose action:");
	    System.out.println("1)Factorization;");
	    System.out.println("2)IsPrime;");
	    System.out.println("3)Exponentiation;");
	    String action;
	    int choice = in.nextInt();
	    if(choice==1){
	    		action = "F";	    	
	    }else
	    	if(choice==2){
	    		action = "I";
	    	}
	    	else{
	    		action = "E";
	    		String pow = in.next();
	    		action = action+pow+'.';
	    	}
	  
	   String message = action + number;
	      
	  RPCClient actionRPC = null;
	  
    try {
      actionRPC = new RPCClient(); 
      System.out.println(" [x] Requesting");   
      String response = actionRPC.call(message);
      System.out.println(" [.] Result: '" + response + "'");
    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (actionRPC!= null) {
        try {
          actionRPC.close();
        }
        catch (Exception ignore) {}
      }
    }
  }
}
