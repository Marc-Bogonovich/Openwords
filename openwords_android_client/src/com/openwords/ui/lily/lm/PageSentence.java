package com.openwords.ui.lily.lm;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.FragmentLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.Sentence;
import com.openwords.model.SentenceConnection;
import com.openwords.model.SentenceItem;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PageSentence extends FragmentLearningModule {

    public static boolean FirstPageDone;

    private final int cardIndex;
    private final ActivityLearning lmActivity;
    private View myFragmentView;
    private LinearLayout root;
    private LinearLayout itemsArea;
    private AutoResizeTextView itemsResultArea, question;
    private ImageView imageCheck, buttonOption;
    private Resources r;
    private int padding;
    private int margin;
    private List<TextView> items;
    private List<LinearLayout> lines;
    private List<Integer> itemGoIndex;
    private ViewSoundBackground soundButton;
    private SentenceConnection sentence;
    private List<SentenceItem> answerItems;
    private Sentence s1, s2;
    private Performance perf;

    public PageSentence(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.lily_page_lm_sentence, container, false);

        sentence = DataPool.currentSentences.get(cardIndex);
        perf = DataPool.currentPerformance.get(cardIndex);
        if (perf == null) {
            MyQuickToast.showShort(lmActivity, "No performance data");
            return null;
        }

        root = (LinearLayout) myFragmentView.findViewById(R.id.page_sentence_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));

        itemsArea = (LinearLayout) myFragmentView.findViewById(R.id.page_sentence_items_area);
        itemsResultArea = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_sentence_items_result_text);
        question = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_sentence_question_text);
        soundButton = (ViewSoundBackground) myFragmentView.findViewById(R.id.lily_button_sound_bg);
        soundButton.config(Color.parseColor("#ffffff"), 255, false, true, getResources().getColor(R.color.main_app_color), null);
        imageCheck = (ImageView) myFragmentView.findViewById(R.id.page_sentence_image_check);
        buttonOption = (ImageView) myFragmentView.findViewById(R.id.page_sentence_image1);
        updateOptionButton(buttonOption, lmActivity);

        imageCheck.setVisibility(View.INVISIBLE);
        setTimer(lmActivity, myFragmentView.findViewById(R.id.lm_frag_advance), 2000);

        r = getResources();
        padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());

        itemsArea.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                LogUtil.logDeubg(this, "onLayoutChange");
                if (cardIndex == 0 && !FirstPageDone) {
                    //MyQuickToast.showShort(lmActivity, "lineWidth " + v.getWidth());
                    int lineWidth = v.getWidth();
                    if (lineWidth <= 0) {
                        return;
                    }
                    makeData();
                    buildOptionsUI(lineWidth);
                    FirstPageDone = true;
                }
            }
        });

        lines = new LinkedList<LinearLayout>();
        items = new LinkedList<TextView>();
        itemGoIndex = new LinkedList<Integer>();

        itemsResultArea.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (!itemGoIndex.isEmpty()) {
                    int index = itemGoIndex.remove(itemGoIndex.size() - 1);
                    TextView t = items.get(index);
                    t.setVisibility(View.VISIBLE);

                    String result = (String) itemsResultArea.getText();
                    int itemSize = t.getText().length();
                    if (s2.hasSpace) {
                        itemsResultArea.setText(result.substring(0, result.length() - itemSize - 1));
                    } else {
                        itemsResultArea.setText(result.substring(0, result.length() - itemSize));
                    }

                }
            }
        });
        LogUtil.logDeubg(this, "itemsArea width " + itemsArea.getWidth());
        return myFragmentView;
    }

    private void makeData() {
        if (LocalSettings.getLearningLanguage().langId == 1) {
            s1 = Sentence.getSentence(sentence.sentenceId);
            s2 = Sentence.getSentence(sentence.uniId);
        } else {
            s1 = Sentence.getSentence(sentence.uniId);
            s2 = Sentence.getSentence(sentence.sentenceId);
        }
        question.setText(s1.text);
        question.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                MyDialogHelper.showMessageDialog(lmActivity, null, null, null);
            }
        });
        addClarificationTrigger(lmActivity, new View[]{question}, 50, s2.text);

        if (LocalSettings.getLearningLanguage().langId == 1) {
            answerItems = SentenceItem.getItems(sentence.uniId);
        } else {
            answerItems = SentenceItem.getItems(sentence.sentenceId);
        }

        Collections.shuffle(answerItems);
        for (SentenceItem i : answerItems) {
            addOptionItem(i.item, items);
        }
    }

    private void clearUI() {
        for (LinearLayout line : lines) {
            line.removeAllViews();
        }
        itemsArea.removeAllViews();
        lines.clear();
        items.clear();
        itemGoIndex.clear();
    }

    private void addOptionItem(final String text, final List<TextView> itemList) {
        final TextView t = new TextView(lmActivity);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        t.setBackgroundResource(R.drawable.bg_lily_sentence_item);
        t.setPadding(padding, 0, padding, 0);
        t.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);
        t.setLayoutParams(params);
        t.setText(text);
        t.setMaxLines(1);

        t.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                t.setVisibility(View.GONE);
                addResultItem(text);
                itemGoIndex.add(itemList.indexOf(t));
                checkAnswer();
            }
        });
        itemList.add(t);
    }

    private void addResultItem(String text) {
        if (s2.hasSpace) {
            itemsResultArea.setText(itemsResultArea.getText() + text + " ");
        } else {
            itemsResultArea.setText(itemsResultArea.getText() + text);
        }
    }

    private void checkAnswer() {
        if (itemGoIndex.size() == items.size()) {
            String answer = ((String) itemsResultArea.getText()).trim();
            if (answer.equals(s2.text.trim())) {
                imageCheck.setVisibility(View.VISIBLE);
                perf.performance = "good";
                fireTimer();
            } else {
                MyQuickToast.showShort(lmActivity, "Try again...");
                perf.performance = "bad";
            }
        }
    }

    public void buildUI() {
        int lineWidth = itemsArea.getWidth();
        if (lineWidth <= 0) {
            return;
        }
        clearUI();
        makeData();
        buildOptionsUI(lineWidth);
    }

    private void buildOptionsUI(int lineWidth) {
        int totalLineWidth = 0;

        LinearLayout line1 = new LinearLayout(lmActivity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        line1.setLayoutParams(params);
        lines.add(line1);

        for (TextView item : items) {
            item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalLineWidth += item.getMeasuredWidth() + margin * 2;//left+right+text

            if (totalLineWidth >= lineWidth) {
                LinearLayout lineLayout = new LinearLayout(lmActivity);
                lineLayout.setLayoutParams(params);
                lines.add(lineLayout);
                totalLineWidth = item.getMeasuredWidth() + margin * 2;
            }

            lines.get(lines.size() - 1).addView(item);
        }

        for (LinearLayout lineLayout : lines) {
            itemsArea.addView(lineLayout);
        }
    }

}
