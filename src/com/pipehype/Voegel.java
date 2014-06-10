/**
 * 
 */
package com.pipehype;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;


public class Voegel extends Activity {

	
	
	Integer anzahlVoegel = 0;
	
	public void voegelAddieren(){		
		anzahlVoegel ++;
	}
	
	
	public String getVoegel(){
		return "Sie haben " + anzahlVoegel +" Vogeldame(n) angelockt!";
		
	}
	
	public void vogelSound(Context context){
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.blm);
		mediaPlayer.start();
		
	}
	
}
