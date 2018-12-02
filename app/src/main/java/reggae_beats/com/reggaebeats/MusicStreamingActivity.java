package reggae_beats.com.reggaebeats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;


public class MusicStreamingActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    PlayerView playerView;
    private SimpleExoPlayer player1;
    private TextView artist_play, song_play;
    private ImageView musicPlayerImage;
    private MediaCodecAudioRenderer audioRenderer;
    private MediaController.MediaPlayerControl playerControl;
    String url = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_streaming);
        playerView = findViewById(R.id.player_view);
        //get data from intent

        artist_play = (TextView) findViewById(R.id.artist_play);
        song_play = (TextView) findViewById(R.id.song_play);
        musicPlayerImage = (ImageView) findViewById(R.id.music_player_image);

        Intent intent = getIntent();
        String song_url = intent.getStringExtra("SONG");
        String song_name = intent.getStringExtra("SONG_NAME");
        String artist = intent.getStringExtra("ARTIST");
        if ((song_url.substring(song_url.lastIndexOf(".")) == "mp3")) {
            musicPlayerImage.setVisibility(View.VISIBLE);
        }
        if (artist_play != null) {

            artist_play.setText(artist);
        }
        if (song_play != null) {

            song_play.setText(song_name);
        }

        url = "http://www.jpsbillfinder.com/jam_player/song_storage/" + song_url;
        Toast.makeText(getApplicationContext(), "URL" + url, Toast.LENGTH_LONG).show();
        initializePlayer();


    }

    private void initializePlayer() {
        player1 = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this)
                , new DefaultTrackSelector(), new DefaultLoadControl());


        Uri uri = Uri.parse(url);

        MediaSource mediaSource = buildMediaSource(uri);
        playerView.setPlayer(player1);
        player1.prepare(mediaSource, true, false);

        player1.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);

    }
}



