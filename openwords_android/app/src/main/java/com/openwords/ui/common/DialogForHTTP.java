package com.openwords.ui.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.util.log.LogUtil;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.Header;

public class DialogForHTTP {

    private final Context context;
    private ProgressDialog dialogInstance;
    private final String dialogTitle, dialogContent, url;
    private final int timeout;
    private AsyncHttpClient http;
    private final RequestParams httpParams;
    private final OnSuccess callbackSuccess;
    private final OnFailure callbackFail;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * Prepare the Dialog content.
     *
     * @param context
     * @param dialogTitle Can be null.
     * @param dialogContent
     * @param url
     * @param timeout
     * @param httpParams
     * @param callbackSuccess
     * @param callbackFail
     */
    public DialogForHTTP(Context context, String dialogTitle, String dialogContent, String url, int timeout, RequestParams httpParams, OnSuccess callbackSuccess, OnFailure callbackFail) {
        this.context = context;
        this.dialogTitle = dialogTitle;
        this.dialogContent = dialogContent;
        this.url = url;
        this.timeout = timeout;
        this.httpParams = httpParams;
        this.callbackSuccess = callbackSuccess;
        this.callbackFail = callbackFail;
    }

    /**
     * Show the Dialog and start the HTTP request.
     */
    public void start() {
        dialogInstance = ProgressDialog.show(context, dialogTitle, dialogContent, false, true, new DialogInterface.OnCancelListener() {

            public void onCancel(final DialogInterface di) {
                new AlertDialog.Builder(context)
                        .setTitle("Cancel Downloading?")
                        .setMessage("Do you want to cancel \"" + dialogTitle + "\"?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                cancelled.set(true);
                                LogUtil.logDeubg(DialogForHTTP.this, "Try cancel...");
                                http.cancelAllRequests(true);
                                callbackFail.onFailure(-1, null, null, new Exception("User cancelled"));
                            }
                        })
                        .create().show();
            }
        });

        http = new AsyncHttpClient();
        http.setTimeout(timeout);
        http.post(url, httpParams, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                dialogInstance.dismiss();
                if (!cancelled.get()) {
                    callbackSuccess.onSuccess(i, headers, string);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                dialogInstance.dismiss();
                if (!cancelled.get()) {
                    callbackFail.onFailure(i, headers, string, thrwbl);
                }
            }

        });
    }

    public interface OnSuccess {

        public void onSuccess(int i, Header[] headers, String string);
    }

    public interface OnFailure {

        public void onFailure(int i, Header[] headers, String string, Throwable thrwbl);
    }
}
