package router;
import java.util.ArrayList;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsDirect {

  private static final String EXCHANGE_NAME = "direct_logs";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String queueName = channel.queueDeclare().getQueue();
    
    
    
      channel.queueBind(queueName, EXCHANGE_NAME, "1");
      channel.queueBind(queueName, EXCHANGE_NAME, "2");
      channel.queueBind(queueName, EXCHANGE_NAME, "3");

    
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(queueName, true, consumer);

    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      String routingKey = delivery.getEnvelope().getRoutingKey();
      String str="";
      if(routingKey.equals("1")){
    	  str = primeFactors(Integer.parseInt(message));
      }else if(routingKey.equals("2"))
    	  str = isPrime(Integer.parseInt(message));
      else if(routingKey.equals("3")){
    	  Integer integer = fib(Integer.parseInt(message));
    	  str = integer.toString();
      }
      System.out.println(" [x] Received '" + routingKey + "':'" + str + "'");   
    }
  }
  private static int fib(int n) {
	    if (n==0) return 0;
	    if (n==1) return 1;
	    return fib(n-1) + fib(n-2);
	  }
	  
	  private static String isPrime(int n) {
		    //check if n is a multiple of 2
		    if (n%2==0) return "false";
		    //if not, then just check the odds
		    for(int i=3;i*i<=n;i+=2) {
		        if(n%i==0)
		            return "false";
		    }
		    return "true";
		}
	  
		  public static String primeFactors(int number) {
			  if(number==1) return "1";
		    int n = number;
		    ArrayList<Integer> factors = new ArrayList<Integer>();
		    for (int i = 2; i <= n; i++) {
		      while (n % i == 0) {
		        factors.add(i);
		        n /= i;
		      }
		    }
		    
		    String str = "";
		    for(int i=0; i<factors.size(); i++){
		    	str+=factors.get(i).toString();
		    			if(i!=factors.size()-1) str+="*";
		    }
		    return str;
		  }
}