package com.openwords.ui.lily.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openwords.R;

public class FragmentCourse extends Fragment {

    private View myFragmentView;
    private CardView card;

    public FragmentCourse() {
    }

    public static FragmentCourse newInstance() {
        FragmentCourse fragment = new FragmentCourse();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.lily_frag_course, container, false);
        card = (CardView) myFragmentView.findViewById(R.id.page_course_card_view);

        return myFragmentView;
    }

}
