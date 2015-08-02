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

public class AddCalanderInfo extends Activity implements OnClickListener {

    // Variable Declaration that is used to declare the date and time
    private int hour;
    private int minute;
    private int year = 2015;
    private int month = 02;
    private int day = 28;
    private String sysdate;
    // Edit text to read the message name and the content that need to be shown
    EditText event_name, event_message;
    // Button that are in the page
    Button event_date, event_time, event_update;

    TextView date_txt;
    ProgressDialog status;

    static String dal_id, dal_event_name, dal_event_msg, dal_event_date, dal_event_time;
    static String r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_calander_event);

        Intent intent = getIntent();
        // Read the user name from the activity
        r = intent.getStringExtra("dalid");

        // Linking the XML file with the UI
        event_name = (EditText) findViewById(R.id.eventname);
        event_message = (EditText) findViewById(R.id.eventdescription);
        event_date = (Button) findViewById(R.id.date);
        event_date.setOnClickListener(this);
        date_txt = (TextView) findViewById(R.id.date_text);
        event_time = (Button) findViewById(R.id.timeevent);
        event_time.setOnClickListener(this);
        event_update = (Button) findViewById(R.id.update_event);
        event_update.setOnClickListener(this);
    }

    // Pick the time
    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    event_time.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
                }
            };

    // Pick the date
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
// set selected date into textview
            date_txt.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
        }
    };


    // when the value is less than 10 and 0 is appended to the digit
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // Back button to kill the page
    public void onBackPressed() {
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
            case 1:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == event_date) {
            // Launch the dialog
            showDialog(1);
        }
        if (v == event_time) {
            showDialog(0);
        }

        if (v == event_update) {

            // Reading the value from the edit text and storing it a string
            final String name = event_name.getText().toString().replace("", "%20");
            final String time = event_time.getText().toString().replace(" ", "%20");
            final String date = date_txt.getText().toString().replace(" ", "%20");
            final String msg = event_message.getText().toString().replace(" ", "%20");

            /**
             *
             * An updation process done in the background.
             *
             * One the update process is complete the application will post an success
             *
             * message and redirect to the menu screen.
             *
             */

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    status = new ProgressDialog(AddCalanderInfo.this);
                    status.setMessage("Please wait...");
                    status.setCancelable(false);
                    status.show();
                }

                // Request is constructed over here
                @Override
                protected Void doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    String URL = "http://www.spkdroid.com/merlin/eventupdate.php?dal_id=" + r + "&event_name=" + name + "&event_date=" + date + "&event_time=" + time + "&event_msg=" + msg;
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
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
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
                    } else {
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

                // when the process is completed the
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
                }
            }.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Input Field Missing", Toast.LENGTH_LONG).show();
        }
    }
}