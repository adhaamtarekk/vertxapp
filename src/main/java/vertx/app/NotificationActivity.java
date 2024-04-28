package vertx.app;

import io.temporal.activity.ActivityInterface;
@ActivityInterface
public interface NotificationActivity {

    String displayNotificationMessage(String notification);

}

