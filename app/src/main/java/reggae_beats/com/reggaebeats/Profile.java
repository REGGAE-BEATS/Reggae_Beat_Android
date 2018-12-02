package reggae_beats.com.reggaebeats;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private CheckBox reggae_check, dancehall_check, ska_check;
    private Button submitProfile;
    private String reggae_clicked, dancehall_clicked, ska_clicked;
    private ArrayList<SpinnerItemClass> spinnerItemClassArrayList;
    private Spinner spinner;
    private String ACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Get artists to populate the spinner
        sourceSpinnerData();
        spinner = findViewById(R.id.spinner);

    }

    private void sourceSpinnerData() {
        ACTION = "FIND_SPINNER_DATA";
        SettingsAsyncTask settingsAsyncTask = new SettingsAsyncTask();
        settingsAsyncTask.execute(ACTION);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_submit:
                ACTION = "SUBMIT";
                // shoot of an asyncTask to send clicked settings to server
                SettingsAsyncTask settingsAsyncTask = new SettingsAsyncTask();
                settingsAsyncTask.execute(ACTION);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.reggae_check:
                //set the swith string in clicked array that corresponds to the first entry
                reggae_clicked = "SET";


                break;

            case R.id.dancehall_check:
                dancehall_clicked = "SET";

                break;


            case R.id.ska_check:
                dancehall_clicked = "SET";

                break;


        }

    }


    private class SettingsAsyncTask extends AsyncTask<String, Void, String> {

        String upload_profile_url = "http://jpsbillfinder.com/onlinedoc/upload_profile.php";

        protected String doInBackground(String... strings) {
            String response = null;
            Uri.Builder builder = new Uri.Builder();
            if (strings[0] == "SUBMIT") {
                builder.appendQueryParameter("REGGAE", reggae_clicked)
                        .appendQueryParameter("DANCEHALL", dancehall_clicked)
                        .appendQueryParameter("SKA", ska_clicked);
            }

            if (strings[0] == "FIND_SPINNER_DATA") {
                builder.appendQueryParameter("ACTION", strings[0]);

            }
            String query = builder.build().getEncodedQuery();
            try

            {
                response = new NetWorkConnection().ServerConnectionResults(query, upload_profile_url);

            } catch (
                    MalformedURLException e)

            {
                e.printStackTrace();
            }


            return response;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Profile.this, s, Toast.LENGTH_SHORT).show();
            try {
                buildSpinner(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildSpinner(String s) throws JSONException {
        ArrayList<String> stringList = new ArrayList<String>();
/*        JSONObject jsonObject = new JSONObject(s);
        for (int i = 0; i < jsonObject.length(); i++) {
            String artist = jsonObject.getString("ARTIST");
            stringList.add(artist);
ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_multichoice,stringList);
        spinner.setAdapter(arrayAdapter);
    }*/
    }
}
