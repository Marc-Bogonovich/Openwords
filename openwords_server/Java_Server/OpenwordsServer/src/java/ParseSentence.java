
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class ParseSentence {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(new File("sentences.csv"));
        File out = new File("eng.txt");

        int i = 0;
        int page = 1;

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] tokens = line.split("\t");
//            for (String token : tokens) {
//                System.out.println(token);
//            }
            try {
                if (tokens[1].equals("eng")) {
                    i += 1;
                    String r = tokens[0] + "\t" + tokens[2] + "\n";
                    FileUtils.writeStringToFile(out, r, true);

                    if (i >= page * 50000) {
                        page += 1;
                        out = new File("eng" + page + ".txt");
                    }
                    //System.out.println(i);
                }
            } catch (Exception e) {
                System.out.println("-----------------------" + line);
            }
        }
    }

}
