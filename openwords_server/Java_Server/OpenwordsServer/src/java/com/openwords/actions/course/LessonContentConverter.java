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
        StepContent content = null;
        Queue<StepContentItem> answerWaitQueue = new LinkedList<>();

        for (String line : lines) {
            String betterLine = line.trim();
            if (betterLine.isEmpty()) {
                continue;
            }
            if (betterLine.startsWith("=")) {
                if (content != null) {
                    insertAnswerItem(content, answerWaitQueue);
                    steps.add(content);
                }
                content = new StepContent();

            } else if (betterLine.startsWith("*")) {
                List<StepContentItem> newLine = new LinkedList<>();

                String[] proItems = betterLine.replace("*", "").trim().split("]");
                for (int i = 0; i < proItems.length; i++) {
                    proItems[i] = proItems[i].replace("[", "").trim();
                    newLine.add(new StepContentItem(new String[]{proItems[i]}, StepContentItem.Item_Type_Problem));
                }
                content.lines.add(newLine);

            } else if (betterLine.startsWith("#")) {
                String[] ansItems = betterLine.replace("#", "").trim().split("]");
                for (int i = 0; i < ansItems.length; i++) {
                    ansItems[i] = ansItems[i].replace("[", "").trim();
                }
                answerWaitQueue.add(new StepContentItem(ansItems, StepContentItem.Item_Type_Answer));

            } else if (betterLine.startsWith("%")) {
                String[] marItems = betterLine.replace("%", "").trim().split("]");
                for (int i = 0; i < marItems.length; i++) {
                    marItems[i] = marItems[i].replace("[", "").trim();
                }
                content.marplots.add(new StepContentItem(marItems, StepContentItem.Item_Type_Marplot));

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
        answerWaitQueue.clear();
    }

    private LessonContentConverter() {
    }
}
