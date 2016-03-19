package com.openwords.ui.lily.lm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.openwords.ui.graphics.AnimationTimerBar;

import java.util.Timer;
import java.util.TimerTask;


public class TimerBarAnimation {
    private View advanceTimerBar;
    private Animation advanceTimerAnimation;
    private boolean timerIsFired;
    private Timer advanceTimer;
    private Context context;
    private int duration;
    private TimerDone callback;

    public TimerBarAnimation(Context context, View advanceTimerBar, int duration, TimerDone callback) {
        this.context = context;
        this.advanceTimerBar = advanceTimerBar;
        this.duration = duration;
        this.callback = callback;
        advanceTimerAnimation = new AnimationTimerBar(0, 100, advanceTimerBar);
        advanceTimerAnimation.setDuration(duration);
    }

    public synchronized void fire() {
        if (!timerIsFired) {
            timerIsFired = true;
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .create();
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface di) {
                    advanceTimer.cancel();
                    advanceTimerBar.setVisibility(View.INVISIBLE);
                    advanceTimerBar.clearAnimation();
                    timerIsFired = false;
                }
            });
            dialog.show();

            advanceTimerBar.clearAnimation();
            advanceTimerBar.startAnimation(advanceTimerAnimation);
            advanceTimer = new Timer();
            final Handler next = new Handler(new Handler.Callback() {

                public boolean handleMessage(Message msg) {
                    if (msg.what == 0) {
                        dialog.cancel();
                        timerIsFired = false;
                        if (callback != null) {
                            callback.done();
                        }
                    }
                    return true;
                }
            });
            advanceTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    next.sendEmptyMessage(0);
                }
            }, duration);
        }
    }

    public interface TimerDone {
        void done();
    }
}
