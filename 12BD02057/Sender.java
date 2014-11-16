import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class Sender {
  String ans="";
  private static final String TASK_QUEUE_NAME = "router";

  public  void send(String task, String num) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    
    Scanner sc=new Scanner(System.in);
   
    String message="";
    
  // QueueingConsumer consumer = new QueueingConsumer(channel);
   //channel.basicConsume("ans", false, consumer);  
   
        while(true){
        	 //QueueingConsumer.Delivery delivery = consumer.nextDelivery();
             //String ans = new String(delivery.getBody());
        message=task+" "+num;
    	if(message.equals("done")) break;
        channel.basicPublish( "", TASK_QUEUE_NAME, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        break;
    }
        channel.queueDeclare("answr", true, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume("answr", false, consumer);  
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        ans = new String(delivery.getBody());
        
        System.out.println(ans);
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    channel.close();
    connection.close();
  }
    
}