package com.openwords.ui.lily.lm;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openwords.R;
import com.openwords.learningmodule.FragmentLearningModule;
import com.openwords.sound.SoundPlayer;
import com.openwords.util.log.LogUtil;
import java.io.IOException;

public class FragmentHear extends FragmentLearningModule {

    private final int cardIndex;
    private View myFragmentView;
    private ViewSoundBackground soundButton;
    private ViewHomeBackground homeButton;

    public FragmentHear(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.logDeubg(this, "onDestroy for card: " + cardIndex);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.logDeubg(this, "onDetach for card: " + cardIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        String w1 = "", w2 = "", transcription = "";
        final String[] mp3 = {""};
        switch (cardIndex) {
            case 0:
                w1 = "胖";
                w2 = "fat";
                transcription = "pàng";
                mp3[0] = "pang.mp3";
                break;
            case 1:
                w1 = "童年的朋友";
                w2 = "a friend known since childhood";
                transcription = "tóng nián dè péng yǒu";
                mp3[0] = "tong.mp3";
                break;
            case 2:
                w1 = "培根鸡蛋";
                w2 = "a dish consisting of bacon and eggs";
                transcription = "péi gēn jī dàn";
                mp3[0] = "pei.mp3";
                break;
        }

        myFragmentView = inflater.inflate(R.layout.fragment_lily_hear, container, false);

        soundButton = (ViewSoundBackground) myFragmentView.findViewById(R.id.lily_button_sound_bg);
        soundButton.config(myFragmentView, Color.parseColor("#ff00ff"), 255, false, new View.OnClickListener() {

            public void onClick(View view) {
                soundButton.touchAnimation();
                try {
                    AssetFileDescriptor test = getActivity().getAssets().openFd(mp3[0]);
                    SoundPlayer.playSound(test);
                } catch (IOException ex) {
                    LogUtil.logWarning(this, ex);
                }

            }
        });

        homeButton = (ViewHomeBackground) myFragmentView.findViewById(R.id.lily_button_home_bg);
        homeButton.config(myFragmentView, Color.parseColor("#ff00ff"), 255, true, new View.OnClickListener() {

            public void onClick(View view) {
                homeButton.touchAnimation();
                getActivity().onBackPressed();
            }
        });

        ViewMaxText hint = (ViewMaxText) myFragmentView.findViewById(R.id.frag_hear_text1);
        hint.config(Color.parseColor("#ff00ff"), 255, "?", 128, w2);

        return myFragmentView;
    }
}
