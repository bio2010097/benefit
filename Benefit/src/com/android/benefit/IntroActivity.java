package com.android.benefit;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends Activity {

	@Override
	protected void onResume(){
		super.onResume();
		
		
		if( NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) ) {
			Intent intent = new Intent(IntroActivity.this, TagDiscoveredActivity.class);
			startActivity(intent);
			finish();
		}
		else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())){
			Intent intent = new Intent(IntroActivity.this, TagDiscoveredActivity.class);
			startActivity(intent);
			finish();
		} else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())){
			Intent intent = new Intent(IntroActivity.this, TagDiscoveredActivity.class);
			startActivity(intent);
			finish();
		} else {	
			Loading();
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

	}

	private void Loading(){
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(IntroActivity.this, MainActivity.class));
				finish();
			}
		}, 1000);
		
	}

}
