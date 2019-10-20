/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import io.netty.handler.codec.mqtt.MqttQoS;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import io.vertx.mqtt.MqttClientOptions;

import org.apache.edgent.connectors.mqtt.MqttConfig;
import org.apache.edgent.connectors.mqtt.MqttStreams;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttConnectionException;
import io.vertx.mqtt.impl.MqttClientImpl;
import io.vertx.rxjava.ext.shell.term.Pty;
import java.util.List;
import javax.naming.ServiceUnavailableException;
import kafka.ProducerCreatorKafka;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import tatu.TATUWrapper;
import util.MqttClientUtil;

/**
 *
 * @author cleberlira
 */
public class Sensor extends AbstractVerticle {

    private Topology topology;

    private int defaultCollectionTime;
    private int defaultPublishingTime;
    private String Sensorid;
    private String type;
    private int collectionTime;
    private int publishingTime;
    private MqttClient subscriber;
    private MqttClient publish;
    private MqttClientOptions mqttOptions;

    private MqttStreams connector;
    private String topicPrefix = "";
    private int qos;
    private Device device;
    private Path pathLog;
    private Path newFilePath;
    private BufferedWriter writer;
    private boolean changeCusum;
    private boolean isInitialized;

    private KafkaProducer<Long, String> producerKafka;

    private String topicKafka;
    private int throughputSensor;
    private ProducerCreatorKafka producerCreatorKafka;

    @Override
    public void start() {

    }

    public Sensor(MqttClientOptions mqttOptions,
            String Sensorid, Device device, Path pathLog) {

        this.Sensorid = Sensorid;
        this.isInitialized = true;
        this.device = device;
        this.pathLog = pathLog;
        //    

        this.qos = 0;

        sendFlowRequest();
    }

    public void sendFlowRequest() {

        String flowRequest;
        if (this.getCollectionTime() <= 0) {
            
            flowRequest = TATUWrapper.getTATUFlowValue(this.Sensorid, 10000, 20000);
        } else {
             flowRequest = TATUWrapper.getTATUFlowValue(this.Sensorid, this.getCollectionTime(), this.getPublishingTime());
        }
        System.out.println("[topic: " + getDevice().getDeviceId() + "] " + flowRequest);

        publishTATUMessage(flowRequest, getDevice().getDeviceId());
    }

    private void publishTATUMessage(String msg, String topicName) {
     
        Buffer buffer = Buffer.buffer(msg.getBytes());

        String topic = TATUWrapper.topicBase + topicName;

        try {
            this.publish = MqttClientUtil.getMqttClientUtil();

            publish.publish(topic, buffer, MqttQoS.AT_MOST_ONCE, false, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the collectionTime
     */
    public int getCollectionTime() {
        return collectionTime;
    }

    /**
     * @return the publishingTime
     */
    public int getPublishingTime() {
        return publishingTime;
    }

    /**
     * @return the device
     */
    public Device getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
     * @return the defaultCollectionTime
     */
    public int getDefaultCollectionTime() {
        return defaultCollectionTime;
    }

    /**
     * @param defaultCollectionTime the defaultCollectionTime to set
     */
    public void setDefaultCollectionTime(int defaultCollectionTime) {
        this.defaultCollectionTime = defaultCollectionTime;
    }

    /**
     * @return the defaultPublishingTime
     */
    public int getDefaultPublishingTime() {
        return defaultPublishingTime;
    }

    /**
     * @param defaultPublishingTime the defaultPublishingTime to set
     */
    public void setDefaultPublishingTime(int defaultPublishingTime) {

        this.defaultPublishingTime = defaultPublishingTime;
    }

    /**
     * @return the Sensorid
     */
    public String getSensorid() {
        return Sensorid;
    }

    /**
     * @param Sensorid thethis.connector.publish(cmdOutput, topic, this.qos,
     * false); Sensorid to set
     */
    public void setSensorid(String Sensorid) {
        this.Sensorid = Sensorid;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param collectionTime the collectionTime to set
     */
    public void setCollectionTime(int collectionTime) {
        this.collectionTime = collectionTime;
    }

    /**
     * @param publishingTime the publishingTime to set
     */
    public void setPublishingTime(int publishingTime) {
        this.publishingTime = publishingTime;
    }

    /**
     * @return the connector
     */
    public MqttStreams getConnector() {
        return connector;
    }

    /**
     * @param connector the connector to set
     */
    public void setConnector(MqttStreams connector) {
        this.connector = connector;
    }

    /**
     * @return the topicPrefix
     */
    public String getTopicPrefix() {
        return topicPrefix;
    }

    /**
     * @param topicPrefix the topicPrefix to set
     */
    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    /**
     * @return the qos
     */
    public int getQos() {
        return qos;
    }

    /**
     * @param qos the qos to set
     */
    public void setQos(int qos) {
        this.qos = qos;
    }

    /**
     * @return the pathLog
     */
    public Path getPathLog() {
        return pathLog;
    }

    /**
     * @param pathLog the pathLog to set
     */
    public void setPathLog(Path pathLog) {
        this.pathLog = pathLog;
    }

    /**
     * @return the newFilePath
     */
    public Path getNewFilePath() {
        return newFilePath;
    }

    /**
     * @param newFilePath the newFilePath to set
     */
    public void setNewFilePath(Path newFilePath) {
        this.newFilePath = newFilePath;
    }

    /**
     * @return the writer
     */
    public BufferedWriter getWriter() {
        return writer;
    }

    /**
     * @param writer the writer to set
     */
    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * @return the changeCusum
     */
    public boolean isChangeCusum() {
        return changeCusum;
    }

    /**
     * @param changeCusum the changeCusum to set
     */
    public void setChangeCusum(boolean changeCusum) {
        this.changeCusum = changeCusum;
    }

    /**
     * @return the isInitialized
     */
    public boolean isIsInitialized() {
        return isInitialized;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setIsInitialized(boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the topicKafka
     */
    public String getTopicKafka() {
        return topicKafka;
    }

    /**
     * @param topicKafka the topicKafka to set
     */
    public void setTopicKafka(String topicKafka) {
        this.topicKafka = topicKafka;
    }

    /**
     * @return the throughputSensor
     */
    public int getThroughputSensor() {
        return throughputSensor;
    }

    /**
     * @param throughputSensor the throughputSensor to set
     */
    public void setThroughputSensor(int throughputSensor) {
        this.throughputSensor = throughputSensor;
    }

    /**
     * @return the topology
     */
    public Topology getTopology() {
        return topology;
    }

    /**
     * @param topology the topology to set
     */
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public void printError(Exception e) {
        System.out.println("Error Kafka method " + this.Sensorid + " " + e.getMessage());
        StackTraceElement[] stack = e.getStackTrace();
        for (StackTraceElement stackTraceElement : stack) {
            System.out.println("Error class " + " " + stackTraceElement.getClassName());
            System.out.println("Error file " + " " + stackTraceElement.getFileName());
            System.out.println("Error method " + " " + stackTraceElement.getMethodName());
            System.out.println("Error line " + " " + stackTraceElement.getLineNumber());

        }
    }

    /**
     * @return the publish
     */
    public MqttClient getPublish() {
        return publish;
    }

    /**
     * @param publish the publish to set
     */
    public void setPublish(MqttClient publish) {
        this.publish = publish;
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
