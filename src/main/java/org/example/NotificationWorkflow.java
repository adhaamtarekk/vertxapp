package org.example;

import io.temporal.activity.ActivityInterface;
import io.vertx.core.json.JsonObject;

@ActivityInterface
public interface NotificationWorkflow {
    void sendNotification(JsonObject notification);
}
