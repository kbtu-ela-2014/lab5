/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import static connect.FactorTask.getFactorial;
import java.util.StringTokenizer;

/**
 *
 * @author User
 */
public class MathTask {
    private static final String MY_QUEUE_NAME = "evennum";
 public static void main(String[] argv) throws Exception {
  ConnectionFactory factory = new ConnectionFactory();
   factory.setHost("localhost");
   Connection connection = factory.newConnection();
   Channel channel = connection.createChannel();
   
   channel.queueDeclare(MY_QUEUE_NAME, true, false, false, null);
   channel.basicQos(1);
   QueueingConsumer consumer = new QueueingConsumer(channel);
   channel.basicConsume(MY_QUEUE_NAME, false, consumer);
 
  while (true) {
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    String message = new String(delivery.getBody());
    System.out.println(" [x] Received '" + message + "'");
    int a = Integer.parseInt(message);
    boolean res = isEven(a);
    System.out.println(" [x] Done");
    String result="answer "+res;
    System.out.println(result);
    //StringTokenizer token = new StringTokenizer(message, ",");
    //int n = Integer.parseInt(token.nextToken());
    channel.basicPublish( "", "mainqueue", 
             MessageProperties.PERSISTENT_TEXT_PLAIN,
            result.getBytes());           
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    //String response ="answer" + isEven(n);
   }         
 }
 
    static boolean isEven(int num){
        boolean even=false;
        //int n = 5;
       
        for(int i = 2; i<= num; i++){
            if(num%i==0){
                even=true;
                break;
            }
        }
	
    return even;
	
    }
       /*  static int getFactorial(Integer num){
             int fact = 1;
             
                for ( int c = 1 ; c <= num ; c++ ){
                   fact = fact*c;
                   System.out.println("Factorial of "+num+" is = "+fact);
      
             return fact;
                }
             return 0;
         }*/

   /* private static boolean isEven(String message) {
       boolean even=false;
       int num = Integer.parseInt(message);
        for(int i = 2; i<= num; i++){
            if(num%i==0){
                even=true;
                break;
            }
        }
	
    return even;
    }*/
    
}
