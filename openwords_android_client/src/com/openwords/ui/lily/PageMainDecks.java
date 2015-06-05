package com.openwords.ui.lily;

import android.app.Activity;
import static android.content.Context.INPUT_METHOD_SERVICE;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import com.openwords.R;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author hanaldo
 */
public class PageMainDecks extends Activity {

    private GridView gridView, gridView2;
    private EditText searchInput;
    private Timer searchInputTimer;
    private Handler finishInput;
    private ListAdapterDeckGrid deckListAdapter;
    private List<DeckInfo> deckListDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main_decks);

        gridView = (GridView) findViewById(R.id.act_main_decks_gridview1);
        deckListDataModel = DeckInfo.getNewTestingDecks();
        int i = 1;
        while (i < 500) {
            deckListDataModel.add(new DeckInfo(String.valueOf(i)));
            i += 1;
        }
        deckListAdapter = new ListAdapterDeckGrid(this, deckListDataModel);
        gridView.setAdapter(deckListAdapter);
        gridView.requestFocus();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeckInfo deck = deckListDataModel.get(position);
                MyQuickToast.showShort(PageMainDecks.this, "Deck: " + deck.name);
            }
        });

        gridView2 = (GridView) findViewById(R.id.act_main_decks_gridview2);
        LinkedList<DeckInfo> addDeck = new LinkedList<DeckInfo>();
        addDeck.add(null);
        addDeck.add(new DeckInfo("Create Deck", true));
        addDeck.add(null);
        gridView2.setAdapter(new ListAdapterDeckGrid(this, addDeck));
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new DialogCreateDeck(PageMainDecks.this, "Create Deck").show();
            }
        });

        finishInput = new Handler(new Callback() {

            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    View focus = getCurrentFocus();
                    if (focus != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
                    }
                    deckListAdapter.clear();
                    String term = searchInput.getText().toString().trim();
                    deckListDataModel = DeckInfo.searchDecks(term);
                    deckListAdapter.addAll(deckListDataModel);
                    deckListAdapter.notifyDataSetChanged();
                    MyQuickToast.showShort(PageMainDecks.this, String.format("You got %s decks", deckListDataModel.size()));
                }
                return true;
            }
        });
        searchInput = (EditText) findViewById(R.id.act_main_decks_edit1);
        searchInput.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (searchInputTimer != null) {
                    searchInputTimer.cancel();
                    searchInputTimer = null;
                }
            }

            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                //do nothing
            }

            public void afterTextChanged(Editable edtbl) {
                if (searchInputTimer != null) {
                    searchInputTimer.cancel();
                    searchInputTimer = null;
                }
                if (edtbl.toString().trim().isEmpty()) {
                    return;
                }
                searchInputTimer = new Timer();
                searchInputTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        finishInput.sendEmptyMessage(0);
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
