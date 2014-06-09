package com.pipehype;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
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

@SuppressLint("WrongCall")
public class Record extends Activity implements Callback{

	 //Integer BeispielTonFrequenz = 600;
	 Integer dBlevel = 98;
	 Bundle Level;
	 	Integer zeitLevel;
	 Integer erreichteVoegel = 0;
	 Integer counter = 0;
	 Integer zeit = 20000; // in ms
	 double dbwert;
	 boolean db_active = false;
	 DB db = new DB();
	 Voegel voegel = new Voegel();
	 EditText db_ausgabe, Bewertung, timer;
	 
	 
	 
	// Hier wird der Handler definiert welcher die Message entgegen nimmt (siehe unten)
     final Handler handler = new Handler(this);
     final Handler handler1 = new Handler(this);
      
     
     Runnable runnable = new Runnable() {
         @Override
         public void run() {

             while (db_active == true && zeit > 0){
                 handler.sendEmptyMessage(0);
                 try {
                     Thread.sleep(100);
                 } catch (InterruptedException e) {
                 }  
             }  
             try {
				Thread.sleep(5000);
				//finish();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		timer = (EditText) findViewById(R.id.editText3);	
		db.start(); 
		
		Level = getIntent().getExtras();
		zeitLevel = Level.getInt("Level");
		
		System.out.println(zeitLevel);
		
		//Levelwerte werden empfangen

		//Button f�r Beispielton wird zugeordnet
		//Button button_ton = (Button) findViewById(R.id.button1);
		//button_ton.setOnClickListener(new OnClickListener(){
		//	@Override
		//	public void onClick(View v) {
		//	//Ein Objekt der Klasse "Tone" wird mit der dem angegebenen Beispielton entsprechenden Frequenz erzeugt...
		//	Tone tone = new Tone();
		//	tone.genTone(BeispielTonFrequenz);
		//	//...Der Ton wird abgespielt.
		//	tone.playSound();
		//	}});

		
		
		//Button f�r Aufnahme des Pfeiftons wird mit Listener belegt.
		ToggleButton button_Start = (ToggleButton) findViewById(R.id.toggleButton1);
		button_Start.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		
			@Override public void onCheckedChanged(final CompoundButton button_Start, boolean state) {
				
				if(state){
					//Ist der Button aktiv, wird der oben erstellte Thread "thread" gestartet. 
					Toast.makeText(getApplicationContext(), "Los geht's!", Toast.LENGTH_LONG).show();
					thread.start();
					db_active = true;

					
					if (button_Start.isChecked()){
		                handler1.postDelayed(new Runnable() {
		                    public void run() {
		                         button_Start.setChecked(false);
		                    }
		                }, zeit);}

				} else{
					//Ist der Button inaktiv wird der Thread "thread" gestoppt.
					db_active = false;
					close();
					Toast.makeText(getApplicationContext(), voegel.getVoegel(), Toast.LENGTH_LONG).show();
				}
		}});	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record, menu);
		return true;
	}
	
	//Activity wird geschlossen
	public void close(){
		Intent intent = new Intent(this, Selection.class);
		System.out.println("halt.");	
		db.stop();
    	startActivity(intent);
    	this.finish();		
	}

	@Override
	//MessageHandler f�r "handler" nimmt Nachrichten von "thread" entgegegen, w�hrend dieser l�uft.
	public boolean handleMessage(Message arg0) {
		//Die berechnete Amplitude wird in Dezibel umgerechnet und ausgegeben.
		double dbWert = db.getAmplitudeEMA();
		System.out.println(dbWert);
		db_ausgabe.setText("Lautst�rke: " + dbWert);	
		//Zeit wird heruntergez�hlt
		zeit = zeit - 100;
		timer.setText("Verbleibende Zeit: " + (zeit+1)/1000 + " Sekunden!");
		
		//Solange sich der Wert im richtigen Bereich befindet wird ein "Gut!!!" ausgegeben.
		if(dbWert> dBlevel-5 & dbWert < dBlevel+5){
			Bewertung.setText("Gut!!!");
			counter++;	
            if((counter % zeitLevel) == 0){
            	 voegel.voegelAddieren();
            	 voegel.vogelSound(getApplicationContext());
             }  
		}
		else{
			Bewertung.setText("LAUTER!!!");
		}
		return false;
	}
	

}
