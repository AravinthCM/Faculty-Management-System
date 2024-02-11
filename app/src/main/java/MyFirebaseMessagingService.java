import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Handle the notification as needed
            handleNotification(title, body);
        }
    }

    private void handleNotification(String title, String body) {
        // Implement your logic to handle the notification
        // You can show a notification using NotificationCompat.Builder
    }
}
