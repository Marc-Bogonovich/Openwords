//This program is to get english to all other languages data
package openwords_data;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.*;

public class Openwords_data {
  //@SuppressWarnings("empty-statement")
  public static void main(String[] args) throws XMLStreamException, Exception {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLStreamReader reader = null;            //Use "SAX" way to parse xml file, powered by XMLStreamReader interface
      
      MySQLAccess db_connect = new MySQLAccess();        //connect to the database, implemented in "MySQLAccess.java"
      db_connect.connectDatabase();
      
      int uniqueDef = 1; //This can be set manually to determine if one definition or multiple ones will be returned
      int firstDef = 1;
      
      int id = 0;
      int L1id = 0;
      int defID = 0;
      int textLine = 0;
      String text;
      String text_tmp;
      String ns;
      String title = null;
      String temp;

      boolean inNs = false;
      boolean inPage = false;
      boolean isData = false;
      boolean isWord = false;
      boolean inTitle = false;
      boolean inText = false;
      boolean isEnglish = false;
      boolean inTrans = false;
      
      //definition for a particular language purpose
      String regexTitle = "^[a-zA-Z]+$";
      String subText = "==English==";
      String transTag = "{{trans-top|";
      String anti_transTag = "{{trans-bottom}}";
      
      Pattern outter = Pattern.compile("\\{t\\+??\\|([^\\{^\\}^\\|]+)\\|([^\\{]*?)\\}");
      Pattern trans = Pattern.compile("\\{([^\\{^\\}^\\|]*?)\\|([^\\{^\\}^\\|]*?)\\|([^\\{^\\}^\\|]*?)[\\||\\}]");
      Pattern tr = Pattern.compile("tr=([^\\|^\\}]*?)[\\||\\}]");
      Pattern mean = Pattern.compile("\\{\\{trans-top\\|(.*?)\\}\\}");
      Matcher matcher_outter;
      Matcher matcher_trans;
      Matcher matcher_tr;
      Matcher matcher_mean;
      String lang = "";
      String word = "";
      String proun = "";
      String meaning = "";
      String grammerType = "";
      
      
      try {    
          //start to read xml file
          reader = factory.createXMLStreamReader(new FileInputStream("/Users/Archie/Documents/enwiktionary-20140702-pages-meta-current.xml"));
      } catch (FileNotFoundException e) {
          System.out.println("File not found");
      }
      while(reader.hasNext()){               //Read next section of xml file if exists
          int event = reader.next();
          switch(event) {               //choose behaviors when encountering following events
              case XMLStreamConstants.START_ELEMENT:        //when encountering elements "<...>"
                  switch(reader.getLocalName()) {           //set some flags when encountering the following elements
                      case "page":                       
                          inPage = true;        //when encountering "<page>"
                          break;
                      case "ns":                //when encountering "<ns>"
                          inNs = true;
                          break;
                      case "title":             //when encountering "<title>"
                          inTitle = true;       
                          break;
                      case "text":              //when encountering "<text>"
                          inText = true;
                          break;
                  }
                  break;
              case XMLStreamConstants.CHARACTERS:       //when encoutering contents between a start element <...> and end element </...>
                  //When encountering "ns"                
                  if(inPage == true && inNs == true && inTitle == false && inText == false) {
                      ns = reader.getText().trim();
                      if(ns.equals("0")) {
                          isData = true;
                      }
                  } 
                  //When encountering "title"
                  if(inPage == true && inNs == false && inTitle == true && inText == false) {
                      title = reader.getText().trim();  
                  }
                  //When encountering "text"
                  if(isWord && inPage == true && inNs == false && inTitle == false && inText == true) {
                      //text processing part  
                      if(textLine < 2) {  
                          //judge if it is english
                          if(reader.getText().trim().contains(subText)) {
                              isEnglish = true; 
                              ++L1id;
                          }                          
                      }
                      else {
                          //end English flag when goes to other languages
                          if(reader.getText().trim().matches("==([^=]*?)==")) {
                              isEnglish = false;
                          }
                          //when it's in English area
                          if(isEnglish) {
                              if(reader.getText().trim().contains("===")) {         //get grammer type                  
                                  temp = Openwords_data.getGrammerType(reader);
                                  if(!temp.equals("")) {
                                      grammerType = temp;
                                  }
                              }
                              if(reader.getText().trim().contains(transTag)) {       //get definition
                                  
                                  inTrans = true;
                                  //System.out.println(reader.getText().trim());
                                  //System.out.println("----------------");
                                  matcher_mean = mean.matcher(reader.getText().trim());
                                  ++defID;
                                  while(matcher_mean.find()) {
                                      meaning = matcher_mean.group(1);
                                  }                                                                  
                              }
                              if(reader.getText().trim().contains(anti_transTag)) { 
                                  firstDef = 0;
                                  inTrans = false;
                                  
                              }
                              if(inTrans) {                 //get translation                 
                                  text = reader.getText().trim();  
                                  matcher_outter = outter.matcher(text);
                                  while (matcher_outter.find()) {   
                                      //to find the block of translation
                                      text_tmp = matcher_outter.group();
                                      //to find the translation word
                                      matcher_trans = trans.matcher(text_tmp);
                                      while(matcher_trans.find()) {                            
                                          word = matcher_trans.group(3);  
                                          lang = matcher_trans.group(2);
                                      }   
                                      //to find the translation pronounce
                                      matcher_tr = tr.matcher(text_tmp);
                                      while(matcher_tr.find()) {
                                          proun = matcher_tr.group(1);          //get transcription
                                      }
                                      
                                      //insert into database
                                      
                                      //System.out.print(title+" ");
                                      //System.out.print(grammerType+" ");
                                      //System.out.print(meaning+" ");
                                      //System.out.print(lang+" ");
                                      //System.out.print(word+" ");
                                      //System.out.println(proun+" ");
                                      try{
                                          if(uniqueDef == 0) {
                                              db_connect.writeDatabase(++id, L1id, title, grammerType, defID, meaning, lang, word, proun);  
                                              if(id%1000 == 0) {
                                                  System.out.println(id);
                                              }
                                          }
                                          else {
                                              if(firstDef == 1) {
                                                  db_connect.writeDatabase(++id, L1id, title, grammerType, defID, meaning, lang, word, proun);  
                                                  if(id%1000 == 0) {
                                                      System.out.println(id);
                                                  }
                                              }
                                          }
                                          
                                      } catch(Exception e) {
                                          System.out.println("database writing error");
                                          System.out.print(title+" ");
                                          System.out.print(grammerType+" ");
                                          System.out.print(meaning+" ");
                                          System.out.print(lang+" ");
                                          System.out.print(word+" ");
                                          System.out.println(proun+" ");
                                          System.out.println("------------------");
                                      }
                                      proun = "";
                                  }
                              }
                          }
                      }                   
                      textLine++;
                  }                    
                  break;
              case XMLStreamConstants.END_ELEMENT:              //when encountering end element "</...>"
                  switch(reader.getLocalName()) {
                      //leaving "page"
                      case "page": 
                          proun = "";
                          word = "";
                          lang = "";
                          meaning = "";
                          isEnglish = false;
                          inNs = false;
                          inPage = false;
                          isData = false;
                          isWord = false;
                          inTitle = false;
                          inText = false;
                          textLine = 0;
                          firstDef = 1;
                          break;
                      //leaving "ns"
                      case "ns":
                          inNs = false;
                          if(isData && title.matches(regexTitle)) {
                              //when it's data, check the legality of title to decide "isWord"
                              isWord = true;
                          }
                          break;
                      //leaving "title"
                      case "title":
                          inTitle = false;
                          break;
                      //leaving "text"
                      case "text":                   
                          inText = false;
                          break;
                  }
                  break;
              case XMLStreamConstants.START_DOCUMENT:
                  System.out.println("Start");
                  break;       
          }
      }
      System.out.println("no more");       
      db_connect.close();
  }    
  public static String getGrammerType(XMLStreamReader reader) {             //helper method to get the grammer type
      String text = reader.getText().trim();
      if(text.contains("===Noun===")) {
          return "noun";
      }
      if(text.contains("===Verb===")) {
          return "verb";
      }
      if(text.contains("===Adjective===")) {
          return "adjective";
      }
      if(text.contains("===Adverb===")) {
          return "adverb";
      }
      if(text.contains("===Article===")) {
          return "article";
      }
      if(text.contains("===Conjunction===")) {
          return "Conjunction";
      }
      if(text.contains("===Determiner===")) {
          return "determiner";
      }
      if(text.contains("===Interjection===")) {
          return "interjection";
      }
      if(text.contains("===Pronoun===")) {
          return "pronoun";
      }
      if(text.contains("===Phrase===")) {
          return "phrase";
      }
      if(text.contains("===Preposition===")) {
          return "preposition";
      }    
      return "";
  }
}