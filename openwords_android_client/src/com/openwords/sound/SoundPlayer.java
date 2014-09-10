package com.openwords.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.openwords.util.log.LogUtil;
import java.io.IOException;

public class SoundPlayer {

    private static MediaPlayer mediaPlayer;

    public static void clean() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            LogUtil.logDeubg(SoundPlayer.class, "Clean SoundPlayer");
        }
    }

    public static void playMusic(String path, boolean local) {
        try {
            clean();
            mediaPlayer = new MediaPlayer();
            if (local) {
                mediaPlayer.setDataSource(path);
                LogUtil.logDeubg(SoundPlayer.class, "Prepare sound at: " + path);

            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(path);
                LogUtil.logDeubg(SoundPlayer.class, "Prepare sound at: " + path);
            }
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    LogUtil.logDeubg(this, "onCompletion");
                    mediaPlayer.stop();
                    clean();
                }
            });
            LogUtil.logDeubg(SoundPlayer.class, "play " + path);
            mediaPlayer.start();

        } catch (IOException e) {
            LogUtil.logWarning(SoundPlayer.class, e);
        }
    }

    private SoundPlayer() {
    }

}
