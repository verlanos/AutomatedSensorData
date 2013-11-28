package com.verlanos.automatedsensordata;

/**
 * Created by Sefverl on 11/28/13.
 */
public class SensorDataRecord
{
    private String data;
    private String metaData;
    private String deviceId;

    public SensorDataRecord(String data,String metaData)
    {
        this.data = data;
        this.metaData = metaData;
    }

    public SensorDataRecord(String data,String metaData,String deviceId)
    {
        this.data = data;
        this.metaData = metaData;
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"data\":");
        builder.append("\""+this.data+"\",");
        builder.append("\"meta\":");
        builder.append("\""+this.metaData+"\",");
        builder.append("\"id\":");
        builder.append("\""+this.deviceId+"\"");
        builder.append("}");

        return builder.toString();
    }
}
