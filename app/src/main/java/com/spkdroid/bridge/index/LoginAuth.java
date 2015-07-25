package com.spkdroid.bridge.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.spkdroid.dayatdalclient.Index;
import com.spkdroid.dayatdalclient.MainActivity;
import com.spkdroid.dayatdalclient.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

/*
 * LoginAuth is the Login Authentication service. When the user successfully login for the first time a dayatdal.txt 
 * 
 * file is created and stored in the sdcard. This text file will be holding the user name.
 * 
 * if the text file is missing the Day at Dal client app will force the user to run for an authentication test
 *  
 * this logic will allow the client application to enable one time login method
 * 
 * 
 */

public class LoginAuth extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_bridge);
		File file=new File(Environment.getExternalStorageDirectory()+"/dayatdal.txt");
		if(file.exists())
		{
			finish();
			Intent i=new Intent(this,Index.class);
		    i.putExtra("idName",read());
			startActivity(i);
		}
		else
		{
		    finish();
		    Intent i=new Intent(this,MainActivity.class);
		    startActivity(i);
		}
	}
	
	/*
	 * 
	 * Read Function is used to fetch the Username from the text file.
	 * 
	 */
	
	 public String read(){
         BufferedReader br = null;
         String response = null;
            try {
                StringBuffer output = new StringBuffer();
                String fpath = Environment.getExternalStorageDirectory()+"/dayatdal.txt";
                br = new BufferedReader(new FileReader(fpath));
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
                }
                response = output.toString(); 
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return response;	
}
}