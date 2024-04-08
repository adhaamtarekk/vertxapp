package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;



public class NotificationVerticle extends AbstractVerticle {
    private static final String WORKFLOW_TASK_QUEUE = "notification-workflow";
    private static final String ACTIVITY_TASK_QUEUE = "notification-activity";

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

                // Start the temporal workflow
                WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
                WorkflowClient client = WorkflowClient.newInstance(service);
                WorkflowOptions workflowOptions = getWorkflowOptions();
                NotificationWorkflow workflow = client.newWorkflowStub(NotificationWorkflow.class, workflowOptions);

                // Execute the workflow
                WorkflowClient.start(workflow::sendNotification, requestBody);

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
    private WorkflowOptions getWorkflowOptions() {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(WORKFLOW_TASK_QUEUE)
                .build();
    }
}













//
//
//public class NotificationVerticle extends AbstractVerticle {
//
//    @Override
//    public void start() {
//        Router router = Router.router(vertx);
//        router.post("/api/v1/send-notification").handler(this::handleNotification);
//        vertx.createHttpServer().requestHandler(router).listen(8080);
//    }
//
//    private void handleNotification(RoutingContext routingContext) {
//        routingContext.request().bodyHandler(buffer -> {
//            if (buffer != null) {
//                JsonObject requestBody = buffer.toJsonObject();
//                String notificationBody = requestBody.encode();
//                String responseMessage = "Notification sent successfully\nRequest body: " + notificationBody;
//
//                HttpServerResponse response = routingContext.response();
//                response.putHeader("Content-Type", "text/plain").end(responseMessage);
//            } else {
//                HttpServerResponse response = routingContext.response();
//                response.setStatusCode(400) // Bad Request
//                        .putHeader("Content-Type", "text/plain")
//                        .end("Invalid request body");
//            }
//        });
//    }
//}