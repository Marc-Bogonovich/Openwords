package com.openwords.view.actionbar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.openwords.R;

public class WordsPage extends Activity implements OnClickListener {
	private String[] nextWordsArray;
	private static String[] searchWordsArray;
	public static AlertDialog.Builder dg;
	public static final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_page);
        
        //ActionBarIcons.builder(this);
        new ActionBarBuilder(this, ActionBarBuilder.Words_Page);
        
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
        nextWordsArray = new String[]{"�� I","�� you","�� he"};
        searchWordsArray = new String[]{"�� cloud","��˾ company"};
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
         case R.id.wordsPage_Button_getNextWords:
             Log.d("Click","Next words");
             WordsPage.this.startActivity(new Intent(WordsPage.this, NextWords.class));
             break;
         case R.id.wordsPage_Button_searchWords:
             Log.d("Click","searchWords");
             searchWordsButtonClick(1,null);
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
	
	/*
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
	*/
	
	private void searchWordsButtonClick(int entry, String searchTxt) {
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		final View infView = inflater.inflate(R.layout.fragment_search_word, null);
		dg = new AlertDialog.Builder(this);
          dg.setTitle("Search words");
          dg.setView(infView);
          dg.setMultiChoiceItems(searchWordsArray, null,
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
          });
          
          final EditText ed = (EditText) infView.findViewById(R.id.editText_search);
          ed.setText(searchTxt);
          
           dg.setPositiveButton("Add Words", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   // User clicked OK, so save the mSelectedItems results somewhere
                   // or return them to the component that opened the dialog
                   dialog.dismiss();
               }
           });
           dg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
               }
           });
           dg.setNeutralButton("Search",  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				searchWordsArray = new String[]{"�� cloud","��˾ company","��˾ blah" };
	     		   //testmethod();
	     		   Log.d("text", ed.getText().toString());
	     		   dialog.dismiss();
	     		   searchWordsButtonClick(0,ed.getText().toString());
			}
		});
		dg.create().show();
	
	}
	
	//
	public void searchWordFromServer(String searchText)
	{
		
	}
        
        @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        WordsPage.super.onBackPressed();
                    }
                }).create().show();
    }
}

