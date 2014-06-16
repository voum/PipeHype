/**
 * 
 */
package com.pipehype;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

public class Voegel extends Activity {

	Integer anzahlVoegel = 0;
	boolean sound;
	
	
	public void voegelAddieren(){		
		anzahlVoegel ++;
	}
	
	public String getVoegel(){
		return "Sie haben " + anzahlVoegel +" Vogeldame(n) angelockt!";	
	}
	
	public void vogelSound(Context context){	
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.blm);
		if(sound == true){
			mediaPlayer.setVolume((float) 0.3,(float) 0.3);
		}
		else if(sound == false) {
			mediaPlayer.setVolume((float) 0.0,(float) 0.0);
		}
		mediaPlayer.start();	
	}
	
}
