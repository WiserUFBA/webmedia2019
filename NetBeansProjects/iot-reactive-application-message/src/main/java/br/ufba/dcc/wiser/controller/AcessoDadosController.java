package br.ufba.dcc.wiser.controller;

import br.ufba.dcc.wiser.util.Util;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class AcessoDadosController extends AbstractVerticle {

    @Override
    public void start() {

        System.out.println("Initializing vertx");

        EventBus eventBus = vertx.eventBus();

        eventBus.<String>consumer(Util.ENDERECOBARRAMENTO, message -> {
            System.out.println("Accessing message: ");

            JsonObject json = new JsonObject()
                    .put("served-by", this.toString());

            if (message.body().isEmpty()) {
                message.reply(json.put("message", "hello"));
            } else {
                message.reply(json.put("message", message.body()));

                System.out.println("message" + message.body());
            }
        });

    }

}
