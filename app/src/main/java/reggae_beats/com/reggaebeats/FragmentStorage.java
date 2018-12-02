package reggae_beats.com.reggaebeats;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by ron on 28/10/2017.
 */

public class FragmentStorage extends Fragment {
    private ArrayList<FriendsItem> arrayListItems;
    Context ctx;
    MyCommanAsyncTask myCommanAsyncTask;
    Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        arrayListItems = new ArrayList<>();
    }

    public void setArrayList(ArrayList<FriendsItem> arrayFromActivity) {
        this.arrayListItems = arrayFromActivity;
        // Toast.makeText(ctx,"setArrayList CALLED",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {

            this.activity = (Activity) context;
            if (myCommanAsyncTask != null) {
                myCommanAsyncTask.onAttach(activity);
            }
        }
    }

    public ArrayList<FriendsItem> getArrayList() {


        //Toast.makeText(ctx,"getArrayList CALLED",Toast.LENGTH_LONG).show();
        return arrayListItems;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    public void startAsyncTask() {
        myCommanAsyncTask = new MyCommanAsyncTask(activity);
        myCommanAsyncTask.execute();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (myCommanAsyncTask != null) {
            myCommanAsyncTask.onDetach();
        }

    }
}
