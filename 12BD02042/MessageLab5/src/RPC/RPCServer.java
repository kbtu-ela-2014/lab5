package RPC;

import java.math.BigInteger;
import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
  
public class RPCServer {
  
  private static final String RPC_QUEUE_NAME = "rpc_queue";
  
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
  
  
    
  public static void main(String[] argv) {
    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
  
      connection = factory.newConnection();
      channel = connection.createChannel();
      
      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
  
      channel.basicQos(1);
  
      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
  
      System.out.println(" [x] Awaiting RPC requests");
      System.out.println(1);
      while (true) {
        String response = null;
        
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        
        BasicProperties props = delivery.getProperties();
        BasicProperties replyProps = new BasicProperties
                                         .Builder()
                                         .correlationId(props.getCorrelationId())
                                         .build();
        
        try {
        	
          String message = new String(delivery.getBody(),"UTF-8");
          System.out.println(message);
          if(message.charAt(0)=='F'){
              response = new String(fac(message.substring(1)).toString()); 
          }else 
        	  if(message.charAt(0)=='I'){
        		  if(isPrime(message.substring(1))==true){
        			  response = "prime";
        		  }else response = "not prime";
                  System.out.println(response);
        	  }else{
        		  response = new String(exp	(message.substring(1)).toString());
                  System.out.println(response);
        	  }          
        }
        catch (Exception e){
          System.out.println(" [.] " + e.toString());
          response = "";
        }
        finally {  
          channel.basicPublish( "", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
  
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
      }
    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (Exception ignore) {}
      }
    }      		      
  }
}