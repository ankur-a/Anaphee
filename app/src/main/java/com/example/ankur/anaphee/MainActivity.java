package com.example.ankur.anaphee;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.*;
import android.support.v7.widget.Toolbar;
import java.lang.Runnable;
import com.gigamole.library.PulseView;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public boolean ch=false;
    PulseView pulseView;
    DatabaseHelper mydb;
    private TextView mHearbeatText;
    final String instru= "Instructions";
    final String steps= "Step 1.Take off the safety cap\nStep 2.Hold the injector firmly in your hand.\nStep 3.Press it against your thigh firmly and hold it there.\nStep 4.Wait till the LED on top goes off\nStep 5.Remove the injector and massage the site for 10 seconds.";
    Button btnstart,btnstop;
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Button btnAddData;
    FloatingActionButton viewallBtn;
    Button btnOn, btnOff;
    TextView txtArduino, txtString, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    Handler bluetoothIn;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    String time_now = new String();
    private ConnectedThread mConnectedThread;
    SimpleDateFormat date,time;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
    NotificationCompat.Builder notification;
    private int id=123;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pulseView =(PulseView)findViewById(R.id.pv);

        mHearbeatText = (TextView) findViewById(R.id.txt_value);
        mydb = new DatabaseHelper(this);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationview= findViewById(R.id.nav_view);
        navigationview.bringToFront();
        navigationview.setNavigationItemSelectedListener(this);
        date = new SimpleDateFormat("yyyy/MM/dd");
        time = new SimpleDateFormat("HH/mm/ss");
        pulseView.startPulse();
        
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;
                    /*
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("!");
                    if(endOfLineIndex>0){
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        int dataLength= dataInPrint.length();
                        if(recDataString.charAt(0)=='#'){
                            String text= recDataString.substring(1,endOfLineIndex);
                            mHearbeatText.setText(readMessage);
                        }
                        recDataString.delete(0,recDataString.length());
                        dataInPrint=" ";
                    }
*/
                    if(!readMessage.contains("inf")){

                        ch=true;
                        String dateV = date.format(new Date());
                        long time = new Date().getTime();
                        boolean inserted= false;
                        Random rand = new Random();

                        int  n = rand.nextInt(10) + 72;
                        inserted= mydb.insertData(dateV,time,n);
                        int index1 = readMessage.indexOf("#");
                        int index2 = readMessage.indexOf("!");
                        mHearbeatText.setText(String.valueOf(n));
                        readMessage=" ";


                    }
                    readMessage="";

                }
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you having an anaphylactic shock ");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notification_func();
                                sendEmail();
                                dialog.dismiss();
                                showMessage(instru,steps);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

    }

    private void sendEmail() {
        //Getting content for email
        String email = "iiitdmj7@gmail.com";
        String subject = "Patient is having anaphylactic shock";
        String message = "Yash Pratap Singh is having a anaphylactic shock";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }


    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();

    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    public void notification_func() {
        notification.setSmallIcon(R.drawable.ic_launcher_background);
        notification.setTicker("warning");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Unstable rythm");
        notification.setContentText("Dangerous Vitals");
        Intent intent= new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager nm= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id,notification.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Log.e("here","akjakdjaskjd");
        int id=item.getItemId();
        //Toast.makeText(this,"SecondActivity",Toast.LENGTH_SHORT).show();
        if (id == R.id.home){
            //Toast.makeText(this,"SecondActivity",Toast.LENGTH_SHORT).show();
            Intent i= new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.statistics){
            //Toast.makeText(this,"SecondActivity",Toast.LENGTH_SHORT).show();
            Intent i= new Intent(this, statistics.class);
            startActivity(i);
        }
        if(id==R.id.history){
            Intent i= new Intent(this, history.class);
            startActivity(i);
        }
        if(id == R.id.instructions){
           showMessage(instru,steps);
        }
        if(id==R.id.about){
            Intent i= new Intent(this, About.class);
            startActivity(i);
        }
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mdrawerLayout.closeDrawers();
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        BluetoothDevice device;
        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        try {
            address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Failed to get address", Toast.LENGTH_LONG).show();
        }
        //create device and set the MAC address
        try{
            device = btAdapter.getRemoteDevice(address);
        }
        catch (Exception e){
            device = btAdapter.getRemoteDevice("00:21:13:02:1F:72");
        }


        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket connection failed", Toast.LENGTH_LONG).show();

        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);


                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Connection failed!!!!");
                alertDialog.setMessage("Do you want to retry");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                recreate();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
                //finish();

            }
        }
    }
}
