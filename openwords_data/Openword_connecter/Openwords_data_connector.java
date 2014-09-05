/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package openwords_data_connector;

import java.sql.*;

/**
 *
 * @author Archie
 */
public class Openwords_data_connector {
    //JDBC driver name and database url
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/oworg_owr_1_0?" + "useUnicode=true&characterEncoding=UTF-8&user=root&password=root";
    
    //Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    
    //connection and statement declaration
      
        
    public static void main(String[] args) throws SQLException {
      
        Connection conn = null;
        Statement stmt = null;
        Statement stmt_tszh = null;
        
        int lastDefID = 0;
        int lastDefNum = 0;
        int count = 0;
        int wordIDL1;
        int wordIDL2;
        int wordIDL3;
        boolean L1exist = false;
        boolean L2exist = false;
        boolean L3exist = false;
        String enWord;
        String grammerType;
        int defID;
        int rank;
        String definition = null;
        String lang;
        int lang_id = 0;
        String word;
        //For Chinese use only
        String word_simple_zh;
        String word_tradi_zh;
        
        String tr;
        ResultSet rs;
        ResultSet rs_def_num;
        try{
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            System.out.println("Connecting to the database");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connect database sucessfully");
            //create statement
            stmt = conn.createStatement();
            System.out.println("Distributing data to each table");            
            //execute a query
            String sql = "SELECT id, L1id, enWord, grammerType, defID, definition, lang, word, tr FROM english_wiktionary_raw;";
            rs = stmt.executeQuery(sql); 
              
            stmt_tszh = conn.createStatement();
            int lang_id_zht = getLanguageID("zht", stmt_tszh);
            int lang_id_zhs = getLanguageID("zhs", stmt_tszh);
            System.out.println("lang_id_zht:"+lang_id_zht);
            System.out.println("lang_id_zhs:" + lang_id_zhs);
            stmt_tszh.close();
        
            
            while(rs.next()) {  
                try{
                    ++count;
                    if(count%1000 == 0) {
                        System.out.println(count);
                    }
                    Statement stmt_insert = null;
                    stmt_insert = conn.createStatement();
                    //Extract data from result set
                    enWord = reviseString.escapeSingleQuote(rs.getString("enWord"));
                    grammerType = rs.getString("grammerType");
                    defID = rs.getInt("defID");
                    definition = reviseString.escapeSingleQuote(rs.getString("definition"));
                    lang = rs.getString("lang");
                    word = reviseString.escapeSingleQuote(rs.getString("word"));
                    tr = reviseString.escapeSingleQuote(rs.getString("tr"));
                    
                    if(defID != lastDefID) {
                        String sql_count_def = "SELECT count(*) as defNum FROM english_wiktionary_raw WHERE defID =" + defID + ";";
                        rs_def_num = stmt_insert.executeQuery(sql_count_def);
                        rs_def_num.next();
                        rank = rs_def_num.getInt("defNum");
                        lastDefID = defID;
                        lastDefNum = rank;
                    } 
                    else {
                        rank = lastDefNum;
                    }
                    
                    
                   
                    
                    //Display values
                    //System.out.print("ID: " + id);
                    //System.out.print(", enWord: " + enWord);
                    //System.out.print(", grammerType: " + grammerType);
                    //System.out.print(", defID: " + defID);
                    //System.out.print(", definition: " + definition);
                    //System.out.print(", lang: " + lang);
                    //System.out.print(", word: " + word);
                    //System.out.println(", tr: " + tr);

                    lang_id = getLanguageID(lang, stmt_insert);          
                    if(lang.equals("cmn")) {
                        //convert word to traditional Chinese and simplified Chinese
                        word_simple_zh = openwords_data_connector.convertChinese.convert("s", word);
                        word_tradi_zh = openwords_data_connector.convertChinese.convert("t", word);
                        
                        
                        //insert into tables:
                        //get L1 word and L2 word ID in words table , if not exists, insert record(s) and return ID    
                        wordIDL1 = words_lookup(enWord, stmt_insert);
                        wordIDL2 = words_lookup(word_simple_zh, stmt_insert);
                        wordIDL3 = words_lookup(word_tradi_zh, stmt_insert);

                        if(wordIDL1 == 0) {
                            L1exist = false;
                            String sql_insert_wordL1 = "insert into oworg_owr_1_0.words (language_id, word) VALUES (1, '" + enWord + "')"; //the Language id for wordL1 is always 1(English)
                            stmt_insert.executeUpdate(sql_insert_wordL1);
                            wordIDL1 = words_lookup(enWord, stmt_insert);
                        } else {
                            L1exist = true;
                        }
                        if(wordIDL2 == 0) {
                            L2exist = false;
                            String sql_insert_wordL2 = "insert into oworg_owr_1_0.words (language_id, word) VALUES (" + lang_id_zhs + ", '" + word_simple_zh + "')";
                            stmt_insert.executeUpdate(sql_insert_wordL2);
                            wordIDL2 = words_lookup(word_simple_zh, stmt_insert);
                        } else {
                            L2exist = true;
                        }
                        if(wordIDL3 == 0) {
                            L3exist = false;
                            String sql_insert_wordL3 = "insert into oworg_owr_1_0.words (language_id, word) VALUES (" + lang_id_zht + ",'" + word_tradi_zh + "')";
                            stmt_insert.executeUpdate(sql_insert_wordL3);
                            wordIDL3 = words_lookup(word_tradi_zh, stmt_insert);
                        } else {
                            L3exist = true;
                        }
                        //insert into word_connection table
                        String sql_insert_wordConnection = "insert into oworg_owr_1_0.word_connections (word1_id, word2_id, connection_type) VALUES (" + wordIDL1 + ", " + wordIDL2 + ", " + "'primary mapping')";      
                        stmt_insert.executeUpdate(sql_insert_wordConnection);
                        sql_insert_wordConnection = "insert into oworg_owr_1_0.word_connections (word1_id, word2_id, connection_type) VALUES (" + wordIDL1 + ", " + wordIDL3 + ", " + "'primary mapping')";
                        stmt_insert.executeUpdate(sql_insert_wordConnection);
                        //insert into word_meaning table
                        if(!L1exist) {
                            String sql_insert_wordMeaningL1 = "insert into oworg_owr_1_0.word_meaning (word_id, type, meaning_text) VALUES (" + wordIDL1 + ", " + 0 + ", '" + definition + "')";
                            stmt_insert.executeUpdate(sql_insert_wordMeaningL1);
                        }
                        if(!L2exist) {
                            String sql_insert_wordMeaningL2 = "insert into oworg_owr_1_0.word_meaning (word_id, type, meaning_text) VALUES (" + wordIDL2 + ", " + 0 + ", '" + definition + "')";
                            stmt_insert.executeUpdate(sql_insert_wordMeaningL2);
                        }     
                        if(!L3exist) {
                            String sql_insert_wordMeaningL3 = "insert into oworg_owr_1_0.word_meaning (word_id, type, meaning_text) VALUES (" + wordIDL3 + ", " + 0 + ", '" + definition + "')";
                            stmt_insert.executeUpdate(sql_insert_wordMeaningL3);
                        }   
                        //insert into word_rank table
                        if(!L1exist) {
                            String sql_insert_wordRankL1 = "insert into oworg_owr_1_0.word_rank (word_id, rank, rank_type) VALUES (" + wordIDL1 + ", " + rank + ", " + 1 + ")";
                            stmt_insert.executeUpdate(sql_insert_wordRankL1);
                        }
                        if(!L2exist) {
                            String sql_insert_wordRankL2 = "insert into oworg_owr_1_0.word_rank (word_id, rank, rank_type) VALUES (" + wordIDL2 + ", " + rank + ", " + 1 + ")";              
                            stmt_insert.executeUpdate(sql_insert_wordRankL2);
                        }   
                        if(!L3exist) {
                            String sql_insert_wordRankL3 = "insert into oworg_owr_1_0.word_rank (word_id, rank, rank_type) VALUES (" + wordIDL3 + ", " + rank + ", " + 1 + ")";              
                            stmt_insert.executeUpdate(sql_insert_wordRankL3);
                        }   
                        //insert into word_transcription table
                        if(!L2exist) {
                            String sql_insert_wordTranscription1 = "insert into oworg_owr_1_0.word_transcription (word_id, transcription, transcription_type) VALUES (" + wordIDL2 + ", '" + tr + "', " + 1 + ")"; 
                            stmt_insert.executeUpdate(sql_insert_wordTranscription1);
                        } 
                        if(!L3exist) {
                            String sql_insert_wordTranscription2 = "insert into oworg_owr_1_0.word_transcription (word_id, transcription, transcription_type) VALUES (" + wordIDL3 + ", '" + tr + "', " + 1 + ")"; 
                            stmt_insert.executeUpdate(sql_insert_wordTranscription2);
                        } 
                        if(stmt_insert != null) {
                            stmt_insert.close();
                        }              
                        L1exist = false;
                        L2exist = false;
                        L3exist = false;
                    }

                    else if(lang_id != 0) {

                        //insert into tables:
                        //get L1 word and L2 word ID in words table , if not exists, insert record(s) and return ID    
                        wordIDL1 = words_lookup(enWord, stmt_insert);
                        wordIDL2 = words_lookup(word, stmt_insert);

                        if(wordIDL1 == 0) {
                            L1exist = false;
                            String sql_insert_wordL1 = "insert into oworg_owr_1_0.words (language_id, word) VALUES (1, '" + enWord + "')"; //the Language id for wordL1 is always 1(English)
                            stmt_insert.executeUpdate(sql_insert_wordL1);
                            wordIDL1 = words_lookup(enWord, stmt_insert);
                        } else {
                            L1exist = true;
                        }
                        if(wordIDL2 == 0) {
                            L2exist = false;
                            String sql_insert_wordL2 = "insert into oworg_owr_1_0.words (language_id, word) VALUES (" + lang_id + ", '" + word + "')";
                            stmt_insert.executeUpdate(sql_insert_wordL2);
                            wordIDL2 = words_lookup(word, stmt_insert);
                        } else {
                            L2exist = true;
                        }
                        //insert into word_connection table
                        String sql_insert_wordConnection = "insert into oworg_owr_1_0.word_connections (word1_id, word2_id, connection_type) VALUES (" + wordIDL1 + ", " + wordIDL2 + ", " + "'primary mapping')";
                        stmt_insert.executeUpdate(sql_insert_wordConnection);
                        //insert into word_meaning table
                        if(!L1exist) {
                            String sql_insert_wordMeaningL1 = "insert into oworg_owr_1_0.word_meaning (word_id, type, meaning_text) VALUES (" + wordIDL1 + ", " + 0 + ", '" + definition + "')";
                            stmt_insert.executeUpdate(sql_insert_wordMeaningL1);
                        }
                        if(!L2exist) {
                            String sql_insert_wordMeaningL2 = "insert into oworg_owr_1_0.word_meaning (word_id, type, meaning_text) VALUES (" + wordIDL2 + ", " + 0 + ", '" + definition + "')";
                            stmt_insert.executeUpdate(sql_insert_wordMeaningL2);
                        }            
                        //insert into word_rank table
                        if(!L1exist) {
                            String sql_insert_wordRankL1 = "insert into oworg_owr_1_0.word_rank (word_id, rank, rank_type) VALUES (" + wordIDL1 + ", " + rank + ", " + 1 + ")";
                            stmt_insert.executeUpdate(sql_insert_wordRankL1);
                        }
                        if(!L2exist) {
                            String sql_insert_wordRankL2 = "insert into oworg_owr_1_0.word_rank (word_id, rank, rank_type) VALUES (" + wordIDL2 + ", " + rank + ", " + 1 + ")";              
                            stmt_insert.executeUpdate(sql_insert_wordRankL2);
                        }              
                        //insert into word_transcription table
                        if(!L2exist) {
                            String sql_insert_wordTranscription = "insert into oworg_owr_1_0.word_transcription (word_id, transcription, transcription_type) VALUES (" + wordIDL2 + ", '" + tr + "', " + 1 + ")"; 
                            stmt_insert.executeUpdate(sql_insert_wordTranscription);
                        } 
                        if(stmt_insert != null) {
                            stmt_insert.close();
                        }              
                        L1exist = false;
                        L2exist = false;
                    } 
                }catch(SQLException se) {
                    System.out.println(count);
                    System.out.println(definition);
                    se.printStackTrace();
                }catch(Exception s){
                    s.printStackTrace();
                }
            }                            
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception s){
            s.printStackTrace();
        }finally{
            //close resources        
            if(stmt!=null)
                stmt.close();
            if(conn!=null)
                conn.close();       
        }
    
    }
    //look word up in "words" table, if exists return word's id, if not return 0
    public static int words_lookup(String word, Statement stmt) {
        String sql_words_lookup = "SELECT id, language_id, word FROM words WHERE word = '" + word + "';";
        int wordID = 0;
        ResultSet rs_words_lookup;
        try{
            rs_words_lookup = stmt.executeQuery(sql_words_lookup);
            if(rs_words_lookup.next()) {
                wordID = rs_words_lookup.getInt("id");
            }
            else {
                wordID = 0;
            }
            rs_words_lookup.close();
        }catch(SQLException se) {
            se.printStackTrace();
        }
        return wordID;
    }
    //Check if L2 language supported. if so, return language id, if not return 0
    public static int getLanguageID(String lang_code, Statement stmt) {
        String sql_langid_lookup = "SELECT id, language, code FROM languages WHERE code = '" + lang_code + "';";
        int langID = 0;
        ResultSet rs_lang_lookup;
        try{
            rs_lang_lookup = stmt.executeQuery(sql_langid_lookup);
            if(rs_lang_lookup.next()) {
                langID = rs_lang_lookup.getInt("id");
            }
            else {
                langID = 0;
            }
            rs_lang_lookup.close();
        }catch(SQLException se) {
            se.printStackTrace();
        }
        return langID;        
    }
}
