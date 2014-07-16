/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package openwords_data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public void writeDatabase(int wordID, String L1Word, String grammerType, int defID, String meaning, String lang, String L2Word, String tr ) throws Exception {
        L2Word = reviseString.escapeSingleQuote(L2Word);
        tr = reviseString.escapeSingleQuote(tr);
        meaning = reviseString.escapeSingleQuote(meaning);
        insertCommand = "insert into wiktionary_raw."+ table + " values(" + wordID + ", '" + L1Word + "', '" + grammerType + "', " + defID +", '" + meaning + "', '" + lang + "', '" + L2Word + "', '" + tr + "')";
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
    
    
