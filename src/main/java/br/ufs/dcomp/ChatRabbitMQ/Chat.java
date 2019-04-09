//Alunos: 
//        Ivisson de Jesus Silva
//        Felipe Meneses dos Santos
package br.ufs.dcomp.ChatRabbitMQ;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Chat {

    private static String destinatario = " ";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://ivissonijs:132435@100.24.58.45");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Scanner in = new Scanner(System.in);//-->Entrada de dados
        String user; //-->Para armazenar o usuário
//-----------Garantir a quebra de linha no Mac, Windowns e Linux----------------
        String quebraLinha = System.getProperty("line.separator"); //Recebendo metódo que quebra linha 
//-----------Data e Hora atual--------------------------------------------------
        Date data_Hora = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(data_Hora);
        String hora = new SimpleDateFormat("HH:mm:ss").format(data_Hora);
        ByteString bufferConteudo = null;//ByteStringo para enviar o conteudo
        ByteArrayOutputStream byti;
        ChatGrupo grupo = new ChatGrupo();//Instanciando Classe de Criação de grupos
        MensagemProto msg = new MensagemProto();//InstÂncia da Classe de Envio e Recebimento de Mensagens via ProtoBuf
        RESTClient listar = new RESTClient();
        
//----------Inserir usuário e criar fila no Rabbitmq----------------------------
        System.out.println("Digite o nome do usuário!");
        System.out.print("User: ");
//----------Acrescenta o @ na frente do usuário---------------------------------
        user = ("@" + in.nextLine());
        System.out.println("Iniciando Chat ....");
        System.out.println("Selecione um amigo(a) ou um grupo a conversar...");
//-----------------------(queue-name, durable, exclusive, auto-delete, params);-    
        channel.queueDeclare(user, false, false, false, null);//Fila comun para mensagens
        channel.queueDeclare("arq" + user, false, false, false, null);//Fila para recebimento de arquivos
//------------------------------------------------------------------------------
//----------------Metódo Consumidor de Mensagens e arquivos----------------------
        Consumer consumer = new DefaultConsumer(channel) {//Consumidor de Mensagens
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                msg.ReceberConteudoProto(destinatario, body);//Mensagens
            }
        };
        Consumer consumer2 = new DefaultConsumer(channel) {//Cosumidor de Arquivos
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                msg.receberArquivoProto(destinatario, body);//Arquivo
            }
        };

//-----------------------(queue-name, autoAck, consumer);-----------------------    
        channel.basicConsume(user, true, consumer);//Consumir Mensagens
        channel.basicConsume("arq" + user, true, consumer2);//Consumir Arquivos

//-------Neste momento o destinatário está null, logo só imprimirá no promt ">>"    
        System.out.print(destinatario + ">>");
        destinatario = in.nextLine();

