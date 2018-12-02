package reggae_beats.com.reggaebeats;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    MusicPlayerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        albumAsyncTask albumAsyncTask = new albumAsyncTask();
        albumAsyncTask.execute();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_fragment);
        recyclerView.setHasFixedSize(true);
    }

    public class albumAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String response = null;


            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("pain", "0");
            String query = builder.build().getEncodedQuery();
            String url = "http://jpsbillfinder.com/jam_player/get_album_data.php";
            //urlRequest = "http://www.jpsbillfinder.com/onlinedoc/interface_methods.php";

            try {
                //Check for internet connectivity
                if (new Configuration(PlayListActivity.this).IsNetworkAvailable()) {


                    response = new NetWorkConnection().ServerConnectionResults(query, url);
                }
            } catch (MalformedURLException e) {

                e.printStackTrace();

            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            feedRecycler(s);
            String success;
            String albumImage;
            String song_url;
            String songTitle;
            String size;
            String views;

           /* try {
                JSONObject jsonObject = new JSONObject(s);


                if ((String) jsonObject.get("SUCCESS") == "SUCCESS") {


                    albumImage = (String) jsonObject.get("ALBUM_IMAGE");

                    song_url = (String) jsonObject.get("SONG_LOCATION");
                    songTitle = (String) jsonObject.get("SONG_TITLE");
                    size = (String) jsonObject.get("SIZE");
                    views = (String) jsonObject.get("VIEWS");


                }
            }catch(JSONException e){
                e.printStackTrace();
                Toast.makeText(PlayListActivity.this,"Could not retrieve any album from the server",Toast.LENGTH_LONG).show();
            }

*/

        }
    }

    private void feedRecycler(String response) {


        String success;
        String albumImage;
        String song_url;
        String songTitle;
        String artist;
        String size;
        String views;
        final ArrayList<Albums> listAlbums = new ArrayList<>();
        Albums albums = null;

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i <= jsonArray.length() - 1; i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                albumImage = (String) jsonObject.get("ALBUM_IMAGE");
                artist = (String) jsonObject.get("ARTIST");
                song_url = (String) jsonObject.get("SONG_LOCATION");
                songTitle = (String) jsonObject.get("SONG_TITLE");
                size = (String) jsonObject.get("SIZE");
                views = (String) jsonObject.get("VIEWS");


                albums = new Albums(songTitle, artist, views, size, albumImage, song_url);

                listAlbums.add(albums);


            }

            adapter = new MusicPlayerAdapter(this, listAlbums);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new MusicPlayerAdapter.onItemClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(PlayListActivity.this, MusicStreamingActivity.class);
                    intent.putExtra("SONG", listAlbums.get(position).getSong_url());
                    intent.putExtra("ARTIST", listAlbums.get(position).getArtist());
                    intent.putExtra("SONG_NAME", listAlbums.get(position).getAlbumName());
                    startActivity(intent);


                }
            });


        } catch (JSONException e) {

            e.printStackTrace();
        }


    }


}



















