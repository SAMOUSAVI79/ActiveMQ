import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Session;
import javax.jms.*;


public class producer {
    public static void main(String[] args) {
        thread(new HelloWorldProducer(), false);
        thread(new consumer1.HelloWorldConsumer(), false);
        thread(new consumer2.HelloWorldConsumer(), false);

    }
    public static void thread(Runnable runnable, boolean daemon){

        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
    public static class HelloWorldProducer implements Runnable{
        public void run(){
            try{
                //create a connectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");

                //create a connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                //create a session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                //create a destination (topic or queue)
                Destination destination = session.createQueue("message.queue");

                //create a MessageProducer frome the session to the topuc or queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                //create multi Message
                String text = "message" ;
                for (int i=0; i < 2 ; i++) {
                    String fulltext = text + i;
                    TextMessage message = session.createTextMessage(fulltext);

                    //Tell the producer to send the message
                    System.out.println("Sent message : '" + fulltext + "'" );
                    producer.send(message);
                }



                //clean up
                session.close();
                connection.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
