package br.ufs.dcomp.ExemploRabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;

import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatRabbit {

  private final static String QUEUE_NAME = "minha-fila";
  private final static DateFormat sdf = new SimpleDateFormat("'('dd/MM/yyyy 'às' HH:mm:ss') '");

  public static void main(String[] argv) throws Exception {
    Scanner entrada = new Scanner(System.in);
    
    
    
    
    ConnectionFactory factory = new ConnectionFactory();
    //factory.setUri("amqp://huarumck:VncxT9rNIpuDuLcCkfJqne0JWAlKbA0k@otter.rmq.cloudamqp.com/huarumck");
    factory.setHost("ec2-34-220-41-87.us-west-2.compute.amazonaws.com");
    factory.setUsername("accelwo");
    factory.setPassword("meupass");
    factory.setVirtualHost("/");
    
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Esperando recebimento de mensagens...");

    Consumer consumer = new DefaultConsumer(channel) {
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        //System.out.println(" [x] Mensagem recebida: '" + message + "'");
        System.out.println(message);
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
    for (int i = 0; i <= 5; i++){
        //System.out.println("loop "+ i);
    
    
    System.out.print("Digite sua menssage: ");
    String message = entrada.nextLine();
    Date data = new Date();

    System.out.println("Data  = "+ data);
    System.out.println("Data2 = "+ sdf.format(data));
    String texto = sdf.format(data) + QUEUE_NAME + " diz: " + message; 
    
    //String message = "Olá!!!";
    channel.basicPublish("", QUEUE_NAME, null, texto.getBytes("UTF-8"));
    //System.out.println(" [x] Mensagem enviada: '" + message + "'");
    }
  }
}