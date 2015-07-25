package com.spkdroid.dayatdalclient;

/*
 *  FileName: AddUser.java
 *  
 *  
 *  This is used when a new user sign up with the system.
 *  
 *  
 *  This particular activity will be reading the username,password,emailid and dalid from the user and makes
 *  an entry into the system
 * 
 * 
 */


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spkdroid.servicehandle.ServiceHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddUser extends Activity implements OnClickListener {

	EditText name,password,mailid,dalid;
	Button signUp;
	TextView alertmsg;
	String client_name,client_password,client_mailid,client_dalid;
	private static String service;
	private static String jsonStr;
	String Name;
	ProgressDialog pg;
	
	// http://www.spkdroid.com/merlin/updatelogin.php?login_dpt=2&login_id=B00999999&login_name=Thomas&login_password=Tom
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * 
		 * Linking the XML data with the JAVA Activity
		 * 
		 */
		setContentView(R.layout.activity_add_user);
		name=(EditText)findViewById(R.id.addUserName);
		password=(EditText)findViewById(R.id.addPassword);
		mailid=(EditText)findViewById(R.id.dalmailid);
		dalid=(EditText)findViewById(R.id.dalid);
		signUp=(Button)findViewById(R.id.SignUp);
		signUp.setOnClickListener(this);
		alertmsg=(TextView)findViewById(R.id.alertmsg);
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onBackPressed()
	{
		finish();
		Intent i=new Intent(this,MainActivity.class);
		startActivity(i);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
	/*
	 * Removing the NULL space and replacing with %20 so that it can be fired into the server
	 */
		
		
    client_name=name.getText().toString().replace(" ","%20");
	client_password=password.getText().toString().replace(" ","%20"); 
	client_mailid=mailid.getText().toString().replace(" ","%20");	
	client_dalid=dalid.getText().toString().replace(" ","%20");
	
	/*
	 * 
	 * A condition check wheather all the field has been entered with the data
	 * 
	 */
	
	if(client_name.length()>0 && client_password.length()>0 && client_mailid.length()>0 && client_dalid.length()>0)
	{
	    
		/*
		 * Constructing the URL to fire the update service.
		 * 
		 * Once the Update is made in the server
		 * 
		 * The server will be responding with a SUCCESS tag or FAILURE tag
		 * 
		 */
		service="http://www.spkdroid.com/merlin/updatelogin.php?login_dpt="+client_mailid+"&login_id="+client_dalid+"&login_name="+client_name+"&login_password="+client_password;
		/*
		 * 
		 * BackGround Process to Hit the Server
		 * 
		 */
		
		
		new AsyncTask<Void, Void,Void>() {
			@Override
			protected void onPreExecute()
			{
				pg=new ProgressDialog(AddUser.this);
				pg.setMessage("Please Wait");
				pg.show();
			}
			@Override
			protected Void doInBackground(Void... params) {
				ServiceHandler sh = new ServiceHandler();
				jsonStr = sh.makeServiceCall(service, ServiceHandler.GET);
				try {
					JSONArray jr=new JSONArray(jsonStr);
					JSONObject jsonObj=new JSONObject();
					jsonObj=jr.getJSONObject(0);
					Name=jsonObj.getString("result");
					Log.i("Ram:",Name);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			    pg.dismiss();
			    
			    /*
			     * when the server returns a success tag the application will be showing a success message and will
			     * redirect it to the login page.
			     * 
			     * The else part of the code will be explaining if there is any failure of the from the server
			     */
			    
			    if(Name.equals("SUCCESS"))
			    {
			    	File fp=new File(Environment.getExternalStorageDirectory()+"/dayatdal.txt");
			    	try {
						FileUtils.writeStringToFile(fp,client_dalid);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	
	    	    	new AlertDialog.Builder(AddUser.this)
			        .setTitle("Thanks for Signing Up")
			        .setMessage("Thanks For Adding")
			        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) { 
			                // continue with delete
			            	finish();
			        		Intent i=new Intent(AddUser.this,MainActivity.class);
			        		startActivity(i);
			            }
			         })
			        .setIcon(R.drawable.ic_launcher)
			        .show();
			    }
			    else
			    {
			    	new AlertDialog.Builder(AddUser.this)
			        .setTitle("Error")
			        .setMessage("Already you had done the registration")
			        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) { 
			                // continue with delete
			            dialog.dismiss();
			            }
			         })
			        .setIcon(R.drawable.ic_launcher)
			        .show();
			    }
			}
			}.execute();	
	}       
	else
	{
		/*
		 * when the field is empty the toast message is show.
		 * 
		 */
		Toast.makeText(AddUser.this,"Text Feild Missing",Toast.LENGTH_LONG).show();
	}
	}
}