package router;

import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class EmitLogDirect {

  private static final String EXCHANGE_NAME = "direct_logs";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");

    
    Scanner in = new Scanner(System.in);
    
    System.out.println("Enter number: ");
    String message = in.nextLine();
    System.out.println("Choose operation: ");
    String severity = in.nextLine();
    channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
    System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

    channel.close();
    connection.close();
  }
  
  
}