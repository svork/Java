package GoogleAPI;

import okhttp3.*; // Biblioteca para usar URL
import org.json.*; // Biblioteca para usar objetos JSON
import java.io.IOException; // Erro de IO

public class Distancia {    
    
    // Atributos
    private final String api_key = "AIzaSyDiLv_P9a6ePQvUxuR3lbYmJEHm1ap0bi8"; // Chave do Google
    private String origem; // Endereço de Origem
    private String destino; // Endereço de Destino
    private final String url; // URL com os dados
    
    // Construtor
    public Distancia(String origem, String destino){
        this.origem = origem;
        this.destino = destino;
        
        // URL que vai ser passada ao Google
        this.url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + getOrigem() + "&destinations=" + getDestino() + "&language=pt-BR&key=" + getAPIKey();
    }
    
    // Gets e Sets
    public void setOrigem(String origem){
        this.origem = origem;
    }
    public String getOrigem(){
        return this.origem;
    }
    public void setDestino(String destino){
        this.destino = destino;
    }
    public String getDestino(){
        return this.destino;
    } 
    public String getAPIKey(){
        return this.api_key;
    }
    public String getUrl(){
        return this.url;
    }   
        
    // Objeto para usar URL
    OkHttpClient cliente = new OkHttpClient();    
  
    // Método para buscar no Google as informações
    public String buscarDados() throws IOException {
        Request requisicao = new Request.Builder().url(getUrl()).build(); // Monta a URL com a origem, destino e API_KEY
        Response resposta = cliente.newCall(requisicao).execute(); // Recebe a resposta da Web 
        return resposta.body().string(); // Retorna uma string com os dados no formato JSON
  }
    
    // Método recebe um JSON e retorna o tempo de viagem como uma String
    public String getTempoViagem(String dados){        
        JSONObject json = new JSONObject(dados); // Objeto JSON recebe os dados do Google
        JSONObject rows = json.getJSONArray("rows").getJSONObject(0); // Vetor ROWS do objeto JSON
        JSONObject elements = rows.getJSONArray("elements").getJSONObject(0); // Vetor ELEMENTS do objeto ROWS
        JSONObject distance = elements.getJSONObject("duration"); // Objeto DURATION do objeto ELEMENTS
        return distance.getString("text"); // Atributo TEXT do objeto DISTANCE        
    }
    
    // Método recebe um JSON e retorna a distancia da viagem como uma String
    public String getDistanciaViagem(String dados){
        JSONObject json = new JSONObject(dados); // Objeto JSON recebe os dados do Google
        JSONObject rows = json.getJSONArray("rows").getJSONObject(0); // Vetor ROWS do objeto JSON
        JSONObject elements = rows.getJSONArray("elements").getJSONObject(0); // Vetor ELEMENTS do objeto ROWS
        JSONObject distance = elements.getJSONObject("distance"); // Objeto DISTANCE do objeto ELEMENTS
        return distance.getString("text"); // Atributo TEXT do objeto DISTANCE        
    }
}
