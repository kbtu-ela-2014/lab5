package Message;

import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {
	
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args)  throws java.io.IOException {
		// TODO Auto-generated method stub
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    
	    String message,action;
	    Scanner in = new Scanner(System.in);
	    
	    System.out.println("Enter a number:");
	    String number = in.next();
	    
	    System.out.println("Choose action:");
	    System.out.println("1)Factorization;");
	    System.out.println("2)IsPrime;");
	    System.out.println("3)Exponentiation;");
	    
	    
	    int choice = in.nextInt();
	    if(choice==1){
	    		action = "F";	    	
	    }else
	    	if(choice==2){
	    		action = "I";
	    	}
	    	else{
	    		action = "E";
	    		String pow = in.next();
	    		action = action+pow+'.';
	    	}
	    
	    
	    message = action + number;
	    
	    
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	    System.out.println(" [x] Sent ");
	    channel.close();
	    connection.close();
	}

}
