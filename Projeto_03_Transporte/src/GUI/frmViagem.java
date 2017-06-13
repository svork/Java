package GUI;

import java.sql.*; // Biblioteca SQL
import javax.swing.*; // Biblioteca para Interface Gráfica
import Database.DBConnect; // Classe de Conexão
import GoogleAPI.Distancia; // Calcular distância da Viagem
import GoogleAPI.Mapa; // Gera um mapa da Viagem
import java.awt.Image; // Biblioteca para usar imagens
import java.io.IOException; // Biblioteca para tratar erros de I/O, ou entrada e saída de dados

public class frmViagem extends javax.swing.JFrame {
    // Instancia da classe de Conexão
    DBConnect connection;
    
    // controlar erro de navegacao nos botoes proximo e anterior
    int navega = 0;
    
    // select principal
    String SQL = "select * from viagem";
        
    public frmViagem() {
        initComponents();

        // Criando objeto connection
        connection = new DBConnect();
        
        // Abrir conexão com o banco de dados
        connection.connect(); 
        
        // Carregar dados
        connection.executeSQL(SQL);
        try{
            // Procura o primeiro registro no banco
            connection.resultset.first();
            exibirDados();
        }
        catch (SQLException e){            
            JOptionPane.showMessageDialog(null,"Erro ao acessar dados\n" + e,"Erro",JOptionPane.ERROR_MESSAGE);            
        }
        
    }   
    
    // Método exibirDados - mostra os dados na tela
    public void exibirDados(){                
        try{
            lblId.setText(connection.resultset.getString(1)); // Código
            txt_Id_caminhao.setText(connection.resultset.getString(2)); // ID Caminhão
            txtTipo_Viagem.setText(connection.resultset.getString(3));// Tipo da viagem, ida ou volta SOMENTE!!!
            txtKm.setText(connection.resultset.getString(4)); // Quilômetros
            txtCusto.setText(connection.resultset.getString(5)); // Custo  combustível
            txtqtd.setText(connection.resultset.getString(6)); // quantidade de carga, não pode ser maior que a capacidade do caminhão
            txtTempo.setText(connection.resultset.getString(7)); // Tempo Viagem em Horas
            txtOrigem.setText(connection.resultset.getString(8)); // Origem da Viagem
            txtDestino.setText(connection.resultset.getString(9)); // Destino da Viagem
            double dinheiros = Double.parseDouble(connection.resultset.getString(4)) * Double.parseDouble(connection.resultset.getString(5));
            lblValor.setText("Custo Viagem R$ " + dinheiros); // Custo da Viagem - KM * Distância
            lblCapacidade_caminhao.setText("Carga Máxima: " + connection.resultset.getString(6)); // Carga máxima do caminhão
        }
        catch (SQLException e){            
            if (navega == 1){ 
                JOptionPane.showMessageDialog(null,"Erro!\nVocê já está no primeiro registro.\n" + e,"Erro",JOptionPane.ERROR_MESSAGE);                
            }
            else if (navega == 2){                
                JOptionPane.showMessageDialog(null,"Erro!\nVocê já está no último registro.\n" + e,"Erro",JOptionPane.ERROR_MESSAGE);                
            }
            else {                
                JOptionPane.showMessageDialog(null,"Erro ao acessar dados\n" + e,"Erro",JOptionPane.ERROR_MESSAGE);
                navega = 0;
            }
        }
    }
    
