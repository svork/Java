package GUI;

import java.sql.*; // Biblioteca SQL
import javax.swing.*; // Biblioteca para Interface Gráfica
import Database.DBConnect; // Classe de Conexão
import java.awt.Image; // Biblioteca para usar imagens
import javax.swing.ImageIcon; // Biblioteca para usar imagens

public class frmCaminhao extends javax.swing.JFrame {

    // Instancia da classe de Conexão
    DBConnect connection;
    
    // controlar erro de navegacao nos botoes proximo e anterior
    int navega = 0;
    
    // select principal
    String SQL = "select * from caminhao";
        
    // Construtor   
    public frmCaminhao(){
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
            JOptionPane.showMessageDialog(null,"Erro ao acessar dados\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
        }
    }     
        
    // Método exibirDados - mostra os dados na tela
    public void exibirDados(){                
        try{
            lblId.setText(connection.resultset.getString(1)); // Código
            txtPlaca.setText(connection.resultset.getString(2)); // Placa
            txtFabricante.setText(connection.resultset.getString(3)); // Fabricante
            txtAno.setText(connection.resultset.getString(4)); // Ano
            txtTipo.setText(connection.resultset.getString(5)); // Tipo
            txtCarga.setText(connection.resultset.getString(6)); // Carga
            txtCapacidade.setText(connection.resultset.getString(7)); // Capacidade
            txtEixos.setText(connection.resultset.getString(8)); // Eixos
            // Foto 
            lblFoto.setIcon(new ImageIcon(new ImageIcon("./fotos/" + connection.resultset.getString(9)).getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(),Image.SCALE_DEFAULT)));
        }
        catch (SQLException e){            
            if (navega == 1){ 
                JOptionPane.showMessageDialog(null,"Erro!\nVocê já está no primeiro registro.\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);                
            }
            else if (navega == 2){                
                JOptionPane.showMessageDialog(null,"Erro!\nVocê já está no último registro.\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);                
            }
            else {                
                JOptionPane.showMessageDialog(null,"Erro ao acessar dados\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);
                navega = 0;
            }
        }
    }
    
    // Método excluir - apaga um registro
    public void excluirCaminhao(){
        try {
            // Busca no banco o registro do caminhão atual
            connection.executeSQL("select * from caminhao where id_caminhao = " + lblId.getText());
            connection.resultset.first();
            
            // Mensagem ao usuário
            String caminhao = "Tem certeza que deseja excluir o caminhão?\n        " + connection.resultset.getString(3) + "\n        Placa " + connection.resultset.getString(2);
            
            // Verifica se o usuário clicou no SIM e deleta o caminhao, se não, faz NADA
            if (JOptionPane.showConfirmDialog(null, caminhao, "Excluir caminhão?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                
                // Exclui o caminhao e se der certo, exibe uma mensagem ao usuário
                if (connection.statement.executeUpdate("delete from caminhao where id_caminhao = " + lblId.getText()) == 1){
                    JOptionPane.showMessageDialog(null, "O caminhão foi excluído com sucesso.");
                    
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
    
    // Método criar - cria um novo registro
    public void salvarCaminhao(){    
        try{
            // Guardar informações da tela em variáveis            
            String placa = txtPlaca.getText();
            String fabricante = txtFabricante.getText();
            int ano = Integer.parseInt(txtAno.getText());
            String tipo = txtTipo.getText();
            String carga = txtCarga.getText();
            double capacidade = Double.parseDouble(txtCapacidade.getText());
            int eixos = Integer.parseInt(txtEixos.getText());
            
            // Insert no banco
            String insertSQL = "insert into caminhao (placa_caminhao, fabricante_caminhao, ano_caminhao, tipo_caminhao, carga_caminhao, capacidade_caminhao, eixos_caminhao, foto_caminhao) " +
                               "values ('"+placa+"', '"+fabricante+"', "+ano+", '"+tipo+"', '"+carga+"', "+capacidade+", "+eixos+", '1.jpg')";            
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
    
    // Método salvar - salvar os dados do registro
    public void alterarCaminhao(){
        try{
            // Guardar informações da tela em variáveis
            int id = Integer.parseInt(lblId.getText());
            String placa = txtPlaca.getText();
            String fabricante = txtFabricante.getText();
            int ano = Integer.parseInt(txtAno.getText());
            String tipo = txtTipo.getText();
            String carga = txtCarga.getText();
            double capacidade = Double.parseDouble(txtCapacidade.getText());
            int eixos = Integer.parseInt(txtEixos.getText());
            
            // update no banco
            String updateSQL = "update caminhao set placa_caminhao = '"+placa+"', fabricante_caminhao = '"+fabricante+"', ano_caminhao = "+ano+", tipo_caminhao = '"+tipo+"', carga_caminhao = '"+carga+"', capacidade_caminhao = "+capacidade+", eixos_caminhao = "+eixos+" where id_caminhao = " + id;
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelCaminhao = new javax.swing.JPanel();
        btnVoltar = new javax.swing.JButton();
        lblFoto = new javax.swing.JLabel();
        btnPrimeiro = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnProximo = new javax.swing.JButton();
        btnUltimo = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        painelDadosCaminhao = new javax.swing.JPanel();
        lblNome = new javax.swing.JLabel();
        lblPlaca = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        lblAno = new javax.swing.JLabel();
        txtAno = new javax.swing.JTextField();
        lblFabricante = new javax.swing.JLabel();
        txtFabricante = new javax.swing.JTextField();
        lblTipo_Caminhao = new javax.swing.JLabel();
        txtTipo = new javax.swing.JTextField();
        lblTipo_Carga = new javax.swing.JLabel();
        txtCarga = new javax.swing.JTextField();
        lblPeso_Suportado = new javax.swing.JLabel();
        txtCapacidade = new javax.swing.JTextField();
        lblCapacidade = new javax.swing.JLabel();
        lblEixos = new javax.swing.JLabel();
        txtEixos = new javax.swing.JTextField();
        lblId = new javax.swing.JLabel();
        btnAlterar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        painelCaminhao.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Caminhões", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 36))); // NOI18N

        btnVoltar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        lblFoto.setFont(new java.awt.Font("Tahoma", 0, 5)); // NOI18N
        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setName(""); // NOI18N

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

        btnExcluir.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnNovo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnNovo.setText("Salvar");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        painelDadosCaminhao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNome.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNome.setText("Código do Veículo: ");

        lblPlaca.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPlaca.setText("Placa do veículo: ");

        txtPlaca.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblAno.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblAno.setText("Ano do Veículo: ");

        txtAno.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblFabricante.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblFabricante.setText("Fabricante: ");

        txtFabricante.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblTipo_Caminhao.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTipo_Caminhao.setText("Tipo de Caminhão: ");

        txtTipo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblTipo_Carga.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTipo_Carga.setText("Tipo de Carga: ");

        txtCarga.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblPeso_Suportado.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPeso_Suportado.setText("Capacidade Máxima: ");

        txtCapacidade.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblCapacidade.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCapacidade.setText("KG ou Litros");

        lblEixos.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblEixos.setText("Número de Eixos: ");

        txtEixos.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        lblId.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblId.setText(".........");

        javax.swing.GroupLayout painelDadosCaminhaoLayout = new javax.swing.GroupLayout(painelDadosCaminhao);
        painelDadosCaminhao.setLayout(painelDadosCaminhaoLayout);
        painelDadosCaminhaoLayout.setHorizontalGroup(
            painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                        .addComponent(lblNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblId))
                    .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                        .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                                .addComponent(lblPeso_Suportado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCapacidade, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, painelDadosCaminhaoLayout.createSequentialGroup()
                                    .addComponent(lblPlaca)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtPlaca))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, painelDadosCaminhaoLayout.createSequentialGroup()
                                    .addComponent(lblTipo_Caminhao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                                .addComponent(lblAno)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblFabricante))
                            .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                                .addComponent(lblCapacidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblEixos))
                            .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                                .addComponent(lblTipo_Carga)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEixos, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFabricante, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        painelDadosCaminhaoLayout.setVerticalGroup(
            painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelDadosCaminhaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(lblId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlaca)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAno)
                    .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFabricante)
                    .addComponent(txtFabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCarga, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTipo_Caminhao)
                        .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTipo_Carga)))
                .addGap(18, 18, 18)
                .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCapacidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPeso_Suportado))
                    .addGroup(painelDadosCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCapacidade)
                        .addComponent(lblEixos)
                        .addComponent(txtEixos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );

        btnAlterar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelCaminhaoLayout = new javax.swing.GroupLayout(painelCaminhao);
        painelCaminhao.setLayout(painelCaminhaoLayout);
        painelCaminhaoLayout.setHorizontalGroup(
            painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelCaminhaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelCaminhaoLayout.createSequentialGroup()
                        .addComponent(painelDadosCaminhao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(painelCaminhaoLayout.createSequentialGroup()
                        .addGroup(painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(painelCaminhaoLayout.createSequentialGroup()
                                .addComponent(btnPrimeiro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAnterior)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnProximo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUltimo)
                                .addGap(72, 72, 72)
                                .addComponent(btnNovo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAlterar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExcluir))
                            .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVoltar)))
                .addContainerGap())
        );
        painelCaminhaoLayout.setVerticalGroup(
            painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelCaminhaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelDadosCaminhao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelCaminhaoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPrimeiro, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(btnAnterior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProximo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelCaminhaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnExcluir))
                            .addComponent(btnUltimo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelCaminhaoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
                        .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelCaminhao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(painelCaminhao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            JOptionPane.showMessageDialog(null,"Erro ao acessar primeiro registro!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
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
            JOptionPane.showMessageDialog(null,"Erro ao acessar o registro anterior!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
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
            JOptionPane.showMessageDialog(null,"Erro ao acessar próximo resgistro!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
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
            JOptionPane.showMessageDialog(null,"Erro ao acessar o último registro!\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
        }      
    }//GEN-LAST:event_btnUltimoActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // Esse botão exclui o registro atual
        excluirCaminhao();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        // Esse botão cria um novo registro
        salvarCaminhao();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // Esse botão salva os dados do registro
        alterarCaminhao();
    }//GEN-LAST:event_btnAlterarActionPerformed

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
            java.util.logging.Logger.getLogger(frmCaminhao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmCaminhao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmCaminhao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmCaminhao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmCaminhao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnUltimo;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel lblAno;
    private javax.swing.JLabel lblCapacidade;
    private javax.swing.JLabel lblEixos;
    private javax.swing.JLabel lblFabricante;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPeso_Suportado;
    private javax.swing.JLabel lblPlaca;
    private javax.swing.JLabel lblTipo_Caminhao;
    private javax.swing.JLabel lblTipo_Carga;
    private javax.swing.JPanel painelCaminhao;
    private javax.swing.JPanel painelDadosCaminhao;
    private javax.swing.JTextField txtAno;
    private javax.swing.JTextField txtCapacidade;
    private javax.swing.JTextField txtCarga;
    private javax.swing.JTextField txtEixos;
    private javax.swing.JTextField txtFabricante;
    private javax.swing.JTextField txtPlaca;
    private javax.swing.JTextField txtTipo;
    // End of variables declaration//GEN-END:variables
}
