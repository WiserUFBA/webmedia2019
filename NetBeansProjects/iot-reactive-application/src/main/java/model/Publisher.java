/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import java.util.List;
import tatu.TATUWrapper;

/**
 *
 * @author cleberlira
 */
public class Publisher extends AbstractVerticle {

    private MqttClient mqttClient;
    private MqttClientOptions mqttOptions;
    private Device devices;

    @Override
    public void start() {
        try {
            this.mqttOptions = new MqttClientOptions();

            this.mqttOptions.setLocalAddress("192.168.0.12");

            this.mqttClient = MqttClient.create(vertx);

            this.mqttClient.connect(1883, mqttOptions.getLocalAddress(), s -> {

            });
        } catch (Exception e) {
            System.out.println("controller.Publisher.start()" + e.getMessage());
        }

    }

    public void sendFlowRequestBySensorDevice() {
//        try {
//            List<Device> devices = devices.getListDevices();
//            for (Device device : devices) {
//                List<Sensor> sensors = device.getListSensors();
//                for (Sensor sensor : sensors) {
//                    String flowRequest;
//                    if (sensor.getCollectionTime() <= 0) {
//                        flowRequest = TATUWrapperOld.getTATUFlowValue(sensor.getId(), defaultCollectionTime, defaultPublishingTime);
//                    } else {
//                        flowRequest = TATUWrapperOld.getTATUFlowValue(sensor.getId(), sensor.getCollection_time(), sensor.getPublishing_time());
//                    }
//                    System.out.println("[topic: " + device.getDeviceId() + "] " + flowRequest);
//                  //  sensor.publishTATUMessage(flowRequest, device.getDeviceId());
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
