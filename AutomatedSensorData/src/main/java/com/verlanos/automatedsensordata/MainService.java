package com.verlanos.automatedsensordata;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class MainService extends Service implements SensorEventListener
{
    private SensorManager sensorManager;
    private WifiManager wifiManager;
    private TelephonyManager telephonyManager;
    private String uniqueID;
    private static final String TAG = "com.verlanos.AutomatedSensorData";
    private boolean isAlive;
    private HashMap<String,SensorDataPacket> sensorValues;
    private InetAddress server_host;
    private int server_port;

    @Override
    public void onCreate() {
        Log.i(TAG,"Service onCreate");
        this.isAlive = true;
        this.sensorValues = new HashMap<String, SensorDataPacket>();
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        this.telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent,flags,startId);

        Bundle b = intent.getExtras();
        String server_address = b.getString("server_address");
        String server_port = b.getString("server_port");

        setDestinationServer(server_address, server_port);

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,temperature,SensorManager.SENSOR_DELAY_NORMAL);

        Log.i(TAG,"Service onStartCommand");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"Service running");
                while(isAlive)
                {
                    long endTime = System.currentTimeMillis() + 60*1000;
                    while(System.currentTimeMillis() < endTime)
                    {
                        synchronized (this)
                        {
                            try{
                                wait(endTime - System.currentTimeMillis());
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }
                        SensorDataPacket[] array = new SensorDataPacket[sensorValues.size()];
                        sensorValues.values().toArray(array);
                        sendSensorData(array);

                }
                stopSelf();
            }
        };

        Thread t = new Thread(r);
        t.start();

        Toast.makeText(this,"Sensor Service starting",Toast.LENGTH_SHORT);
        return Service.START_STICKY;
    }

    private void setDestinationServer(String serverAddress,String serverPort)
    {
        try
        {
            this.server_host = InetAddress.getByName(serverAddress);
            this.server_port = Integer.parseInt(serverPort);
        }
        catch (Exception e)
        {
            System.out.println("Unable to resolve hostname of remote hub");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service onDestroy");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            String data = ""+sensorEvent.values[0];
            String meta_data = "Lux";
            String device_id = wifiManager.getConnectionInfo().getMacAddress();
            SensorDataRecord dataRecord = new SensorDataRecord(data,meta_data,device_id);
            SensorDataPacket packet = new SensorDataPacket(server_host,server_port,dataRecord);
            sensorValues.put("LIGHT",packet);

        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
        {
            String data = ""+sensorEvent.values[0];
            String meta_data = "Celsius";
            String device_id = wifiManager.getConnectionInfo().getMacAddress();
            SensorDataRecord dataRecord = new SensorDataRecord(data,meta_data,device_id);
            SensorDataPacket packet = new SensorDataPacket(server_host,server_port,dataRecord);
            sensorValues.put("TEMPERATURE",packet);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void sendSensorData(SensorDataPacket dataPacket)
    {
       UDPPacketSender packetSender = new UDPPacketSender();
       packetSender.execute(dataPacket);
    }

    private void sendSensorData(SensorDataPacket[] dataPackets)
    {
        UDPPacketSender packetSender = new UDPPacketSender();
        packetSender.execute(dataPackets);
    }
}
