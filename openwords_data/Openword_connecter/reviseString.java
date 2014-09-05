package openwords_data_connector;

/**
 *
 * @author Archie
 */
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
