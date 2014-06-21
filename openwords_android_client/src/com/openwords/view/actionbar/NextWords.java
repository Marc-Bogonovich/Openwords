package com.openwords.view.actionbar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.UserWords;
import com.openwords.util.WordsPageTool;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NextWords extends Activity implements OnClickListener{
	private static String url_write_downloaded_words_to_server = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/setUserWords.php";
	private static String nextwords_url = "http://geographycontest.ipage.com/OpenwordsOrg/WordsDB/wordsPageGetWordList.php";
	public static ListView words_listview=null;
	public static ArrayList<WordsPageTool> wordslist = new ArrayList<WordsPageTool>();
	public static JSONArray jArrMain;
	public static String ConIds = "";
	
	 protected void onCreate(Bundle savedInstanceState) {
		 	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 	StrictMode.setThreadPolicy(policy);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.wordspage_nextwords);
	        
	        // Creating a new listview object
            words_listview = (ListView)findViewById(R.id.nextwords_listview);
            
	        runOnUiThread(new Runnable() { public void run(){getFromServer();} } );
	        Log.d("dataOnMain", wordslist.get(0).getWord1());
	        //wordslist.add(0, new WordsPageTool(99, "wordl2", "wordl1", true));           
            WordListAdapter wordsadapter = new WordListAdapter(this, R.layout.nextwords_adapter, wordslist);
            // setting the custom adapter to the listview object
            words_listview.setAdapter(wordsadapter);
            //Log.d("adapter", wordsadapter.getItem(2).getWord1()+wordsadapter.getItemId(0));
	        //ActionBarIcons.builder(this);
	        //new ActionBarBuilder(this, ActionBarBuilder.Words_Page);
            
            View OkButton = findViewById(R.id.WP_OkButton);
            OkButton.setOnClickListener(this);
            
            View CancelButton = findViewById(R.id.WP_CancelButton);
            CancelButton.setOnClickListener(this);
	 }
	 


		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId())
    		{
			case R.id.WP_OkButton:
				Toast.makeText(getApplicationContext(), "Ok Clicked", Toast.LENGTH_SHORT).show();
				
				ConIds="";
				for(int j=0;j<wordslist.size();j++)
				{
					//downloading words
					if(wordslist.get(j).isChecked_word())
					{
						try {
							JSONObject c=jArrMain.getJSONObject(j);
							UserWords uwRec = new UserWords(this,c.getInt("connection_id"),
									c.getInt("wordl1"),c.getString("wordl1_text"),c.getInt("wordl2"),
									c.getString("wordl2_text"),c.getInt("l2id"),c.getString("l2name"),c.getString("audio"));
							uwRec.save();
							
							//concatenating connection ids
							if(ConIds.length()==0)
								ConIds = ConIds + c.getInt("connection_id");
							else
								ConIds = ConIds + "|" + c.getInt("connection_id");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				
				//sending information to server
				new Thread(new Runnable() {
	                public void run() {
	                	updateWordsOnServer(ConIds,getUnixTime());                          
	                }
	              }).start(); 
				break;
			case R.id.WP_CancelButton:
				Toast.makeText(getApplicationContext(), "Cancel Clicked", Toast.LENGTH_SHORT).show();
				List<UserWords> uwlst = UserWords.listAll(UserWords.class);
				Log.d("data in user words", uwlst.get(0).wordLTwo);
				break;
    		}
		}    
	 
		public void updateWordsOnServer(String conIds, long dTime)
		{
			try
			{
				int user = OpenwordsSharedPreferences.getUserInfo().getUserId();
				int langTwo = OpenwordsSharedPreferences.getUserInfo().getLang_id();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("conid", conIds));
	            params.add(new BasicNameValuePair("dtime", Long.toString(dTime)));
	            params.add(new BasicNameValuePair("user",Integer.toString(user)));
	            params.add(new BasicNameValuePair("lTwo",Integer.toString(langTwo)));
	            JSONParser jsonParse = new JSONParser();
                JSONObject jObj = jsonParse.makeHttpRequest(url_write_downloaded_words_to_server, "POST", params);
                
                if(jObj.getInt("success")==1)
                	Log.d("Message From Server", jObj.getString("message"));
	            
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
            
			
		}
		
		//get time in UNIX TIME
		public long getUnixTime()
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			return unixTime;
		}
	  public void getFromServer()
        {
                ArrayList<WordsPageTool> words_list = new ArrayList<WordsPageTool>();
                try 
                {
                        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
                        params.add(new BasicNameValuePair("user", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId())));
                        params.add(new BasicNameValuePair("langOne", "1"));
                        params.add(new BasicNameValuePair("langTwo", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id())));
                        Log.d("User", "47");
                        JSONParser jsonParse = new JSONParser();
                        JSONObject jObj = jsonParse.makeHttpRequest(nextwords_url, "POST", params);
                        Log.d("Obj", jObj.toString());
                        if(jObj.getInt("success")==1)
                        {
                        JSONArray jArr = jObj.getJSONArray("data");
                        String abc = Integer.toString(jArr.length());
                        Log.d("Array", abc);
                        jArrMain = jArr;
                        for (int i = 0; i < jArr.length(); i++) 
                        {  // **line 2**
                        	JSONObject childJSONObject = jArr.getJSONObject(i);
                    
                        	words_list.add(new WordsPageTool(childJSONObject.getInt("connection_id"),childJSONObject.getString("wordl1_text"), childJSONObject.getString("wordl2_text"), true));
                        	//Log.d("Loop", childJSONObject.getString("wordl1_text"));
                        	Log.d("Loop", words_list.get(i).getWord1());
                        }
                        }else{words_list.add(0, new WordsPageTool(99, "wordl2", "wordl1", true));}
                        
                }
                        catch(Exception e)
                        {e.printStackTrace();}
                
                 wordslist=words_list;
        }
	 
	         public class WordListAdapter extends ArrayAdapter<WordsPageTool>
        {
                public WordListAdapter(Context context, int textViewResourceId, 
                                ArrayList<WordsPageTool> words_list) {
                        super(context, textViewResourceId, words_list);
                        this.words_list = new ArrayList<WordsPageTool>();
                        this.words_list.addAll(words_list);
                }

                private ArrayList<WordsPageTool> words_list;
        
        private class ViewHolder {
                   CheckBox checked;
                   TextView name1;
                   TextView name2;
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
                 
                   ViewHolder holder1;
                   Log.v("ConvertView", String.valueOf(position));
                 
                   if (convertView == null) {
                   LayoutInflater vi = (LayoutInflater)getSystemService(
                     Context.LAYOUT_INFLATER_SERVICE);
                   convertView = vi.inflate(R.layout.nextwords_adapter, null);
                 
                   holder1 = new ViewHolder();
                   holder1.name1 = (TextView) convertView.findViewById(R.id.word1);
                   holder1.name2 = (TextView) convertView.findViewById(R.id.word2);
                   holder1.checked = (CheckBox) convertView.findViewById(R.id.words_checkbox);
                   convertView.setTag(holder1);
                   Log.d("Holder", Integer.toString(holder1.name1.getBottom()));
                 
                    holder1.checked.setOnClickListener( new View.OnClickListener() {  
                     public void onClick(View v) {  
                    	 Log.d("Tag", "tag_"+v.getId());	
                    	 
                      CheckBox cb = (CheckBox) v; 
                      for(int i=0;i<wordslist.size();i++)
                      {
                    	  if(wordslist.get(i).getId()==v.getId())
                    	  {
                    		  wordslist.get(i).setChecked_word(cb.isChecked());
                    	  }
                      }
                      Log.d("chkbxid", "chkd_"+cb.isChecked());
                      
                     }  
                    });
                   }
                   
                   else {
                    holder1 = (ViewHolder) convertView.getTag();
                   }
                 
                   WordsPageTool word = words_list.get(position);
                   holder1.name1.setText(" (" +  word.getWord1() + ")");
                   holder1.name2.setText(" (" +  word.getWord2() + ")");
                   holder1.checked.setId(word.getId());
                   holder1.checked.setChecked(true);
                   
                   return convertView;
                 
        	}
        }
	  
}
