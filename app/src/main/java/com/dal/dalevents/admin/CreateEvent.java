package com.dal.dalevents.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.spkdroid.dayatdalclient.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEvent extends Activity implements OnClickListener {

private static final int TIME_DIALOG_ID = 0;

private int hour;

private int minute;

EditText eventname;

Spinner department,venue;

Button time,date,next;

TextView time_txt,date_txt;

private int year=2015;

private int month=02;

private int day=28;

private String sysdate;

Date d;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_login);
	
		eventname=(EditText)findViewById(R.id.editText1);
		
		time=(Button)findViewById(R.id.selecttimebutton);
		date=(Button)findViewById(R.id.calendarselectbutton);
        next=(Button)findViewById(R.id.next);
        
        department=(Spinner)findViewById(R.id.departmentlist);
        venue=(Spinner)findViewById(R.id.venuelist);
        
        time_txt=(TextView)findViewById(R.id.time);
        date_txt=(TextView)findViewById(R.id.calendar);
        
         time.setOnClickListener(this);
         date.setOnClickListener(this);
         next.setOnClickListener(this);
        
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         String sysdate= formatter.format(new Date());
         
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         try {
			d=sdf.parse(time_txt.getText().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==time)
		{
			showDialog(0);
		}
		if(v==date)
		{
			showDialog(1);
		}
		
		if(v==next)
		{
			boolean f=true;
			if(eventname.getText().toString().length()>0)
			{
				f=false;
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Enter Event Name",Toast.LENGTH_LONG).show();
			}
			
			if(!time_txt.getText().toString().equals("Select Time"))
			{
				f=false;
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Enter Time",Toast.LENGTH_LONG).show();
			}
			
		//	if(sysdate.compareTo(d)>0)
		//	{
		//		f=true;
		//	}
		//	else
		//	{
		//		Toast.makeText(getApplicationContext(),"Date Has been already ended",Toast.LENGTH_LONG).show();
		//	}
		
			Intent i =new Intent(getApplicationContext(),AddDescription.class);
			i.putExtra("eventname",eventname.getText().toString());
			i.putExtra("time",time_txt.getText().toString());
			i.putExtra("date",date_txt.getText().toString());
			i.putExtra("department",department.getSelectedItem().toString());
			i.putExtra("venue",venue.getSelectedItem().toString());
			startActivity(i);
			finish();
			
		}
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

	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			time_txt.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
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
	//	Intent i=new Intent(getApplicationContext(),DalEvents.class);
	//	startActivity(i);
	}
	
}





