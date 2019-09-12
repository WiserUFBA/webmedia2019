/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttClientOptions;
import org.apache.edgent.topology.Topology;
import org.eclipse.paho.client.mqttv3.MqttException;
/**
 *
 * @author cleberlira
 */
public class Device extends AbstractVerticle{
    
    
    private String gatewayID;
    private String deviceId;
    private double latitude;
    private double longitude;
    private List<Sensor> listSensors;
    private Topology topology;
    private MqttClientOptions mqttOptions;
 
    
    public Device( String gatewayID) {
        this.gatewayID = gatewayID;
        
    }
    
    
    
     public Device(MqttClientOptions mqttOptions, String gatewayID) {
        this.gatewayID = gatewayID;
        this.deviceId = "";
           
        
//        if (mqttConfig == null) {
//            mqttConfig = MqttConfig.fromProperties(properties);
//        // mqttConfig.setClientId(this.clientMqttId);
//        }
        this.mqttOptions = mqttOptions;
        
    }
    
   
    /**
     * @return the gatewayID
     */
    public String getGatewayID() {
        return gatewayID;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return the listSensors
     */
    public List<Sensor> getListSensors() {
        return listSensors;
    }

    /**
     * @param gatewayID the gatewayID to set
     */
    public void setGatewayID(String gatewayID) {
        this.gatewayID = gatewayID;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @param listSensors the listSensors to set
     */
    public void setListSensors(List<Sensor> listSensors) {
        this.listSensors = listSensors;
    }

    /**
     * @return the mqttOptions
     */
    public MqttClientOptions getMqttOptions() {
        return mqttOptions;
    }

    /**
     * @param mqttOptions the mqttOptions to set
     */
    public void setMqttOptions(MqttClientOptions mqttOptions) {
        this.mqttOptions = mqttOptions;
    }
    
   
    
}
