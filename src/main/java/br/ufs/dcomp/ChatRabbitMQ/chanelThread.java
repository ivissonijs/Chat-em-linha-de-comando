//Alunos: 
//        Ivisson de Jesus Silva
//        Felipe Meneses dos Santos
package br.ufs.dcomp.ChatRabbitMQ;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.File;
import java.io.FileInputStream;
/**
 *
 * @author Ivisson e Felipe
 */
public class chanelThread extends Thread {

    MensagemProto msg = new MensagemProto();
    String user, destinatario, uri, data, hora;
    ByteString bufferConteudo = null;

    public chanelThread(String user, String destinatario, String uri, String data,
            String hora) {
        this.user = user;
        this.destinatario = destinatario;
        this.uri = uri;
        this.data = data;
        this.hora = hora;
        start();
    }

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://ivissonijs:132435@100.24.58.45");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
               
            File f = new File(uri);

            FileInputStream arquivoBuffer = null;
            arquivoBuffer = new FileInputStream(f); //Criando buffer do arquivo
            //bufferConteudo = bufferConteudo.readFrom(arquivoBuffer);
            //System.out.println("Copiando arquivo em buffer");
            byte[] byt = new byte[(int) f.length()];
            for (int i = 0; i < f.length(); i++) {
                byt[i] = (byte) arquivoBuffer.read();
            }
            String nomeArquivo = f.getName();

//"/home/tarcisio/aula1.pdf" para @marciocosta."
            bufferConteudo = ByteString.copyFrom(byt);
            byte[] buffer = msg.mensagemProtoEnviar(bufferConteudo, user, data, hora, destinatario, " ", nomeArquivo);//Publica o arquivo
//msg.mensagemProtoEnviar(bufferConteudo, user, data, hora, uri, uri, hora)
            char ativadorUsuario = destinatario.charAt(0);
            if (ativadorUsuario == '@') {
                channel.basicPublish("", "arq" + destinatario, null, buffer);
            } else if (ativadorUsuario == '#') {
                channel.basicPublish( "arq" + destinatario, "", null, buffer);
            }

        } catch (Exception ex) {
          
        }  
    }//Fim run()
}//Fim da clase
