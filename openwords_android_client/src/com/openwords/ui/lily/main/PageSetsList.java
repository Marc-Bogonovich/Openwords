package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultSetItems;
import com.openwords.model.ResultWordSets;
import com.openwords.model.SetInfo;
import com.openwords.model.SetItem;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PageSetsList extends Activity {

    private GridView listAllSets, buttonMake;
    private EditText searchInput;
    private Timer searchInputTimer;
    private Handler finishInput;
    private ListAdapterWordSets listAdapter;
    private ImageView buttonBack;
    private List<SetInfo> allSets;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_lily_sets_list_page);

        title = (TextView) findViewById(R.id.act_sl_text1);
        title.setText(LocalizationManager.getTitleWordSets());

        buttonBack = (ImageView) findViewById(R.id.act_sl_image_1);
        buttonBack.setColorFilter(getResources().getColor(R.color.main_app_color), PorterDuff.Mode.MULTIPLY);
        listAllSets = (GridView) findViewById(R.id.act_sl_grid1);

        allSets = new LinkedList<SetInfo>();
        listAdapter = new ListAdapterWordSets(this, allSets, LocalSettings.getUserId());
        listAllSets.setAdapter(listAdapter);
        listAllSets.requestFocus();
        listAllSets.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SetInfo set = allSets.get(position);
                loadSet(set);
            }
        });

        buttonMake = (GridView) findViewById(R.id.act_sl_grid2);
        LinkedList<SetInfo> buttonContent = new LinkedList<SetInfo>();
        buttonContent.add(null);
        buttonContent.add(new SetInfo(LocalizationManager.getButtonCreate(), true));
        buttonContent.add(null);
        buttonMake.setAdapter(new ListAdapterWordSets(this, buttonContent, LocalSettings.getUserId()));
        buttonMake.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (DataPool.OffLine) {
                    MyQuickToast.showShort(PageSetsList.this, "Cannot create set in offline mode.");
                    return;
                }
                DataPool.currentSet.setId = -1;
                DataPool.currentSet.name = null;
                DataPool.currentSetItems.clear();
                DataPool.currentSetItems.add(new SetItem(0, LocalSettings.getBaseLanguage().displayName,
                        LocalSettings.getLearningLanguage().displayName, true, true));
                PageSetsList.this.startActivity(new Intent(PageSetsList.this, PageSetContent.class));
            }
        });

        finishInput = new Handler(new Callback() {

            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    MyQuickToast.showShort(PageSetsList.this, "Searching is not supported yet.");
//                    View focus = getCurrentFocus();
//                    if (focus != null) {
//                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                        inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
//                    }
//                    listAdapter.clear();
//                    String term = searchInput.getText().toString().trim();
//                    //deckListDataModel = DeckInfo.searchDecks(term);
//                    //listAdapter.addAll(deckListDataModel);
//                    listAdapter.notifyDataSetChanged();
//                    //MyQuickToast.showShort(PageSetsList.this, String.format("You got %s decks", deckListDataModel.size()));
                }
                return true;
            }
        });
        searchInput = (EditText) findViewById(R.id.act_sl_edit1);
        searchInput.setHint(LocalizationManager.getHintSearchSets());
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

        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageSetsList.super.onBackPressed();
            }
        });
    }

    private void refreshListView(List<SetInfo> sets) {
        listAdapter.clear();
        listAdapter.addAll(sets);
        listAdapter.notifyDataSetChanged();
    }

    private void loadSet(final SetInfo set) {
        if (set.nativeLang != LocalSettings.getBaseLanguageId()
                || set.learningLang != LocalSettings.getCurrentLearningLanguage()) {
            MyQuickToast.showShort(PageSetsList.this, "This set is not for your chosen languages.");
            return;
        }
        MyDialogHelper.tryShowQuickProgressDialog(this, "Loading set content...");
        if (DataPool.OffLine) {
            List<SetItem> items = SetItem.loadAllItems(set.setId);
            if (items.isEmpty()) {
                MyQuickToast.showShort(PageSetsList.this, "This set is not available.");
            } else {
                applySetAndGo(set, items);
            }
            MyDialogHelper.tryDismissQuickProgressDialog();
        } else {
            SetItem.getItems(set.setId, set.userId, new ResultSetItems() {

                public void result(List<SetItem> result) {
                    if (result == null) {
                        MyQuickToast.showShort(PageSetsList.this, "Cannot load content");
                    } else {
                        applySetAndGo(set, result);
                    }
                    MyDialogHelper.tryDismissQuickProgressDialog();
                }
            });
        }
    }

    private void applySetAndGo(SetInfo set, List<SetItem> items) {
        DataPool.currentSet.copyAllValues(set);
        DataPool.currentSetItems.clear();
        DataPool.currentSetItems.addAll(items);
        PageSetsList.this.startActivity(new Intent(PageSetsList.this, PageSetContent.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDialogHelper.tryShowQuickProgressDialog(this, "Loading all word sets...");
        if (DataPool.OffLine) {
            List<SetInfo> sets = SetInfo.loadAllSets();
            if (sets.isEmpty()) {
                MyQuickToast.showShort(PageSetsList.this, "No sets available.");
            } else {
                refreshListView(sets);
            }
            MyDialogHelper.tryDismissQuickProgressDialog();
        } else {
            SetInfo.getAllSets(1, 50, new ResultWordSets() {

                public void result(List<SetInfo> result) {
                    if (result != null) {
                        refreshListView(result);
                    } else {
                        MyQuickToast.showShort(PageSetsList.this, "Cannot load sets.");
                    }
                    MyDialogHelper.tryDismissQuickProgressDialog();
                }
            });
        }
    }
}
