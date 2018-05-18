package br.ufs.dcomp.ExemploRabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class Emissor {

  private final static String QUEUE_NAME = "minha-fila";

  public static void main(String[] argv) throws Exception {
    Scanner entrada = new Scanner(System.in);
    
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("amqp://huarumck:VncxT9rNIpuDuLcCkfJqne0JWAlKbA0k@otter.rmq.cloudamqp.com/huarumck");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.print("Digite sua mensage: ");
    String message = entrada.nextLine();
    //String message = "Olá!!!";
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Mensagem enviada: '" + message + "'");

    channel.close();
    connection.close();
  }
}