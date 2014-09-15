package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.sound.SoundPlayer;
import com.openwords.sound.WordAudioManager;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;

/**
 *
 * @author hanaldo
 */
public abstract class FragmentLearningModule extends Fragment {

    public void updateAudioIcon(ImageView audioPlay, int wordId) {
        final String audio = WordAudioManager.hasAudio(wordId);
        if (audio == null) {
            audioPlay.setImageResource(R.drawable.ic_self_evaluate_audio_null);
        } else {
            audioPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    LogUtil.logDeubg(this, "Play: " + audio);
                    SoundPlayer.playMusic(LocalFileSystem.getAudioFullPath(audio), true);
                }
            });
        }
    }
}
