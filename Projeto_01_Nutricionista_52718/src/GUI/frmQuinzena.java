/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;
import javax.swing.ImageIcon;
// Import JFreeChart - Criador de graficos
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;



/**
 *
 * @author Rodrigo
 */
public class frmQuinzena extends javax.swing.JFrame {
    
        Statement stat = null;
        ResultSet rs = null;                
        Connection con = null;
        
        private void CarregaMes(){
        // Esse método carrega os meses da tabela Mes
            try{
                con = DBConnection.SQL.getConnection();
                String sql = "select * from Mes";
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
            
                while(rs.next()){
                    String mes = rs.getString("nome_mes").trim();
                    comboBox_Mes.addItem(mes);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        private void CarregaNomesPacientes(){
        // Carregar nomes dos Pacientes
            try {
                con = DBConnection.SQL.getConnection();
                String sql = "select * from Paciente";
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
            
                while(rs.next()){
                    String nome = rs.getString("nome_paciente").trim();
                    comboBox_nome_paciente.addItem(nome);
                }
            }
            catch (Exception e){
            e.printStackTrace();
            }
        }
        private int getPacienteID(){
        // Esse método verifica qual paciente foi selecionado e retorna o ID do mesmo
        int id_paciente=0;
            try {
                con = DBConnection.SQL.getConnection();
                String sql = ("select id_paciente from Paciente where nome_paciente like '"+comboBox_nome_paciente.getSelectedItem()+"'");
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
                while(rs.next()){
                    id_paciente = Integer.parseInt(rs.getString("id_paciente"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return id_paciente;
        }
        private int getMesID(){
        // Esse método verifica qual mes foi selecionado e retorna o ID do mesmo
            int id_mes=0;
            try {
                con = DBConnection.SQL.getConnection();
                String sql = ("select id_mes from Mes where nome_Mes like '"+comboBox_Mes.getSelectedItem()+"'");
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
                while(rs.next()){
                    id_mes = Integer.parseInt(rs.getString("id_mes"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return id_mes;
        }
        private void MostrarVariacaoMensal(){
        // Esse método verifica qual paciente foi selecionado pela combo box e busca as informçãoes dele no Banco de dados
            try {
                con = DBConnection.SQL.getConnection();
                String sql =("select Paciente.nome_paciente, Pesagem.peso_inicio, Pesagem.peso_quinzena, Mes.nome_mes, Pesagem.peso_inicio-Pesagem.peso_quinzena as 'diferenca' "+
                             " from Pesagem join Paciente on Paciente.id_paciente=Pesagem.id_paciente join Mes on Mes.id_mes=Pesagem.id_mes "+
                             " where Paciente.id_paciente = "+getPacienteID()+" and Mes.id_mes = "+getMesID());
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
                while(rs.next()){
                    String resultado=("Paciente: "+rs.getString(1)+" Peso Inicial: "+rs.getString(2)+" KG  Peso Quinzena: "+rs.getString(3)+" KG  Mês: "+rs.getString(4)+" Diferença: "+rs.getString(5)+" KG");
                    lblResult.setText(resultado);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        
    }
        
        private void criaGraficoPizza(){
        // Esse método cria um grafico usando a library Jfreechart   
            try{
                con = DBConnection.SQL.getConnection();
                String sql = "select * from Mes";
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
                DefaultPieDataset dataset = new DefaultPieDataset();
                
                while(rs.next()){
                    // Carrega os dados do Banco no grafico
                    dataset.setValue(rs.getString("nome_mes"),Integer.parseInt(rs.getString("id_mes")));
                }
                
                // Monta o grafico
            JFreeChart chart = ChartFactory.createPieChart(
            "Meses",  // chart title           
            dataset,         // data           
            true,            // include legend          
            true,           
            false);
            
            int largura = 300;
            int altura = 300;
            File graficoPizza = new File("grafico.jpeg");
            ChartUtilities.saveChartAsJPEG(graficoPizza, chart, largura, altura);
            
            lblGrafico.setIcon(new ImageIcon("./grafico.jpeg"));
            
            }
            catch (Exception e){
                e.printStackTrace();
            }
            
            
        }
        
        private void criaGraficoLinha(){
        // Esse método cria um grafico em linha usando a library Jfreechart 
            try{
                con = DBConnection.SQL.getConnection();
                String sql = ("select Paciente.nome_paciente, Pesagem.peso_inicio, Mes.nome_mes "+
                            "from Pesagem join Paciente on Paciente.id_paciente=Pesagem.id_paciente join Mes on Mes.id_mes=Pesagem.id_mes "+
                            "where Paciente.id_paciente = "+getPacienteID());
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                
                while(rs.next()){
                    // Carrega os dados do Banco no grafico
                    dataset.addValue(Double.parseDouble(rs.getString("peso_inicio")),rs.getString("nome_paciente"),rs.getString("nome_mes"));
                }
                
                // Monta o grafico
                JFreeChart graficoObjeto = ChartFactory.createLineChart(
                "Pesagem","Meses do Ano",
                "Peso em KG",
                dataset,PlotOrientation.VERTICAL,true,true,false);
            
                int largura = lblGrafico.getWidth();
                int altura = lblGrafico.getHeight();
            
                File graficoLinha = new File("graficoLinha.jpeg");
                ChartUtilities.saveChartAsJPEG(graficoLinha, graficoObjeto, largura, altura);            
                lblGrafico.setIcon(new ImageIcon("./graficoLinha.jpeg"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        
    /**
     * Creates new form frmVMensal
     */
public frmQuinzena() {
        initComponents();
        
        CarregaMes();
        CarregaNomesPacientes();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblVMensalTitle = new javax.swing.JLabel();
        panelVMensal = new javax.swing.JPanel();
        btnShow = new javax.swing.JButton();
        comboBox_nome_paciente = new javax.swing.JComboBox<>();
        lblPacientes = new javax.swing.JLabel();
        checkBox_ano_todo = new javax.swing.JCheckBox();
        comboBox_Mes = new javax.swing.JComboBox<>();
        lblResultado = new javax.swing.JLabel();
        lblResult = new javax.swing.JLabel();
        lblMes = new javax.swing.JLabel();
        lblGrafico = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Variação Mensal de Peso");
        setResizable(false);

        lblVMensalTitle.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblVMensalTitle.setText("Variação Quinzenal de Peso");

        btnShow.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnShow.setText("Mostrar Variação de Peso");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        comboBox_nome_paciente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblPacientes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPacientes.setText("Pacientes");

        checkBox_ano_todo.setText("Todos os meses juntos");
        checkBox_ano_todo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_ano_todoActionPerformed(evt);
            }
        });

        comboBox_Mes.setToolTipText("");

        lblMes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblMes.setText("Mês");

        btnMenu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVMensalLayout = new javax.swing.GroupLayout(panelVMensal);
        panelVMensal.setLayout(panelVMensalLayout);
        panelVMensalLayout.setHorizontalGroup(
            panelVMensalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVMensalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVMensalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblResult)
                    .addComponent(lblResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelVMensalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelVMensalLayout.createSequentialGroup()
                            .addComponent(lblPacientes)
                            .addGap(18, 18, 18)
                            .addComponent(comboBox_nome_paciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(89, 89, 89)
                            .addComponent(lblMes)
                            .addGap(18, 18, 18)
                            .addComponent(comboBox_Mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(52, 52, 52)
                            .addComponent(checkBox_ano_todo)
                            .addGap(18, 18, 18)
                            .addComponent(btnShow)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelVMensalLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(lblGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panelVMensalLayout.setVerticalGroup(
            panelVMensalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVMensalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVMensalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBox_nome_paciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPacientes)
                    .addComponent(comboBox_Mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMes)
                    .addComponent(checkBox_ano_todo)
                    .addComponent(btnShow)
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(lblResult)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(panelVMensal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(352, 352, 352)
                        .addComponent(lblVMensalTitle)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblVMensalTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelVMensal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        // Esse botão fecha o frame frmVMensal e volta ao frame principal, frmMain
        this.dispose();
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        // TODO add your handling code here:
        // Esse botão vai exibir os dados do paciente, como nome e o peso em cada mes
        criaGraficoLinha();
        MostrarVariacaoMensal();
    }//GEN-LAST:event_btnShowActionPerformed

    private void checkBox_ano_todoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_ano_todoActionPerformed
        // TODO add your handling code here:
        // Clicar nessa check Box, desabilita a combo Box mes, ou seja, mostra os resultados do ano todo
        if(checkBox_ano_todo.isSelected())
            comboBox_Mes.setEnabled(false);
        else{
            comboBox_Mes.setEnabled(true);
        }
    }//GEN-LAST:event_checkBox_ano_todoActionPerformed

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
            java.util.logging.Logger.getLogger(frmVMensal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVMensal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVMensal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVMensal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVMensal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnShow;
    private javax.swing.JCheckBox checkBox_ano_todo;
    private javax.swing.JComboBox<String> comboBox_Mes;
    private javax.swing.JComboBox<String> comboBox_nome_paciente;
    private javax.swing.JLabel lblGrafico;
    private javax.swing.JLabel lblMes;
    private javax.swing.JLabel lblPacientes;
    private javax.swing.JLabel lblResult;
    private javax.swing.JLabel lblResultado;
    private javax.swing.JLabel lblVMensalTitle;
    private javax.swing.JPanel panelVMensal;
    // End of variables declaration//GEN-END:variables
}
