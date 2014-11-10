package com.openwords.utils;

import com.openwords.database.WordMetaInfo;
import com.thoughtworks.xstream.XStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hanaldo
 */
public class MyXStream {

    private static Map<String, XStream> roots = new HashMap<>(5);

    public static void init() {
        XStream wordRoot = new XStream();
        wordRoot.alias("word", WordMetaInfo.class);
        roots.put("word", wordRoot);
    }

    public static void clean() {
        roots.clear();
        roots = null;
    }

    public static String toXml(String root, Object o) {
        return roots.get(root).toXML(o);
    }

    public static Object fromXml(String root, String xml) {
        return roots.get(root).fromXML(xml);
    }

    private MyXStream() {
    }
}
