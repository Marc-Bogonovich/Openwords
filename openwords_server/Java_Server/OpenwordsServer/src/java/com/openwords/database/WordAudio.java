package com.openwords.database;

import com.openwords.utils.MyContextListener;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "word_audiocall")
public class WordAudio implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void saveAudioFile(Session s, WordAudio record, File tempFile, String filePostfix) throws IOException {
        record.setFileName(record.getId().getWordId() + filePostfix);
        s.save(record);

        String newFileName = "audio/" + record.getFileName();
        String path = MyContextListener.getContextPath() + newFileName;
        File newFile = new File(path);
        FileUtils.copyFile(tempFile, newFile);
        UtilLog.logInfo(WordAudio.class, "Audio saved: " + path);

        s.beginTransaction().commit();
    }

    public static int getAudioCount(Session s, int type, int language) {
        return ((Number) s.createCriteria(WordAudio.class)
                .add(Restrictions.eq("id.type", type))
                .add(Restrictions.eq("languageId", language))
                .setProjection(Projections.rowCount()).uniqueResult())
                .intValue();

    }

    @SuppressWarnings("unchecked")
    public static List<WordAudio> getAudioByIds(Session s, Integer[] ids, int type, int language) {
        return s.createCriteria(WordAudio.class)
                .add(Restrictions.eq("id.type", type))
                .add(Restrictions.eq("languageId", language))
                .add(Restrictions.in("id.wordId", ids)).list();
    }

    private WordAudioId id;
    private int languageId;
    private String fileName;

    public WordAudio() {
    }

    public WordAudio(WordAudioId id, int languageId, String fileName) {
        this.id = id;
        this.languageId = languageId;
        this.fileName = fileName;
    }

    @Id
    @JSON(serialize = false, deserialize = false)
    public WordAudioId getId() {
        return id;
    }

    public void setId(WordAudioId id) {
        this.id = id;
    }

    @Column(name = "language_id")
    @JSON(serialize = false, deserialize = false)
    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Transient
    public int getWordId() {
        return id.getWordId();
    }

}