    // Método salvar - cria um novo registro
    public void salvarViagem(){
        try{
            // Guardar informações da tela em variáveis
            int id_caminhao = Integer.parseInt(txt_Id_caminhao.getText());
            String tipo_viagem = txtTipo_Viagem.getText();
            String km_viagem = txtKm.getText();
            Double custo_viagem = Double.parseDouble(txtCusto.getText());
            Double quantidade_viagem = Double.parseDouble(txtqtd.getText());
            String tempo_viagem = txtTempo.getText();   
            String origem_viagem = txtOrigem.getText();
            String destino_viagem = txtDestino.getText();
            
            // Insert no banco
            String insertSQL = "insert into viagem (id_caminhao, tipo_viagem, km_viagem, custo_viagem, quantidade_viagem, tempo_viagem, origem_viagem, destino_viagem) "+
            "values ("+id_caminhao+", '"+tipo_viagem+"', '"+km_viagem+"', "+custo_viagem+", "+quantidade_viagem+", '"+tempo_viagem+"', '"+origem_viagem+"', '"+destino_viagem+"');";            
            connection.statement.executeUpdate(insertSQL);            
            JOptionPane.showMessageDialog(null, "Informações salvas com sucesso.","Pronto",JOptionPane.OK_OPTION);
            
            // Mostra o primeiro registro novamente  
            connection.executeSQL(SQL);
            connection.resultset.first();            
            exibirDados();
        }
        catch (SQLException e){            
            JOptionPane.showMessageDialog(null, "Erro ao salvar informações!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
        }    
    }
    
    // Método alterar - alterar um registro
    public void alterarViagem(){
        try{
            // Guardar informações da tela em variáveis
            int id = Integer.parseInt(lblId.getText());
            int id_caminhao = Integer.parseInt(txt_Id_caminhao.getText());
            String tipo_viagem = txtTipo_Viagem.getText();
            String km_viagem = txtKm.getText();
            Double custo_viagem = Double.parseDouble(txtCusto.getText());
            Double quantidade_viagem = Double.parseDouble(txtqtd.getText());
            String tempo_viagem = txtTempo.getText();         
            String origem_viagem = txtOrigem.getText();
            String destino_viagem = txtDestino.getText();
            
            // Insert no banco
            String updateSQL = "update viagem set id_caminhao = "+id_caminhao+", tipo_viagem = '"+tipo_viagem+"', km_viagem = '"+km_viagem+"', custo_viagem = "+custo_viagem+", quantidade_viagem = "+quantidade_viagem+", tempo_viagem = '"+tempo_viagem+"' , origem_viagem = '"+origem_viagem+"', destino_viagem = '"+destino_viagem+"' where id_viagem = " + id;            
            connection.statement.executeUpdate(updateSQL);            
            JOptionPane.showMessageDialog(null, "Informações salvas com sucesso.","Pronto",JOptionPane.OK_OPTION);
            
            // Mostra o primeiro registro novamente  
            connection.executeSQL(SQL);
            connection.resultset.first();            
            exibirDados();
        }
        catch (SQLException e){            
            JOptionPane.showMessageDialog(null, "Erro ao salvar informações!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
        }    
    }
    
    // Método excluir - excluir um registro
    public void excluirViagem(){
        try {
            // Busca no banco o registro da viagem atual
            connection.executeSQL("select * from viagem where id_viagem = " + lblId.getText());
            connection.resultset.first();
            
            // Mensagem ao usuário
            String viagem = "Tem certeza que deseja excluir a viagem?";
            
            // Verifica se o usuário clicou no SIM e deleta o caminhao, se não, faz NADA
            if (JOptionPane.showConfirmDialog(null, viagem, "Excluir viagem?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                
                // Exclui o caminhao e se der certo, exibe uma mensagem ao usuário
                if (connection.statement.executeUpdate("delete from viagem where id_viagem = " + lblId.getText()) == 1){
                    JOptionPane.showMessageDialog(null, "O viagem foi excluída com sucesso.");
                    
                    // Mostra o primeiro registro novamente                        
                    connection.executeSQL(SQL);
                    connection.resultset.first();
                    exibirDados();
                }                
            }            
        }
        catch (SQLException e){            
            JOptionPane.showMessageDialog(null, "Erro ao excluir registro!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
        }
    }
    
    // Método google - mostra um mapa da viagem usando google maps
    public void google() throws IOException{
        try {
            // Distância e Tempo de Viagem
            Distancia dist = new Distancia(txtOrigem.getText(),txtDestino.getText()); // Instancia da classe Distancia
        
            String json = dist.buscarDados(); // Variável json recebe o retorno do metodo buscarDados()
        
            lblTime.setText(dist.getTempoViagem(json)); // Exibe a duração da Viagem
            lblDistance.setText(dist.getDistanciaViagem(json)); // Exibe a distância da Viagem
            
            // Os valores da viagem recebem os dados do Google 
            double km = Double.parseDouble(lblDistance.getText().replace(",",".").replaceFirst("km",""));            
            txtKm.setText("" + km);
            String tempo = lblTime.getText();
            txtTempo.setText(tempo);
            
            
            // Mapa
            Mapa map = new Mapa(txtOrigem.getText(),txtDestino.getText()); // Instancia da classe Mapa
            
            map.baixarMapa(); // Cria o arquivo mapa.png para ser exibido no frame
            
            // Carrega a imagem no label Mapa
            lblMapa.setIcon(new ImageIcon(new ImageIcon("mapa.png").getImage().getScaledInstance(lblMapa.getWidth(), lblMapa.getHeight(),Image.SCALE_DEFAULT)));
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Erro!\nErro ao calcular a distância da Viagem.\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);
        }
    }    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnVoltar = new javax.swing.JButton();
        btnPrimeiro = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnProximo = new javax.swing.JButton();
        btnUltimo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        lblNome = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblNome2 = new javax.swing.JLabel();
        txt_Id_caminhao = new javax.swing.JTextField();
        lblCapacidade_caminhao = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        lblDistancia = new javax.swing.JLabel();
        txtKm = new javax.swing.JTextField();
        lblCusto = new javax.swing.JLabel();
        txtCusto = new javax.swing.JTextField();
        lblTempo = new javax.swing.JLabel();
        txtTempo = new javax.swing.JTextField();
        lblValor = new javax.swing.JLabel();
        lblqtd = new javax.swing.JLabel();
        txtqtd = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnCalcularDistancia = new javax.swing.JButton();
        lblOrigem = new javax.swing.JLabel();
        txtOrigem = new javax.swing.JTextField();
        lblDestino = new javax.swing.JLabel();
        txtDestino = new javax.swing.JTextField();
        lblDistanciaViagem = new javax.swing.JLabel();
        lblDuracaoViagem = new javax.swing.JLabel();
        lblDistance = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblMapa = new javax.swing.JLabel();
        txtTipo_Viagem = new javax.swing.JTextField();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Viagens", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 36))); // NOI18N

        btnVoltar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        btnPrimeiro.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnPrimeiro.setText("Primeiro");
        btnPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeiroActionPerformed(evt);
            }
        });

