import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
  
public class Worker {

  private static final String TASK_QUEUE_NAME = "isprime";

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
      
      System.out.println(" [x] Received '" + message + "'");
      //Integer num=new Integer(message);
      boolean res=isPrime(new Integer(message));
      System.out.println(" [x] Done");
    String answer="ans "+res;
     System.out.println(answer);
    channel.basicPublish( "", "router", 
              MessageProperties.PERSISTENT_TEXT_PLAIN,
              answer.getBytes());
             
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }         
  }
  
 
	 static boolean isPrime(Integer num){
		  boolean prime=true;
		  for(int i=2; i*i<=num; i++){
			  System.out.println("dadsa" + num+"a s"+i);
			  if(num%i==0){
				  prime=false;
				  break;
			  }
		  }
		  return prime;
	  
  }
}