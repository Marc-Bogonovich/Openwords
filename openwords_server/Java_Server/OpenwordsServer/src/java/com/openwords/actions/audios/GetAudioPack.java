package com.openwords.actions.audios;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.WordAudio;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyContextListener;
import com.openwords.utils.MyGson;
import com.openwords.utils.MyZipUtil;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetAudioPack extends MyAction {

    private static final long serialVersionUID = 1L;
    private String wordIds;
    private int type, language;
    private int userId;

    @Action(value = "/getWordAudioPack")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWordAudioPack: " + wordIds);
        Session s = DatabaseHandler.getSession();
        try {
            Integer[] ids = (Integer[]) MyGson.fromJson(wordIds, Integer[].class);
            List<WordAudio> audios = WordAudio.getAudioByIds(s, ids, type, language);
            List<String> names = new LinkedList<>();
            for (WordAudio audio : audios) {
                names.add(MyContextListener.getContextPath(true) + "audio/" + audio.getFileName());
            }
            String zipFilePath = MyContextListener.getContextPath(false) + "audio_temp/" + "u" + userId + "_audio.zip";
            MyZipUtil.doZip(names, zipFilePath);
            File file = new File(zipFilePath);
            getHttpResponse().setHeader("Content-Type", "application/zip");
            getHttpResponse().setHeader("Content-disposition", "attachment; filename=temp_audio_package.zip");
            getHttpResponse().setHeader("Content-Length", String.valueOf(file.length()));
            FileUtils.copyFile(file, getHttpResponse().getOutputStream());
        } catch (Exception e) {
            UtilLog.logWarn(this, e);
            sendBadRequest(e.toString());
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return null;
    }

    public void setWordIds(String wordIds) {
        this.wordIds = wordIds;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
