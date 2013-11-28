package com.verlanos.automatedsensordata;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.StrictMode;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int mId = 1419;
    private Intent mainService;
    private EditText server_host_entry,server_port_entry;
    private Button startServiceButton,stopServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
        }

        View v = getWindow().getDecorView();
        server_host_entry = (EditText) v.findViewById(R.id.server_address_entry);
        server_port_entry = (EditText)v.findViewById(R.id.server_port_entry);
        startServiceButton = (Button) v.findViewById(R.id.start_service_button);
        stopServiceButton = (Button) v.findViewById(R.id.stop_service_button);

        /*Notification.Builder nBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("ASD Service")
                .setContentText("Service is running");

        nBuilder.setOngoing(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(mId,nBuilder.build());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void startService(View view)
    {
        mainService = new Intent(this,MainService.class);

        /* Pack reporting server address and server port into a Bundle, so that it's available to the
         service that is about to be started.
          */

        if(view != null)
        {
            Bundle b = new Bundle();
            EditText server_address = (EditText)view.findViewById(R.id.server_address_entry);
            EditText server_port = (EditText)view.findViewById(R.id.server_port_entry);
            System.out.println(server_address);
            System.out.println(server_port);

            b.putString("server_address", server_host_entry.getText().toString());
            b.putString("server_port", server_port_entry.getText().toString());
            mainService.putExtras(b);

            startService(mainService);

            //Button startButton = (Button) view.findViewById(R.id.start_service_button);
            //Button stopButton = (Button) view.findViewById(R.id.stop_service_button);

            startServiceButton.setEnabled(false);
            stopServiceButton.setEnabled(true);
        }
    }

    public void stopService(View view)
    {
        if(mainService != null)
        {
            stopService(mainService);
        }

        startServiceButton.setEnabled(true);
        stopServiceButton.setEnabled(false);
    }

}
