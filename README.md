# ChatRabbitMQ

Esta tarefa consiste no desenvolvimento de um cliente de chat do tipo linha de comando usando o RabbitMQ como servidor de mensagens de acordo com o apresentado em sala de aula.

Ver demais arquivos das etapas do projeto.




Comandos:

Compilação: mvn compile assembly:single
Executar: java -jar target/chat<tab>


protobuf
//Compilar arquivo .proto
// ex do comando: protoc --java_out=${OUTPUT_DIR} path/to/your/proto/file
//Antes de compilar de esse comando abaixo e em seguida o comando para compilar.

export PATH=$PATH:/home/ubuntu/workspace/chat-em-linha-de-comando-via-rabbitmq-ivissonijs/protobuf-compiler/bin 

protoc --java_out=src/main/java/ src/main/java/br/ufs/dcom/ChatRabbitMQ/ProtoBuf.proto


https://www.rabbitmq.com/releases/rabbitmq-java-client/v1.7.0/rabbitmq-java-client-javadoc-1.7.0/com/rabbitmq/client/Channel.html
https://developers.google.com/protocol-buffers/docs/reference/java/com/google/protobuf/ByteString
https://blog.alura.com.br/upload-de-arquivos-em-java/
https://www.guj.com.br/t/converter-de-byte-para-java-io-file/134808


Caminho para teste do Trabalho de Sistemas Distribuidos

F:\\Documentos\\NetBeansProjects\\Chat_Rabbit\\src\\br\\ufs\\dcomp\\ChatRabbitMQ\\arquivos\\foto_3.png

!upload /home/ubuntu/workspace/chat-em-linha-de-comando-via-rabbitmq-ivissonijs/meusArquivos/foto_3.png