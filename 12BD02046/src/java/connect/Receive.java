package connect;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
public class Receive {
     private final static String MY_QUEUE_NAME = "mainqueue";

  public static void main(String[] argv)
      throws java.io.IOException,
             java.lang.InterruptedException,
             URISyntaxException,
             NoSuchAlgorithmException,
             KeyManagementException {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    //factory.setUri(ConnectUri.uri);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(MY_QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    channel.basicQos(1);
    String queueName="";
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(MY_QUEUE_NAME, false, consumer);
  
  while (true) {
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    String message = new String(delivery.getBody());
    System.out.println(" [x] Received '" + message + "'");
    StringTokenizer token=new StringTokenizer(message, " ");
    String work="";
    String number="";
    int c = 0;
     while(token.hasMoreTokens()){
   	  if(c == 0){
            work=token.nextToken();
            c++;
          }
          else{
            number=token.nextToken();
          }
     }
    if(work.equals("even")){
    	 queueName="evennum";
    }
   else if(work.equals("prime")){
        queueName = "primenumber";
   }
    else if(work.equals("factor")){
        queueName = "factornumber";
   }
     channel.queueDeclare("theResult", true, false, false, null);
     channel.basicPublish( "", queueName, 
              MessageProperties.PERSISTENT_TEXT_PLAIN,
              number.getBytes());
     channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
     }
  }
}


