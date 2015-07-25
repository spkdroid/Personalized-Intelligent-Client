package com.dal.dalevents.admin;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDescription extends Activity implements OnClickListener 
{
	
	
	// http://www.spkdroid.com/webapp/update.php?event_name=WALTER&event_school=waLTER&event_time=21.00&event_date=13-2-2015&event_desc=dasknda&event_location=Dalhousie&submit=submit
	
	String eventname,venue,time,date,department,descr;
	
	EditText description;
	
	Button submit;
	
	ProgressDialog status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event_final);

		description=(EditText)findViewById(R.id.descr);
		submit=(Button)findViewById(R.id.submit);
		
		eventname=getIntent().getExtras().getString("eventname");
		venue=getIntent().getExtras().getString("venue");
		time=getIntent().getExtras().getString("time");
		date=getIntent().getExtras().getString("date");
		department=getIntent().getExtras().getString("department");
	
		date=date.trim();
		
		
		eventname=eventname.replace(" ","+");
		venue=venue.replace(" ","+");
		time=time.replace(" ","+");
		date=date.replace(" ","+");
		department=department.replace(" ","+");
		
		
	//	Toast.makeText(getApplicationContext(),eventname,Toast.LENGTH_LONG).show();
	//	Toast.makeText(getApplicationContext(),venue,Toast.LENGTH_LONG).show();
	//	Toast.makeText(getApplicationContext(),time,Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(),date,Toast.LENGTH_LONG).show();
	//	Toast.makeText(getApplicationContext(),department,Toast.LENGTH_LONG).show();
		
		submit.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		descr=description.getText().toString();
	    descr=descr.replace(" ","+");
	
	    new AsyncTask<Void,Void,Void>() {
		
	    	@Override
	    	protected void onPreExecute()
	    	{
	    		status=new ProgressDialog(AddDescription.this);
	    		status.setMessage("Please wait...");
			    status.setCancelable(false);
				status.show();
	    	}
	    	
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String URL="http://www.spkdroid.com/webapp/update.php?event_name="+eventname+"&event_school="+department+"&event_time="+time+"&event_date="+date+"&event_desc="+descr+"&event_location="+venue+"&submit=submit";
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
				status.dismiss();
				finish();
			//	Intent i=new Intent(getApplicationContext(),DalEvents.class);
			//	startActivity(i);
				}
			}
		}.execute();
	    }
	
	public void onBackPressed()
	{
		
	}
	
	}