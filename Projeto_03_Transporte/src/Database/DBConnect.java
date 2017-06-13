package Database;

import java.sql.*;
import javax.swing.JOptionPane;

public class DBConnect {
    // Classe de Conexão MariaDB
    // Detalhes da Conexão com o Banco de Dados
    final private String url = "jdbc:mariadb://localhost:3306/Projeto_03_Transporte";
    final private String driver = "org.mariadb.jdbc.Driver";
    final private String user = "root";
    final private String password = "";
    private Connection connection;
    public Statement statement;
    public ResultSet resultset;   
    
    // Método para criar uma conexão com o banco
    public boolean connect(){     
        boolean result = true;
            try {                
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);                
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
    /*
    // Classe de Conexão SQL Server
    static String connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Projeto_03_Transporte;integratedSecurity=true;";        
    public static Connection getConnection(){        
        Connection con = null;    
        // stabilishes the connection
            
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");                    
            con = DriverManager.getConnection(connectionURL);                     
        }
        catch(Exception e){            
            JOptionPane.showMessageDialog(null, "O banco de dados está fora do ar");            
            e.printStackTrace();        
        }        
        return con;    
    }
    */
}
