/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iot_reactive.circuit.breaker.example.webmedia.client;

import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.rxjava.circuitbreaker.CircuitBreaker;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.client.HttpRequest;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import rx.Single;

/**
 *
 * @author cleberlira
 */
public class Client extends AbstractVerticle {

    private WebClient webmedia;
    private CircuitBreaker circuit;

    private boolean ready;

    @Override
    public void start() {

        circuit = CircuitBreaker.create("my-circuit-webmedia", vertx,
                new CircuitBreakerOptions()
                        .setFallbackOnFailure(true) 
                        .setTimeout(2000) 
                        .setMaxFailures(3) // Número de falhas antes de ir para o status = OPEN
                        .setResetTimeout(5000));

        Router router = Router.router(vertx);
        router.get("/").handler(this::invokeHelloMicroservice);
        router.get("/health").handler(HealthCheckHandler.create(vertx).register("servidor http ativo",
                future -> future.complete(ready ? Status.OK() : Status.KO())));

        ServiceDiscovery.create(vertx, discovery -> {

            Single<WebClient> single = HttpEndpoint.rxGetWebClient(discovery,
                    rec -> rec.getName().equalsIgnoreCase("circuit-breaker"));

            circuit.halfOpenHandler(v -> single.subscribe(client -> this.webmedia = client));

            single.subscribe(
                    client -> {
                        this.webmedia = client;
                        // start the HTTP server
                        vertx.createHttpServer()
                                .requestHandler(router::accept)
                                .listen(8080, ar -> ready = ar.succeeded());
                    },
                    err -> System.out.println("serviço inexistente")
            );
        });

    }

    private void invokeHelloMicroservice(RoutingContext rc) {
        circuit.rxExecuteCommandWithFallback(
                future -> {
                    HttpRequest<JsonObject> request1 = webmedia.get("/webmedia2019")
                            .as(BodyCodec.jsonObject());

                    HttpRequest<JsonObject> request2 = webmedia.get("/ufba2020")
                            .as(BodyCodec.jsonObject());

                    Single<JsonObject> s1 = request1
                            .rxSend().map(HttpResponse::body);
                    Single<JsonObject> s2 = request2
                            .rxSend().map(HttpResponse::body);

                    Single
                            .zip(s1, s2, (webmedia2019, ufba2020) -> {
                                return new JsonObject()
                                        .put("webmedia2019", webmedia2019.getString("message")
                                                + " " + webmedia2019.getString("served-by"))
                                        .put("ufba2020", ufba2020.getString("message")
                                                + " " + ufba2020.getString("served-by"));
                            })
                            .subscribe(future::complete, future::fail);
                },
                error -> new JsonObject().put("mensagem", " (fallback, "
                        + circuit.state().toString() + ")")
        ).subscribe(
                x -> rc.response().end(x.encodePrettily()),
                t -> rc.response().end(t.getMessage()));
    }
}
