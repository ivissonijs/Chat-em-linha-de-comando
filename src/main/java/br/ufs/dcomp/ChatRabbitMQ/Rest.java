//Alunos: 
//        Ivisson de Jesus Silva
//        Felipe Meneses dos Santos
package br.ufs.dcomp.ChatRabbitMQ;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Rest{
    @SerializedName("source")   
    private String source;
    @SerializedName("destination")
    private String destination;
    @SerializedName("vhost")
    private String vhost;
    @SerializedName("destination_type")
    private String destination_type;
    @SerializedName("routing_key")
    private String routing_key;
    @SerializedName("properties_key")
    private String properties_key;
    @SerializedName("arguments")
    private Arguments arguments;
    //private List<Arguments> String arguments;

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getVhost() {
        return vhost;
    }

    public String getProperties_key() {
        return properties_key;
    }

    public void setProperties_key(String properties_key) {
        this.properties_key = properties_key;
    }

    public String getRouting_key() {
        return routing_key;
    }

    public void setRouting_key(String routing_key) {
        this.routing_key = routing_key;
    }

    public void setDestination_type(String destination_type) {
        this.destination_type = destination_type;
    }

    public String getDestination_type() {
        return destination_type;
    }

    public /*List<Arguments>*/ Arguments getArguments() {
        return arguments;
    }

    public void setArguments(/*List<Arguments>*/Arguments arguments) {
        this.arguments = arguments;
    }

//https://www.guj.com.br/t/problema-com-jsonarray/140084/2
    
} 