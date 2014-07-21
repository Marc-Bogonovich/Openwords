package com.openwords.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
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
    private MediaPlayer mediaPlayer;
    private MusicDoneCallback musicDoneCallback;
    private boolean hasSongNow;

    private MusicPlayer() {
        hasSongNow = false;
    }

    public void setMusicDoneCallback(MusicDoneCallback musicDoneCallback) {
        this.musicDoneCallback = musicDoneCallback;
    }

    public boolean hasSongNow() {
        return hasSongNow;
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public void playMusic(String fileName, boolean local) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                if (local) {
                    String songPath = Environment.getExternalStorageDirectory().getPath() + "/OpenwordsSound/" + fileName;
                    mediaPlayer.setDataSource(songPath);
                    LogUtil.logDeubg(this, "prepare song at: " + songPath);

                } else {
                    String songPath = "http://www.openwords.com/api-v1/audio/" + fileName;

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(songPath);
                    LogUtil.logDeubg(this, "prepare song at: " + songPath);
                }
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer arg0) {
                        stopMusic();
                        if (musicDoneCallback != null) {
                            LogUtil.logDeubg(this, "execute musicDoneCallback");
                            musicDoneCallback.done();
                        }
                    }
                });
                LogUtil.logDeubg(this, "we have musicDoneCallback: " + musicDoneCallback);
                LogUtil.logDeubg(this, "play " + fileName);
                mediaPlayer.start();
            } else {
                mediaPlayer.start();
            }
            hasSongNow = true;
        } catch (IOException e) {
            LogUtil.logWarning(this, e);
        }
    }

    public void stopMusic() {
        clean();
        hasSongNow = false;
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void clean() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            LogUtil.logDeubg(this, "clean MusicPlayer");
        }
    }

    public interface MusicDoneCallback {

        public void done();
    }

}
