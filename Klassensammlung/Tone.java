/**
 * 
 */
package com.pipehype;

import android.app.Activity;
import android.media.AudioTrack;


//Diese Klasse erzeugt einen Beispielton in entsprechender Frequenz
public class Tone extends Activity {

	private final int dauer = 3; 
	private final int sampleRate = 44000; 
	private final int AnzahlSamples = dauer * sampleRate;
	private final double sample[] = new double[AnzahlSamples];
	private final byte generatedSnd[] = new byte[2 * AnzahlSamples];
	
	
	
	//Ton erstellen...
	void genTone(double dbFreq){

        for (int i = 0; i < AnzahlSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/dbFreq));
        }
        
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
           
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

	//Ton abspielen
    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(1, sampleRate, 4, 2, AnzahlSamples, 0);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
   }
}



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

