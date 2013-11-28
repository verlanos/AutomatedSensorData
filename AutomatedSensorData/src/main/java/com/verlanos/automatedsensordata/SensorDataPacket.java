package com.verlanos.automatedsensordata;

import java.net.InetAddress;

/**
 * Created by Sefverl on 11/28/13.
 */
public class SensorDataPacket {

    private InetAddress address;
    private int port;

    public SensorDataRecord getRecord() {
        return record;
    }

    public void setRecord(SensorDataRecord record) {
        this.record = record;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private SensorDataRecord record;

    public SensorDataPacket(InetAddress address, int port, SensorDataRecord record)
    {
        this.address = address;
        this.port = port;
        this.record = record;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }
}
