import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
  
public class Router {

  private static final String TASK_QUEUE_NAME = "router";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    channel.basicQos(1);
    String queue="";
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println(" [x] Received '" + message + "'");
      //
      StringTokenizer st=new StringTokenizer(message, " ");
      String task="";
      String number="";
      int cnt=0;
      while(st.hasMoreTokens()){
    	  if(cnt==0){
    		  task=st.nextToken();
    		  cnt++;
    	  }else{
    		  number=st.nextToken();
    	  }
      }
      if(task.equals("isprime")){
    	 queue="isprime";
      }else if(task.equals("exp")){
    	 queue="exp";
      }else if(task.equals("ans")){
    	  queue="answr";
      }
      //asd
      System.out.println(number+"num");
      channel.queueDeclare("answr", true, false, false, null);
      System.out.println("task");
      channel.basicPublish( "", queue, 
              MessageProperties.PERSISTENT_TEXT_PLAIN,
              number.getBytes());
      System.out.println(" [x] Sent '" + number + "'");
      //
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }         
  }
  
  private static void doWork(String message) throws InterruptedException {
    
	  
  }
}