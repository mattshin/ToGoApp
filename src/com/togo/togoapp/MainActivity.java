package com.togo.togoapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.togo.togoapp.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener  {
	protected boolean loggedOn = true;
	private boolean debug = true;
	
	 private TabHost mTabHost;
     private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
     private TabInfo mLastTab = null;
     /**
      * 
      */
     private class TabInfo {
              private String tag;
      private Class<?> clss;
      private Bundle args;
      private Fragment fragment;
      TabInfo(String tag, Class<?> clazz, Bundle args) {
              this.tag = tag;
              this.clss = clazz;
              this.args = args;
      }
             
     }
     /**
      * 
      *
      */
     class TabFactory implements TabContentFactory {

             private final Context mContext;

         /**
          * @param context
          */
         public TabFactory(Context context) {
             mContext = context;
         }

         /** (non-Javadoc)
          * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
          */
         public View createTabContent(String tag) {
             View v = new View(mContext);
             v.setMinimumWidth(0);
             v.setMinimumHeight(0);
             return v;
         }

     }

     /**
      * Init the Tab Host
      */
     private void initialiseTabHost(Bundle args) {
             mTabHost = (TabHost)findViewById(android.R.id.tabhost);
     mTabHost.setup();
     TabInfo tabInfo = null;
     MainActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Board").setIndicator("Board"), ( tabInfo = new TabInfo("Board", BoardFragment.class, args)));
     this.mapTabInfo.put(tabInfo.tag, tabInfo);
     MainActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Calendar").setIndicator("Calendar"), ( tabInfo = new TabInfo("Calendar", CalendarFragment.class, args)));
     this.mapTabInfo.put(tabInfo.tag, tabInfo);
     MainActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("History").setIndicator("History"), ( tabInfo = new TabInfo("History", HistoryFragment.class, args)));
     this.mapTabInfo.put(tabInfo.tag, tabInfo);
     // Default to first tab
     this.onTabChanged("Board");
     //
     mTabHost.setOnTabChangedListener(this);
     }
     
     /**
      * @param activity
      * @param tabHost
      * @param tabSpec
      * @param clss
      * @param args
      */
     private static void addTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
             // Attach a Tab view factory to the spec
             tabSpec.setContent((TabContentFactory) activity.new TabFactory(activity));
     String tag = tabSpec.getTag();

     // Check to see if we already have a fragment for this tab, probably
     // from a previously saved state.  If so, deactivate it, because our
     // initial state is that a tab isn't shown.
     tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
     if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
         FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
         ft.detach(tabInfo.fragment);
         ft.commit();
         activity.getSupportFragmentManager().executePendingTransactions();
     }

     tabHost.addTab(tabSpec);
     }

     /** (non-Javadoc)
      * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
      */
     public void onTabChanged(String tag) {
             TabInfo newTab = this.mapTabInfo.get(tag);
             if (mLastTab != newTab) {
                     FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
         if (mLastTab != null) {
             if (mLastTab.fragment != null) {
                     ft.detach(mLastTab.fragment);
             }
         }
         if (newTab != null) {
             if (newTab.fragment == null) {
                 newTab.fragment = Fragment.instantiate(this,
                         newTab.clss.getName(), newTab.args);
                 ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
             } else {
                 ft.attach(newTab.fragment);
             }
         }

         mLastTab = newTab;
         ft.commit();
         this.getSupportFragmentManager().executePendingTransactions();
             }
 }
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!debug){
			if(savedInstanceState == null){
				redirectLogon();
			}
			else{
				loggedOn = savedInstanceState.getBoolean("LOGON");
			}
			
			if(!loggedOn){
				redirectLogon();
			}
		}
		
		initializeBoard();
		
		
		setContentView(R.layout.activity_main);
		initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
        	mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }

		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		  savedInstanceState.putString("TAB", mTabHost.getCurrentTabTag()); //save the tab selected
		  savedInstanceState.putBoolean("LOGON", false);
		}
	

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		  loggedOn = savedInstanceState.getBoolean("LOGON");
		}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.logout:
	            redirectLogon();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void redirectLogon(){
		loggedOn = false;
		try{
		Intent redirectLogon = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(redirectLogon);
		//finish();
		loggedOn = true;
		}
		catch(Exception c){
			
		}
		
	}
	
	public void initializeBoard(){
		writeFile("Hello Drivers!", "boardText.txt");
	}
	
	public void writeFile(String content, String fileName) {
        try {
            FileWriter out = new FileWriter(new File(this.getFilesDir(), fileName));
            out.write(content);
            out.close();
        } catch (IOException e) {
            
        }
    }


}
