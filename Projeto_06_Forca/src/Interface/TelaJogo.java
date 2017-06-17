package Interface;

// Bibliotecas
import BancoDados.Banco;
import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.Image;
import javax.swing.ImageIcon;

public class TelaJogo extends javax.swing.JFrame {
    
    // Nivel de dificuldade do Jogo
    private String nivel;
    
    // Palavra escolhida    
    String palavra;
    
    // Dica para a palavra
    String dica;
            
    // Tentativas, a cada erro, diminui 1
    int tentativas = 7;
    
    // Variavel para controlar a posição do vetor
    int i = 0;
     
    // Instancia da classe de Conexão
    Banco connection;     
           
    // Construtor
    public TelaJogo(String nivel){
        
        initComponents();
        
        // Define o nivel do jogo, escolhido pelo usuário
        this.nivel = nivel;
        
        // Desenha a Forca
        enforcar(tentativas);
        
        // Define o foco no campo para digitar a letra
        txtLetra.requestFocus();
        
        // Esconder a palavra escolhida
        lblPalavraEscolhida.setVisible(false);
        
        // Criando objeto connection
        connection = new Banco();
        
        // Abrir conexão com o banco de dados
        connection.connect();    
        
        // Variavel Random para escolher a palavra
        int n = (int) (Math.random()*10);
        if(n == 0){
            n = 1; // N não pode ser zero
        }
        
        // Select principal
        String SQL = "select * from " + this.nivel + " where id = " + n;
        
        // Carregar dados
        connection.executeSQL(SQL);
        try{
            // Procura o primeiro registro no banco
            connection.resultset.first();
            palavra = connection.resultset.getString(2).toUpperCase();
            dica = connection.resultset.getString(3).toUpperCase();
            exibirDados();
        }
        catch (SQLException e){            
            JOptionPane.showMessageDialog(null,"Erro ao acessar dados\n" + e,"Erro!",JOptionPane.ERROR_MESSAGE);            
        } 
    }
    
    // Gets e Sets
    public void setNivel(String nivel){
        this.nivel = nivel;
    }
    public String getNivel(){
        return this.nivel;
    }
            
    // Esse método mostra as informações na tela
    public void exibirDados(){
        lblPalavraEscolhida.setText(palavra);
        lblDica.setText("Dica: " + dica); // Dica da Palavra
        lblTentativa.setText("Tentativas: " + tentativas); // Número de Tentativas
        lblNivel.setText("Nível: " + getNivel()); // Nível de Dificuldade do Jogo
        lblTamanho.setText("Tamanho: " + palavra.length()); // Tamanho da Palavra
    }
    
    // Esse método compara a letra que o usuário digitou com a palavra escolhida
    public void compara(String letra){
        
        // Limpar o texto para o usuário digitar um nova letra
        txtLetra.setText("");
        
        // Define o foco no campo para digitar a letra
        txtLetra.requestFocus();
                
        // Mostrar letras ja escolhidas                
        txtLetraErrada.setText(txtLetraErrada.getText()+ "  -  " + letra);
                
        // Compara uma letra de cada vez
        if( palavra.charAt(i) ==  letra.charAt(0)){
                        
            txtLetrasCorretas.setText(txtLetrasCorretas.getText()+letra);
            i++; // Compara a próxima letra
            
            // Verifica se o jogador ganhou o jogo
            if( palavra.length() == i ){
                // Mostrar mensagem de consolação e volta para o menu principal
                lblPalavraEscolhida.setVisible(true);
                JOptionPane.showMessageDialog(null, "Parabéns! Você ganhou! A palavra é " + palavra, "Mensagem", JOptionPane.INFORMATION_MESSAGE);
                new Menu().setVisible(true);
                this.dispose();                    
            }     
        }
        else{
            tentativas--; // Diminui uma tentativa                
                
            exibirDados(); // Mostra dados
            JOptionPane.showMessageDialog(null, "Erro! A letra " + letra + " não faz parte da palavra", "Erro", JOptionPane.ERROR_MESSAGE);
            
            // Mostrar boneco enforcado
            enforcar(tentativas);
            
            // Verifica se o jogador perdeu o jogo
            if( tentativas == 0 ){
                // Mostrar mensagem de consolação e volta para o menu principal
                JOptionPane.showMessageDialog(null, "Você perdeu! Mais sorte na próxima vez...\nA palavra era " + palavra, "Mensagem", JOptionPane.ERROR_MESSAGE);
                new Menu().setVisible(true);
                this.dispose();                    
            }                
        }                     
    }
    
