package com.openwords.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import com.openwords.util.log.LogUtil;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author han
 */
public class Speak implements TextToSpeech.OnInitListener {

    private static Speak instance;

    public static Speak getInstance(Context context) {
        if (instance == null) {
            instance = new Speak(context);
        }
        return instance;
    }
    private TextToSpeech tts;
    private final HashMap<String, String> speakIds;
    private boolean speakAvailable = false;

    private Speak(Context context) {
        speakIds = new HashMap<String, String>(0);
        tts = new TextToSpeech(context, this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId.equals("end_speak")) {
                    LogUtil.logDeubg(this, "onDone()");
                    //speak.clean();
                }
            }

            @Override
            public void onError(String utteranceId) {
                LogUtil.logDeubg(this, "onError()");
                //speak.clean();
            }
        });
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            LogUtil.logDeubg(this, "Can start to speak");

            tts.setLanguage(Locale.ENGLISH);
            speakAvailable = true;
        } else {
            LogUtil.logDeubg(this, "Failed to init");
        }
    }

    public void speak(String message) {
        if (speakAvailable) {
            speakIds.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end_speak");
            tts.speak(message, TextToSpeech.QUEUE_ADD, speakIds);
        }
    }

    public void clean() {
        if (tts != null) {
            tts.shutdown();
            tts = null;
            instance = null;
        }
    }
}
