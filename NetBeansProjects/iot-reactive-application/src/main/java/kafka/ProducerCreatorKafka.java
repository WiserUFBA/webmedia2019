/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafka;
import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import java.util.Properties;



/**
 *
 * @author cleberlira
 */
public class ProducerCreatorKafka extends AbstractVerticle {
    
    
    public KafkaProducer<Long, String> createProducer(){
                Properties props = new Properties();

                KafkaProducer<Long, String> producer = KafkaProducer.create(vertx, props);

        return producer;
    
    }
}
