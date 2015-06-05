package com.openwords.ui.lily;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.openwords.R;

/**
 *
 * @author hanaldo
 */
public class DialogCreateDeck extends Dialog {

    public DialogCreateDeck(Context context, String title) {
        super(context);
        setTitle(title);
        setContentView(R.layout.dialog_create_deck);
        EditText input = (EditText) findViewById(R.id.dialog_create_deck_edit1);
        Button ok = (Button) findViewById(R.id.dialog_create_deck_button1);
        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DialogCreateDeck.this.cancel();
            }
        });
    }
}
