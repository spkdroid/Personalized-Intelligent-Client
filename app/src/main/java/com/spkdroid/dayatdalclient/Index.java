
package com.spkdroid.dayatdalclient;

import info.androidhive.camerafileupload.MainActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.spkdroid.calander.event.AddCalanderInfo;
import com.spkdroid.newsfeed.preference.Npreference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/*
 * Filename: Index.java
 * 
 *  The Index class is launched when the user gives correct username and password. This file will scan
 *  
 *  the surrounding for the kiosk. If there is a kiosk available the application will signal the user to show
 *  
 *  connect alertbox.
 *  
 *  When the user grant permission for the connection the kiosk will be showing the personalized information
 *  
 *  in the system.
 */



public class Index extends Activity implements OnClickListener {

	TextView textName,textBanner;
	ProgressDialog Indexpr;
	String jsonStr;
	String indexName,indexBanner;
	BluetoothDevice device;
	Button ConnectKiosk;
	Button profilepic;
	BluetoothAdapter mBluetoothAdapter =null;
	private BluetoothAdapter mBtAdapter;
	static int i=0;
	BluetoothAdapter adapter;
    List<String> supplierNames = new ArrayList();
    Button calc,mailc,newc;
	
	
	// A UUID for the identificaiton between the user and the kiosk
	private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	protected static final String ram = "Red";
	
	// Login Service URL
	private static String url="http://www.spkdroid.com/merlin/fetchdashboard.php?login_id=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		textName=(TextView)findViewById(R.id.Name);
		textBanner=(TextView)findViewById(R.id.bannerid);
		
		ConnectKiosk=(Button)findViewById(R.id.connectServer);
		ConnectKiosk.setOnClickListener(this);
		ConnectKiosk.setVisibility(View.INVISIBLE);
		
		calc=(Button)findViewById(R.id.calder);
		mailc=(Button)findViewById(R.id.Mail);
	//	newc=(Button)findViewById(R.id.NfPref);
		
		profilepic=(Button)findViewById(R.id.upload);
		profilepic.setOnClickListener(this);
		
		
		calc.setOnClickListener(this);
		mailc.setOnClickListener(this);
	//	newc.setOnClickListener(this);
		
		
		adapter=BluetoothAdapter.getDefaultAdapter();
		
		Intent intent=getIntent();
		String id=intent.getStringExtra("idName");
		url=url+id;
		textBanner.setText(id);

		
		IntentFilter filter = new IntentFilter();
		 
		filter.addAction(BluetoothDevice.ACTION_FOUND);
	//	filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		
		BroadcastReceiver mReceiver = new BroadcastReceiver()
		{
		        @Override
		        public void onReceive(Context context, Intent intent)
		        {
		            String action = intent.getAction();

		            // When discovery finds a device
		           if (BluetoothDevice.ACTION_FOUND.equals(action))
		            {
		        //	pairBridge();  
		        	    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		                // Add the name and address to an array adapter to show in a ListView
		        	    supplierNames.add(device.getName()+" "+device.getAddress());
		        	
		        	    if(device.getName()!=null)
		        	    {
		        	    if(device.getName().equals("Kiosk"))
		                {
		           //     Toast.makeText(getApplicationContext(),"Found a Kiosk",Toast.LENGTH_LONG).show();
		                pairBridge();
		                }
		        	    }
		            }
		           // else 
		            	if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
		            {
		      /*      	Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		            	final BluetoothDevice blueDev[] = new BluetoothDevice[pairedDevices.size()];
		        		String[] items = new String[blueDev.length];
		        		
		            	for (BluetoothDevice devicel : pairedDevices) {
		        			blueDev[i] = devicel;
		        	    	items[i] = blueDev[i].getName() + ": " + blueDev[i].getAddress();
		        	    	if(blueDev[i].getName().equals("Kiosk"))
		        	    	{
		        	    		pairBridge();
		        	    	}
		        	    }
		            	Log.v("Ramkumar","Entered the Finished ");*/
		            		
		            //		for(String r:supplierNames)
		            	//	{
		            		//	Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
		            	//	}
		      
		            }
		        }
		};

		
		registerReceiver(mReceiver, filter);
		adapter.startDiscovery();

