package reggae_beats.com.reggaebeats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DropboxActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    FirebaseAuth mAuth;
    SharedPreferences prefs;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference musicDropbox, viddeoDropbox;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dropbox);

        /////////////////////////////////////////////////////////
        prefs = getSharedPreferences(getResources().getString(R.string.SHARED_PREF_CONST), MODE_PRIVATE);
        if(prefs.getBoolean("isSubscribedToDropbox", false))
        {
            findViewById(R.id.dropbox_overlay).setVisibility(View.GONE);
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //database = FirebaseDatabase.getInstance();
        musicDropbox = storage.getReference().child("MusicDropbox");
        viddeoDropbox = storage.getReference().child("VideoDropbox");

        /////////////////////////////////////
        //populate view with files we publish
        playAudio("gs://reggaebeat-40b1e.appspot.com/MusicDropbox/MadeInChina.mp3");
    }

    public void PopulateLists()
    {

    }

    public void playAudio(String url)
    {
        Toast.makeText(DropboxActivity.this, "LOADING MEDIA...", Toast.LENGTH_SHORT).show();

        StorageReference storageRef = storage.getReferenceFromUrl(url);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    // Download url of file
                    mediaPlayer.setDataSource(uri.toString());
                    // wait for media player to get prepare
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", e.getMessage());
                        Toast.makeText(DropboxActivity.this, "Playing", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        prefs.edit().putInt("MediaPlayerID", mediaPlayer.getAudioSessionId());
        prefs.edit().commit();
        Toast.makeText(DropboxActivity.this, "NOW PLAYING", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(DropboxActivity.this, "ERROR: "+ what, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void Subscribe (View v){
        FirebaseMessaging.getInstance().subscribeToTopic("DROPBOX")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg;
                        if (task.isSuccessful()) {
                            msg = "YOU ARE NOW SUBSCRIBED!";
                            prefs.edit().putBoolean("isSubscribedToDropbox", true);
                            prefs.edit().commit();
                            //restart activity
                            startActivity(new Intent(DropboxActivity.this,  DropboxActivity.class));
                        }
                        else {
                            msg = "FAILED, check network";
                        }
                        Log.d("Dropbox", msg);
                        Toast.makeText(DropboxActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        killMediaPlayer();
        super.onDestroy();
    }
}
