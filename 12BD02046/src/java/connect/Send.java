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
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Send {
    private final static String MY_QUEUE_NAME = "mainqueue";
    public String result = "";
    public void sendMessage(String work, String number) throws java.io.IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException, InterruptedException {
     ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //Socket sock = new Socket("127.0.0.1" , 4000); 
     //factory.setUri(ConnectUri.uri);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(MY_QUEUE_NAME, false, false, false, null);
    //String message = "Hello World!";
    //channel.basicPublish("", MY_QUEUE_NAME, null, message.getBytes());
    //System.out.println(" [x] Sent '" + message + "'");
    while (true) {
        String message = " ";
        message=work+" "+number;
        if(message.equals("done")) break;
        channel.basicPublish( "", MY_QUEUE_NAME, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        break;
    
    }
    channel.queueDeclare("theResult", true, false, false, null);
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume("theResult", false, consumer);  
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    result = new String(delivery.getBody());       
    System.out.println(result);
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    channel.close();
    connection.close();
  }

    
    
}
