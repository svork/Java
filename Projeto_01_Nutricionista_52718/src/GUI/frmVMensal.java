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
public class frmVMensal extends javax.swing.JFrame {
    
        Statement stat = null;
        ResultSet rs = null;                
        Connection con = null;
       
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
        private void criaGraficoLinha_Quinzena(){
        // Esse método cria um grafico usando a library Jfreechart
        try{
                int quinzena = 1;
                con = DBConnection.SQL.getConnection();
                String sql = ("select Paciente.nome_paciente, Pesagem.peso_inicio "+
                            "from Pesagem join Paciente on Paciente.id_paciente=Pesagem.id_paciente "+ 
                            "where Paciente.id_paciente = "+getPacienteID()+
                            " union select Paciente.nome_paciente, Pesagem.peso_quinzena "+
                            "from Pesagem join Paciente on Paciente.id_paciente=Pesagem.id_paciente "+
                            "where Paciente.id_paciente = "+getPacienteID());
                stat = con.createStatement();
                rs = stat.executeQuery(sql);
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                
                while(rs.next()){
                    // Carrega os dados do Banco no grafico
                    dataset.addValue(Double.parseDouble(rs.getString("peso_inicio")),rs.getString("nome_paciente"),String.valueOf(quinzena));
                    quinzena++;
                }
                
                // Monta o grafico
                JFreeChart graficoObjeto = ChartFactory.createLineChart(
                "Pesagem","Quinzenas",
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
    public frmVMensal() {
        initComponents();
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
        lblResultado = new javax.swing.JLabel();
        lblResult = new javax.swing.JLabel();
        lblGrafico = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();
        btn_show2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Variação Mensal de Peso");
        setResizable(false);

        lblVMensalTitle.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblVMensalTitle.setText("Variação de Peso");

        btnShow.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnShow.setText("Mensal");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        comboBox_nome_paciente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblPacientes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPacientes.setText("Pacientes:");

        btnMenu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        btn_show2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btn_show2.setText("Quinzena");
        btn_show2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_show2ActionPerformed(evt);
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
                            .addGap(114, 114, 114)
                            .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)
                            .addComponent(btn_show2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShow, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_show2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
                        .addGap(439, 439, 439)
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
    }//GEN-LAST:event_btnShowActionPerformed

    private void btn_show2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_show2ActionPerformed
        // TODO add your handling code here:
        // Esse botão exibe a variação de peso por quinzena
        criaGraficoLinha_Quinzena();
    }//GEN-LAST:event_btn_show2ActionPerformed

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
    private javax.swing.JButton btn_show2;
    private javax.swing.JComboBox<String> comboBox_nome_paciente;
    private javax.swing.JLabel lblGrafico;
    private javax.swing.JLabel lblPacientes;
    private javax.swing.JLabel lblResult;
    private javax.swing.JLabel lblResultado;
    private javax.swing.JLabel lblVMensalTitle;
    private javax.swing.JPanel panelVMensal;
    // End of variables declaration//GEN-END:variables
}
