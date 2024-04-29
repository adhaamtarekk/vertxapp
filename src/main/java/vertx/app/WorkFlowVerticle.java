package vertx.app;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class WorkFlowVerticle extends AbstractVerticle {

    public void start() {
        System.out.println("WorkFlow Verticle Deployed :)");
        EventBus eb = vertx.eventBus();
        MessageConsumer<String> consumer = eb.consumer(MainVerticle.NOTIFICATION_CHANNEL);
        consumer.handler(message -> {
            String notification = message.body();
            System.out.println("Starting Workflow");
            WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
            WorkflowClient client = WorkflowClient.newInstance(service);
            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setWorkflowId(MainVerticle.WORKFLOW_ID)
                    .setTaskQueue(MainVerticle.WORKFLOW_TASK_QUEUE)
                    .build();

            Promise<Void> promise = Promise.promise();
            Future<Void> future = promise.future();
            vertx.executeBlocking(blockingCodeFuture -> {
                try {
                    NotificationWorkflow workflow = client.newWorkflowStub(NotificationWorkflow.class, options);
                    workflow.sendNotification(notification);
                    blockingCodeFuture.complete();
                } catch (Exception e) {
                    blockingCodeFuture.fail(e);
                }
            });

            future.onComplete(result -> {
                if (result.succeeded()) {
                    System.out.println("Workflow completed successfully");
                } else {
                    System.out.println("Workflow failed: " + result.cause().getMessage());
                }
            });
        });
    }
}