package rab;


import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
public class Worker4 {
private static final String TASK_QUEUE_NAME = "task_queue";
public static String toWork() throws Exception {
ConnectionFactory factory = new ConnectionFactory();
factory.setHost("localhost");
Connection connection = factory.newConnection();
Channel channel = connection.createChannel();
channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
channel.basicQos(1);
QueueingConsumer consumer = new QueueingConsumer(channel);
channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
while (true) {
QueueingConsumer.Delivery delivery = consumer.nextDelivery();
String message = new String(delivery.getBody());
System.out.println(" [x] Received '" + message + "'");
String ans = doWork(message);
//String ans2 = Worker5.toWork();
System.out.println(" [x] Done. Answer is "+ ans);
channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
return ans;//+""+ans2;
}
}
private static String doWork(String task) throws InterruptedException {
/*for (char ch: task.toCharArray()) {
if (ch == '.') Thread.sleep(1000);*/
	int number = Read.convertToInt(task);
	int cnt=0;
	String ans=null;
	for(int i=1;i<=number;i++){
		if(number%i==0){
			cnt++;
		}
	}
	if (cnt==2) ans="prime";
	else ans="not prime";
	return ans;
}

}