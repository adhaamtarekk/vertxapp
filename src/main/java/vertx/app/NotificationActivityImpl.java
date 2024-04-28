package vertx.app;


public class NotificationActivityImpl implements NotificationActivity {

    @Override
    public String displayNotificationMessage(String notification) {
        System.out.println("Activity executed from Workflow: ");
        return notification;
    }
}

