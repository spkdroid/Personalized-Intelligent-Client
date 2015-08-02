package com.spkdroid.newsfeed.preference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.spkdroid.dayatdalclient.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/*
 * Filename: Npreference.java
 * 
 * This is used to update the News Preference of the user.The User can update the News Preference
 * 
 * information into the server. Based on the preference given by the user the Newsfeed of the kiosk 
 * 
 * will get changed.
 * 
 */
public class Npreference extends Activity implements OnClickListener
{
	
	RadioButton r1,r2,r3,r4;
	Button updatePref;
	RadioGroup rp;
	String r;
	//http://www.spkdroid.com/merlin/newspref.php?login_id=B00675218&pref=2
    String url;
    ProgressDialog status;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	super.onCreate(savedInstanceState);
    setContentView(R.layout.news_feed_preference);
    r=getIntent().getExtras().getString("dalid");
    Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
	rp=(RadioGroup)findViewById(R.id.newpr);
	updatePref=(Button)findViewById(R.id.updatepref);
	updatePref.setOnClickListener(this);
	
	}
	public void onBackPressed()
	{
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==updatePref)
		{
			int selectedId = rp.getCheckedRadioButtonId();
	        r1=(RadioButton)findViewById(selectedId);
		   Toast.makeText(getApplicationContext(),r1.getText(),Toast.LENGTH_LONG).show();
			
		    new AsyncTask<Void,Void,Void>() {
				
		    	@Override
		    	protected void onPreExecute()
		    	{
		    		status=new ProgressDialog(Npreference.this);
		    		status.setMessage("Please wait...");
				    status.setCancelable(false);
					status.show();
		    	}
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					HttpClient httpclient = new DefaultHttpClient();
				    HttpResponse response = null;
					try {
					    url="http://www.spkdroid.com/merlin/newspref.php?login_id="+r+"&pref="+r1.getText().toString();
						response = httpclient.execute(new HttpGet(url));
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    StatusLine statusLine = response.getStatusLine();
				    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				        ByteArrayOutputStream out = new ByteArrayOutputStream();
				        try {
							response.getEntity().writeTo(out);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        String responseString = out.toString();
				        try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        //..more logic
				    } else{
				        //Closes the connection.
				        try {
							response.getEntity().getContent().close();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        try {
							throw new IOException(statusLine.getReasonPhrase());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					// Dismiss the progress dialog
				
					  	status.dismiss();
						
						new AlertDialog.Builder(Npreference.this)
					    .setTitle("The News Feed Preference has been updated")
					    .setMessage("The News Feed has been updated as per your request")
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					      	finish();
					        }
					     }).show();
						
						
						
				//	Intent i=new Intent(getApplicationContext(),DalEvents.class);
				//	startActivity(i);
				}
			}.execute();
		}
	}
}