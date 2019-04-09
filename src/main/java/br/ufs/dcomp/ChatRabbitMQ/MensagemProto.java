//Alunos: 
//        Ivisson de Jesus Silva
//        Felipe Meneses dos Santos
package br.ufs.dcomp.ChatRabbitMQ;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ivisson e Felipe
 */
public class MensagemProto {

    String quebraLinha = System.getProperty("line.separator"); //Recebendo metódo que quebra linha 

    public byte[] mensagemProtoEnviar(ByteString conteudoEnviar, String emissor, String data, String hora, String grupo, String tipo, String nome) {

        // Agrupando dados do contéudo da mensagem a ser enviada
        ProtoBuf.Conteudo.Builder conteudo1 = ProtoBuf.Conteudo.newBuilder();
        conteudo1.setTipo(tipo);
        conteudo1.setCorpo(conteudoEnviar);
        conteudo1.setNome(nome);

        ProtoBuf.Mensagem.Builder builderMensagem = ProtoBuf.Mensagem.newBuilder();
        builderMensagem.setEmissor(emissor);
        builderMensagem.setData(data);
        builderMensagem.setHora(hora);
        builderMensagem.setGrupo(grupo);
        builderMensagem.addConteudo(conteudo1);
        // Obtendo o Mensagem a ser enviada, com data, hora, emissor e nome do grupo
        ProtoBuf.Mensagem mensagemObtida = builderMensagem.build();
        // Serializando a mensagem 
        byte[] buffer = mensagemObtida.toByteArray();
        return buffer;
    }

    public void ReceberConteudoProto(String destinatario, byte[] body) throws InvalidProtocolBufferException {
        //Mapeando bytes para a mensagem protobuf
        ProtoBuf.Mensagem conteudoRecebido = ProtoBuf.Mensagem.parseFrom(body);
        String emissorRecebido = conteudoRecebido.getEmissor();
        String dataRecebida = conteudoRecebido.getData();
        String horaRecebida = conteudoRecebido.getHora();
        String grupoRecebido = conteudoRecebido.getGrupo();
        String nomeGrupo = conteudoRecebido.getGrupo();
        String corpoConteudoString = null;
        //Extraindo o conteudo recebido
        List<ProtoBuf.Conteudo> conteudo = conteudoRecebido.getConteudoList();
        for (int i = 0; i < conteudo.size(); i++) {
            ProtoBuf.Conteudo conteudosRecebidos = conteudo.get(i);
            ByteString corpo = conteudosRecebidos.getCorpo();
            corpoConteudoString = corpo.toStringUtf8();
            String tipo = conteudosRecebidos.getTipo();
            String nome = conteudosRecebidos.getNome();
        }
        String emissor = emissorRecebido.substring(1);
        String message = quebraLinha + "(" + dataRecebida + " às " + horaRecebida + ") " + emissorRecebido + nomeGrupo + " diz: "
                + corpoConteudoString + destinatario + ">>";

        System.out.print(message);
    }

    public void receberArquivoProto(String destinatario, byte[] body) throws InvalidProtocolBufferException, IOException {

        //Mapeando bytes para a mensagem protobuf
        ProtoBuf.Mensagem conteudoRecebido = ProtoBuf.Mensagem.parseFrom(body);
        String emissorRecebido = conteudoRecebido.getEmissor();
        String dataRecebida = conteudoRecebido.getData();
        String horaRecebida = conteudoRecebido.getHora();
        String grupoRecebido = conteudoRecebido.getGrupo();
        String nomeGrupo = conteudoRecebido.getGrupo();
        byte[] buffer = null;//String corpoConteudoString = null;  

        String nome = null;
        String tipo = null;
        //Extraindo o conteudo recebido
        List<ProtoBuf.Conteudo> conteudo = conteudoRecebido.getConteudoList();
        for (int i = 0; i < conteudo.size(); i++) {
            ProtoBuf.Conteudo conteudosRecebidos = conteudo.get(i);
            ByteString corpo = conteudosRecebidos.getCorpo();
            buffer = corpo.toByteArray();//corpoConteudoString = corpo.toStringUtf8();
            tipo = conteudosRecebidos.getTipo();
            nome = conteudosRecebidos.getNome();
        }
        try {
            String caminhoSalvarArquivo = "/home/ubuntu/workspace/chat-em-linha-de-comando-via-rabbitmq-ivissonijs/Download/" + nome + tipo;
                //Windows    "F:\\Documentos\\NetBeansProjects\\Chat_Rabbit\\src\\br\\ufs\\dcomp\\ChatRabbitMQ\\" + nome + tipo;
                //Linux       "/home/ubuntu/workspace/chat-em-linha-de-comando-via-rabbitmq-ivissonijs/Download" + nome + tipo;
    
            File file = new File(caminhoSalvarArquivo); //Criamos arquivo onde será salvo
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); //Criamos o arquivo
            bos.write(buffer); //Gravamos os bytes lá
            bos.close(); //Fechamos o stream.      

        } catch (FileNotFoundException e) {
            return;//Se não estiver arquivo para tratar sai da função
        }
        //(21/09/2016 às 20:55) Arquivo "aula1.pdf" recebido de @tarcisio !
        String emissor = emissorRecebido.substring(1);
        String message = quebraLinha + "(" + dataRecebida + " às " + horaRecebida + ") Arquivo "
                + nome + tipo + "recebido de " + emissorRecebido + " !" + quebraLinha + destinatario + ">>";;

        System.out.print(message);
    }

}//Fim da Classe
