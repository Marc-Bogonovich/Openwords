package openwords_data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Archie
 */
public class MySQLAccess { 
    private Connection connect = null;
    private Statement statement = null;
    final private String table = "english_languages"; 
    String insertCommand;
    
    public void connectDatabase() throws Exception {    
        Class.forName("com.mysql.jdbc.Driver");
        this.connect = DriverManager.getConnection("jdbc:mysql://localhost/wiktionary_raw?" + "useUnicode=true&characterEncoding=UTF-8&user=root&password=root");
        this.statement = this.connect.createStatement();        
    }
    
    public void writeDatabase(int id, int L1id, String L1Word, String grammerType, int defID, String meaning, String lang, String L2Word, String tr ) throws Exception {
        L2Word = reviseString.escapeSingleQuote(L2Word);
        tr = reviseString.escapeSingleQuote(tr);
        meaning = reviseString.escapeSingleQuote(meaning);
        insertCommand = "insert into wiktionary_raw."+ table + " values(" + id + ", " + L1id + ", '" + L1Word + "', '" + grammerType + "', " + defID +", '" + meaning + "', '" + lang + "', '" + L2Word + "', '" + tr + "')";
        this.statement.executeUpdate(insertCommand);
    }
    
    public void close() throws Exception {
        if(this.statement != null && this.connect != null) {
            try {
                this.statement.close();
                this.connect.close();
            }catch(SQLException e) {
                
            }             
        }         
    }
}
    
    