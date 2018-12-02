package reggae_beats.com.reggaebeats;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.net.MalformedURLException;

public class PairRequestActivity extends AppCompatActivity implements View.OnClickListener, PairingChooser.OnFragmentInteractionListener {
    EditText et_pairRegistrationCode, et_yourRegistrationCode, customMessage;


    private Button sendPairRequest;
    private ImageView quick_attach;
    String retrievedRegistrationCode;
    String pairRegistrationCode;
    String yourRegistrationCode;
    RelativeLayout quick_attach_container;
    String accompanyingMessage;
    private static String type;

    private String retrievedTokenSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_request);
        et_pairRegistrationCode = findViewById(R.id.pair_registration_code);
        et_yourRegistrationCode = findViewById(R.id.your_registration_code);
        customMessage = findViewById(R.id.customMessage);
        sendPairRequest = findViewById(R.id.sendPairRequest);
        quick_attach = findViewById(R.id.quick_attach);
        quick_attach_container = findViewById(R.id.attach_container);
        quick_attach.setOnClickListener(this);
        sendPairRequest.setOnClickListener(this);

        quick_attach_container.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //Get registration code from shared preferences or make a network call to server
                Toast.makeText(getApplicationContext(), "QUICK ATTACK", Toast.LENGTH_LONG).show();
                retrievedRegistrationCode = new SharedPreferenceConfig(PairRequestActivity.this).findValue("REGISTATION_CODE");
                Toast.makeText(getApplicationContext(), "REG_CODE" + retrievedRegistrationCode, Toast.LENGTH_LONG).show();

                if (retrievedRegistrationCode.length() <= 0) {
                    type = "GET_REG_CODE";
                    //send a request to server
                    RegistrationCodeAsyncTask registrationCodeSender = new RegistrationCodeAsyncTask();
                    registrationCodeSender.execute(type);

                } else {
                    et_pairRegistrationCode.setText(retrievedRegistrationCode);
                }


            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendPairRequest:
                type = "SEND_PAIR_REQUEST";
                pairRegistrationCode = et_pairRegistrationCode.getText().toString();
                yourRegistrationCode = et_yourRegistrationCode.getText().toString();
                accompanyingMessage = customMessage.getText().toString();
                RegistrationCodeAsyncTask pairRequestSender = new RegistrationCodeAsyncTask();
                pairRequestSender.execute(type);
                break;

            case R.id.quick_attach:
                //Get registration code from shared preferences or make a network call to server
                // Toast.makeText(getApplicationContext(), "QUICK ATTACK", Toast.LENGTH_LONG).show();
                retrievedRegistrationCode = new SharedPreferenceConfig(PairRequestActivity.this).findValue("REGISTATION_CODE");


                if (retrievedRegistrationCode.length() <= 0) {
                    //send a request to server
                    Toast.makeText(getApplicationContext(), "async shoot off", Toast.LENGTH_LONG).show();
                    RegistrationCodeAsyncTask registrationCodeSender = new RegistrationCodeAsyncTask();
                    registrationCodeSender.execute("GET_REG_CODE");

                } else {
                    et_pairRegistrationCode.setText(retrievedRegistrationCode);
                }
                break;
        }


    }

    @Override
    public void onFragmentInteraction(String PairChooserAction) {
        if (PairChooserAction == "PAIR_WITH_FRIENDS") {

        }
    }

    private class RegistrationCodeAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String urlRequest = "http://jpsbillfinder.com/jam_player/get_registration_code.php";
            String query;
            String response = null;
            Uri.Builder builder = new Uri.Builder();
            String currentUser = new SharedPreferenceConfig(PairRequestActivity.this).findValue("USERNAME");

            String retrievedTokenSharedPrefs = new SharedPreferenceConfig(getApplicationContext()).findValue("REFRESHED_TOKEN");

            if (retrievedTokenSharedPrefs.isEmpty()) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Token request failed", Toast.LENGTH_LONG).show();

                                } else {
                                    String newToken = task.getResult().getToken();
                                    new SharedPreferenceConfig(getApplicationContext()).addToSharedPreferences("REFRESHED_TOKEN", newToken);
                                }
                                //.makeText(getApplicationContext(), "Token succesfully retrieved" + newToken, Toast.LENGTH_LONG).show();


                            }
                        });


            }
            if (strings[0] == "GET_REG_CODE") {
                builder.appendQueryParameter("USER", currentUser)
                        .appendQueryParameter("TYPE", "GET_REG_CODE");
            }
            if (strings[0] == "SEND_PAIR_REQUEST") {
                retrievedTokenSharedPrefs = new SharedPreferenceConfig(getApplicationContext()).findValue("REFRESHED_TOKEN");
                builder.appendQueryParameter("USER", currentUser)
                        .appendQueryParameter("TYPE", "SEND_PAIR_REQUEST")
                        .appendQueryParameter("TO", pairRegistrationCode)
                        .appendQueryParameter("FROM", yourRegistrationCode)
                        .appendQueryParameter("MESSAGE", accompanyingMessage)
                        .appendQueryParameter("REGISTRATION_TOKEN", retrievedTokenSharedPrefs)
                ;

            }


            query = builder.build().getEncodedQuery();


            try {
                //Check for internet connectivity
                if (new Configuration(PairRequestActivity.this).IsNetworkAvailable()) {

                    response = new NetWorkConnection().ServerConnectionResults(query, urlRequest);
                }
            } catch (MalformedURLException e) {

                e.printStackTrace();

            }


            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "response" + s, Toast.LENGTH_LONG).show();
            if (type == "GET_REG_CODE") {
                if (s != null) {
                    et_yourRegistrationCode.setText(s);

                }

            }
            if (type == "SEND_PAIR_REQUEST") {


            }


        }
    }
}
