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

/**
 *
 * @author User
 */
public class FactorTask {
    private static final String MY_QUEUE_NAME = "factornumber";
    public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
   factory.setHost("localhost");
   Connection connection = factory.newConnection();
   Channel channel = connection.createChannel();
   
   channel.queueDeclare(MY_QUEUE_NAME, true, false, false, null);
   channel.basicQos(1);
   QueueingConsumer consumer = new QueueingConsumer(channel);
   channel.basicConsume(MY_QUEUE_NAME, false, consumer);
 
  //while (true) {
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    
   //} 
  String message = new String(delivery.getBody());
    System.out.println(" [x] Received '" + message + "'");
    int a = Integer.parseInt(message);
    int res = getFactorial(a);
    String result="answer "+ res;
    System.out.println(result);
    System.out.println(" [x] Done");
    channel.basicPublish( "", "mainqueue", 
            
             MessageProperties.PERSISTENT_TEXT_PLAIN,
            result.getBytes());           
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
 }
 
   /* static boolean isEven(Integer num){
	boolean even=false;
        for(int i = 2; i<= num; i++){
            if(num%i==0){
                even=true;
                break;
            }
        }
	
    return even;
    }*/
         static int getFactorial(int num){
             int fact = 1;
            
                for ( int c = 1 ; c <= num ; c++ ){
                   fact = fact*c;
                   //System.out.println("Factorial of "+num+" is = "+fact);
                }
                return fact;
         }
}

