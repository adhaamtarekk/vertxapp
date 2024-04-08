package org.example;

import io.temporal.activity.ActivityInterface;
import io.vertx.core.json.JsonObject;
@ActivityInterface
public interface NotificationActivity {

    String displayNotificationMessage(JsonObject notification);
}
