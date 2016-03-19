package com.openwords.ui.lily.lm;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.util.log.LogUtil;

import java.util.LinkedList;
import java.util.List;

public class PageLessonSection extends Fragment {

    private boolean firstPageDone;
    private View myFragmentView;
    private LinearLayout problemArea, answerArea, root;
    private List<LinearLayout> answerLines;
    private List<TextView> answerItems;
    private ImageView check;
    private int fiveDP;
    private int index;
    private boolean[] answerDone;
    private TimerBarAnimation timerBar;

    public PageLessonSection() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.logDeubg(this, "onCreate()");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
            if (index == 0) {
                answerDone = new boolean[2];
            } else if (index == 1) {
                answerDone = new boolean[1];
            } else {
                answerDone = new boolean[1];
            }

        }
        Resources r = getResources();
        fiveDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        LogUtil.logDeubg(this, "index: " + index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.logDeubg(this, "onCreateView()");
        myFragmentView = inflater.inflate(R.layout.lily_page_lm_lesson, container, false);
        root = (LinearLayout) myFragmentView.findViewById(R.id.page_lesson_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));
        problemArea = (LinearLayout) myFragmentView.findViewById(R.id.page_lesson_problem_items);
        answerArea = (LinearLayout) myFragmentView.findViewById(R.id.page_lesson_answer_items);
        check = (ImageView) myFragmentView.findViewById(R.id.page_lesson_image_check);
        check.setVisibility(View.INVISIBLE);

        answerLines = new LinkedList<>();
        answerItems = new LinkedList<>();

        answerArea.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (index == 0 && !firstPageDone) {
                    int lineWidth = v.getWidth();
                    if (lineWidth <= 0) {
                        return;
                    }
                    makeData();
                    buildLines(lineWidth);
                    firstPageDone = true;
                }
            }
        });

        View.OnDragListener onDrag = new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                TextView dest = (TextView) v;

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        dest.setBackgroundResource(R.drawable.bg_lily_input_fill_hover);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        dest.setBackgroundResource(R.drawable.bg_lily_input_fill_white);
                        break;
                    case DragEvent.ACTION_DROP:
                        dest.setBackgroundResource(R.drawable.bg_lily_input_fill_white);
                        TextView data = (TextView) event.getLocalState();
                        String inputText = data.getText().toString();
                        //MyQuickToast.showShort(getContext(), data.getText().toString());
                        //answerArea.removeView(data);
                        dest.setText(inputText);

                        AnswerItem answer = (AnswerItem) dest.getTag();
                        checkAnswer(inputText, answer);

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        };

        if (index == 0) {
            problemArea.addView(makeProblemItemView(parse("[Today is Monday.]"), onDrag, null));
            problemArea.addView(makeProblemItemView(parse("[@-][是][@-][。]"), onDrag, new String[]{"今天", "星期一"}));
        } else if (index == 1) {
            problemArea.addView(makeProblemItemView(parse("[你好]"), onDrag, null));
            problemArea.addView(makeProblemItemView(parse("[@]"), onDrag, new String[]{"Hello"}));
        } else {
            problemArea.addView(makeProblemItemView(parse("[Is \"变化\"(change) a Noun?]"), onDrag, null));
            problemArea.addView(makeProblemItemView(parse("[@]"), onDrag, new String[]{"Sometimes"}));
        }

        timerBar = new TimerBarAnimation(getContext(), myFragmentView.findViewById(R.id.lm_frag_advance), 3000,
                new TimerBarAnimation.TimerDone() {
                    @Override
                    public void done() {
                        if (ActLesson.instance != null) {
                            ActLesson.instance.goToNextPage();
                        }
                    }
                });

        return myFragmentView;
    }

    private void makeData() {
        if (index == 0) {
            makeAnswerItemView("星期二", answerItems);
            makeAnswerItemView("明天", answerItems);
            makeAnswerItemView("昨天", answerItems);
            makeAnswerItemView("星期一", answerItems);
            makeAnswerItemView("今天", answerItems);
            makeAnswerItemView("星期日", answerItems);
        } else if (index == 1) {
            makeAnswerItemView("How are you", answerItems);
            makeAnswerItemView("Hello", answerItems);
            makeAnswerItemView("What's up", answerItems);
        } else {
            makeAnswerItemView("Yes", answerItems);
            makeAnswerItemView("No", answerItems);
            makeAnswerItemView("Sometimes", answerItems);
        }
    }

    private void checkAnswer(String input, AnswerItem answer) {
        //MyQuickToast.showShort(getContext(), "check: " + input + " " + answer.text);
        if (input.equals(answer.text)) {
            answerDone[answer.order] = true;
        } else {
            answerDone[answer.order] = false;
            check.setVisibility(View.INVISIBLE);
        }
        for (boolean done : answerDone) {
            if (!done) {
                return;
            }
        }
        //MyQuickToast.showShort(getContext(), "All correct");
        check.setVisibility(View.VISIBLE);
        timerBar.fire();
    }

    public static List<String> parse(String s) {
        List<String> items = new LinkedList<>();

        String item = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                if (!item.isEmpty()) {
                    items.add(item);
                    item = "";
                }
            } else if (c == ']') {
                items.add(item);
                item = "";
            } else {
                item += c;
            }
        }
        return items;
    }

    private void clearUI() {
        for (LinearLayout line : answerLines) {
            line.removeAllViews();
        }
        answerArea.removeAllViews();
        answerLines.clear();
        answerItems.clear();
    }

    public void buildUI() {
        int lineWidth = answerArea.getWidth();
        if (lineWidth <= 0) {
            return;
        }
        clearUI();
        makeData();
        buildLines(lineWidth);
    }

    private void buildLines(int lineWidth) {
        int totalLineWidth = 0;

        LinearLayout line1 = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        line1.setLayoutParams(params);
        answerLines.add(line1);

        for (TextView item : answerItems) {
            item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalLineWidth += item.getMeasuredWidth() + fiveDP * 4;//left+right+text

            if (totalLineWidth >= lineWidth) {
                LinearLayout lineLayout = new LinearLayout(getContext());
                lineLayout.setLayoutParams(params);
                answerLines.add(lineLayout);
                totalLineWidth = item.getMeasuredWidth() + fiveDP * 4;
            }

            answerLines.get(answerLines.size() - 1).addView(item);
        }

        for (LinearLayout lineLayout : answerLines) {
            answerArea.addView(lineLayout);
        }
    }

    private void makeAnswerItemView(String text, List<TextView> itemList) {
        final TextView t = new TextView(getContext());
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        t.setBackgroundResource(R.drawable.bg_lily_sentence_item);
        t.setPadding(fiveDP * 2, 0, fiveDP * 2, 0);
        t.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(fiveDP * 2, fiveDP * 2, fiveDP * 2, fiveDP * 2);
        t.setLayoutParams(params);
        t.setText(text);
        t.setMaxLines(1);

        t.setMovementMethod(new ScrollingMovementMethod());
        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(null, shadowBuilder, t, 0);
                    LogUtil.logDeubg(this, "ACTION_DOWN");
                    return true;
                }
                return false;
            }
        });
        itemList.add(t);
    }

    private LinearLayout makeProblemItemView(List<String> items, View.OnDragListener onDrag, String[] answers) {
        LinearLayout line = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(fiveDP * 2, fiveDP * 2, fiveDP * 2, fiveDP * 2);
        line.setLayoutParams(params);

        int index = 0;
        for (String text : items) {
            TextView t = new TextView(getContext());
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            t.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            t.setLayoutParams(params2);
            if (text.startsWith("@")) {
                t.setText("");
                t.setTextColor(getResources().getColor(R.color.black));
                t.setBackgroundResource(R.drawable.bg_lily_input_fill_white);
                t.setMinWidth(fiveDP * 10);
                t.setOnDragListener(onDrag);
                t.setPadding(fiveDP, fiveDP, fiveDP, fiveDP);
                t.setTag(new AnswerItem(answers[index], index));
                index += 1;
            } else {
                t.setText(text);
            }
            t.setMaxLines(1);
            line.addView(t);
        }

        return line;
    }
}
