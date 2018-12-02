package reggae_beats.com.reggaebeats;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.MalformedURLException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MY_FIREBASE_MSG_SERVICE";
    public static final String CHANNEL_ID = "CHANNEL_1";
    public static String uploadTokenUrl = "http://jpsbillfinder.com/onlinedoc/upload_onlinedoc_token.php";
    private static Context context;
    private String currentToken = null;
    String retrievedTokenSharedPrefs;
    String lastToken = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData().size() > 0) {
            String Title = remoteMessage.getData().get("title");
            String Message = remoteMessage.getData().get("body");
            String RegistrationId = remoteMessage.getData().get("RegistrationId");
            // McreateNotification(String Message);

            //prepare an intent and get a localbroadcastmanager to broadcast data with a specific action string  --foreground
            Intent intent = new Intent("reggae_beats.com.reggaebeats");
            intent.putExtra("TITLE", Title);
            intent.putExtra("BODY", Message);
            intent.putExtra("REGISTRATION_ID", RegistrationId);
            createNotification(Title, Message, RegistrationId);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }


//Check if message contains a notification payload
     /*       if (remoteMessage.getNotification() != null) {
        String Title = remoteMessage.getNotification().getTitle();

        String Message = remoteMessage.getNotification().getBody();

        // McreateNotification(String Message);

        //prepare an intent and get a localbroadcastmanager to broadcast data with a specific action string  --foreground
        Intent intent = new Intent("reggae_beats.com.reggaebeats");
        intent.putExtra("TITLE", Title);
        intent.putExtra("BODY",Message);

        createNotification(Title,Message,null );
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }*/

    }

    @Override
    public void onNewToken(final String token) {
        super.onNewToken(token);

        currentToken = token;
//Toast.makeText(context,"New token called",Toast.LENGTH_SHORT).show();


        Log.d(TAG, "Refreshed token " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(final String token) {
        /*create network thread to send token to server and update necessary shared preferences*/

        final String activeUser = new SharedPreferenceConfig(this).findValue("USERNAME");


        Thread thread = new Thread() {
            String response = null;
            Handler handler = new Handler(Looper.getMainLooper());
            String query;

            @Override
            public void run() {
                super.run();
                //if(!new SharedPreferenceConfig(context).findValue("REFRESHED_TOKEN").isEmpty())

                try {
                    String lastToken = new SharedPreferenceConfig(context).findValue("REFRESHED_TOKEN");
                    Log.d("TAG:LST_TOKEN", lastToken);
                    Log.d("TAG:ACTIVE_USER", activeUser);
                } catch (NullPointerException e) {
                    Log.d(TAG, "NULL TOKEN");
                }

                if (lastToken != currentToken) {
                    Uri builder = new Uri.Builder()
                            .appendQueryParameter("TOKEN", token)
                            .appendQueryParameter("ACTIVE_USER", activeUser).build();
                    query = builder.getEncodedQuery();
                }
                try {
                    response = new NetWorkConnection().ServerConnectionResults(query, uploadTokenUrl);
                    if ((response).equals("SUCCESS")) {
                        new SharedPreferenceConfig(context).addToSharedPreferences("REFRESHED_TOKEN", token);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "New token sent to server in  new token call", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
    }


    private void createNotification(String Title, String Message, String RegistrationId) {
        // Intent intent = new Intent(MyFirebaseMessagingService.this,FriendRequestActivity.class);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 99, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.jam_player)
                        .setContentTitle("Pair Request")
                        .setContentText(Message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel used by onlineDoc to send messages to patients",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}







