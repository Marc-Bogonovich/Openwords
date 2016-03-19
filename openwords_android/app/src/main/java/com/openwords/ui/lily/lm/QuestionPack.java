package com.openwords.ui.lily.lm;


import java.io.Serializable;

public class QuestionPack implements Serializable {
    public String[] problems, answers, marplots;

    public QuestionPack(String[] problems, String[] answers, String[] marplots) {
        this.problems = problems;
        this.answers = answers;
        this.marplots = marplots;
    }
}
