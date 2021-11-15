import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class consumer2 {
    public static void main(String[] args) {
        thread(new HelloWorldConsumer(), false);

    }
    public static void thread(Runnable runnable,boolean deamon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(deamon);
        brokerThread.start();
    }
    public static class HelloWorldConsumer implements Runnable,ExceptionListener{
        public void run(){
            try{
                //create connentionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");

                ////create connention
                Connection connection = connectionFactory.createConnection();
                connection.start();

                connection.setExceptionListener(this);

                //create a session
                Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

                //create destination
                Destination destination = session.createQueue("message.queue");

                //create a MessageConsumer frome the session to the topuc or queue
                MessageConsumer consumer = session.createConsumer(destination);

                //wait for a message
                Message message = consumer.receive(1000);

                //action
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("recived : " + text);

                }else{
                    System.out.println("recived : " + message);
                }
                consumer.close();
                session.close();
                connection.close();

            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        public synchronized void  onException(JMSException ex){
            System.out.println("exception occured. client shutdown");
        }
    }


}
