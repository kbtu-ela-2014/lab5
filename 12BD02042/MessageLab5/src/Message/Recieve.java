package Message;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class Recieve {
	
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args)  throws java.io.IOException, java.lang.InterruptedException{
		// TODO Auto-generated method stub
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	    while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        String message = new String(delivery.getBody());
	        
	        if(message.charAt(0)=='F'){
	        	
	        	BigInteger ans = BigInteger.ONE;
	            BigInteger cnt = BigInteger.ONE;
	            BigInteger number = new BigInteger(message.substring(1));
	            number = number.add(BigInteger.ONE);
	            while(true){
	                ans = ans.multiply(cnt);
	                cnt = cnt.add(BigInteger.ONE);
	                if(cnt.equals(number)) break;
	            }
	            System.out.println("Faktorial is: " + ans);
	        }else
	        	if(message.charAt(0)=='I'){
	        		BigInteger number = new BigInteger(message.substring(1));
	        		if(number.isProbablePrime(100)==true){
	    	            System.out.println("The number is prime"); 
	        		}else
	    	            	System.out.println("The number is not prime");
	        		}
	        
	        	else{
	        		StringTokenizer st = new StringTokenizer(message.substring(1),".");
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
	        		 
		            System.out.println("Answer is: " + ans);
	 	        } 
	    
	    }
	        
	      }
	}


