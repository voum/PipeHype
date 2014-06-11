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

	 //Levelwerte aus Selection.java werden im Bundle Level gesichert und uebergeben.
	 Bundle Level;
	 	Integer zeitLevel, stufe;
	 //Integer BeispielTonFrequenz = 600;
	 Integer dBlevel = 98;
	 Integer counter = 0;
	 Integer zeit = 21000; // in ms
	 double dbwert;
	 boolean db_active = false;
	 DB db = new DB();
	 Voegel voegel = new Voegel();
	 Selection selection = new Selection();
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
             }     
     };
     
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
		stufe = Level.getInt("Stufe");
		Bewertung.setText("Level " + stufe);
		
		//Zurueck-Button
		Button button_Close = (Button) findViewById(R.id.button1);
		button_Close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				close();		
			}	
		});
		
		//Neustarten-Button
		final Button button_Restart = (Button) findViewById(R.id.button2);
		button_Restart.setEnabled(false);	
		button_Restart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				restart();		
			}	
		});
		
		//Button fuer Aufnahme des Pfeiftons wird mit Listener belegt.
		ToggleButton button_Start = (ToggleButton) findViewById(R.id.toggleButton1);
		button_Start.setOnCheckedChangeListener(new OnCheckedChangeListener(){		
			@Override public void onCheckedChanged(final CompoundButton button_Start, boolean state) {
				if(state){
					Thread thread = new Thread(runnable);
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
					db_active = false;	
					Toast.makeText(getApplicationContext(), voegel.getVoegel(), Toast.LENGTH_LONG).show();
					button_Start.setEnabled(false);
					button_Restart.setEnabled(true);			
					//if(zeit <= 0){
					//	nextLevel();
					//}
				}
		}});	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record, menu);
		return true;
	}
	
	public void restart(){
		db_active = false;	
		voegel.finish();
		db.stop();
		Intent intent = new Intent(this, Record.class);
		intent.putExtras(Level);
		startActivity(intent);
		this.finish();
	}
	
	//Activity wird geschlossen
	public void close(){	
		db_active = false;	
		voegel.finish();
		db.stop();
		Intent intent = new Intent(this, Selection.class);	
    	startActivity(intent);
    	this.finish();
	}
	
	public void nextLevel(){
		db_active = false;	
		voegel.finish();
		db.stop();
    	if(stufe == 1){	
    		selection.gotoLevel2();	
    	}
    	else if(stufe == 2){
    		selection.gotoLevel3();	
    	}
    	this.finish();
		
		
	}

	@Override
	//MessageHandler fuer "handler" nimmt Nachrichten von "thread" entgegegen, waehrend dieser laeuft.
	public boolean handleMessage(Message arg0) {
		//Die berechnete Amplitude wird in Dezibel umgerechnet und ausgegeben.
		double dbWert = db.getAmplitudeEMA();
		//Zeit wird heruntergezaehlt
		zeit = zeit - 100;		
		db_ausgabe.setText("Lautstärke: " + dbWert);	
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