//------------------------------------------------------------------------------
//---A variável ativadorUsuario recebe o primeiro caracter digitado pelo usuário
        char ativadorUsuario = destinatario.charAt(0);
        while ((ativadorUsuario != '!') && (ativadorUsuario != '@') && (ativadorUsuario != '#')) {
            System.err.println("Selecione um usuário, ou digite um comando de grupos válido");
            destinatario = "";
            System.out.print(destinatario + ">>");
            destinatario = in.nextLine();
            ativadorUsuario = destinatario.charAt(0);
        }

        while ((ativadorUsuario != '@') || (ativadorUsuario != '#')) {

            if (destinatario.length() > 8) {
                do {
                    if (destinatario.length() > 8) {//Adcionar usuário a um grupo
                        if ("!addUser".equalsIgnoreCase(destinatario.substring(0, 8))) {
//--------------------Criação de Grupo no promt inicial
                            destinatario = grupo.addUser(destinatario);
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine();
                        }//-------------------fim do se
                    }
                    if (destinatario.length() > 9) {//Criar um Grupo
                        if ("!newGroup".equalsIgnoreCase(destinatario.substring(0, 9))) {
                            destinatario = grupo.newGrupo(destinatario, user);//Criando Grupo e adcionando o usuário
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine(); //Recebe a mensagem a ser enviada
                        }
                    }
                     if(destinatario.length() > 10){//listar usuários que participam de um grupo
                        if("!listUsers".equalsIgnoreCase(destinatario.substring(0,10))){
                            String[] info = destinatario.split(" ");
                            String nGrupo = info[1];
                            System.out.println("Listando Usuarios do grupo " + nGrupo);
                            
                            listar.listarUsuarios(nGrupo);
                            destinatario = "";
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine();
                            
                        }
                     }
                    if(destinatario.length() > 10){//listar grupos que o usuário participa
                        if("!listGroups".equalsIgnoreCase(destinatario.substring(0,11))){
                            System.out.println("Listando Grupo: ");
                            listar.listarGrupos();
                            destinatario = "";
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine(); //Recebe a mensagem a ser enviada
                        }
                    }
                    if (destinatario.length() > 12) {//Remover Grupo
                        if ("!removeGroup".equalsIgnoreCase(destinatario.substring(0, 12))) {
                            destinatario = grupo.removeGroup(destinatario);
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine(); //Recebe a mensagem a ser enviada                    
                        }
                    }
                    if (destinatario.length() > 13) {//Remover usuário de um grupo
                        if ("!delFromGroup".equalsIgnoreCase(destinatario.substring(0, 13))) {                            
                            destinatario = grupo.delFromGroup(destinatario);
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine(); //Recebe a mensagem a ser enviada                 
                        }
                    } else {
                        ativadorUsuario = destinatario.charAt(0);
                        while ((ativadorUsuario != '!') && (ativadorUsuario != '@') && (ativadorUsuario != '#')) {
                            System.err.println("Selecione um usuário antes de conversar ou digite um comando de grupos válido");
                            destinatario = "";
                            System.out.print(destinatario + ">>");
                            destinatario = in.nextLine();
                            ativadorUsuario = destinatario.charAt(0);
                        }
                        if(destinatario.length() <= 13){
                            //System.out.print(">>");
                            //destinatario = in.nextLine();
                            break;//Não deixa fazer as comparações
                        }
                    }
                } while ("!addUser".equalsIgnoreCase(destinatario.substring(0, 8))
                        || "!newGroup".equalsIgnoreCase(destinatario.substring(0, 9))
                        ||"!listUsers".equalsIgnoreCase(destinatario.substring(0,10))
                        || "!listGroups".equalsIgnoreCase(destinatario.substring(0,11))
                        || "!removeGroup".equalsIgnoreCase(destinatario.substring(0, 12))
                        || "!delFromGroup".equalsIgnoreCase(destinatario.substring(0, 13)));
            } else {
                break;
            }
            ativadorUsuario = destinatario.charAt(0);

        }//fim while 

