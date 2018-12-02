package reggae_beats.com.reggaebeats;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements View.OnClickListener, PairingChooser.OnFragmentInteractionListener {
    private TextView playlist, launch_upload, pair;
    private Toolbar toolbar;
    private FragmentStorage fragmentStorage;
    private FragmentManager fragmentManager;
    private FriendAdapter friendAdapter;
    private String urlAllFriends = "http://jpsbillfinder.com/friends.php";
    private RecyclerView gridView;
    private BroadcastReceiver broadcastReceiver;
    // private FirebaseAuth mAuth;
    private static String TAG = "MAIN_ACTIVITY";
    // private DatabaseReference defaultDatabaseRefernce;
    private RecyclerView.LayoutManager GridLayoutManager;
    private ArrayList<FriendsItem> personArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrame, true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        gridView = (RecyclerView) findViewById(R.id.Gridview);
        GridLayoutManager = new GridLayoutManager(this, 4);
        pair = findViewById(R.id.pair);
        setSupportActionBar(toolbar);
        // mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Profile");
        launch_upload = (TextView) findViewById(R.id.launch_upload_activity);
        playlist = (TextView) findViewById(R.id.playlist);
        playlist.setOnClickListener(this);
        ArrayList itemArrayList;
        launch_upload.setOnClickListener(this);
        pair.setOnClickListener(this);

        if (savedInstanceState == null) {

            fragmentStorage = new FragmentStorage();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragmentStorage, "STORAGE_FRAGMENT").commit();

            compileFriendList();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentStorage = (FragmentStorage) getSupportFragmentManager().findFragmentByTag("STORAGE_FRAGMENT");

        if (personArrayList.size() > 0) {
            personArrayList = fragmentStorage.getArrayList();
            friendAdapter = new FriendAdapter(MainActivity.this, personArrayList, R.layout.custom_friend_layout);
            gridView.setLayoutManager(GridLayoutManager);
            gridView.setAdapter(friendAdapter);
        } else {
            compileFriendList();
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.playlist:
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
                startActivity(intent);
                break;

            case R.id.launch_upload_activity:
                Intent intent2 = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent2);
                break;

            case R.id.pair:
                PairingChooser pairingListFragment = new PairingChooser();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container_main, pairingListFragment);
                ft.addToBackStack(null).commit();


                break;
        }

    }

    private void compileFriendList() {


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST
                , urlAllFriends, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseJson(response);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(this).AddToRequestQue(jsonArrayRequest);
    }

    private void parseJson(String response) {

        String befriended = null;
        String friendImage = null;
        String friendName = null;
        FriendsItem friendsItem = null;
        ArrayList<FriendsItem> personArrayList1 = new ArrayList<>();


        try {


            JSONArray jArray = new JSONArray(response);
            //Toast.makeText(getApplicationContext(), "length" + jArray.length(), Toast.LENGTH_LONG).show();
            int i = 0;
            for (i = 0; i <= jArray.length() - 1; i++) {

                JSONObject jsonObject = jArray.getJSONObject(i);
                friendImage = jsonObject.getString("IMAGE");
                friendName = jsonObject.getString("FRIEND");
                befriended = jsonObject.getString("BEFRIENDED");
                friendsItem = new FriendsItem(friendImage, friendName);
                Toast.makeText(getApplicationContext(), "I" + friendName, Toast.LENGTH_LONG).show();

                personArrayList1.add(friendsItem);
            }


            FragmentStorage newFragmentStorage = (FragmentStorage) fragmentManager.findFragmentByTag("STORAGE_FRAGMENT");
            newFragmentStorage.setArrayList(personArrayList);

            friendAdapter = new FriendAdapter(MainActivity.this, personArrayList1, R.layout.custom_friend_layout);
            gridView.setLayoutManager(GridLayoutManager);
            gridView.setAdapter(friendAdapter);


            //Build an arraylist of product names to feed the GridImageadapter
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.chat_menu) {
            //Launch chat login fragment

            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.add(R.id.container_main, new Chat_login_fragment())
                    .addToBackStack(null).commit();


        }
        if (id == R.id.profile) {

            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(String string) {

    }
}





