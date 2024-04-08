package org.example;
import io.vertx.core.json.JsonObject;

public class NotificationActivityImpl implements NotificationActivity {

    @Override
    public String displayNotificationMessage(JsonObject notification) {
        String type = notification.getString("type");
        String recipient = notification.getString("recipient");
        return "The " + type + " has been sent to " + recipient;
    }
}
