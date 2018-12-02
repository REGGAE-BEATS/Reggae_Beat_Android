package reggae_beats.com.reggaebeats;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by ron on 28/10/2017.
 */

public class MyCommanAsyncTask extends AsyncTask<String, Void, String> {
    private Activity activity;
    private String urlAllItemForGallery = "http://jpsbillfinder.com/jam_player/friends.php";
    FriendAdapter gridImageAdapter;
    ArrayList<FriendsItem> itemArrayList = new ArrayList<>();
    String each_item;
    private GridView gridView;

    public MyCommanAsyncTask(Activity myactivity) {

        this.activity = getActivity();
    }

    public Activity getActivity() {

        return activity;
    }

    public void onAttach(Activity activity) {
        this.activity = activity;

    }

    public ArrayList<FriendsItem> getStoredArrayList() {

        return this.itemArrayList;
    }

    public void setArrayList(ArrayList<FriendsItem> arrayItems) {
        this.itemArrayList = arrayItems;
    }

    public void onDetach() {
        activity = null;

    }


    @Override
    protected String doInBackground(String... params) {
        String response = null;
        try {
            response = new NetWorkConnection().NetworkCallNoQuery(urlAllItemForGallery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return response;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(activity, "respose" + s, Toast.LENGTH_LONG).show();
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(s);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        /*for (int i = 0; i < jArray.length(); i++) {

            JSONObject jsonObject = null;
            try {
                jsonObject = jArray.getJSONObject(i);
                each_item = jsonObject.getString("ITEM");
                String flag = jsonObject.getString("FLAG");
                Items item = new Items(each_item, null, null, null, null, null, flag);
                itemArrayList.add(item);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
        // save itemArraylist in appCache with a key

        ((ItemsActivity)activity).sendArrayListToGridAdapter(itemArrayList);*/
        //gridImageAdapter = new GridImageAdapter(ItemsActivity.this, itemArrayList, R.layout.grid_item);
        // gridView.setAdapter(gridImageAdapter);

        //Build an arraylist of product names to feed the GridImageadapter


    }


}
