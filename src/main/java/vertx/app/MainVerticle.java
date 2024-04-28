package vertx.app;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {
    public static final String WORKFLOW_TASK_QUEUE = "notification-workflow";
    public  static  final String WORKFLOW_ID = "123";
    public static final String NOTIFICATION_CHANNEL = "notification.channel";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
        vertx.deployVerticle(new ServerVerticle());
        vertx.deployVerticle(new WorkFlowVerticle());

    }

    @Override
    public void start() {
        System.out.println("Main Verticle Deployed :)");

        // Create the Temporal service client
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        // Create the workflow client
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create the worker factory
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(WORKFLOW_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(NotificationWorkflowImpl.class);
        worker.registerActivitiesImplementations(new NotificationActivityImpl());
        factory.start();


    }
}

