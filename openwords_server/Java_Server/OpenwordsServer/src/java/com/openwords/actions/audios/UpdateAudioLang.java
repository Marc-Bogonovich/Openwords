package com.openwords.actions.audios;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.database.WordAudio;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@ParentPackage("json-default")
public class UpdateAudioLang extends MyAction {

    private static final long serialVersionUID = 1L;
    public static AtomicBoolean inUse = new AtomicBoolean(true);
    private int start, end;

    @Action("/fixAudioLang")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/fixAudioLang: " + start + " to " + end);
        if (inUse.get()) {
            sendError("One process is ongoing");
            return null;
        }
        inUse.set(true);
        Session s = DatabaseHandler.getSession();
        try {
            while (start <= end) {
                int firstRecord = (start - 1) * 100;
                @SuppressWarnings("unchecked")
                List<WordAudio> audios = s.createCriteria(WordAudio.class)
                        .addOrder(Order.asc("id.wordId"))
                        .setFirstResult(firstRecord)
                        .setMaxResults(100)
                        .list();
                for (WordAudio audio : audios) {
                    Word word = Word.getWord(s, audio.getWordId());
                    audio.setLanguageId(word.getLanguageId());
                    UtilLog.logInfo(this, "change " + audio.getId().getWordId());
                }
                s.beginTransaction().commit();
                start += 1;
            }

        } catch (Exception e) {
            UtilLog.logWarn(this, e);
        } finally {
            inUse.set(false);
            DatabaseHandler.closeSession(s);
        }
        return null;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
