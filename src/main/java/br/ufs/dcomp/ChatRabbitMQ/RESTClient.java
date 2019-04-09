//Alunos: 
//        Ivisson de Jesus Silva
//        Felipe Meneses dos Santos
package br.ufs.dcomp.ChatRabbitMQ;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RESTClient {
 
 public void listarGrupos(){
        try {
            // JAVA 8 como pré-requisito (ver README.md)
            Rest rest = new Rest();
            
            String username = "ivissonijs";
            String password = "132435";
     
            String usernameAndPassword = username + ":" + password;
            String authorizationHeaderName = "Authorization";
            String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
     
            // Perform a request
            String restResource = "http://100.24.58.45:15672/";
            Client client = ClientBuilder.newClient();
            Response resposta = client.target( restResource )
            	//.path("/api/exchanges/iagffzqu/ufs/bindings/source") // lista todos os binds que tem "ufs" como source	
                //.path("/api/exchanges/%2f/#amigos/bindings/source")
                .path("/api/exchanges/%2f")
            	.request(MediaType.APPLICATION_JSON)
                .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
                .get();     // Perform a post with the form values
           
            if (resposta.getStatus() == 200) {
            	String json = resposta.readEntity(String.class);
                System.out.println(json);
                
                Gson gson = new Gson();
                List<Rest> listaGrupo = gson.fromJson(json, new TypeToken<List<Rest>>() {}.getType());
				
				 for(Rest RestRetorno: listaGrupo){
                    System.out.println(RestRetorno.getSource());
                }
				
              
            }    
		} catch (Exception e) {
			e.printStackTrace();
		}
 }

public void listarUsuarios(String grupo){
        try {
            // JAVA 8 como pré-requisito (ver README.md)
            Rest rest = new Rest();
            String[] info = grupo.split(" ");
            String nomeGrupo = "#" +grupo;            
            
            String username = "ivissonijs";
            String password = "132435";
     
            String usernameAndPassword = username + ":" + password;
            String authorizationHeaderName = "Authorization";
            String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
            
            // Perform a request
            String restResource = "http://100.24.58.45:15672/";
            Client client = ClientBuilder.newClient();
            Response resposta = client.target( restResource )
            	//.path("/api/exchanges/iagffzqu/ufs/bindings/source") // lista todos os binds que tem "ufs" como source	
                .path("/api/exchanges/%2f/"+ nomeGrupo +"/bindings/source")
            	.request(MediaType.APPLICATION_JSON)
                .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
                .get();     // Perform a post with the form values
           
            if (resposta.getStatus() == 200) {
            	String json = resposta.readEntity(String.class);
                
                Gson gson = new Gson();
                List<Rest> listaUsuario = gson.fromJson(json, new TypeToken<List<Rest>>() {}.getType());
				
				 for(Rest RestRetorno: listaUsuario){
                    System.out.println(RestRetorno.getDestination());
                }
				
            }    
		} catch (Exception e) {
			e.printStackTrace();
		}
 


}
}
 
 

