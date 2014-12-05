package rab;


import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
public class Worker5 {
private static final String TASK_QUEUE_NAME = "task_queue";
public static String toWork() throws Exception {
ConnectionFactory factory = new ConnectionFactory();
factory.setHost("localhost");
Connection connection = factory.newConnection();
Channel channel = connection.createChannel();
channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
channel.basicQos(1);
System.out.println("jhg");
QueueingConsumer consumer = new QueueingConsumer(channel);
System.out.println("jhg2");
channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
while (true) {
QueueingConsumer.Delivery delivery = consumer.nextDelivery();
System.out.println("jhg3");
String message = new String(delivery.getBody());
System.out.println(" [x] Received '" + message + "'");
String ans = doWork(message);
System.out.println(" [x] Done. Answer is "+ ans);
channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
return ans;
}
}
private static String doWork(String task) throws InterruptedException {
/*for (char ch: task.toCharArray()) {
if (ch == '.') Thread.sleep(1000);*/
	int number = Read.convertToInt(task);
	int fact=1;
	while(number>0){
		fact=number*fact;
		number--;
	}
	String ans=fact+"";
	Thread.sleep(1000);
	return ans;
}

}