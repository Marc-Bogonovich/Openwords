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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultSetItems;
import com.openwords.model.ResultWordSets;
import com.openwords.model.SetInfo;
import com.openwords.model.SetItem;
import com.openwords.services.implementations.ServiceGetUserPublicInfo;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.time.DateFormatUtils;

public class PageSetsList extends Activity {

    public static int mode;
    public final static int Mode_Study = 1;
    public final static int Mode_Manage = 2;
    private GridView listAllSets;
    private ViewDeckCircle buttonMake, buttonMy, buttonAll;
    private EditText searchInput;
    private Timer searchInputTimer;
    private Handler finishInput;
    private ListAdapterWordSets listAdapter;
    private ImageView buttonBack;
    private List<SetInfo> allSets;
    private TextView title, buttonTextMake, buttonTextMy, buttonTextAll;
    private DialogForSettingSelection settingDialog;
    private int searchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lily_page_setlist);

        title = (TextView) findViewById(R.id.act_sl_text1);
        if (mode == Mode_Study) {
            title.setText(LocalizationManager.getButtonPractice());
        } else if (mode == Mode_Manage) {
            title.setText(LocalizationManager.getButtonManageSets());
            buildButtonMake();
        }
        buildButtonMy();
        buildButtonAll();

        buttonBack = (ImageView) findViewById(R.id.act_sl_image_1);
        buttonBack.setColorFilter(getResources().getColor(R.color.main_app_color), PorterDuff.Mode.MULTIPLY);
        listAllSets = (GridView) findViewById(R.id.act_sl_grid1);

        allSets = new LinkedList<SetInfo>();
        listAdapter = new ListAdapterWordSets(this, allSets, LocalSettings.getUserId());
        listAllSets.setAdapter(listAdapter);
        listAllSets.requestFocus();
        listAllSets.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (mode) {
                    case Mode_Study:
                        if (settingDialog != null) {
                            settingDialog.cancel();
                            settingDialog = null;
                        }
                        settingDialog = new DialogForSettingSelection(PageSetsList.this)
                                .addItem("Details")
                                .addItem(LocalizationManager.getButtonStudy())
                                .build(new AdapterView.OnItemClickListener() {

                                    public void onItemClick(AdapterView<?> parent, View view, int optionPosition, long id) {
                                        final SetInfo set = allSets.get(position);
                                        switch (optionPosition) {
                                            case 0:
                                                new ServiceGetUserPublicInfo().doRequest(set.userId, new HttpResultHandler() {

                                                    public void hasResult(Object resultObject) {
                                                        ServiceGetUserPublicInfo.Result r = (ServiceGetUserPublicInfo.Result) resultObject;
                                                        DialogSetOverview d = new DialogSetOverview(PageSetsList.this,
                                                                new SetDetails("This is ...",
                                                                        r.username,
                                                                        DateFormatUtils.format(set.updatedTimeLong, "yyyy-MM-dd HH:mm"),
                                                                        "xxx, yyy...",
                                                                        null));
                                                        d.show();
                                                    }

                                                    public void noResult(String errorMessage) {
                                                        MyQuickToast.showShort(PageSetsList.this, "Cannot get user info: " + errorMessage);
                                                    }
                                                });
                                                break;
                                            case 1:
                                                loadSet(set, new SetLoadOk() {

                                                    public void done() {
                                                        startActivity(new Intent(PageSetsList.this, PageLMOption.class));
                                                    }
                                                });
                                                break;
                                        }
                                        settingDialog.cancel();
                                        settingDialog = null;
                                    }
                                }, (int) view.getX(), (int) view.getY());
                        settingDialog.show();
                        break;
                    case Mode_Manage:
                        SetInfo set = allSets.get(position);
                        loadSet(set, new SetLoadOk() {

                            public void done() {
                                startActivity(new Intent(PageSetsList.this, PageSetContent.class));
                            }
                        });
                        break;
                    default:
                        break;
                }
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
                    String term = searchInput.getText().toString().trim();

                    MyDialogHelper.tryShowQuickProgressDialog(PageSetsList.this, "Searching word sets...");
                    SetInfo.getAllSets(1, 50, LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage(), searchUser, term, new ResultWordSets() {

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
                }, 1500);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageSetsList.super.onBackPressed();
            }
        });
    }

    private void buildButtonMy() {
        View.OnClickListener click = new View.OnClickListener() {
            public void onClick(View v) {
                MyDialogHelper.tryShowQuickProgressDialog(PageSetsList.this, "Loading word sets...");
                searchUser = LocalSettings.getUserId();
                SetInfo.getAllSets(1, 50, LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage(), searchUser, null, new ResultWordSets() {

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
        };

        buttonMy = (ViewDeckCircle) findViewById(R.id.page_sl_button_my);
        buttonMy.config(getResources().getColor(R.color.blue), 255,
                R.drawable.ic_study, 0,
                0, 0);
        buttonMy.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                buttonMy.updateDimension(v.getWidth(), v.getHeight());
            }
        });
        buttonMy.setOnClickListener(click);

        buttonTextMy = (TextView) findViewById(R.id.page_sl_buttontext_my);
        buttonTextMy.setText("My Sets");
        buttonTextMy.setTextColor(getResources().getColor(R.color.blue));
        buttonTextMy.setOnClickListener(click);

        LinearLayout group = (LinearLayout) findViewById(R.id.page_sl_group_my);
        group.setVisibility(View.VISIBLE);
    }

    private void buildButtonAll() {
        View.OnClickListener click = new View.OnClickListener() {
            public void onClick(View v) {
                MyDialogHelper.tryShowQuickProgressDialog(PageSetsList.this, "Loading word sets...");
                searchUser = 0;
                SetInfo.getAllSets(1, 50, LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage(), searchUser, null, new ResultWordSets() {

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
        };

        buttonAll = (ViewDeckCircle) findViewById(R.id.page_sl_button_all);
        buttonAll.config(getResources().getColor(R.color.blue), 255,
                R.drawable.ic_global1, 0,
                0, 0);
        buttonAll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                buttonAll.updateDimension(v.getWidth(), v.getHeight());
            }
        });
        buttonAll.setOnClickListener(click);

        buttonTextAll = (TextView) findViewById(R.id.page_sl_buttontext_all);
        buttonTextAll.setText("All Sets");
        buttonTextAll.setTextColor(getResources().getColor(R.color.blue));
        buttonTextAll.setOnClickListener(click);

        LinearLayout group = (LinearLayout) findViewById(R.id.page_sl_group_all);
        group.setVisibility(View.VISIBLE);
    }

    private void buildButtonMake() {
        View.OnClickListener clickCreate = new View.OnClickListener() {
            public void onClick(View v) {
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
        };
        buttonMake = (ViewDeckCircle) findViewById(R.id.page_sl_button_make);
        buttonMake.config(getResources().getColor(R.color.blue), 255,
                R.drawable.ic_plus, 0,
                0, 0);
        buttonMake.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                buttonMake.updateDimension(v.getWidth(), v.getHeight());
            }
        });
        buttonMake.setOnClickListener(clickCreate);

        buttonTextMake = (TextView) findViewById(R.id.page_sl_buttontext_make);
        buttonTextMake.setText(LocalizationManager.getButtonCreate());
        buttonTextMake.setTextColor(getResources().getColor(R.color.blue));
        buttonTextMake.setOnClickListener(clickCreate);

        LinearLayout group = (LinearLayout) findViewById(R.id.page_sl_group_make);
        group.setVisibility(View.VISIBLE);
    }

    private void refreshListView(List<SetInfo> sets) {
        listAdapter.clear();
        listAdapter.addAll(sets);
        listAdapter.notifyDataSetChanged();
    }

    private void loadSet(final SetInfo set, final SetLoadOk ok) {
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
                applySetAndGo(set, items, ok);
            }
            MyDialogHelper.tryDismissQuickProgressDialog();
        } else {
            SetItem.getItems(set.setId, set.userId, new ResultSetItems() {

                public void result(List<SetItem> result) {
                    if (result == null) {
                        MyQuickToast.showShort(PageSetsList.this, "Cannot load content");
                    } else {
                        applySetAndGo(set, result, ok);
                    }
                    MyDialogHelper.tryDismissQuickProgressDialog();
                }
            });
        }
    }

    private void applySetAndGo(SetInfo set, List<SetItem> items, SetLoadOk ok) {
        DataPool.currentSet.copyAllValues(set);
        DataPool.currentSetItems.clear();
        DataPool.currentSetItems.addAll(items);
        if (ok != null) {
            ok.done();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDialogHelper.tryShowQuickProgressDialog(this, "Loading all word sets...");
        if (DataPool.OffLine) {
            List<SetInfo> sets = SetInfo.loadAllSets(LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage());
            if (sets.isEmpty()) {
                MyQuickToast.showShort(PageSetsList.this, "No sets available.");
            } else {
                refreshListView(sets);
            }
            MyDialogHelper.tryDismissQuickProgressDialog();
        } else {
            searchUser = 0;
            SetInfo.getAllSets(1, 50, LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage(), searchUser, null, new ResultWordSets() {

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

    private interface SetLoadOk {

        public void done();
    }
}
