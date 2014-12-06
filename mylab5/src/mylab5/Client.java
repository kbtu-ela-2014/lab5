package mylab5;

import java.util.Scanner;
import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class Client {
  
  private static final String TASK_QUEUE_NAME = "task_queue2", ANS_QUEUE_NAME = "ans2";
  
  public static String run(String message) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    
    
    
    channel.basicPublish( "", TASK_QUEUE_NAME, 
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");
    
    
    Connection connection2 = factory.newConnection();
    Channel channel2 = connection.createChannel();
    
    channel2.queueDeclare(ANS_QUEUE_NAME, true, false, false, null);
    
    channel2.basicQos(1);
    
    QueueingConsumer consumer = new QueueingConsumer(channel2);
    channel2.basicConsume(ANS_QUEUE_NAME, false, consumer);
    String res, token1, token2;
	QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	res = new String(delivery.getBody());
	
    
    channel.close();
    channel2.close();
    connection.close();
    System.out.println(res);
    
	return res;
  }
    

}