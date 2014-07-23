package com.openwords.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openwords.R;
import com.openwords.model.InitDatabase;
import com.openwords.model.JSONParser;
import com.openwords.model.UserInfo;
import com.openwords.services.ModelLanguage;
import com.openwords.util.LanguagePageTool;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.WordsPage;

public class LanguagePage extends Activity {
        
		public static final String TAG_SUCCESS="success";
		public static final String TAG_MESSAGE="message";
		private int requestcode = 0;
		private static String url_l2_options = "http://www.openwords.org/ServerPages/WordsDB/getLtwoOptions.php";
		public static final String USERID = "userid";
        public static ArrayList<LanguagePageTool> langlist_global = new ArrayList<LanguagePageTool>();
        //public static L2LangAdapter langadapter=null;
        public static ListView lang_listview=null;
        public static String url_write_l2_choice = "http://www.openwords.org/ServerPages/OpenwordsDB/writeL2Choices.php";
        private UserInfo userinfo;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                super.onCreate(savedInstanceState);
                
                setContentView(R.layout.activity_language_page);
                //langlist_global.add(new LanguagePageTool("Bengalitest",1,false));
                
                // Creating a new listview object
                lang_listview = (ListView)findViewById(R.id.language_list_view);
                
                userinfo = OpenwordsSharedPreferences.getUserInfo();
                Log.d("UserID in LangPage",Integer.toString(userinfo.getUserId()));
                
                runOnUiThread(new Runnable() { public void run(){getFromServer();} } );
                //AsyncTaskRunner runner = new AsyncTaskRunner();
                //runner.execute();
                //Log.d("Async", "Executing the asynctask");
                // creating an object of the custom adapter
                L2LangAdapter langadapter = new L2LangAdapter(this, R.layout.lang_list, langlist_global);
                // setting the custom adapter to the listview object
                lang_listview.setAdapter(langadapter); 
                
                final View langButton = findViewById(R.id.langbutton);
        	    //langButton.setOnClickListener(this);
                langButton.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {               
                    	langButton.setEnabled(false);
                    	AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
                    		ProgressDialog progress;
        					@Override
        					protected void onPreExecute() {
        						progress = new ProgressDialog(LanguagePage.this);
        						progress.setMessage("Synchronizing with server...");
        						progress.setCancelable(false);
        						progress.setIndeterminate(true);
        						progress.show();
        					}
        						
        					@Override
        					protected Boolean doInBackground(Void... arg0) {
        						//We could modify this function to return a boolean value
        						return writeToServer(userinfo.getUserId());
        					}
        					
        					@Override
        					protected void onPostExecute(Boolean result) {
        						if (progress!=null) {
        							progress.dismiss();
        							if(result.equals(true)) {
        								int spinnerSize = HomePage.LanguageList.size();
        								for(int i=0;i<spinnerSize-1;i++) HomePage.LanguageList.remove(0);
        				                for(int i=0;i<langlist_global.size();i++){
    				                        if(langlist_global.get(i).isSelected()){
    				                        	HomePage.LanguageList.add(HomePage.LanguageList.size()-1,new ModelLanguage(langlist_global.get(i).getId(),
    				                        			langlist_global.get(i).getName()));
    				                        }
        				                }
        								startActivityForResult(new Intent(LanguagePage.this.getApplicationContext(), ChosenPage.class), requestcode);
        							} else {
        								Toast.makeText(LanguagePage.this, "You must select at least one", Toast.LENGTH_SHORT).show();
        							}
        							
        							langButton.setEnabled(true);
        						}
        					}
                    	};
                    	task.execute((Void[])null);
        				return false;
                    }
                    
                });
            }
        
