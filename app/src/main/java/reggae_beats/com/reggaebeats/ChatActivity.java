package reggae_beats.com.reggaebeats;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements PairingChooser.OnFragmentInteractionListener {
    private RecyclerView recyclerView;
    private EditText etUserChat;
    Button btSubmitChat;
    private String ContactDetailsValue;
    String urlChat = "http://www.jpsbillfinder.com/jam_player/get_messages.php";
    private String userChatValue;
    private Boolean isactive = false;
    private Handler handler;
    private String userName;
    private ArrayList<chatItems> chatItemsArrayList;

    private ArrayList<FriendsItem> friendItemArrayList;
    private ChatAdapter chatAdapter;
    private FriendAdapter friendAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int previousMessageArraySize = 0;
    private int currentMessageArraySize = 0;
    int currentFriendArraySize = 0;
    private int previousFriendArraySize = 0;
    private String type = "";
    private String retrievedFriend = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chat_recycler);
        etUserChat = findViewById(R.id.userChat);
        int currentFriendArraySize = 0;
        btSubmitChat = findViewById(R.id.submitChat);
        handler = new Handler();
        userName = new SharedPreferenceConfig(this).findValue("USERNAME");
        //Toast.makeText(this,"You are logged in as " + userName ,Toast.LENGTH_LONG).show();
        if (userName != null) {
            type = "GET_FRIENDS";

            GetFriendTask friendTask = new GetFriendTask();
            friendTask.execute(userName, null, type);

        }

        /*-----------------------end of view initialization---------------------------*/
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);

        //Get and display all friends


        btSubmitChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                type = "INSERT";
                if (new Configuration(ChatActivity.this).IsNetworkAvailable()) {
                    SendUserChat SUC = new SendUserChat();
                    SUC.execute(urlChat);

                }

                //Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
            }
        });

    }


    private Runnable runnable = new Runnable() {
        @Override

        public void run() {
            new getMessagesAsyncTask().execute(userName, retrievedFriend, "RETRIEVE");
            Toast.makeText(getApplicationContext(), "runnable called", Toast.LENGTH_LONG).show();
            handler.postDelayed(runnable, 10000);

        }
    };

    private void calRetrieveRunnable() {
        runnable.run();
    }

    @Override
    public void onFragmentInteraction(String PairChooserAction) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        isactive = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }


    private class SendUserChat extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            userChatValue = etUserChat.getText().toString();


        }

        @Override
        protected String doInBackground(String... params) {

            //Append values to be sent to server

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("FROM", userName)
                    .appendQueryParameter("TO", retrievedFriend)
                    .appendQueryParameter("CHAT", userChatValue)
                    .appendQueryParameter("TYPE", "INSERT");
            String query;
            query = builder.build().getEncodedQuery();
            String response = null;
            try {

                if (new Configuration(ChatActivity.this).IsNetworkAvailable()) {
                    response = new NetWorkConnection().ServerConnectionResults(query, urlChat);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),"send chat " + result, Toast.LENGTH_LONG).show();
            Log.d("CHAT RESPONSE", result);
            super.onPostExecute(result);

        }

    }

    private class GetFriendTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(ChatActivity.this, "Get friend tSK CALLED", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Uri.Builder builder = new Uri.Builder();
            if (strings[2] == "GET_FRIENDS") {
                builder.appendQueryParameter("FROM", strings[0]).
                        appendQueryParameter("TYPE", strings[2]);
            }
            if (strings[2] == "START_SESSION") {
                builder.appendQueryParameter("FROM", strings[0])
                        .appendQueryParameter("TO", strings[1]).
                        appendQueryParameter("TYPE", strings[2]);

            }
            String query;
            query = builder.build().getEncodedQuery();
            String response = null;
            try {

                if (new Configuration(ChatActivity.this).IsNetworkAvailable()) {
                    response = new NetWorkConnection().ServerConnectionResults(query, urlChat);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "This is your friend list " + s, Toast.LENGTH_LONG).show();

            if (type == "GET_FRIENDS") {

                buildFriendListDisplay(s);
                //Toast.makeText(getApplicationContext(), "buildFriendList" + type, Toast.LENGTH_LONG).show();

            }


            if (type == "START_SESSION") {

                //Toast.makeText(ChatActivity.this, "A session has been started", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void buildFriendListDisplay(String s) {
        friendItemArrayList = new ArrayList<>();
        FriendsItem friendsItem;
        String image;
        String friend;
        try {
            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i <= jsonArray.length() - 1; i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                image = jsonObject.getString("IMAGE");
                friend = (String) jsonObject.getString("FOLLOWER");


                friendsItem = new FriendsItem(image, friend);
                friendItemArrayList.add(friendsItem);
            }
            //determine first creation of chatItemsArrayList
            currentFriendArraySize = friendItemArrayList.size();
            //if(currentFriendArraySize > previousFriendArraySize){
            friendAdapter = new FriendAdapter(ChatActivity.this, friendItemArrayList, R.layout.custom_friend_layout);

            recyclerView.setAdapter(friendAdapter);

            recyclerView.setLayoutManager(layoutManager);
            friendAdapter.notifyDataSetChanged();

            // then set previousMessageArrayListSize to currentMessageArrayListSize
            previousFriendArraySize = currentFriendArraySize;
            //Toast.makeText(getApplicationContext(), "inbuild friend display list " + friendItemArrayList.size(), Toast.LENGTH_LONG).show();
            //get clicks from itemview click

            friendAdapter.setOnItemClickListener(new FriendAdapter.onItemSelectedListener() {
                @Override
                public void onItemSelected(int position) {
                    if (position >= 0) {
                        calRetrieveRunnable();
                        Toast.makeText(ChatActivity.this, "friend name clicked" + friendItemArrayList.get(position).getFriendName(), Toast.LENGTH_LONG).show();
                        if (etUserChat.getVisibility() == View.INVISIBLE) {
                            etUserChat.setVisibility(View.VISIBLE);
                        }
                        if (btSubmitChat.getVisibility() == View.INVISIBLE) {
                            btSubmitChat.setVisibility(View.VISIBLE);
                        }
                        type = "START_SESSION";
                        //Start a session with clicked party
                        retrievedFriend = friendItemArrayList.get(position).getFriendName();
                        GetFriendTask getFriendTask = new GetFriendTask();
                        getFriendTask.execute(userName, friendItemArrayList.get(position).getFriendName(), type);
                    }
                }
            });
            //}


        } catch (JSONException e) {

            e.printStackTrace();
        }


    }


    private class getMessagesAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            //Append values to be sent to server

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("FROM", userName)
                    .appendQueryParameter("TO", retrievedFriend)
                    .appendQueryParameter("TYPE", "RETRIEVE");
            String query;
            query = builder.build().getEncodedQuery();
            String response = null;
            try {

                if (new Configuration(ChatActivity.this).IsNetworkAvailable()) {
                    response = new NetWorkConnection().ServerConnectionResults(query, urlChat);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return response;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(),"GET MESSAGE TASK" +  s, Toast.LENGTH_LONG).show();

            Log.i("CHAT_MESSAGE", s);
            String timeStamp;
            String users;

            String chat;
            chatItemsArrayList = new ArrayList<>();
            chatItems mChatItems;
            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i <= jsonArray.length() - 1; i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    timeStamp = jsonObject.getString("TIME_STAMP");
                    users = (String) jsonObject.getString("USERS");

                    chat = (String) jsonObject.getString("CHAT");
                    //Toast.makeText(getApplicationContext(), "DATE " + date, Toast.LENGTH_LONG).show();

                    mChatItems = new chatItems(chat, users, timeStamp);
                    chatItemsArrayList.add(mChatItems);
                }
                //determine first creation of chatItemsArrayList
                currentMessageArraySize = chatItemsArrayList.size();
                // if (currentMessageArraySize > previousMessageArraySize) {
                chatAdapter = new ChatAdapter(ChatActivity.this, chatItemsArrayList, null, R.layout.chat_custom_layout);

                recyclerView.setAdapter(chatAdapter);


                recyclerView.setLayoutManager(layoutManager);
                chatAdapter.notifyDataSetChanged();
                //then set previousMessageArrayListSize to currentMessageArrayListSize
                previousMessageArraySize = currentMessageArraySize;
                //}
                Toast.makeText(getApplicationContext(), "jarr arraylist " + chatItemsArrayList.size(), Toast.LENGTH_LONG).show();
                //Attach chat to textViews


                //Set adapter ,receyclerview ,layout manager


                // Toast.makeText(getApplicationContext(), "patient que called from on tab selected", Toast.LENGTH_LONG).show();


            } catch (JSONException e) {

                e.printStackTrace();
            }


        }


    }
}







