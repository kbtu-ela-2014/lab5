package WorkQueues;

import java.math.BigInteger;
import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
  




public class Worker {

	
	private static BigInteger fac(String n){
		  BigInteger ans = BigInteger.ONE;
	      BigInteger cnt = BigInteger.ONE;
	      BigInteger number = new BigInteger(n);
	      number = number.add(BigInteger.ONE);
	      while(true){
	          ans = ans.multiply(cnt);
	          cnt = cnt.add(BigInteger.ONE);
	          if(cnt.equals(number)) break;
	      }
	      return ans;
	  }
	  
	  private static boolean isPrime(String n){
		  boolean ans;
		  BigInteger number = new BigInteger(n);
			if(number.isProbablePrime(100)==true){
				ans = true;
			}else
					ans = false;
			return ans;
	  }
	  
	  private static BigInteger exp(String n){
		  StringTokenizer st = new StringTokenizer(n,".");
			BigInteger pow = new BigInteger(st.nextToken());
			BigInteger number = new BigInteger(st.nextToken());
			BigInteger ans = BigInteger.ONE;
	      BigInteger cnt = BigInteger.ONE;
	      
	      pow = pow.add(BigInteger.ONE);
	       
	      
	      while(true){
	           ans = ans.multiply(number);
	           cnt = cnt.add(BigInteger.ONE);
	           if(cnt.equals(pow)) break;
	       }
	      return ans;
	  }
	  
  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    channel.basicQos(1);
    
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println(" [x] Received '" + message.substring(1) + "'");
      System.out.println(" [x] Answer is: " + doWork(message));

      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }         
  }
  
  private static String doWork(String message) throws InterruptedException {
	  String response;
	  if(message.charAt(0)=='F'){
          response = new String(fac(message.substring(1)).toString()); 
      }else 
    	  if(message.charAt(0)=='I'){
    		  if(isPrime(message.substring(1))==true){
    			  response = "prime";
    		  }else response = "not prime";
    	  }else{
    		  response = new String(exp	(message.substring(1)).toString());
    	  }
	  
	  return response;
  }
}