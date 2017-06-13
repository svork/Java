/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Rodrigo
 */
public class SQL {
    
    static String connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Projeto_01_Nutricionista_52718;integratedSecurity=true;";
        
    public static Connection getConnection(){
        
        Connection con = null;
    
        // stabilishes the connection
            
        try {
                        
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    
            con = DriverManager.getConnection(connectionURL);
            
            //JOptionPane.showMessageDialog(null, "O banco de dados está funcionando");
           
        }
        catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "O banco de dados está fora do ar");
            
            e.printStackTrace();
        
        }
        
        return con;
    
    }
    
}
