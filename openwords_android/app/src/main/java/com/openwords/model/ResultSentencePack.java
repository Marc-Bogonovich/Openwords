package com.openwords.model;

import java.util.List;

/**
 *
 * @author hanaldo
 */
public interface ResultSentencePack {

    public void result(List<SentenceConnection> connections);

    public void error(String error);
}
