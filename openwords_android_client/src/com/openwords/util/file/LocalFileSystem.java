package com.openwords.util.file;

import android.os.Environment;
import com.openwords.util.log.LogUtil;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author hanaldo
 */
public class LocalFileSystem {

    public static final String[] Folders = new String[]{
        Environment.getExternalStorageDirectory().getAbsolutePath() + "/OpenwordsCache/",
        Environment.getExternalStorageDirectory().getAbsolutePath() + "/OpenwordsCache/audio/"
    };

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void makeFolders() {
        for (String path : Folders) {
            File file = new File(path);

            if (!file.exists()) {
                if (!file.mkdirs()) {
                    LogUtil.logDeubg(LocalFileSystem.class, "Folders not created");
                }
            }
            LogUtil.logDeubg(LocalFileSystem.class, "Folder[" + path + "] created");
        }

    }

    public static void cleanAudioFolder() {
        File dir = new File(Folders[1]);
        if (!dir.exists()) {
            return;
        }
        String[] files = dir.list();
        for (String file : files) {
            File f = new File(dir, file);
            if (f.delete()) {
                LogUtil.logDeubg(LocalFileSystem.class, "delete file: " + f.toString());
            }
        }
    }

    public static List<String> unzipAudioPackage(File zipAudioPackage) throws IOException {
        List<String> names = new LinkedList<String>();

        ZipFile zip = new ZipFile(zipAudioPackage);
        Enumeration<? extends ZipEntry> files = zip.entries();
        while (files.hasMoreElements()) {
            ZipEntry entry = files.nextElement();
            String name = entry.getName();
            if (name.contains("/")) {
                String[] tokens = name.split("/");
                name = tokens[tokens.length - 1];
            }
            File local = new File(Folders[1] + name);
            FileUtils.copyInputStreamToFile(zip.getInputStream(entry), local);
            names.add(name);
        }
        zipAudioPackage.delete();
        return names;
    }

    public static String getAudioFullPath(String audioName) {
        return Folders[1] + audioName;
    }

    private LocalFileSystem() {
    }
}
