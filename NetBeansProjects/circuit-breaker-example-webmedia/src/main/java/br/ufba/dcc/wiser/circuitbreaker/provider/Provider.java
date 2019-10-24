/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.circuitbreaker.provider;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


/**
 *
 * @author cleberlira
 */
public class Provider extends AbstractVerticle {
    
    
    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/").handler(this::webmedia);
        router.get("/:nome").handler(this::webmedia);
        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080);
    }

    private void webmedia(RoutingContext rc) {
        String message = "webmedia 2019";
        if (rc.pathParam("nome") != null) {
            message += " " + rc.pathParam("nome");
        }
        JsonObject json = new JsonObject()
            .put("mensagem : ", message)
            .put("Fornecido por: ", System.getenv("HOSTNAME"));
        rc.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(json.encode());
    }
}
