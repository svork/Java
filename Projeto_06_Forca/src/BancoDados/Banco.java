package BancoDados;

import java.sql.*;
import javax.swing.JOptionPane;

public class Banco {
    
    // Detalhes da Conexão com o Banco de Dados
    final private String url = "jdbc:sqlserver://localhost:1433;databaseName=Forca;integratedSecurity=true;";
    final private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";    
    private Connection connection;
    public Statement statement;
    public ResultSet resultset;   
    
    // Método para criar uma conexão com o banco
    public boolean connect(){     
        boolean result = true;
            try {                
                Class.forName(driver);
                connection = DriverManager.getConnection(url);                
            }
            catch(ClassNotFoundException e){                
                JOptionPane.showMessageDialog(null, "Erro! Driver de conexão não foi encontrando\n" + e);
                result = false;                
            }
            catch (SQLException e){                
                JOptionPane.showMessageDialog(null, "Erro na conexão com o banco de dados\n" + e);
                result = false;                
            }            
        return(result);        
    }    
    
    // Método para fechar uma conexão com o banco
    public void disconnect(){        
        boolean result = true;        
        try {            
            connection.close();                       
        }        
        catch(SQLException SQLerror){
            JOptionPane.showMessageDialog(null,"Erro ao fechar a conexão com o banco de dados\n"+ SQLerror.getMessage());
            result = false;            
        }       
    }    
    // Método para executar um comando SQL
    public void executeSQL(String sql){     
        try {        
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultset = statement.executeQuery(sql);        
        }
        catch (SQLException sqlex){            
            JOptionPane.showMessageDialog(null,"Erro ao executar o commando SQL: "+ sqlex,"Erro",JOptionPane.ERROR_MESSAGE);            
        }        
    } 
}