		/**
		 *  A Async task running in the background that will fetch the name and the B00 id and will be
		 *  
		 *   displaying in the dashboard
		 * 
		 */
		
/*		new AsyncTask<Void, Void,Void>() {
			
			@Override
			protected void onPreExecute()
			{
				Indexpr=new ProgressDialog(Index.this);
				Indexpr.setMessage("Please Wait");
				Indexpr.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				ServiceHandler sh = new ServiceHandler();
				jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
				
				try {
					JSONArray jr=new JSONArray(jsonStr);
					JSONObject jsonObj=new JSONObject();
					jsonObj=jr.getJSONObject(0);
					indexName=jsonObj.getString("name");
					indexBanner=jsonObj.getString("id");
					Log.i("Ram:",indexName);
					Log.i("Kumar:",indexBanner);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			textName.setText(indexName);
			textBanner.setText(indexBanner);
			Indexpr.dismiss();
			}
			}.execute();	
	*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			
	    return true;
		}
		if(id == R.id.ucalinfo)
		{
			Intent i=new Intent(getApplicationContext(),AddCalanderInfo.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onBackPressed()
	{
		finish();
	//	Intent i=new Intent(getApplicationContext(),MainActivity.class);
	//	startActivity(i);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		if(v== ConnectKiosk)
		{
		 BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();	   
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    	// If there are paired devices
    	if (pairedDevices.size() > 0) {
    	    // Loop through paired devices
    		final BluetoothDevice blueDev[] = new BluetoothDevice[pairedDevices.size()];
    		String[] items = new String[blueDev.length];
    		int i =0;
    		for (BluetoothDevice devicel : pairedDevices) {
    			blueDev[i] = devicel;
    	    	items[i] = blueDev[i].getName() + ": " + blueDev[i].getAddress();
    	    	//mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
    	    	i++;
    	    }
    		AlertDialog.Builder builder = new AlertDialog.Builder(Index.this);
    		builder.setTitle("Kiosk Detected in the Range.Shall i connect?");
    		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    				dialog.dismiss();
    				if (item >= 0 && item <blueDev.length) { 
    					device = blueDev[item];
                        Log.e("IOP","EXE");    
    					if (device != null) {   	
    			    		new Thread(new ConnectThread(device,"SUCCESS:"+textBanner.getText().toString())).start();
    			    	}
    				}
    		    }
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
		}
		
		
		if(v == calc)
		{
			
			Intent i=new Intent(getApplicationContext(),AddCalanderInfo.class);
			i.putExtra("dalid",textBanner.getText().toString());
			startActivity(i);
		}
		
		if(v == mailc)
		{
			Intent i=new Intent(getApplicationContext(),Npreference.class);
			i.putExtra("dalid",textBanner.getText().toString());
			startActivity(i);
		}
		
		if(v == newc)
		{
		
		}
		
		if(v==profilepic)
		{
			Intent i=new Intent(getApplicationContext(),MainActivity.class);
			i.putExtra("dalid",textBanner.getText().toString());
			startActivity(i);
		}
		
	}
	
	void pairBridge()
	{

		// TODO Auto-generated method stub
		 BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();	   
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    	// If there are paired devices
    	if (pairedDevices.size() > 0) {
    	    // Loop through paired devices
    		final BluetoothDevice blueDev[] = new BluetoothDevice[pairedDevices.size()];
    		String[] items = new String[blueDev.length];
    		int i =0;
    		for (BluetoothDevice devicel : pairedDevices) {
    			blueDev[i] = devicel;        	   	
    			items[i] = blueDev[i].getName() + ": " + blueDev[i].getAddress();
    			i++;        	    
    	    }
    		AlertDialog.Builder builder = new AlertDialog.Builder(Index.this);
    		builder.setTitle("Kiosk Detected in the Range.Shall i connect?");
        	builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    				dialog.dismiss();
    				if (item >= 0 && item <blueDev.length) { 
    					device = blueDev[item];
                        Log.e("IOP","EXE");    
    					if (device != null) {   	
    			    		new Thread(new ConnectThread(device,"SUCCESS"+textBanner.getText().toString())).start();
    			    	}
    				}
    		    }
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
	}
	
	
	/**
	 * 
	 * ConnectThread is a thread class that is used to connect the  server
	 * 
	 * This part of the thread class ConnectThread is adopted from 
	 * 
	 * http://developer.android.com/guide/topics/connectivity/bluetooth.html
	 * 
	 * 
	 */
	
	
	private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice mmDevice;
        public String iod;
        
        public ConnectThread(BluetoothDevice device,String name) {
            mmDevice = device;
            iod=name;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            socket = tmp;
        }
        public void run() {
            try {
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e2) {
                    socket = null;
                }
            }
        	if (socket != null) {
        		try {
        			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
        			out.println(iod);
        			out.flush();
        			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        			String str = in.readLine();
        		} catch(Exception e) {
            		} finally {
        			try {
						socket.close();
					} catch (IOException e) {
					}
        		}
        	} else {
        	}
            }
	}
}