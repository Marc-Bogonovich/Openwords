package com.openwords.actions.course;

import com.openwords.database.StepContent;
import com.openwords.database.StepContentItem;
import com.openwords.utils.UtilLog;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LessonContentConverter {

    public static List<StepContent> getStepsFromText(List<String> lines) {
        List<StepContent> steps = new LinkedList<>();
        StepContent stepContent = null;
        Queue<StepContentItem> answerWaitQueue = new LinkedList<>();

        for (String line : lines) {
            String betterLine = line.trim();
            if (betterLine.isEmpty()) {
                continue;
            }
            if (betterLine.startsWith("=")) {
                if (stepContent != null) {
                    if (!stepContent.lines.isEmpty()) {
                        insertAnswerItem(stepContent, answerWaitQueue);
                        steps.add(stepContent);
                    }
                    answerWaitQueue.clear();
                    stepContent = null;
                }
                stepContent = new StepContent();

            } else if (betterLine.startsWith("*")) {
                final List<StepContentItem> newLine = new LinkedList<>();

                readOneLineItems(betterLine, new GotOneItem() {

                    @Override
                    public void gotOne(String item) {
                        newLine.add(new StepContentItem(new String[]{item}, StepContentItem.Item_Type_Problem));
                    }
                });
                if (!newLine.isEmpty()) {
                    stepContent.lines.add(newLine);
                }

            } else if (betterLine.startsWith("#")) {
                String[] items = readOneLineItems(betterLine, null);
                if (items != null) {
                    answerWaitQueue.add(new StepContentItem(items, StepContentItem.Item_Type_Answer));
                }

            } else if (betterLine.startsWith("%")) {
                String[] items = readOneLineItems(betterLine, null);
                if (items != null) {
                    stepContent.marplots.add(new StepContentItem(items, StepContentItem.Item_Type_Marplot));
                }

            }
        }
        if (stepContent != null) {
            if (!stepContent.lines.isEmpty()) {
                insertAnswerItem(stepContent, answerWaitQueue);
                steps.add(stepContent);
            }
        }

        return steps;
    }

    private static void insertAnswerItem(StepContent content, Queue<StepContentItem> answerWaitQueue) {
        boolean hasBlank = false;
        for (List<StepContentItem> line : content.lines) {
            for (int i = 0; i < line.size(); i++) {
                StepContentItem item = line.get(i);
                if (item.type.equals(StepContentItem.Item_Type_Problem) && item.text[0].equals(StepContentItem.Answer_Placeholder)) {
                    line.set(i, answerWaitQueue.remove());
                    hasBlank = true;
                }
            }
        }
        if (!answerWaitQueue.isEmpty() && hasBlank) {
            UtilLog.logWarn(LessonContentConverter.class, "answer items are more than blanks?");
        }
    }

    private static String[] readOneLineItems(String line, GotOneItem callback) {
        List<String> items = new LinkedList<>();
        String s = "";

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '[') {
                s = "";
                continue;
            }
            if (c == ']') {
                if (!s.isEmpty()) {
                    items.add(s);
                    if (callback != null) {
                        callback.gotOne(s);
                    }
                }
                continue;
            }
            s += c;
        }
        if (items.isEmpty()) {
            return null;
        } else {
            return items.toArray(new String[items.size()]);
        }
    }

    private LessonContentConverter() {
    }

}
