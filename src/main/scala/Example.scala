import javax.jms.{Connection, ConnectionFactory, DeliveryMode, Destination, ExceptionListener, JMSException, Message, MessageConsumer, MessageProducer, Session, TextMessage, Topic}
import javax.naming.Context
import javax.naming.InitialContext
import org.apache.qpid.jms.message.JmsTextMessage

import scala.util.Try


object Example extends App {

  class MyExceptionListener extends ExceptionListener {
    override def onException(exception: JMSException): Unit = {
      System.out.println("Connection ExceptionListener fired, exiting.");
      exception.printStackTrace(System.out);
      System.exit(1);
    }
  }

  Try {
    // The configuration for the Qpid InitialContextFactory has been supplied in
    // a jndi.properties file in the classpath, which results in it being picked
    // up automatically by the InitialContext constructor.
    val context = new InitialContext()


    val factory = context.lookup("myFactoryLookup").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("myQueueLookup").asInstanceOf[Destination]
    val topic = context.lookup("myTopicLookup").asInstanceOf[Topic]

    val connection = factory.createConnection("admin", "admin")
    connection.setExceptionListener(new MyExceptionListener())
    connection.start();

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    println("Creating Consumer and Producer")
    val messageConsumer = session.createConsumer(queue)
    val messageProducer = session.createProducer(queue)

    println("Sent message")
    messageProducer.send(session.createTextMessage("Hello!!!"),DeliveryMode.NON_PERSISTENT,Message.DEFAULT_PRIORITY,Message.DEFAULT_TIME_TO_LIVE)
    val receivedMessage = messageConsumer.receive(20000L).asInstanceOf[TextMessage];

    if (receivedMessage != null) {
      System.out.println(receivedMessage.getText());
    } else {
      System.out.println("No message received within the given timeout!");
    }

    connection.close();
  }.get
}