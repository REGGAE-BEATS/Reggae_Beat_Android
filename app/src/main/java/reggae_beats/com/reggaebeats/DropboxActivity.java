package reggae_beats.com.reggaebeats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class DropboxActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dropbox);
        mAuth = FirebaseAuth.getInstance();
        /////////////////////////////////////
        prefs = new SharedPreferenceConfig(this).getSharedpreferences();

        if(prefs.getBoolean("isSubscribedToDropbox", false) )
        {
            findViewById(R.id.dropbox_overlay).setVisibility(View.GONE);
        }

    }

    public void Subscribe (View v){
        FirebaseMessaging.getInstance().subscribeToTopic("DROPBOX")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg;
                        if (task.isSuccessful()) {
                            msg = "YOU ARE NOW SUBSCRIBED!";
                            prefs.edit().putBoolean("isSubscribedToDropbox", true);;
                            prefs.edit().apply();
                            //restart activity
                            startActivity(new Intent(DropboxActivity.this,  DropboxActivity.class));
                        }
                        else {
                            msg = "FAILED, check network";
                        }
                        Log.d("Dropbox", msg);Toast.makeText(DropboxActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
