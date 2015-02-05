package com.openwords.model;

/**
 *
 * @author hanaldo
 */
public interface ResultLanguage {

    public final static String Result_No_Server_Response = "no-server";
    public final static String Result_No_Language_Data = "no-langs";

    public void result(String result);
}
