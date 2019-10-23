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
public class Provider extends AbstractVerticle{
    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/").handler(this::webmedia);
        router.get("/:name").handler(this::webmedia);
        
        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080);
    }

    private void webmedia(RoutingContext rc) {
        String message = "webmedia 2019";
        if (rc.pathParam("name") != null) {
            message += " " + rc.pathParam("name");
        }
        JsonObject json = new JsonObject()
            .put("mensagem", message)
            .put("fornecido por ", System.getenv("HOSTNAME"));
        rc.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(json.encode());
    }
}
