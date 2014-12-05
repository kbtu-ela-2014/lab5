import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MainWindow{
	private static Scanner in;
	private final static String QUEUE_NAME = "Aki-5";
	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		in = new Scanner(System.in);
		int choice;
		Long number;
		Mesg msgnum = null;
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection conn = factory.newConnection();
	    Channel channel = conn.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	        
		while(true){
			System.out.println("Enter the number you want to process!");
			number = in.nextLong();
			System.out.println("What do you want to do: \n1)Factorize \n2)Identify if this number is prime \n3)If this number is divisible by 7 \n4)Exit");
			choice = in.nextInt();
			if(choice <= 4) {
				msgnum = new Mesg(number, choice);
				if(choice == 4) {
					channel.close();
					conn.close();
					break;
				}
			}

	        byte[] msgbyte = msgnum.getBytes();
	        channel.basicPublish("", QUEUE_NAME, null, msgbyte);	        
	        
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        String response = new String(delivery.getBody());
	        System.out.println(response);        
	        	        			
			System.out.println("Do you want to enter another number? \n1)Yes \n2)No");
			if(in.nextInt() == 2) {
				msgnum = new Mesg(number, 4);
				msgbyte = msgnum.getBytes();
				channel.basicPublish("", QUEUE_NAME, null, msgbyte);
				channel.close();
				conn.close();
				break;
			}	
		}
	}
}
