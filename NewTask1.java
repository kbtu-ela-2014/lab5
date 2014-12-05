package rab;


import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
public class NewTask1 {
private static final String TASK_QUEUE_NAME = "task_queue";
public static String getMessage(String num) throws Exception {
ConnectionFactory factory = new ConnectionFactory();
factory.setHost("localhost");
Connection connection = factory.newConnection();
Channel channel = connection.createChannel();
channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
String message = num;
channel.basicPublish( "", TASK_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
System.out.println(" [x] Sent '" + message + "'");
String answer1 = Worker4.toWork();
String answer2 = Worker5.toWork();
channel.close();
connection.close();

return answer1+""+answer2;
}


/*static String getMessage(String num){
//if (strings.length < 1)
return num;*/
//return joinStrings(strings, " ");
//}
private static String joinStrings(String[] strings, String delimiter) {
int length = strings.length;
if (length == 0) return "";
StringBuilder words = new StringBuilder(strings[0]);
for (int i = 1; i < length; i++) {
words.append(delimiter).append(strings[i]);
}
return words.toString();
}
}