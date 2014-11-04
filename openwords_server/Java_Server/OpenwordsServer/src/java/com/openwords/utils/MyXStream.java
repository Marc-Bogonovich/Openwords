package com.openwords.utils;

import com.thoughtworks.xstream.XStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hanaldo
 */
public class MyXStream {

    private static Map<String, XStream> roots = new HashMap<>(5);

    public static void clean() {
        roots.clear();
        roots = null;
    }

    public static String toXml(String root, Class c, Object o) {
        if (!roots.containsKey(root)) {
            XStream xs = new XStream();
            xs.alias(root, c);
            roots.put(root, xs);
        }
        return roots.get(root).toXML(o);
    }

    private MyXStream() {
    }
}
