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
  public static boolean novo_login;
  public static boolean novo_destino;
  public static String Destino = new String("");

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
    
    System.out.print("Usuário: ");
    String user = entrada.nextLine();
    
    System.out.println("Destino =" + Destino);
    
    System.out.println("Bem vindo " + user);
    novo_login = true;
    //System.out.print(">>");

    channel.queueDeclare(user, false, false, false, null);
    System.out.println(" [*] Carregando Mensagens salvas!  [*]");

    Consumer consumer = new DefaultConsumer(channel) {
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        //System.out.println(" [x] Mensagem recebida: '" + message + "'");
        if (novo_login) {
          System.out.println(message);
          System.out.println("novo");
        } else {
          System.out.println("");
          System.out.println(message);
          System.out.print(Destino + ">>");
        }
      }
    };
    channel.basicConsume(user, true, consumer);
    
    System.out.println(" [*] Finalizando mensagens salvas! [*]");
    novo_login = false;

    
    for (int i = 0; i <= 5; i++){
        //System.out.println("loop "+ i);
    
    
    //System.out.print("Digite sua menssage: ");
    System.out.print(Destino + ">>");
    String message = entrada.nextLine();
    
    //System.out.println("primeiro Char = "+ message.substring(0,1));
    if (message.substring(0,1).equals("@")){
      //System.out.println("Recebida @");
      Destino = message.substring(1,message.length());
      //System.out.println("Destinatario : " + Destino);
      novo_destino = true;
    }
    Date data = new Date();

    //System.out.println("Data  = "+ data);
    //System.out.println("Data2 = "+ sdf.format(data));
    String texto = sdf.format(data) + user + " diz: " + message; 
    
    //String message = "Olá!!!";
    if (novo_destino){
      //System.out.println("");
      novo_destino = false;
    } else {
      if (!Destino.equals("")){
        channel.basicPublish("", Destino, null, texto.getBytes("UTF-8"));  
        //System.out.println("Enviado: "+ texto);
        
      } else {
        System.out.println("[*] Erro: Nenhum destinatario selecionado  [*]");
        System.out.println("[*] Por favor utilize o comando @nome para [*]");
        System.out.println("[*] selecionar o destinatario              [*]");
      }
    }
    
    //System.out.println(" [x] Mensagem enviada: '" + message + "'");
    }
  }
}