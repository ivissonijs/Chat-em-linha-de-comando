//Alunos: 
//        Ivisson de Jesus Silva
//        Felipe Meneses dos Santos
package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Scanner;

/**
 *
 * @author Ivisson e Felipe
 */
public class ChatGrupo {

    //Garantir a quebra de linha no Mac, Windowns e Linux
    String quebraLinha = System.getProperty("line.separator"); //Recebendo metódo que quebra linha 
    Scanner in = new Scanner(System.in);

    //-------------Método para criação de grupo----------------------------------------------------------
    String newGrupo(String grupo, String usuario /*String destinatario*/) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://ivissonijs:132435@100.24.58.45");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /*Verifica o tamanho da variavel nomeGrupo, se for maior que 9 ele verifica 
         se a palavra é "!newGroup", comando para criação de grupos*/
        grupo = grupo.substring(10);
        grupo = '#' + grupo;//Concatenando o # na variável nomeGrupo
        channel.exchangeDeclare(grupo, "fanout");
        channel.exchangeDeclare("arq" + grupo, "fanout");
        channel.queueBind(usuario, grupo, "");//Adcionar o usuário ao próprio grupo criado
        channel.queueBind("arq" + usuario, "arq" + grupo, "");//Adcionar o usuário ao próprio grupo criado arquivo
        System.out.println("Grupo " + grupo + " Criado com Sucesso!" + quebraLinha
                + "Você foi adcionado ao grupo " + grupo + "!");
        channel.close();

        return "";
    }

//---------------Metódo para adcionar usuários em grupos------------------------------------------------
    String addUser(String usuario) throws Exception {
        //Conexão 
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://ivissonijs:132435@100.24.58.45");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /*Verifica o tamanho da variavel usuario, se for maior que 8 ele verifica 
         se a palavra é "!addUser", comando para adição de usuários a grupos*/
        String[] info = usuario.split(" ");
        String usuarioDestino = '@' + info[1];//Variável para receber o nome do usuário a ser inserido ao grupo
        String nome_grupo = '#' + info[2];//Variável para receber o nome do grupo a ser inserido o usuário
        channel.queueBind(usuarioDestino, nome_grupo, "");//Adcionar o usuário a um grupo
        channel.queueBind("arq" + usuarioDestino, "arq" + nome_grupo, "");//Adcionar o usuário a um grupo arquivo
        System.out.println(usuarioDestino.substring(1) + " adcionando ao grupo " + nome_grupo);
        channel.close();

        return "";
    }
//Exclusão de Usuário de um Grupo

    String delFromGroup(String msg) throws Exception {
        //Conexão 
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://ivissonijs:132435@100.24.58.45");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String[] info = msg.split(" ");
        String usuario = "@" + info[1];
        String grupo = "#" + info[2];
        channel.queueUnbind(usuario, grupo, "");//Exclusão de usuário de um grupo
         channel.queueUnbind("arq" + usuario, "arq" + grupo, "");//Exclusão de usuário de um grupo arquivo
        System.out.println(usuario.substring(1) + " Excluido do Grupo " + grupo.substring(1));
        channel.close();

        return "";
    }
//Remoção de grupos

    String removeGroup(String msg) throws Exception {
        //Conexão 
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://ivissonijs:132435@100.24.58.45");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        String[] info = msg.split(" ");
        String grupo = "#" + info[1];
        channel.exchangeDelete(grupo);//Comando para deletar um grupo
         channel.exchangeDelete("arq" + grupo);//Comando para deletar um grupo arquivo
        System.out.println("Grupo " + grupo.substring(1) + " Excluido com Sucesso!");
        channel.close();
        
        return "";
    }

}//fim da classe
