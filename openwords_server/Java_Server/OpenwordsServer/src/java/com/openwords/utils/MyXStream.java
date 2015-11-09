package com.openwords.utils;

import com.openwords.database.SentenceMetaInfo;
import com.openwords.database.SetMetaInfo;
import com.openwords.database.UserLanguageMetaInfo;
import com.openwords.database.WordMetaInfo;
import com.thoughtworks.xstream.XStream;

public class MyXStream {

    private static XStream instance;

    private static void init() {
        if (instance == null) {
            instance = new XStream();
            instance.alias("word", WordMetaInfo.class);
            instance.alias("set", SetMetaInfo.class);
            instance.alias("ulang", UserLanguageMetaInfo.class);
            instance.alias("sentence", SentenceMetaInfo.class);
        }
    }

    public static String toXml(Object o) {
        init();
        return instance.toXML(o);
    }

    public static Object fromXml(String xml) {
        init();
        return instance.fromXML(xml);
    }

    private MyXStream() {
    }
}
