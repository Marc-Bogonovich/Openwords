package com.openwords.view.actionbar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.openwords.R;

public class WordsPage extends Activity implements OnClickListener {
	private String[] nextWordsArray;
	private String[] searchWordsArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_page);
        
        ActionBarIcons.builder(this);
        
        Button getNextWords = (Button) findViewById(R.id.wordsPage_Button_getNextWords);
        getNextWords.setOnClickListener(this); 
        Button searchWords = (Button) findViewById(R.id.wordsPage_Button_searchWords);
        searchWords.setOnClickListener(this); 
        Button searchWordSets = (Button) findViewById(R.id.wordsPage_Button_searchWordSets);
        searchWordSets.setOnClickListener(this); 
        Button viewMyWords = (Button) findViewById(R.id.wordsPage_Button_viewMyWords);
        viewMyWords.setOnClickListener(this); 
        ImageButton syncButton = (ImageButton) findViewById(R.id.wordsPage_ImageButton_syncButton);
        syncButton.setOnClickListener(this); 
        nextWordsArray = new String[]{"我 I","你 you","他 he"};
        searchWordsArray = new String[]{"云 cloud","公司 company"};
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
         case R.id.wordsPage_Button_getNextWords:
             Log.d("Click","Next words");
             getNextWordsButtonClick();
             break;
         case R.id.wordsPage_Button_searchWords:
             Log.d("Click","searchWords");
             searchWordsButtonClick();
             break;
         case R.id.wordsPage_Button_searchWordSets:
             Log.d("Click","searchWordSets");
             break;
         case R.id.wordsPage_Button_viewMyWords:
             Log.d("Click","viewMyWords");
             break;
         case R.id.wordsPage_ImageButton_syncButton:
             Log.d("Click","syncButton");
             break;
		 }
	}	
	private void getNextWordsButtonClick() {
		final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
		  new AlertDialog.Builder(this)
          .setTitle("Next words")
          .setMultiChoiceItems(nextWordsArray, null,
                      new DialogInterface.OnMultiChoiceClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which,
                       boolean isChecked) {
                   if (isChecked) {
                       // If the user checked the item, add it to the selected items
                       mSelectedItems.add(which);
                   } else if (mSelectedItems.contains(which)) {
                       // Else, if the item is already in the array, remove it 
                       mSelectedItems.remove(Integer.valueOf(which));
                   }
               }
          })
           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   // User clicked OK, so save the mSelectedItems results somewhere
                   // or return them to the component that opened the dialog
                   
               }
           })
           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           }).create().show();
	}
	private void searchWordsButtonClick() {
		final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
		LayoutInflater inflater = this.getLayoutInflater();
		  new AlertDialog.Builder(this)
          .setTitle("Search words")
          .setView(inflater.inflate(R.layout.fragment_search_word, null))
          .setMultiChoiceItems(searchWordsArray, null,
                      new DialogInterface.OnMultiChoiceClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which,
                       boolean isChecked) {
                   if (isChecked) {
                       // If the user checked the item, add it to the selected items
                       mSelectedItems.add(which);
                   } else if (mSelectedItems.contains(which)) {
                       // Else, if the item is already in the array, remove it 
                       mSelectedItems.remove(Integer.valueOf(which));
                   }
               }
          })
           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   // User clicked OK, so save the mSelectedItems results somewhere
                   // or return them to the component that opened the dialog
                   
               }
           })
           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           }).create().show();
	}
          
}

