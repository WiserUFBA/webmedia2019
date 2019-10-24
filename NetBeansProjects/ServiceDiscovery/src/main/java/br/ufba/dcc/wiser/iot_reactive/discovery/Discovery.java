/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iot_reactive.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

/**
 *
 * @author cleberlira
 */
public class Discovery extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Discovery.class.getName());
    }

    @Override
    public void start() {

        ServiceDiscovery discovery = ServiceDiscovery.create(vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress("uri-webmedia")
                        .setName("discovery"));

     
        Record record = new Record()
                .setType("eventbus")
                .setLocation(new JsonObject().put("endpoint", "the-event-address"))
                .setName("webmedia2019")
                .setMetadata(new JsonObject().put("bus", "this bus is clusterized"));

       
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                System.out.println("\"" + record.getName() + "\" successfully published!");
                Record publishedRecord = ar.result();
            } else {
                System.out.println("erro");
            }
        });

      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("webmedia2019"), ar -> {
           if (ar.succeeded() && ar.result() != null) {
                System.out.println("realizar ações..");
            }

        });

        
        discovery.close();
    }
}
