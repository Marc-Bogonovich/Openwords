package com.openwords.learningmodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.WordMeaning;
import com.openwords.sound.SoundPlayer;
import com.openwords.sound.WordAudioManager;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.List;

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
            audioPlay.setSoundEffectsEnabled(false);
        }
    }

    public void addClarificationTrigger(final Activity activity, View[] actions, final View near, final String message) {
        for (View v : actions) {
            v.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    try {
                        showInfo(activity, near, message);
                    } catch (Exception e) {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showInfo(Activity activity, View near, String message) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .create();
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layout = dialog.getWindow().getAttributes();
        layout.gravity = Gravity.CENTER;
        layout.y = near.getMeasuredHeight();

        dialog.show();
    }
}
