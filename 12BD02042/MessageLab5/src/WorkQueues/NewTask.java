package WorkQueues;

import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
  
  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    
    
    Scanner in = new Scanner(System.in);
	  System.out.println("Enter a number:");
	    String number = in.next();
	    
	    System.out.println("Choose action:");
	    System.out.println("1)Factorization;");
	    System.out.println("2)IsPrime;");
	    System.out.println("3)Exponentiation;");
	    String action;
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
	    

	    
	   String message = action + number;

    
    
    channel.basicPublish( "", TASK_QUEUE_NAME, 
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
    System.out.println(" [x] Sent '" + message.substring(1) + "'");
    
    channel.close();
    connection.close();
  }
    
 /* private static String getMessage(String[] strings){
    if (strings.length < 1)
      return "Hello World!";
    return joinStrings(strings, " ");
  }  
  
  private static String joinStrings(String[] strings, String delimiter) {
    int length = strings.length;
    if (length == 0) return "";
    StringBuilder words = new StringBuilder(strings[0]);
    for (int i = 1; i < length; i++) {
      words.append(delimiter).append(strings[i]);
    }
    return words.toString();
  }*/
}