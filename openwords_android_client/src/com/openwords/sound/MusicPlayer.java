package com.openwords.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.openwords.util.log.LogUtil;
import java.io.IOException;

/**
 *
 * @author hanaldo
 */
public class MusicPlayer {

    private static MusicPlayer instance;

    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    public static void clean() {
        if (instance != null) {
            instance.release();
            LogUtil.logDeubg(MusicPlayer.class, "Clean MusicPlayer");
        }
    }
    private MediaPlayer mediaPlayer;
    private MusicDoneCallback musicDoneCallback;

    private MusicPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    public void setMusicDoneCallback(MusicDoneCallback musicDoneCallback) {
        this.musicDoneCallback = musicDoneCallback;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void playMusic(String path, boolean local) {
        try {
            mediaPlayer.reset();
            if (local) {
                mediaPlayer.setDataSource(path);
                LogUtil.logDeubg(this, "prepare song at: " + path);

            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(path);
                LogUtil.logDeubg(this, "prepare song at: " + path);
            }
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    LogUtil.logDeubg(this, "onCompletion");
                    stopMusic();
                    if (musicDoneCallback != null) {
                        LogUtil.logDeubg(this, "execute musicDoneCallback");
                        musicDoneCallback.done();
                    }
                }
            });
            LogUtil.logDeubg(this, "we have musicDoneCallback: " + musicDoneCallback);
            LogUtil.logDeubg(this, "play " + path);
            mediaPlayer.start();

        } catch (IOException e) {
            LogUtil.logWarning(this, e);
        }
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    private void release() {
        mediaPlayer.release();
    }

    public interface MusicDoneCallback {

        public void done();
    }

}
