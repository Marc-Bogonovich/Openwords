package com.openwords.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author han
 */
public class Speak implements TextToSpeech.OnInitListener {

    private static final String className = Speak.class.getName();
    private static Speak instance;

    public static Speak getInstance(Context context) {
        if (instance == null) {
            instance = new Speak(context);
        }
        return instance;
    }
    private TextToSpeech tts;
    private final HashMap<String, String> speak_ids;
    private boolean speakAvailable = false;

    private Speak(Context context) {
        speak_ids = new HashMap<String, String>(0);
        tts = new TextToSpeech(context, this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId.equals("end_speak")) {
                    Log.d(this.getClass().getName(), "onDone()");
                }
            }

            @Override
            public void onError(String utteranceId) {
                Log.d(this.getClass().getName(), "onError()");
            }
        });
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d(className, "can start to speak");

            tts.setLanguage(Locale.US);
            speakAvailable = true;
        } else {
            Log.d(className, "failed to init");
        }
    }

    public void speak(String message) {
        if (speakAvailable) {
            speak_ids.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end_speak");
            tts.speak(message, TextToSpeech.QUEUE_ADD, speak_ids);
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
