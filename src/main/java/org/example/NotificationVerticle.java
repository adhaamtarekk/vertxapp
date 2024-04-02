package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class NotificationVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.post("/api/v1/send-notification").handler(this::handleNotification);
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    private void handleNotification(RoutingContext routingContext) {
        routingContext.request().bodyHandler(buffer -> {
            if (buffer != null) {
                JsonObject requestBody = buffer.toJsonObject();
                String notificationBody = requestBody.encode();
                String responseMessage = "Notification sent successfully\nRequest body: " + notificationBody;

                HttpServerResponse response = routingContext.response();
                response.putHeader("Content-Type", "text/plain").end(responseMessage);
            } else {
                HttpServerResponse response = routingContext.response();
                response.setStatusCode(400) // Bad Request
                        .putHeader("Content-Type", "text/plain")
                        .end("Invalid request body");
            }
        });
    }
}