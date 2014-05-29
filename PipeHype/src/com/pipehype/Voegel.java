/**
 * 
 */
package com.pipehype;

import android.app.Activity;


public class Voegel extends Activity {

	Integer anzahlVoegel = 0;
	
	public void voegelAddieren(){		
		anzahlVoegel ++;
	}
	
	
	public String getVoegel(){
		return "Sie haben " + anzahlVoegel +" Vogeldamen erobert!";
		
	}
	
	
}
