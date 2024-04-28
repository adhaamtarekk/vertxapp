package vertx.app;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class NotificationWorkflowImpl implements NotificationWorkflow {
    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(60))
            .build();
    NotificationActivity activity = Workflow.newActivityStub(NotificationActivity.class, options);
    @Override
    public void sendNotification(String notification) {

        String message = activity.displayNotificationMessage(notification);
        System.out.println("Notification Sent!  "+ message);

    }
}

