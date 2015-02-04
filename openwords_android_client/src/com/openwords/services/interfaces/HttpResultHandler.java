package com.openwords.services.interfaces;

/**
 *
 * @author hanaldo
 */
public interface HttpResultHandler {

    public void hasResult(Object resultObject);

    public void noResult(String errorMessage);
}
