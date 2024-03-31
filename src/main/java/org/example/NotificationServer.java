package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class NotificationServer {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.post("/api/v1/send-notification").handler(NotificationServer::handleNotification);
        server.requestHandler(router).listen(8080);

    }

    private static void handleNotification(RoutingContext routingContext) {
        routingContext.request().bodyHandler(buffer -> {
            if (buffer != null) {
                JsonObject requestBody = buffer.toJsonObject();
                String notificationBody = requestBody.encode();
                String responseMessage = "Notification sent successfully\nRequest body: " + notificationBody;

                routingContext.response()
                        .putHeader("Content-Type", "text/plain")
                        .end(responseMessage);
            } else {
                routingContext.response()
                        .setStatusCode(400) // Bad Request
                        .putHeader("Content-Type", "text/plain")
                        .end("Invalid request body");
            }
        });
    }

}
