package GoogleAPI;

import java.io.*; // Biblioteca para usar funções de I/O, ou entrada e saída de dados
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL; // Biblioteca para usar uma URL da Internet

public class Mapa {
    // Atributos
    private final String api_key = "AIzaSyDiLv_P9a6ePQvUxuR3lbYmJEHm1ap0bi8"; // Chave do Google
    private String origem; // Endereço de Origem
    private String destino; // Endereço de Destino
    private final String url; // URL com os dados
    
    // Construtor
    public Mapa(String origem, String destino){
        this.origem = origem;
        this.destino = destino;
        
        // URL que vai ser passada ao Google
        this.url = "https://maps.googleapis.com/maps/api/staticmap?center=" + getOrigem() + "&size=640x480&path=color:red|" + getOrigem() + "|" + getDestino() + "&markers=color:red%7Clabel:Origem%7C" + getOrigem() + "&markers=color:red%7Clabel:Destino%7C" + getDestino() + "&key=" + getAPIKey();
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
    
    // Método baixa uma imagem da web com os detalhes do mapa e salva em um arquivo PNG
    public void baixarMapa()throws IOException, URISyntaxException{
        try{
            String enderecoMapa = getUrl(); // Endereço do Mapa contém espaços, eles tem que ser removidos
            URL site = new URL(enderecoMapa.replace(" ", "+")); // Objeto site recebe como atributo o link para o mapa, agora sem espaços
            InputStream entrada = new BufferedInputStream(site.openStream()); // Objeto entrada recebe os dados do objeto site
            ByteArrayOutputStream saida = new ByteArrayOutputStream(); // Objeto saida grava os dados no arquivo que foi criado            

            byte[] buffer = new byte[1024]; // Vetor buffer para receber os bytes da imagem do mapa
            int tamanho = 0; // Variável tamanho para controlar o vetor buffer

            while ((tamanho = entrada.read(buffer)) != -1) { // Ler os dados até encontrar o final do arquivo, ou seja byte = -1
                saida.write(buffer, 0, tamanho); // Objeto saida grava os dados lidos
            }

            saida.close(); // Encerra o objeto saida
            entrada.close(); // Encerra o objeto entrada

            byte[] dados = saida.toByteArray(); // Vetor dados recebe o objeto saida, convertido em um vetor
            
            FileOutputStream salvarImagem = new FileOutputStream("mapa.png"); // Objeto salvarImagem recebe como atributo o nome do arquivo
            salvarImagem.write(dados); // Objeto salvarImagem recebe o vetor dados
            salvarImagem.close(); // Encerra o objeto salvarImagem   
        }
        catch (IOException e) {
            e.printStackTrace(); // Exibe o erro no console            
        }
    }   
}