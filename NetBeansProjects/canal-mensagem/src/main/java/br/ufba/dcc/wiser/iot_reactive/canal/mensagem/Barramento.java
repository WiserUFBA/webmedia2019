/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iot_reactive.canal.mensagem;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

/**
 *
 * @author cleberlira
 */
public class Barramento extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Barramento.class.getName());
    }

    @Override
    public void start() {
        vertx.setPeriodic(5000, id -> {
            EventBus eventBus = vertx.eventBus();
            
            eventBus.publish("webmedia2019barramento", "enviando mensagens webmedia RIO " + id);

            EventBus eb = vertx.eventBus();

            MessageConsumer<String> consumer = eb.consumer("webmedia2019barramento");

            vertx.setPeriodic(5000, id1 -> {
                consumer.handler(message -> {
                    System.out.println("Recebendo mensagens: " + message.body());
                });
            });

        });

    }

}