//---------------------------------------------------------------------------------------------------        
        //Segunda Parte, depois de selecionado o destinatário
        while (true) {

            //imprimir na tela ex: usuárioDestino >>   
            System.out.print(destinatario + ">>");
            String mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada

            /*Se o usuário digitar '@' na frente da mensagem, o promt automaticamente será mudado para
             o nome do novo usuário a enviar a mensagem*/
            char mudarUsuario = ' ';
            do {
                mudarUsuario = mensagemEnviar.charAt(0); //mudarDestino recebe o primeiro caracter da String mensagemEnviar 
                if (mudarUsuario == '@' || mudarUsuario == '#') {
                    destinatario = mensagemEnviar;
                    System.out.print(destinatario + ">>");
                    mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada 
                    mudarUsuario = mensagemEnviar.charAt(0);
                }
            } while (mudarUsuario == '@' || mudarUsuario == '#');

            if (mensagemEnviar.length() > 7) {
                do {
                    if (mensagemEnviar.length() > 7) {
                        if ("!upload".equalsIgnoreCase(mensagemEnviar.substring(0, 7))) {//Enviar arquivos
                            String[] info = mensagemEnviar.split(" ");
                            String uri = info[1];
                            new chanelThread(user, destinatario, uri, data, hora);
                            System.out.println("Enviando " + uri + " para " + destinatario);
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine();
                        }//-----------------fim do se
                    }
                    if (mensagemEnviar.length() > 8) {//Adcionar usuário a um grupo
                        if ("!addUser".equalsIgnoreCase(mensagemEnviar.substring(0, 8))) {
                            grupo.addUser(mensagemEnviar);
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine();
                        }//-------------------fim do se
                    }
                    if (mensagemEnviar.length() > 9) {//Criar um Grupo
                        if ("!newGroup".equalsIgnoreCase(mensagemEnviar.substring(0, 9))) {
                            grupo.newGrupo(mensagemEnviar, user);//Criando Grupo e adcionando o usuário
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada
                        }
                    } //-------------------fim do se
                    if(mensagemEnviar.length() >= 10){//listar usuários que participam de um grupo
                        if("!listUsers".equalsIgnoreCase(mensagemEnviar.substring(0,10))){
                            String[] info = mensagemEnviar.split(" ");
                            String nGrupo = info[1];
                            System.out.println("Listando Usuarios do grupo " + nGrupo);
                            listar.listarUsuarios(nGrupo);
                            //destinatario = "";
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada
                        }
                     }
                    if(mensagemEnviar.length() > 10){//listar grupos que o usuário participa
                        if("!listGroups".equalsIgnoreCase(mensagemEnviar.substring(0,11))){
                            System.out.println("Listando Grupo: ");
                            listar.listarGrupos();
                            //destinatario = "";
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada
                        }
                    }
                    if (mensagemEnviar.length() > 12) {//Remover Grupo
                        if ("!removeGroup".equalsIgnoreCase(mensagemEnviar.substring(0, 12))) {
                            grupo.removeGroup(mensagemEnviar);
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada                    
                        }//-------------------fim do se
                    }
                    if (mensagemEnviar.length() > 13) {//Remover usuário de um grupo
                        if ("!delFromGroup".equalsIgnoreCase(mensagemEnviar.substring(0, 13))) {
                            grupo.delFromGroup(mensagemEnviar);
                            System.out.print(destinatario + ">>");
                            mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada
                        }//-------------------fim do se
                    } else {
                        break;
                    }

                } while ("!upload".equalsIgnoreCase(mensagemEnviar.substring(0, 7))
                        || "!addUser".equalsIgnoreCase(mensagemEnviar.substring(0, 8))
                        || "!newGroup".equalsIgnoreCase(mensagemEnviar.substring(0, 9))
                        ||"!listUsers".equalsIgnoreCase(mensagemEnviar.substring(0,10))
                        || "!listGroups".equalsIgnoreCase(mensagemEnviar.substring(0,11))
                        || "!removeGroup".equalsIgnoreCase(mensagemEnviar.substring(0, 12))
                        || "!delFromGroup".equalsIgnoreCase(mensagemEnviar.substring(0, 13)));
            }

            /*Se o usuário digitar '@' na frente da mensagem, o promt automaticamente será mudado para
             o nome do novo usuário a enviar a mensagem*/
            mudarUsuario = ' ';
            do {
                mudarUsuario = mensagemEnviar.charAt(0); //mudarDestino recebe o primeiro caracter da String mensagemEnviar 
                if (mudarUsuario == '@' || mudarUsuario == '#') {
                    destinatario = mensagemEnviar;
                    System.out.print(destinatario + ">>");
                    mensagemEnviar = in.nextLine(); //Recebe a mensagem a ser enviada 
                    mudarUsuario = mensagemEnviar.charAt(0);
                }
            } while (mudarUsuario == '@' || mudarUsuario == '#');


            ativadorUsuario = destinatario.charAt(0);
            switch (ativadorUsuario) {
                case '@'://Publica a mensagem em bytes para um amigo
                    //Complemento da mensagem a ser enviada
                    mensagemEnviar = mensagemEnviar + quebraLinha;
                    //Usa o metódo sustring para tirar o @ da frente do nome do usuário
                    String usuario = user.substring(1);
                    bufferConteudo = ByteString.copyFromUtf8(mensagemEnviar);
                    //Serialização dos Dados para enviar:  //(corpo mensagem, usuário, data, hora, nome do Grupo)
                    byte[] buffer = msg.mensagemProtoEnviar(bufferConteudo, usuario, data, hora, " ", " ", " ");
                    //Publicando Mensagem
                    channel.basicPublish("", destinatario, null, buffer /*message.getBytes("UTF-8")*/);
                    break;
                case '#'://Publica a mensagem em bytes para um grupo
                    //Complemento da mensagem a ser enviada
                    mensagemEnviar = mensagemEnviar + quebraLinha;
                    //Usa o metódo sustring para tirar o @ da frente do nome do usuário
                    usuario = user.substring(1);
                    bufferConteudo = ByteString.copyFromUtf8(mensagemEnviar);
                    //Serialização dos Dados para enviar: //(corpo mensagem, usuário, data, hora, nome do Grupo)
                    buffer = msg.mensagemProtoEnviar(bufferConteudo, usuario, data, hora, destinatario, " ", " ");
                    //Publicando Mensagem
                    channel.basicPublish(destinatario, "", null, buffer /*message.getBytes("UTF-8")*/);
                    break;
            }//fim swithc
        }//fim while true
    }//fim Main
}//Fim classe

