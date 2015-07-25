package com.spkdroid.calander.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dal.dalevents.admin.AddDescription;
import com.spkdroid.dayatdalclient.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



/*
 * Filename : AddCalanderInfo.java
 * 
 * AddCalanderInfo is used to update the event details to the server.
 * 
 * event name,event time,event description and event date are the four parameters that are used 
 * 
 * when the user updates the information.
 * 
 * 
 */

public class AddCalanderInfo extends Activity implements OnClickListener
{

private int hour;
private int minute;
private int year=2015;
private int month=02;
private int day=28;
private String sysdate;
EditText event_name,event_message;
Button event_date,event_time,event_update;
TextView date_txt;
ProgressDialog status;
static String dal_id,dal_event_name,dal_event_msg,dal_event_date,dal_event_time;
static String r;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_calander_event);
		
		Intent intent=getIntent();
		r=intent.getStringExtra("dalid");
		
		event_name=(EditText) findViewById(R.id.eventname);
		event_message=(EditText) findViewById(R.id.eventdescription);
		event_date=(Button) findViewById(R.id.date);
		event_date.setOnClickListener(this);
		date_txt=(TextView)findViewById(R.id.date_text);
		event_time=(Button) findViewById(R.id.timeevent);
		event_time.setOnClickListener(this);
		event_update=(Button) findViewById(R.id.update_event);
		event_update.setOnClickListener(this);
	}
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			event_time.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
		}
	};
	
	
	private DatePickerDialog.OnDateSetListener datePickerListener 
    = new DatePickerDialog.OnDateSetListener() {

// when dialog box is closed, below method will be called.
public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

year = selectedYear;
month = selectedMonth;
day = selectedDay;
// set selected date into textview
date_txt.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
}
};
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	public void onBackPressed()
	{
		finish();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			// set time picker as current time
			return new TimePickerDialog(this,timePickerListener, hour, minute,false);
		case 1:
			  return new DatePickerDialog(this, datePickerListener,year, month,day);
		}
		return null;
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	if(v==event_date)
	{
        showDialog(1);
	}
	if(v==event_time)
	{
		showDialog(0);
	}
	
	if(v==event_update)
	{
		if(event_name.getText().toString().length()>0 && event_message.getText().toString().length()>0 && date_txt.getText().length()<12)
		{
	    new AsyncTask<Void,Void,Void>() {
			@Override
	    	protected void onPreExecute()
	    	{
	    		status=new ProgressDialog(AddCalanderInfo.this);
	    		status.setMessage("Please wait...");
			    status.setCancelable(false);
				status.show();
			}
	    	
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String URL="http://www.spkdroid.com/merlin/eventupdate.php?dal_id="+r+"&event_name="+event_name.getText().toString().replace(" ","%20")+"&event_date="+date_txt.getText().toString().replace(" ","%20")+"&event_time="+event_time.getText().toString().replace(" ","%20")+"&event_msg="+event_message.getText().toString().replace(" ","%20");
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
				status.dismiss();
					new AlertDialog.Builder(AddCalanderInfo.this)
				    .setTitle("You Details Added to Calander")
				    .setMessage("The Event that you have made the update has been added to the Calander.You can now view about the detail in the Kiosk!!!")
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
		else
		{
			Toast.makeText(getApplicationContext(),"Input Field Missing",Toast.LENGTH_LONG).show();
		}
	}
}
}