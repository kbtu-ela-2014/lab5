package mylab5;

import java.math.BigInteger;
import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
  




public class Worker {
	  
  private static final String TASK_QUEUE_NAME = "task_queue2", ANS_QUEUE_NAME = "ans2";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    Connection connection2 = factory.newConnection();
    Channel channel2 = connection.createChannel();
    channel2.queueDeclare(ANS_QUEUE_NAME, true, false, false, null);    
    
    
    
    
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    channel.basicQos(1);
    
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println(" [x] Received '" + message + "'");
      String ans = doWork(message);
      System.out.println(" [x] Answer is: " + ans);
      ans = ans + " " + message;
      channel2.basicPublish( "", ANS_QUEUE_NAME, 
              MessageProperties.PERSISTENT_TEXT_PLAIN,
              ans.getBytes());

      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      channel2.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }         
  }
  
  private static String doWork(String message) throws InterruptedException {
	  BigInteger number = new BigInteger(message);
		if(number.isProbablePrime(100)==true){
			return "prime";
		}else return "no prime";
		 
	
  }
}