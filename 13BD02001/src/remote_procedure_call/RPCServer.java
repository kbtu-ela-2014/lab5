import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import java.util.StringTokenizer;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    //checks whether an int is prime or not.
    static boolean isPrime(int n) {
        for(int i=2;i<n;i++) {
            if(n%i==0)
                return false;
        }
        return true;
    }
    // factorial
    public static int factorial(int n) {
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    // 2*
    private static int binpow(int n) {
        int ans = 1;

        for (int i = 1; i <= n; i++) {
            ans *= 2;
        }

        return ans;
    }

    //exp
    static int ipow(int base, int exp)
    {
        int result = 1;

        for (int i=1; i<=exp;i++) {
            result *= base;
        }
        return result;
    }

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;

    try {
            ConnectionFactory factory = new ConnectionFactory();
            //factory.setHost("localhost");
            factory.setUri(Constants.uri);

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (true) {
                String response = null;

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
                    String message = new String(delivery.getBody(),"UTF-8");
                    //for first 3 ones//int n = Integer.parseInt(message);

                    //System.out.println(" [.] ipow(" + message + ")");
//System.out.println(message);
                    StringTokenizer tokenizer = new StringTokenizer(message,"|");

                    int base = Integer.parseInt(tokenizer.nextToken());
                    int exp = Integer.parseInt(tokenizer.nextToken());

                    response = "power: " + ipow(base, exp) + "\n" + "binpow: " + binpow(base) + "\n" + "factorial: " + factorial(base) + "\n" + "isPrime: " + isPrime(base) + "\n";
                }
                catch (Exception e){
                    System.out.println(" [.] " + e.toString());
                    response = "";
                }
                finally {
                    channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        }
        catch  (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (Exception ignore) {}
            }
        }
    }
}