package com.pipehype;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class DB {

	float blow_value;
	
	
	
	public boolean isBlowing()
    {
        boolean recorder=true;
        Log.v("","Blow Value=1111");

        int minSize = AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);


        short[] buffer = new short[minSize];

        ar.startRecording();
        while(recorder)
        {

            ar.read(buffer, 0, minSize);
            for (short s : buffer) 
            {
                if (Math.abs(s) > 27000)   //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    blow_value=Math.abs(s);
                    System.out.println("Blow Value="+blow_value);
                    Log.v("","Blow Value="+blow_value);
                    ar.stop();
                    recorder=false;

                    return true;

                }

            }
        }
        return false;

    }
	
	
	
	
	
}