        btnAnterior.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });

        btnProximo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnProximo.setText("Próximo");
        btnProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximoActionPerformed(evt);
            }
        });

        btnUltimo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnUltimo.setText("Último");
        btnUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimoActionPerformed(evt);
            }
        });

        btnSalvar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        lblNome.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNome.setText("Código da Viagem: ");

        lblId.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblId.setText("....");

        lblNome2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNome2.setText("Caminhão");

        txt_Id_caminhao.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblCapacidade_caminhao.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCapacidade_caminhao.setText("Carga Máxima");

        lblTipo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTipo.setText("Tipo de Viagem");

        lblDistancia.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDistancia.setText("Distância Total (km)");

        txtKm.setEditable(false);
        txtKm.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblCusto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCusto.setText("Custo Combustível (lts)");

        txtCusto.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblTempo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTempo.setText("Duração Viagem / Hora");

        txtTempo.setEditable(false);
        txtTempo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblValor.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblValor.setText("Custo Viagem");

        lblqtd.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblqtd.setText("Quantidade de Carga");

        txtqtd.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnCalcularDistancia.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCalcularDistancia.setText("Calcular distância");
        btnCalcularDistancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularDistanciaActionPerformed(evt);
            }
        });

        lblOrigem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblOrigem.setText("Origem");

        txtOrigem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblDestino.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDestino.setText("Destino");

        txtDestino.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblDistanciaViagem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDistanciaViagem.setText("Distância: ");

        lblDuracaoViagem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDuracaoViagem.setText("Tempo aproximado: ");

        lblDistance.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDistance.setText(".");

        lblTime.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTime.setText(".");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblOrigem)
                .addGap(18, 18, 18)
                .addComponent(txtOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDestino)
                .addGap(18, 18, 18)
                .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblDistanciaViagem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDistance))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblDuracaoViagem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTime)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCalcularDistancia, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrigem))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDestino))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDistanciaViagem)
                    .addComponent(lblDistance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDuracaoViagem)
                    .addComponent(lblTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCalcularDistancia, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMapa.setText("......");

        txtTipo_Viagem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        btnAlterar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblNome)
                                .addGap(100, 100, 100)
                                .addComponent(lblId))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(644, 644, 644)
                                    .addComponent(btnSalvar)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnAlterar)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnExcluir))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(lblMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(237, 237, 237)
                                            .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lblValor))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTempo)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblNome2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_Id_caminhao, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCapacidade_caminhao))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnPrimeiro)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnAnterior)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnProximo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUltimo))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTipo)
                                        .addComponent(lblqtd)
                                        .addComponent(lblDistancia)
                                        .addComponent(lblCusto))
                                    .addGap(56, 56, 56)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtqtd, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                        .addComponent(txtKm)
                                        .addComponent(txtCusto)
                                        .addComponent(txtTipo_Viagem)))
                                .addComponent(txtTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(162, 162, 162))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(lblId))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNome2)
                                    .addComponent(txt_Id_caminhao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCapacidade_caminhao))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblqtd)
                                    .addComponent(txtqtd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(lblTipo))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTipo_Viagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblDistancia)
                                    .addComponent(txtKm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblCusto)
                                    .addComponent(txtCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTempo)
                                    .addComponent(txtTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnPrimeiro)
                                    .addComponent(btnAnterior)
                                    .addComponent(btnProximo)
                                    .addComponent(btnUltimo)))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(309, 309, 309)
                        .addComponent(lblValor)
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSalvar)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(110, 110, 110)
                        .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1028, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        // Esse botão volta ao form Main, o principal
        new frmMain().setVisible(true);
        this.dispose();

        // Fechar a conexão com o banco
        connection.disconnect();
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeiroActionPerformed
        try{
            // Exibe o primeiro registro
            connection.executeSQL(SQL);
            connection.resultset.first();
            exibirDados();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Erro ao acessar primeiro registro!\n" + e);
        }
    }//GEN-LAST:event_btnPrimeiroActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        try{
            // Exibe o registro Anterior            
            connection.resultset.previous();
            exibirDados();
            navega = 1;
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Erro ao acessar o registro anterior!\n" + e);
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximoActionPerformed
        try{
            // Exibe o próximo registro
            connection.resultset.next();
            exibirDados();
            navega = 2;
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Erro ao acessar próximo resgistro!\n" + e);
        }
    }//GEN-LAST:event_btnProximoActionPerformed

    private void btnUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimoActionPerformed
        try{
            // Exibe o registro final
            connection.executeSQL(SQL);
            connection.resultset.last();
            exibirDados();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Erro ao acessar o último registro!\n" + e);
        }
    }//GEN-LAST:event_btnUltimoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // Esse botão cria um novo registro
        salvarViagem();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCalcularDistanciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularDistanciaActionPerformed
        try {
            // Esse botão gera um mapa da viagem usando o google maps
            google();
        }
        catch (IOException ex) {           
            JOptionPane.showMessageDialog(null,"Erro ao calcular distância!","Erro",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCalcularDistanciaActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // Método alterar - alterar um registro
        alterarViagem();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // Método excluir - excluir um registro
        excluirViagem();
    }//GEN-LAST:event_btnExcluirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmViagem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnCalcularDistancia;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnUltimo;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblCapacidade_caminhao;
    private javax.swing.JLabel lblCusto;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JLabel lblDistance;
    private javax.swing.JLabel lblDistancia;
    private javax.swing.JLabel lblDistanciaViagem;
    private javax.swing.JLabel lblDuracaoViagem;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblMapa;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblNome2;
    private javax.swing.JLabel lblOrigem;
    private javax.swing.JLabel lblTempo;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblValor;
    private javax.swing.JLabel lblqtd;
    private javax.swing.JTextField txtCusto;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtKm;
    private javax.swing.JTextField txtOrigem;
    private javax.swing.JTextField txtTempo;
    private javax.swing.JTextField txtTipo_Viagem;
    private javax.swing.JTextField txt_Id_caminhao;
    private javax.swing.JTextField txtqtd;
    // End of variables declaration//GEN-END:variables
}
