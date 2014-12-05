import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ProcessClass {
	private final static String QUEUE_NAME = "Aki-5";
	public static void main(String[] args) throws IOException{
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection conn =  factory.newConnection();
        Channel channel = conn.createChannel();
        
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);
        boolean somebool = true;
        String response="";
        while (somebool) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                Mesg msgnum = Mesg.fromBytes(delivery.getBody());
                if(msgnum.getFunc()==1) {             	
                	Long result = (long) 1;
            		for (int i=1; i<=msgnum.getNum(); i++){
            			result = result*i;
            		}
            		response = "Factor is: " + result.toString();
                }
                if(msgnum.getFunc()==2) {
                	while(true){
            			if (msgnum.getNum()%2==0) {
            				response = "The number is not prime!";
            				break;
            			}
            			if (msgnum.getNum()<4) {
            				response = "The number is prime!";
            				break;
            			}
            			Long sqrtnum = (long) Math.sqrt(msgnum.getNum());
            			sqrtnum = (long) Math.round(sqrtnum);
            		    for(int i=3;i<=sqrtnum+1;i+=1) {
            		        if(msgnum.getNum()%i==0){
            		        	response = "The number is not prime!";
            		        	break;
            		        }
            		    }
            		    response = "The number is prime!";
            		    break;
            		}  
                }
                if(msgnum.getFunc()==3) {                	
                	if(msgnum.getNum()%7==0) response="The number is divisable by 7!";
            		else response="The number is not divisable by 7!";
                }
                if(msgnum.getFunc()==4) {
                	somebool=false;
                	channel.close();
                	conn.close();
                }
                byte[] responseByte = response.getBytes();
    	        channel.basicPublish("", QUEUE_NAME, null, responseByte);
                
            } catch (InterruptedException ie) {
                continue;
            }
        }
	}
}
