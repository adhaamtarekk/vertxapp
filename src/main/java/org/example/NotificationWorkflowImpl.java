package org.example;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.vertx.core.json.JsonObject;

public class NotificationWorkflowImpl implements NotificationWorkflow {

    private final NotificationActivity notificationActivity;

    public NotificationWorkflowImpl() {
        ActivityOptions options = ActivityOptions.newBuilder().build();
        this.notificationActivity = Workflow.newActivityStub(NotificationActivity.class, options);
    }

    @Override
    public void sendNotification(JsonObject notification) {
        String message = notificationActivity.displayNotificationMessage(notification);
        System.out.println(message);
    }
}
