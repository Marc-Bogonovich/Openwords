package com.openwords.ui.lily.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultSetSaveAll;
import com.openwords.model.SetInfo;
import com.openwords.model.SetItem;
import com.openwords.model.Word;
import com.openwords.services.implementations.ServiceCopySet;
import com.openwords.services.implementations.ServiceSearchWords;
import com.openwords.services.implementations.ServiceSearchWords.Result;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.CallbackCancelButton;
import com.openwords.util.ui.CallbackOkButton;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class PageSetContent extends Activity {
    
    private ListView itemList;
    private ListAdapterWordSetItem listAdapter;
    private ImageView buttonMode, buttonBack, buttonCopy;
    private EditText setTitleInput;
    private TextView setTitle;
    private boolean isEditingMode, contentHasJustChanged;
    private CopyOnWriteArrayList<SetItem> setItems;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.lily_page_setcontent);
        itemList = (ListView) findViewById(R.id.act_ws_list1);
        setTitle = (TextView) findViewById(R.id.act_ws_text1);
        setTitleInput = (EditText) findViewById(R.id.act_ws_edit1);
        buttonMode = (ImageView) findViewById(R.id.act_ws_image_1);
        buttonBack = (ImageView) findViewById(R.id.act_ws_image_2);
        buttonCopy = (ImageView) findViewById(R.id.act_ws_image_3);
        
        if (DataPool.currentSet.name == null) {
            isEditingMode = true;
        } else {
            isEditingMode = false;
            setTitle.setText(DataPool.currentSet.name);
            setTitleInput.setText(DataPool.currentSet.name);
        }
        
        buttonMode.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
                buttonModeOnClick();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
                PageSetContent.super.onBackPressed();
            }
        });
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PageSetContent.this);
                builder.setTitle(LocalizationManager.getConfirmCopySetTitle());
                final EditText input = new EditText(PageSetContent.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint(LocalizationManager.getHintSetName());
                builder.setView(input);
                
                builder.setPositiveButton(LocalizationManager.getOk(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = input.getText().toString();
                        if (name.isEmpty()) {
                            MyQuickToast.showShort(PageSetContent.this, LocalizationManager.getErrorEmpty());
                            return;
                        }
                        dialog.dismiss();
                        
                        MyDialogHelper.showConfirmDialog(PageSetContent.this,
                                LocalizationManager.getConfirmCopySetTitle(),
                                LocalizationManager.getConfirmCopySetContent().replace("@1@", DataPool.currentSet.name).replace("@2@", name),
                                new CallbackOkButton() {
                                    
                                    public void okPressed() {
                                        new ServiceCopySet().doRequest(LocalSettings.getUserId(), DataPool.currentSet.setId, name,
                                                new HttpResultHandler() {
                                                    
                                                    public void hasResult(Object resultObject) {
                                                        MyQuickToast.showShort(PageSetContent.this, LocalizationManager.getErrorCopySuccess());
                                                    }
                                                    
                                                    public void noResult(String errorMessage) {
                                                        MyQuickToast.showShort(PageSetContent.this, LocalizationManager.getErrorCopy() + ": " + errorMessage);
                                                    }
                                                });
                                    }
                                },
                                null);
                    }
                }).setNegativeButton(LocalizationManager.getCancel(), null).show();
            }
        });
        
        if (DataPool.currentSet.userId == LocalSettings.getUserId() || isEditingMode) {
            buttonCopy.setVisibility(View.GONE);
        }
        
        setItems = new CopyOnWriteArrayList<SetItem>();
        listAdapter = new ListAdapterWordSetItem(this, setItems);
        itemList.setAdapter(listAdapter);
        setTitleInput.setHint(LocalizationManager.getHintSetName());
        
        if (isEditingMode) {
            applyEditUI();
        } else {
            applyNonEditUI();
        }
    }
    
    private void refreshListView(List<SetItem> items) {
        listAdapter.clear();
        listAdapter.addAll(items);
        listAdapter.notifyDataSetChanged();
    }
    
    private void buttonModeOnClick() {
        if (DataPool.OffLine) {
            MyQuickToast.showShort(this, LocalizationManager.getErrorEditOffline());
            return;
        }
        if (isEditingMode) {
            if (setTitleInput.getText().toString().isEmpty()) {
                MyQuickToast.showShort(this, LocalizationManager.getErrorSetNameEmpty());
                return;
            }
            int totalItems = 0;
            for (SetItem item : setItems) {
                if (!item.isHead && !item.isNew && !item.isRemoving) {
                    totalItems += 1;
                }
            }
            if (totalItems < 5) {
                MyQuickToast.showShort(this, LocalizationManager.getErrorSetMinItems().replace("@@", "5"));
                return;
            }
            
            String name = setTitleInput.getText().toString();
            checkTitleChange(name);
            if (contentHasJustChanged) {
                MyDialogHelper.showConfirmDialog(this, LocalizationManager.getConfirmSetTitle().replace("@@", name), LocalizationManager.getConfirmSetContent(),
                        new CallbackOkButton() {
                            
                            public void okPressed() {
                                saveAll();
                            }
                        },
                        new CallbackCancelButton() {
                            
                            public void cancelPressed() {
                                isEditingMode = true;
                            }
                        });
            } else {
                MyQuickToast.showShort(PageSetContent.this, "Nothing changed.");
                applyNonEditUI();
            }
        } else {
            applyEditUI();
        }
    }
    
    private void saveAll() {
        String name = setTitleInput.getText().toString();
        List<SetItem> items = new LinkedList<SetItem>();
        for (SetItem item : setItems) {
            if (!item.isHead && !item.isNew && !item.isRemoving) {
                items.add(item);
            }
        }
        MyDialogHelper.tryShowQuickProgressDialog(this, LocalizationManager.getBlockConnectServer());
        SetInfo.saveAll(DataPool.currentSet.setId, DataPool.currentSet.name, name, items, new ResultSetSaveAll() {
            
            public void ok() {
                MyDialogHelper.tryDismissQuickProgressDialog();
                MyQuickToast.showShort(PageSetContent.this, LocalizationManager.getErrorDone());
                applyNonEditUI();
                isEditingMode = false;
            }
            
            public void error(String errorMessage) {
                MyDialogHelper.tryDismissQuickProgressDialog();
                MyDialogHelper.showMessageDialog(PageSetContent.this, LocalizationManager.getError(), errorMessage, null);
                isEditingMode = true;
            }
        });
    }
    
    private void applyNonEditUI() {
        if (DataPool.currentSet.userId == LocalSettings.getUserId()) {
            buttonMode.setVisibility(View.VISIBLE);
        } else {
            buttonMode.setVisibility(View.GONE);
        }
        for (SetItem item : setItems) {
            item.isModifying = false;
            if (item.isHead || item.isNew || item.isRemoving) {
                setItems.remove(item);
            }
        }
        listAdapter.notifyDataSetChanged();
        String name = setTitleInput.getText().toString();
        setTitle.setText(name);
        setTitle.setVisibility(View.VISIBLE);
        setTitleInput.setVisibility(View.GONE);
        buttonMode.setImageResource(R.drawable.ic_set_mode);
        isEditingMode = false;
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m != null) {
            m.hideSoftInputFromWindow(itemList.getWindowToken(), 0);
        }
    }
    
    private void applyEditUI() {
        for (SetItem item : setItems) {
            item.isModifying = true;
        }
        setItems.add(new SetItem(0, LocalSettings.getBaseLanguage().displayName,
                LocalSettings.getLearningLanguage().displayName, true, true));
        listAdapter.notifyDataSetChanged();
        setTitleInput.setText(DataPool.currentSet.name);
        setTitle.setVisibility(View.GONE);
        setTitleInput.setVisibility(View.VISIBLE);
        contentHasJustChanged = false;
        buttonMode.setImageResource(R.drawable.ic_set_mode_save);
        isEditingMode = true;
        itemList.smoothScrollToPosition(setItems.size() - 1);
    }
    
    private void checkTitleChange(String newTitle) {
        if (!newTitle.equals(DataPool.currentSet.name)) {
            contentHasJustChanged = true;
        }
    }
    
    public void search(boolean searchNative, String form) {
        for (SetItem item : setItems) {
            if (item.isNew) {
                setItems.remove(item);
            }
        }
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m != null) {
            m.hideSoftInputFromWindow(itemList.getWindowToken(), 0);
        }
        final int searchLang, targetLang;
        if (searchNative) {
            searchLang = LocalSettings.getBaseLanguageId();
            targetLang = LocalSettings.getCurrentLearningLanguage();
        } else {
            searchLang = LocalSettings.getCurrentLearningLanguage();
            targetLang = LocalSettings.getBaseLanguageId();
        }
        MyDialogHelper.tryShowQuickProgressDialog(this, LocalizationManager.getBlockSearching());
        new ServiceSearchWords().doRequest(20, targetLang, searchLang, form, new HttpResultHandler() {
            
            public void hasResult(Object resultObject) {
                MyDialogHelper.tryDismissQuickProgressDialog();
                Result r = (Result) resultObject;
                Map<Long, Word> allWords = new HashMap<Long, Word>(r.searchResult.size() + r.targetResult.size());
                for (Word w : r.targetResult) {
                    allWords.put(w.wordId, w);
                }
                for (Word w : r.searchResult) {
                    allWords.put(w.wordId, w);
                }
                
                for (Entry<Long, Set<Long>> e : r.linkedSearchWords.entrySet()) {
                    if (r.linkedTargetWords.containsKey(e.getKey())) {
                        long firstSearchWord = 0;
                        String searchWord = "";
                        int i = 1;
                        for (Long searchWordId : e.getValue()) {
                            if (i == 2) {
                                searchWord += " (";
                            }
                            searchWord += allWords.get(searchWordId).word + ",";
                            if (i == 1) {
                                searchWord = searchWord.replace(",", "");
                                firstSearchWord = searchWordId;
                            }
                            i += 1;
                        }
                        if (i > 2) {
                            searchWord += ")";
                        }
                        
                        if (searchLang == LocalSettings.getBaseLanguageId()) {
                            for (Long targetWordId : r.linkedTargetWords.get(e.getKey())) {
                                setItems.add(new SetItem(
                                        firstSearchWord,
                                        targetWordId,
                                        0,
                                        searchWord,
                                        allWords.get(targetWordId).word,
                                        allWords.get(firstSearchWord).getMeta().commonTranslation,
                                        allWords.get(targetWordId).getMeta().commonTranslation,
                                        false, true, true));
                            }
                        }
                        if (searchLang == LocalSettings.getCurrentLearningLanguage()) {
                            for (Long targetWordId : r.linkedTargetWords.get(e.getKey())) {
                                setItems.add(new SetItem(
                                        targetWordId,
                                        firstSearchWord,
                                        0,
                                        allWords.get(targetWordId).word,
                                        searchWord,
                                        allWords.get(targetWordId).getMeta().commonTranslation,
                                        allWords.get(firstSearchWord).getMeta().commonTranslation,
                                        false, true, true));
                            }
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
                itemList.smoothScrollToPosition(setItems.size() - 1);
                if (r.errorMessage != null) {
                    MyQuickToast.showShort(PageSetContent.this, r.errorMessage);
                }
            }
            
            public void noResult(String errorMessage) {
                MyDialogHelper.tryDismissQuickProgressDialog();
                MyQuickToast.showShort(PageSetContent.this, errorMessage);
            }
        });
    }
    
    public void addSetItemFromSearch(SetItem item) {
        int i = 0;
        int insertIndex = -1;
        for (SetItem old : setItems) {
            if (old.isHead) {
                insertIndex = i;
                break;
            }
            if (!old.isNew && !old.isHead) {
                if (old.wordOneId == item.wordOneId && old.wordTwoId == item.wordTwoId) {
                    MyQuickToast.showShort(this, "Word already exists in set");
                    return;
                }
            }
            i += 1;
        }
        
        contentHasJustChanged = true;
        setItems.remove(item);
        item.isNew = false;
        setItems.add(insertIndex, item);
        listAdapter.notifyDataSetChanged();
    }
    
    public void reportContentHasJustChanged() {
        contentHasJustChanged = true;
    }
    
    public void study() {
        startActivity(new Intent(this, PageLMOption.class));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        refreshListView(DataPool.currentSetItems);
    }
    
    @Override
    public void onBackPressed() {
        if (contentHasJustChanged) {
            MyDialogHelper.showConfirmDialog(this, LocalizationManager.getConfirmSetNotChangeTitle(),
                    LocalizationManager.getConfirmSetNotChangeContent(),
                    new CallbackOkButton() {
                        
                        public void okPressed() {
                            PageSetContent.super.onBackPressed();
                        }
                    },
                    null);
        } else {
            super.onBackPressed();
        }
    }
}
