package com.openwords.ui.lily.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.InterfaceLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultSentencePack;
import com.openwords.model.SentenceConnection;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyQuickToast;

import java.util.List;

public class FragmentWords extends Fragment {

    private TextView buttonStudy, buttonManage, buttonDict, buttonTest;
    private View myFragmentView;

    public FragmentWords() {
    }

    public static FragmentWords newInstance() {
        return new FragmentWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.lily_frag_words, container, false);

        buttonStudy = (TextView) myFragmentView.findViewById(R.id.act_home_button_1);
        buttonManage = (TextView) myFragmentView.findViewById(R.id.act_home_button_2);
        buttonDict = (TextView) myFragmentView.findViewById(R.id.act_home_button_3);
        buttonTest = (TextView) myFragmentView.findViewById(R.id.page_home_text_1);
        buttonStudy.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonManage.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonDict.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonStudy.setText(LocalizationManager.getButtonPractice());
        buttonManage.setText(LocalizationManager.getButtonManageSets());
        buttonDict.setText(LocalizationManager.getButtonResume());
        buttonTest.setText(LocalizationManager.getButtonSentence());

        buttonStudy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageSetsList.mode = PageSetsList.Mode_Study;
                startActivity(new Intent(getActivity(), PageSetsList.class));
            }
        });
        buttonManage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (DataPool.OffLine) {
                    MyQuickToast.showShort(getActivity(), "Cannot manage set in offline mode.");
                    return;
                }
                PageSetsList.mode = PageSetsList.Mode_Manage;
                startActivity(new Intent(getActivity(), PageSetsList.class));
            }
        });
        buttonDict.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PageDictionary.class));
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SentenceConnection.loadSentencePack(LocalSettings.getBaseLanguageId(), LocalSettings.getLearningLanguage().langId, 5, new ResultSentencePack() {

                    public void result(List<SentenceConnection> connections) {
                        DataPool.currentSentences.clear();
                        DataPool.currentSentences.addAll(connections);
                        DataPool.LmType = InterfaceLearningModule.Learning_Type_Sentence;
                        startActivity(new Intent(getActivity(), ActivityLearning.class));
                    }

                    public void error(String error) {
                        MyQuickToast.showShort(getActivity(), error);
                    }
                });
            }
        });

        return myFragmentView;
    }
}
