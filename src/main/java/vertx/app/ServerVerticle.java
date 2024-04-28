package vertx.app;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ServerVerticle extends AbstractVerticle {


    public void start() {
        System.out.println("Server Verticle Deployed :)");
        Router router = Router.router(vertx);
        HttpServer server = vertx.createHttpServer();
        int port = 9198;
        server.requestHandler(router).listen(port, "0.0.0.0", res -> {
            if (res.succeeded()) {
                System.out.println("Server started on port 9198");
            } else {
                System.out.println("Failed to start the server: " + res.cause());
            }
        });

        router.post("/api/v1/send-notification").handler(this::handleNotification);
    }

    private void handleNotification(RoutingContext routingContext) {
        routingContext.request().bodyHandler(buffer -> {
            if (buffer.length() > 0) {
                JsonObject requestBody = buffer.toJsonObject();
                String notificationBody = requestBody.toString();
                EventBus eb = vertx.eventBus();
                eb.publish(MainVerticle.NOTIFICATION_CHANNEL, notificationBody);
                HttpServerResponse response = routingContext.response();
                response.putHeader("Content-Type", "text/plain").end("Notification received");
            } else {
                HttpServerResponse response = routingContext.response();
                response.setStatusCode(400)
                        .putHeader("Content-Type", "text/plain")
                        .end("Invalid request body. Please provide a message field");
            }
        });
    }
}
