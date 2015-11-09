
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Sentence;
import com.openwords.database.SentenceItem;
import com.openwords.database.SentenceMetaInfo;
import java.io.File;
import java.util.Scanner;
import org.hibernate.Session;

public class SaveSentences {

    public static void main(String[] args) {
        File file = new File("eng12.txt");
        boolean doParseBySpace = true;
        int language = 1;
        Session s = DatabaseHandler.getSession();

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] tokens = line.trim().split("\t");

                long sentenceId = Long.parseLong(tokens[0]);
                String content = tokens[1];

                Sentence sen = new Sentence(sentenceId, language, content, new SentenceMetaInfo(doParseBySpace).getXmlString());
                s.save(sen);
                System.out.println("sentence " + sen.getSentenceId());

                if (doParseBySpace) {
                    String[] items = content.split(" ");
                    for (int i = 0; i < items.length; i++) {
                        SentenceItem it = new SentenceItem(sentenceId, i + 1, items[i], "test");
                        s.save(it);
                    }
                } else {
                    char[] items = content.toCharArray();
                    for (int i = 0; i < items.length; i++) {
                        SentenceItem it = new SentenceItem(sentenceId, i + 1, String.valueOf(items[i]), "test");
                        s.save(it);
                    }
                }
            }
            s.beginTransaction().commit();//faster but may leak memory
            System.out.println("sentence done");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
    }

}
