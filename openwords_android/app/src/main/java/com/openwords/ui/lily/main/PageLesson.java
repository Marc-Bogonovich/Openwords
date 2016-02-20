package com.openwords.ui.lily.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.openwords.R;
import com.openwords.learningmodule.MyPager;

import java.util.LinkedList;
import java.util.List;

public class PageLesson extends FragmentActivity {

    private ListView list;
    private ListAdapterLessonItem listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lily_page_lesson);

        list = (ListView) findViewById(R.id.page_lesson_list1);

        List<String[]> items = new LinkedList<>();
        items.add(new String[]{"1", "Lesson 1"});
        items.add(new String[]{"2", "Lesson 2"});
        items.add(new String[]{"3", "Lesson 3"});
        items.add(new String[]{"4", "Lesson 4"});
        items.add(new String[]{"5", "Lesson 5"});
        items.add(new String[]{"6", "Lesson 6"});
        items.add(new String[]{"7", "Lesson 7"});
        items.add(new String[]{"8", "Lesson 8"});
        items.add(new String[]{"9", "Lesson 9"});
        items.add(new String[]{"10", "Lesson 10"});

        listAdapter = new ListAdapterLessonItem(this, items);
        list.setAdapter(listAdapter);
        list.setDivider(null);
    }


}