    // Esse método mostra a forca na tela, a cada erro, uma nova parte do boneco e mostrada
    public void enforcar(int imagem){
        // Exibe a imagem da forca, de acordo com o numero de erros
         lblForca.setIcon(new ImageIcon(new ImageIcon("./imagens/" + imagem + ".png").getImage().getScaledInstance(lblForca.getWidth(), lblForca.getHeight(),Image.SCALE_DEFAULT)));               
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Painel = new javax.swing.JPanel();
        btnSair = new javax.swing.JButton();
        lblPalavraEscolhida = new javax.swing.JLabel();
        btnVerificar = new javax.swing.JButton();
        txtLetraErrada = new javax.swing.JTextField();
        lblLetras = new javax.swing.JLabel();
        txtLetra = new javax.swing.JTextField();
        lblNivel = new javax.swing.JLabel();
        lblTentativa = new javax.swing.JLabel();
        Forca = new javax.swing.JPanel();
        lblForca = new javax.swing.JLabel();
        lblDica = new javax.swing.JLabel();
        lblInformacao = new javax.swing.JLabel();
        lblTamanho = new javax.swing.JLabel();
        txtLetrasCorretas = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Painel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Forca", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 24))); // NOI18N

        btnSair.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        lblPalavraEscolhida.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPalavraEscolhida.setText("teste");

        btnVerificar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnVerificar.setText("Tentar");
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        txtLetraErrada.setEditable(false);
        txtLetraErrada.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtLetraErrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblLetras.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblLetras.setText("Letras");

        txtLetra.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtLetra.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblNivel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNivel.setText("Nível: ");

        lblTentativa.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTentativa.setText("Tentativas: ");

        Forca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblForca.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout ForcaLayout = new javax.swing.GroupLayout(Forca);
        Forca.setLayout(ForcaLayout);
        ForcaLayout.setHorizontalGroup(
            ForcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ForcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblForca, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addContainerGap())
        );
        ForcaLayout.setVerticalGroup(
            ForcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ForcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblForca, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblDica.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDica.setText("Dica: ");

        lblInformacao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblInformacao.setText("Digite uma letra e clique no botão");

        lblTamanho.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTamanho.setText("Tamanho: ");

        txtLetrasCorretas.setEditable(false);
        txtLetrasCorretas.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtLetrasCorretas.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout PainelLayout = new javax.swing.GroupLayout(Painel);
        Painel.setLayout(PainelLayout);
        PainelLayout.setHorizontalGroup(
            PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PainelLayout.createSequentialGroup()
                        .addComponent(lblLetras)
                        .addGap(18, 18, 18)
                        .addComponent(txtLetraErrada))
                    .addGroup(PainelLayout.createSequentialGroup()
                        .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PainelLayout.createSequentialGroup()
                                .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLetrasCorretas)
                                    .addGroup(PainelLayout.createSequentialGroup()
                                        .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblNivel)
                                            .addComponent(lblTentativa)
                                            .addComponent(lblDica)
                                            .addComponent(lblPalavraEscolhida)
                                            .addComponent(lblTamanho)
                                            .addGroup(PainelLayout.createSequentialGroup()
                                                .addComponent(txtLetra, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblInformacao)))
                                        .addGap(0, 39, Short.MAX_VALUE)))
                                .addGap(38, 38, 38))
                            .addGroup(PainelLayout.createSequentialGroup()
                                .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSair, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Forca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        PainelLayout.setVerticalGroup(
            PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLetras)
                    .addComponent(txtLetraErrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PainelLayout.createSequentialGroup()
                        .addComponent(lblNivel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTentativa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDica)
                        .addGap(7, 7, 7)
                        .addComponent(lblTamanho)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPalavraEscolhida)
                        .addGap(18, 18, 18)
                        .addComponent(txtLetrasCorretas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(PainelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLetra, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInformacao))
                        .addGap(64, 64, 64)
                        .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PainelLayout.createSequentialGroup()
                        .addComponent(Forca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(btnSair)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Painel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
       
    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        new Menu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed
        // Esse botão compara cada letra digitada com a palavra escolhida
        if (txtLetra.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Erro!   Você tem que digitar uma letra", "Erro", JOptionPane.ERROR_MESSAGE);
            // Define o foco no campo para digitar a letra
            txtLetra.requestFocus();
        }
        else{
            if(txtLetra.getText().length() > 0 && txtLetra.getText().length() < 2){
            
                compara(txtLetra.getText().toUpperCase());            
        }
            else{
                JOptionPane.showMessageDialog(null, "Erro!  Uma letra de cada vez", "Erro", JOptionPane.ERROR_MESSAGE);
                // Define o foco no campo para digitar a letra
                txtLetra.setText("");
                txtLetra.requestFocus();
            }
        }
    }//GEN-LAST:event_btnVerificarActionPerformed
    
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
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaJogo("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Forca;
    private javax.swing.JPanel Painel;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JLabel lblDica;
    private javax.swing.JLabel lblForca;
    private javax.swing.JLabel lblInformacao;
    private javax.swing.JLabel lblLetras;
    private javax.swing.JLabel lblNivel;
    private javax.swing.JLabel lblPalavraEscolhida;
    private javax.swing.JLabel lblTamanho;
    private javax.swing.JLabel lblTentativa;
    private javax.swing.JTextField txtLetra;
    private javax.swing.JTextField txtLetraErrada;
    private javax.swing.JTextField txtLetrasCorretas;
    // End of variables declaration//GEN-END:variables
}

