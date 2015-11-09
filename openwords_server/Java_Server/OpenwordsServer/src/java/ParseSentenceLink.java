
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Sentence;
import com.openwords.database.SentenceConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ParseSentenceLink {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(new File("links.csv"));
        Session s = DatabaseHandler.getSession();
        int i = 0;

        try {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] tokens = line.split("\t");

                try {
                    long left = Long.parseLong(tokens[0]);
                    long right = Long.parseLong(tokens[1]);
                    i += 1;

                    List<Sentence> eng = s.createCriteria(Sentence.class)
                            .add(Restrictions.eq("sentenceId", left))
                            .add(Restrictions.eq("languageId", 1))
                            .list();

                    List<Sentence> cmn = s.createCriteria(Sentence.class)
                            .add(Restrictions.eq("sentenceId", right))
                            .add(Restrictions.eq("languageId", 98))
                            .list();

                    if (!eng.isEmpty() && !cmn.isEmpty()) {
                        SentenceConnection conn = new SentenceConnection(left, right, 98, "test");
                        s.save(conn);
                        s.beginTransaction().commit();
                        System.out.println(i + " saved");
                    }
                } catch (Exception e) {
                    System.out.println("-----------------------" + line);

                }
                System.out.println(i);
            }
        } finally {
            DatabaseHandler.closeSession(s);
        }
    }

}
