/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package openwords_data;

/**
 *
 * @author Archie
 */
//escape the single quote in a string so it can be put into a SQL query
public class reviseString {
    public static String escapeSingleQuote (String target) {
        if(target.contains("'")) {
            target = target.replace("'", "''");
            return target;
        }
        else {
            return target;
        }
    }
    
}
