package com.openwords.learningmodule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.UserLanguage;
import com.openwords.services.implementations.ServiceGetUserPerformanceSum;
import com.openwords.services.implementations.ServiceGetUserPerformanceSum.Result;
import com.openwords.services.implementations.ServiceSetLanguagePage;
import com.openwords.services.implementations.ServiceSetUserPerformance;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.text.DecimalFormat;

public class FragmentPlateCompletion extends Fragment implements InterfaceLearningModule {

    private static Handler RefreshHandler;

    public static void refreshDetails() {
        if (RefreshHandler != null) {
            RefreshHandler.sendEmptyMessage(0);
        }
    }
    private TextView vocabSize, performance, skip, birthday, birthdayDetail, evaluation;
    private Button nextPlate, exit;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        RefreshHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    refresh();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView");

        View myFragmentView = inflater.inflate(R.layout.fragment_plate_completion, container, false);
        vocabSize = (TextView) myFragmentView.findViewById(R.id.plc_TextView_VocabSize);
        performance = (TextView) myFragmentView.findViewById(R.id.plc_TextView_Performance);
        skip = (TextView) myFragmentView.findViewById(R.id.plc_TextView_Skipped);
        birthday = (TextView) myFragmentView.findViewById(R.id.plc_TextView_Birthday);
        birthdayDetail = (TextView) myFragmentView.findViewById(R.id.plc_TextView_BirthdayDetail);
        evaluation = (TextView) myFragmentView.findViewById(R.id.plc_TextView_HumanEvaluation);
        //newWords = (Button) myFragmentView.findViewById(R.id.plc_button_NewWords);
        nextPlate = (Button) myFragmentView.findViewById(R.id.plc_button_NextPlate);
        nextPlate.setText(LocalizationManager.getTextPCNext());
        exit = (Button) myFragmentView.findViewById(R.id.plc_button_Exit);
        exit.setText(LocalizationManager.getTextPCEnd());

        nextPlate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                advanceToNextPageData();
            }
        });
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return myFragmentView;
    }

    private void advanceToNextPageData() {
        final UserLanguage languageInfo = UserLanguage.getUserLanguageInfo(LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang);
        if (languageInfo == null) {
            MyQuickToast.showShort(getActivity(), "Error: no language information specified");
            getActivity().finish();
            return;
        }
        languageInfo.page += 1;

        new ServiceSetLanguagePage().doRequest(LocalSettings.getUserId(), languageInfo.baseLang, languageInfo.learningLang, languageInfo.page,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        languageInfo.save();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), ActivityLearning.class));
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(getActivity(), "Error: " + errorMessage);
                        getActivity().finish();
                    }
                });
    }

    private void refresh() {
        LogUtil.logDeubg(this, "refresh");

        int currentGood = 0;
        int currentBad = 0;
        int currentNew = 0;
        for (Performance perf : DataPool.getAllPerformance(true)) {
            if (perf.performance.contains("good")) {
                currentGood += 1;
            } else if (perf.performance.contains("bad")) {
                currentBad += 1;
            } else if (perf.performance.contains("new")) {
                currentNew += 1;
            }
        }
        performance.setText(currentGood + "/" + DataPool.getAllPerformance(false).size());
        if (DataPool.LmType != Learning_Type_Review) {
            skip.setText("skipped: " + currentNew);
        }
        birthday.setText("");
        birthdayDetail.setText("");

        new ServiceSetUserPerformance().doRequest(LocalSettings.getUserId(), false, DataPool.getAllPerformance(true), "all",
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        MyQuickToast.showShort(getActivity(), "Your performance is recorded.");

                        new ServiceGetUserPerformanceSum().doRequest(LocalSettings.getUserId(), LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang,
                                new HttpResultHandler() {

                                    public void hasResult(Object resultObject) {
                                        Result r = (Result) resultObject;
                                        vocabSize.setText("Total Vocab: " + r.totalGood + "/" + r.total);
                                        double effort = (double) r.totalVersion / r.total;
                                        evaluation.setText("Learning Effort: " + decimalFormat.format(effort) + " revisions per word");
                                    }

                                    public void noResult(String errorMessage) {
                                        MyQuickToast.showShort(getActivity(), "Cannot get your performance summary");
                                    }
                                });
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(getActivity(), "Cannot record your performance.");
                    }
                });

    }
}
