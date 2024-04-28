package vertx.app;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
@WorkflowInterface
public interface NotificationWorkflow {

    @WorkflowMethod
    void  sendNotification(String notification);

}