//package vasilkov.labbpls2;
//
//
//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;
//import org.apache.qpid.jms.JmsConnectionFactory;
//
//public class Connect {
//    public static void main(String[] args) throws Exception {
//
////        ConnectionFactory connFactory = new JmsConnectionFactory("amqps://localhost:5672?jms.username=guest&jms.password=guest&amqp.vhost=/");
//        JmsConnectionFactory connFactory = new JmsConnectionFactory("amqp://localhost/?brokerlist='tcp://localhost:5672?ssl='true'");
//        Connection conn = connFactory.createConnection("guest", "guest");
//        conn.start();
//
//        try {
//            System.out.println("CONNECT: Connected to '"  + "'");
//        } finally {
//            conn.close();
//        }
//    }
