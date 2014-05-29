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
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import android.os.Build;

public class Record extends Activity implements Callback{

	 double BeispielTonFrequenz = 400;
	 DB db = new DB();
	 double dbWert;
	 boolean db_active = false;
	 Integer time = 300;
	 EditText db_ausgabe, Bewertung;
	 
	// Hier wird der Handler definiert welcher die Message entgegen nimmt (siehe unten)
     final Handler handler = new Handler(this);

	 
	// Runnable hier wird festgelegt was passiert
     
     Runnable runnable = new Runnable() {
         @Override
         public void run() {
             // hier machst du eigentlich gar nichts nur eine Schleife die immer eine leere Message an deinen Handler schickt
             while (db_active == true){
                 handler.sendEmptyMessage(0);
                 try {
                     // Hier warte ich eine Sekunde, die Zeit kannst du natürlich nach belieben ändern
                     Thread.sleep(100);
                 } catch (InterruptedException e) {
                     // Nichts machen (eigentlich unschön aber ist ja nur ne demo ;))
                 }
             }
         }
     };
     
     Thread thread = new Thread(runnable);
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		db_ausgabe = (EditText) findViewById(R.id.editText1);
		Bewertung = (EditText) findViewById(R.id.editText2);
		db.start(); 
		
		
		
		
		
		
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
		
		
		//Button für Aufnahme...
		ToggleButton button_Start = (ToggleButton) findViewById(R.id.toggleButton1);
		button_Start.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@SuppressWarnings("deprecation")
			@Override public void onCheckedChanged(CompoundButton button_Start, boolean state) {
				
				if(state){
					
					Toast.makeText(getApplicationContext(), "Los geht's!", Toast.LENGTH_LONG).show();
					thread.start();
					db_active = true;
					
				} else {	
					db_active = false;
					thread.interrupt();
					
					
	
				}
			
		}});
		
	}
	

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record, menu);
		return true;
	}

	@Override
	public boolean handleMessage(Message arg0) {
		double dbWert = db.getAmplitudeEMA();
		System.out.println(dbWert);
		db_ausgabe.setText("" + dbWert);	
		
		if(dbWert> 60 & dbWert < 90){
			Bewertung.setText("Gut!!!");
		}
		else{
			Bewertung.setText("Schlecht!!!");
		}
		
		return false;
	}
	

	


}