//        public void onClick(View v) {
//    		switch (v.getId())
//    		{
//    		case R.id.langbutton:
//    			
//    			int success = writeToServer(userinfo.getUserId());
//    			if(success == 1) {
//    				finish();
//    				startActivity(new Intent(LanguagePage.this.getApplicationContext(), ChosenPage.class));
//    			}
//    			else
//    			{
//    				runOnUiThread(new Runnable() {
//                        public void run() {
//                        	Toast.makeText(LanguagePage.this, "You must select at least one", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//    			}
//    			//break;
//    		}
//        }
        
        public void getFromServer()
        {
                ArrayList<LanguagePageTool> langlist = new ArrayList<LanguagePageTool>();
                try 
                {
                        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                        params.add(new BasicNameValuePair("userid",Integer.toString(userinfo.getUserId())));
                        Log.d("User", "Passed Validation");
                        JSONParser jsonParse = new JSONParser();
                        JSONObject jObj = jsonParse.makeHttpRequest(url_l2_options, "POST", params);
                        Log.d("Obj", jObj.toString());
                        JSONArray jArr = jObj.getJSONArray("langdata");
                        String abc = Integer.toString(jArr.length());
                        Log.d("Array", abc);
                        
                        for (int i = 0; i < jArr.length(); i++) 
                        {  // **line 2**
                    JSONObject childJSONObject = jArr.getJSONObject(i);
                    
                        langlist.add(new LanguagePageTool(childJSONObject.getString("l2name"),childJSONObject.getInt("l2id"), childJSONObject.getBoolean("chosen")));
                        //Log.d("Loop", childJSONObject.getString("l2name"));
                        //Log.d("Loop", langlist.get(i).getName());
                     }
                        
                
                }
                        catch(Exception e)
                        {e.printStackTrace();}
                 langlist_global=langlist;
        }
        
      //Writing options to server
        public boolean writeToServer(int user)
        {
        	
                String l2_ids="";
                int selected=0;
                //-------building the selected languages parameter string
                for(int i=0;i<langlist_global.size();i++)
                {
                        if(langlist_global.get(i).isSelected()==true)
                        {
                                l2_ids=l2_ids+langlist_global.get(i).getId()+"|";
                                selected++;
                        }
                }
                
                if(selected!=0)
                {
	                l2_ids = l2_ids.substring(0, l2_ids.length()-1);
	                
	                Log.d("L2 ids", l2_ids);
	                Log.d("L2 ids", "_"+user);
	                //------------------------------------
	                
	                String user_id=Integer.toString(user);
	                //-------------constucting parameters------------
	                List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			        params.add(new BasicNameValuePair("choices",l2_ids));
			        params.add(new BasicNameValuePair("id",user_id));
			                //-----------------------------------------------
			        try{
			        JSONParser jsonParse = new JSONParser();
			        JSONObject jObj = jsonParse.makeHttpRequest(url_write_l2_choice, "POST", params);
			        
			        int success = jObj.getInt(TAG_SUCCESS);
			        String msg = jObj.getString(TAG_MESSAGE);
			        
			        if (success == 1) {
			        	Log.d("Info","create successfully");
			        	//usernameExist = true;
			            runOnUiThread(new Runnable() {
			                public void run() {
			                    Toast.makeText(LanguagePage.this,"Preference created successfully", Toast.LENGTH_SHORT).show();
			                }
			            });
			        	}
			        return true;
			        } catch(Exception e)
			        {e.printStackTrace(); return false;}
                }
                else
                {
                	return false;
                }
        }
        /*
        class AsyncTaskRunner extends AsyncTask{
                //Creating a new array list of type L2_lang
                ArrayList<L2_lang> langlist = new ArrayList<L2_lang>();
                protected Integer doInBackground(Object... L2_lang) {
                        try 
                        {
                                List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                                params.add(new BasicNameValuePair("userid","47"));
                                Log.d("User", "Passed Validation");
                                JSONParser jsonParse = new JSONParser();
                                JSONObject jObj = jsonParse.makeHttpRequest(url_l2_options, "POST", params);
                                Log.d("Obj", jObj.toString());
                                JSONArray jArr = jObj.getJSONArray("langdata");
                                String abc = Integer.toString(jArr.length());
                                Log.d("Array", abc);
                                
                                for (int i = 0; i < jArr.length(); i++) 
                                {  // **line 2**
                            JSONObject childJSONObject = jArr.getJSONObject(i);
                            
                                langlist.add(new L2_lang(childJSONObject.getString("l2name"),childJSONObject.getInt("l2id"), childJSONObject.getBoolean("chosen")));
                                //Log.d("Loop", childJSONObject.getString("l2name"));
                                Log.d("Loop", langlist.get(i).getName());
                             }
                        
                        }
                                catch(Exception e)
                                {e.printStackTrace();}
                         langlist_global=langlist;
                        return 1;
                }
                 protected void onPostExecute(Integer result) {
                        //langlist_global=langlist;
                         Log.d("inonpost", "in Onpost");
                                 lang_listview = (ListView)findViewById(R.id.language_list_view);
                        langadapter.notifyDataSetChanged();
                        lang_listview.setAdapter(langadapter);
                                 
                    }
        }
        
        */
        
        public class L2LangAdapter extends ArrayAdapter<LanguagePageTool>
        {
                public L2LangAdapter(Context context, int textViewResourceId, 
                                ArrayList<LanguagePageTool> langlist) {
                        super(context, textViewResourceId, langlist);
                        this.langlist = new ArrayList<LanguagePageTool>();
                        this.langlist.addAll(langlist);
                }

                private ArrayList<LanguagePageTool> langlist;
        
        private class ViewHolder {
                   TextView name;
                   CheckBox checked;
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
                 
                   ViewHolder holder = null;
                   Log.v("ConvertView", String.valueOf(position));
                 
                   if (convertView == null) {
                   LayoutInflater vi = (LayoutInflater)getSystemService(
                     Context.LAYOUT_INFLATER_SERVICE);
                   convertView = vi.inflate(R.layout.lang_list, null);
                 
                   holder = new ViewHolder();
                   holder.name = (TextView) convertView.findViewById(R.id.code);
                   holder.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);
                   convertView.setTag(holder);
                 
                    holder.checked.setOnClickListener( new View.OnClickListener() {  
                     public void onClick(View v) {  
                    	 //L2_lang l2lang = (L2_lang) v.g 
                    	 Log.d("Tag", "tag_"+v.getId());	
                    	 
                      CheckBox cb = (CheckBox) v; 
                      //L2_lang l2lang = (L2_lang) cb.getTag(); 
                      Log.d("chkbxid", "chkd_"+cb.isChecked());
                      /*Toast.makeText(getApplicationContext(),
                       "Clicked on Checkbox: " + cb.getText() +
                       " is " + cb.isChecked(), 
                       Toast.LENGTH_LONG).show();*/
                      //l2lang.setSelected(cb.isChecked());
                      for(int j=0; j<langlist_global.size();j++)
                      {
                    	  if (langlist_global.get(j).getId()== v.getId())
                    	  {
                    		  	langlist_global.get(j).setSelected(cb.isChecked());
                    	  }
                      }
                      for(int k=0;k<langlist_global.size();k++)
                      {
                    	  Log.d("langlist", "_"+langlist_global.get(k).getId()+langlist_global.get(k).isSelected());
                      }
                     }  
                    });  
                   } 
                   
                   else {
                    holder = (ViewHolder) convertView.getTag();
                   }
                 
                   LanguagePageTool l2lang = langlist.get(position);
                   holder.name.setText(l2lang.getName());
                   //holder.name.setText(l2lang.getId());
                   if (l2lang.isSelected())
                   {
                	   Log.d("Id's", "_"+l2lang.getId());
                   }
                   holder.checked.setChecked(l2lang.isSelected());
                   holder.checked.setId(l2lang.getId());
                   holder.name.setTag(l2lang);
                   if (l2lang.isSelected())
                   {
                	   Log.d("Id'sofchkbx", "_"+holder.checked.getId());
                   }
                 
                   return convertView;
                 
        }
}        
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.login_page, menu);
                return true;
        }
        
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == 0) {
                if(resultCode == RESULT_OK){
                    finish();
                }
            }
        }
}