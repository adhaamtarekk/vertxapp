package vertx.app;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.vertx.core.AbstractVerticle;
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
            // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
            WorkflowClient client = WorkflowClient.newInstance(service);
            /*
             * Set Workflow options such as WorkflowId and Task Queue so the worker knows where to list and which workflows to execute.
             */
            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setWorkflowId(MainVerticle.WORKFLOW_ID)
                    .setTaskQueue(MainVerticle.WORKFLOW_TASK_QUEUE)
                    .build();

            // Create the workflow client stub. It is used to start our workflow execution.
            NotificationWorkflow workflow = client.newWorkflowStub(NotificationWorkflow.class, options);
            workflow.sendNotification(notification);
        });
    }
}
