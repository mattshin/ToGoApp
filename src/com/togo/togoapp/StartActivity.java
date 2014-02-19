package com.togo.togoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	public void gotoLogin(View view)
	{
		try{
			Intent redirectLogon = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(redirectLogon);
			
			}
		catch(Exception c){
				
			}
	}
	public void gotoSignUp(View view)
	{
		startActivity(new Intent(this,SignUpActivity.class));
	}

}
