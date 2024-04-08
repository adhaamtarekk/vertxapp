package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }


    @Override
    public void start() {
        // Create the Temporal service client
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

        // Create the workflow client
        WorkflowClientOptions clientOptions = WorkflowClientOptions.newBuilder().build();
        WorkflowClient client = WorkflowClient.newInstance(service, clientOptions);

        // Create the worker factory
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Create and register the workflow worker
        WorkerOptions workflowWorkerOptions = WorkerOptions.newBuilder().build();
        Worker workflowWorker = factory.newWorker("notification-workflow", workflowWorkerOptions);
        workflowWorker.registerWorkflowImplementationTypes(NotificationWorkflowImpl.class);
        factory.start();

        // Create and register the activity worker
        WorkerOptions activityWorkerOptions = WorkerOptions.newBuilder().build();
        Worker activityWorker = factory.newWorker("notification-activity", activityWorkerOptions);
        activityWorker.registerActivitiesImplementations(new NotificationActivityImpl());
        factory.start();

        // Deploy your verticle
        vertx.deployVerticle(new NotificationVerticle());
    }
}