package com.verlanos.automatedsensordata;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Sefverl on 11/28/13.
 */
public class UDPPacketSender extends AsyncTask<SensorDataPacket,Void,Void>{

    @Override
    protected Void doInBackground(SensorDataPacket[] packets) {

        try
        {
            DatagramSocket dgramSocket = new DatagramSocket();
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);

            for(SensorDataPacket sensorDataPacket : packets)
            {
                packet.setData(sensorDataPacket.getRecord().toString().getBytes());
                dgramSocket.connect(sensorDataPacket.getAddress(),sensorDataPacket.getPort());
                dgramSocket.send(packet);
                System.out.println("SENDING: "+sensorDataPacket.getRecord()+" TO "+sensorDataPacket.getAddress()+" @ "+sensorDataPacket.getPort());
            }
        }
        catch (Exception e)
        {
            System.out.println("Sending datagram failed");
        }

        return null;
    }
}
