//This program is to get english_korean data
package openwords_data;

import java.util.HashMap;
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
      XMLStreamReader reader = null;
      
      MySQLAccess db_connect = new MySQLAccess();
      db_connect.connectDatabase();
      
      int id = 0;
      int ni;
      int number = 0;
      int textLine = 0;
      String text;
      String text_tmp;
      String ns;
      String title = null;
      boolean inNs = false;
      boolean inPage = false;
      boolean isData = false;
      boolean isWord = false;
      boolean inTitle = false;
      boolean inText = false;
      
      HashMap<String, String> entity = new HashMap();
      entity.clear();
      
      //definition for a particular language purpose
      String regexTitle = "^[a-zA-Z]+$";
      String subText = "==English==";
      boolean isEnglish = false;
      Pattern outter = Pattern.compile("\\{t\\+??\\|ko\\|([^\\{]*?)\\}");
      Pattern inner1 = Pattern.compile("([^\\{^\\}^\\|]*?)(\\||\\})");
      Pattern inner2 = Pattern.compile("tr=([^\\|^\\}]*?)(\\||\\})");
      //Pattern inner3 = Pattern.compile("alt=([^\\|^\\}]*?)(\\||\\})");
      Matcher matcher_outter;
      Matcher matcher_inner1;
      Matcher matcher_inner2;
      //Matcher matcher_inner3;
      String tr = null;
      String word = null;
      
      
      try {          
          reader = factory.createXMLStreamReader(new FileInputStream("/Users/Archie/Documents/enwiktionary-20140609-pages-meta-current.xml"));
          //reader = factory.createXMLStreamReader(new FileInputStream("/Users/Archie/Documents/employee.xml"));
      } catch (FileNotFoundException e) {
          System.out.println("File not found");
      }
      while(reader.hasNext()){
          int event = reader.next();
          switch(event) {
              case XMLStreamConstants.START_ELEMENT:
                  switch(reader.getLocalName()) {
                      case "page":                       
                          inPage = true;
                          break;
                      case "ns":
                          inNs = true;
                          break;
                      case "title":
                          inTitle = true;
                          break;
                      case "text":
                          inText = true;
                          break;
                  }
                  break;
              case XMLStreamConstants.CHARACTERS:
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
                          if(reader.getText().trim().contains(subText)) 
                              isEnglish = true;      
                      } 
                      else if(isEnglish) {
                          if(reader.getText().trim().contains("* Korean:")) {                                   
                              text = reader.getText().trim();
                              //System.out.println(text);
                              matcher_outter = outter.matcher(text);
                              while (matcher_outter.find()) {     
                                  text_tmp = matcher_outter.group();
                                  //System.out.println(text_tmp);
                                  if(text_tmp.contains("tr=")) {                                     
                                      ni = 0;                       
                                      matcher_inner1 = inner1.matcher(text_tmp);
                                      while(matcher_inner1.find()) {
                                          ni++;
                                          if(ni == 3) {
                                              word = matcher_inner1.group(1);
                                          }
                                      }                             
                                      matcher_inner2 = inner2.matcher(text_tmp);
                                      if(matcher_inner2.find()) {                   
                                          tr = matcher_inner2.group(1);
                                      } 
                                          entity.put(word, tr);
                                      }  
                                  else {
                                      ni = 0;                       
                                      matcher_inner1 = inner1.matcher(text_tmp);
                                      while(matcher_inner1.find()) {
                                          ni++;
                                          if(ni == 3) {
                                              word = matcher_inner1.group(1);
                                          }
                                      }
                                      entity.put(word, "");
                                  }                                  
                              }
                          }
                      }
                      textLine++;
                  }                    
                  break;
              case XMLStreamConstants.END_ELEMENT:
                  switch(reader.getLocalName()) {
                      //leaving "page"
                      case "page": 
                          if(isWord && isEnglish) {       
                              if(!entity.isEmpty()) { 
                                  number++;
                                  for(String key : entity.keySet()) {   
                                      try {                                   
                                              db_connect.writeDatabase(++id, title, "", key, entity.get(key));                                       
                                      } catch(Exception e) {
                                          
                                      }
                                      if(id%1000 == 0) {
                                          System.out.println(id);
                                      }
                                  }
                              }                          
                          }
                          entity.clear();
                          word = "";
                          isEnglish = false;
                          inNs = false;
                          inPage = false;
                          isData = false;
                          isWord = false;
                          inTitle = false;
                          inText = false;
                          textLine = 0;
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
      System.out.print("The total number is: ");
      System.out.println(number);
          
      db_connect.close();
  }
}


  
  
  