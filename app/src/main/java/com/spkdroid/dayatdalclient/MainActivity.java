package com.spkdroid.dayatdalclient;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spkdroid.servicehandle.ServiceHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  Filename : MainActivity.java
 *
 *  This is the first screen in the client application
 *
 *
 *  the user can login into the system or just can sign up
 *
 *  Once the Authentication is done this page will not be fired again
 *
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	Button LoginButton,NewUser;
	EditText netid,password;
	ProgressDialog pd;
	TextView tv;
    private static String url;
	private static String jsonStr="ram";
	String tokenresult;
	
	ArrayList<String> r=new ArrayList<String>();
	ProgressDialog pr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LoginButton=(Button) findViewById(R.id.loginButton);
		NewUser=(Button) findViewById(R.id.newUser);
		LoginButton.setOnClickListener(this);
		NewUser.setOnClickListener(this);
		netid=(EditText)findViewById(R.id.uname);
		password=(EditText)findViewById(R.id.pword);
		tv=(TextView)findViewById(R.id.errormessage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		/*
		 * 
		 * When the User clicks the AddUser button
		 * 
		 */
		
		if(v==NewUser)
		{
			finish();
			Intent i=new Intent(this,AddUser.class);
			startActivity(i);
		}
		
		
		
		/*
		 * When the User clicks the login button
		 * 
		 * The application will fetch the username and the password and will correlate with the server
		 * 
		 * The Server will be returing TRUE tag or FALSE tag.
		 * 
		 * 
		 */
		
		
		if(v==LoginButton)
		{
		final String userName;
		String passWord;
		userName=netid.getText().toString();
		passWord=password.getText().toString();
	//	url=url+"?login_id="
		
		if(userName.length()>0 && passWord.length()>0)
		{
			Toast.makeText(getApplicationContext(),userName+":"+passWord,Toast.LENGTH_LONG).show();
			url="http://www.spkdroid.com/merlin/passwordcheck.php?login_id="+userName.trim().replace(" ","%20")+"&login_password="+passWord.trim().replace(" ","%20");
			new AsyncTask<Void, Void,Void>() {
				@Override
				protected void onPreExecute()
				{
					pr=new ProgressDialog(MainActivity.this);
					pr.setMessage("Please Wait");
					pr.show();
				}
				@Override
				protected Void doInBackground(Void... params) {
					ServiceHandler sh = new ServiceHandler();
					jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
					
					try {
						JSONArray jr=new JSONArray(jsonStr);
						JSONObject jsonObj=new JSONObject();
						jsonObj=jr.getJSONObject(0);
						tokenresult=jsonObj.getString("result");
						Log.i("Ram:",tokenresult);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					if(tokenresult.equals("TRUE"))
					{
				    Toast.makeText(MainActivity.this,tokenresult,Toast.LENGTH_LONG).show();
				    finish();
				    File file=new File(Environment.getExternalStorageDirectory()+"dayatdal.txt");
					if(!file.exists())
					{
						File fp=new File(Environment.getExternalStorageDirectory()+"/dayatdal.txt");
				    	try {
							FileUtils.writeStringToFile(fp,netid.getText().toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				    Intent i=new Intent(MainActivity.this,Index.class);
				    i.putExtra("idName",userName);
				    startActivity(i);
					}
					else
					{
				    tv.setText("Password Incorrect");
					pr.dismiss();
					}
					}
			}.execute();	
		}
		else
		{
			Toast.makeText(getApplicationContext(),"Input Missing!!!",Toast.LENGTH_LONG).show();
		}
		}
	}
}