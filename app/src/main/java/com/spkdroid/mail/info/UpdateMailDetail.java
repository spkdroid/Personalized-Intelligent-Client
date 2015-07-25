package com.spkdroid.mail.info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.spkdroid.calander.event.AddCalanderInfo;
import com.spkdroid.dayatdalclient.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


/**
 * Filename: UpdateMailDetail.java
 * 
 * This particular function has been deprecated
 * 
 * @deprecated  as of July 5,2015
 * 
 */

public class UpdateMailDetail extends Activity implements OnClickListener{
	
	EditText gUname,gPassword;
	Button gUpdate;
	static String r;
	ProgressDialog status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.update_mail_detail);
	gUname=(EditText)findViewById(R.id.gmailusername);
	gPassword=(EditText)findViewById(R.id.gmailpassword);
	gUpdate=(Button)findViewById(R.id.gupdate);
	gUpdate.setOnClickListener(this);
	Intent intent=getIntent();
	r=intent.getStringExtra("dalid");
	}
	
	public void onBackPressed()
	{
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==gUpdate)
		{
		
			
		    new AsyncTask<Void,Void,Void>() {
				
		    	@Override
		    	protected void onPreExecute()
		    	{
		    		status=new ProgressDialog(UpdateMailDetail.this);
		    		status.setMessage("Please wait...");
				    status.setCancelable(false);
					status.show();
				}
		    	
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					String URL="http://www.spkdroid.com/merlin/gmailupdate.php?gmail_dal_id="+r+"&gmail_username="+gUname.getText().toString()+"&gmail_password="+gPassword.getText().toString();
					HttpClient httpclient = new DefaultHttpClient();
				    HttpResponse response = null;
					try {
						response = httpclient.execute(new HttpGet(URL));
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
					if (status.isShowing())
					{
					
						
						new AlertDialog.Builder(UpdateMailDetail.this)
					    .setTitle("You Details Added to Calander")
					    .setMessage("The Event that you have made the update has been added to the Calander.You can now view about the detail in the Kiosk!!!")
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        	status.dismiss();
								finish();
					        }
					     });
					}
				}
			}.execute();
		}
	}
}