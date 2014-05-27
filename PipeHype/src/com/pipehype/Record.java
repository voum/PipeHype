package com.pipehype;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class Record extends Activity {

	 double BeispielTonFrequenz = 400;
	 Recorder recorder = new Recorder();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		//Button für Beispielton wird zugeordnet
		Button button_ton = (Button) findViewById(R.id.button1);
		button_ton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	 
			//Ein Objekt der Klasse "Tone" wird mit der dem angegebenen Beispielton entsprechenden Frequenz erzeugt...
			Tone tone = new Tone();
			tone.genTone(BeispielTonFrequenz);
			//...Der Ton wird abgespielt.
			tone.playSound();
			}});
		
		
		Button Button_AufnahmeStart = (Button) findViewById(R.id.button2);
		Button_AufnahmeStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				recorder.start();
				
				
				
			}
		});
		
		
		Button Button_AufnahmeStop = (Button) findViewById(R.id.button3);
		Button_AufnahmeStop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				recorder.stop();
				
				
				
				
			}
		});
	}
	
		
		
		
		
		
		
	
	
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record, menu);
		return true;
	}
	

	


}
