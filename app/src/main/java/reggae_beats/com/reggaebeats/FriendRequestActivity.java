package reggae_beats.com.reggaebeats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendRequestActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView RegistrationId, customMessage;


    private Button denyPairRequest, acceptPairRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        RegistrationId = findViewById(R.id.registrationId);
        customMessage = findViewById(R.id.message);
        denyPairRequest = findViewById(R.id.denyPairRequest);
        acceptPairRequest = findViewById(R.id.acceptPairRequest);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {

                if ((key).equals("TITLE")) {

                    String Title = getIntent().getExtras().getString(key);
                    RegistrationId.setText(Title);

                }

                if ((key).equals("BODY")) {

                    String Body = getIntent().getExtras().getString("BODY");
                    Toast.makeText(getApplicationContext(), "bdy" + Body, Toast.LENGTH_SHORT).show();
                    customMessage.setText(Body);
                }

            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.acceptPairRequest:
                Toast.makeText(getApplicationContext(), "Pair request accepted", Toast.LENGTH_SHORT).show();
                break;

            case R.id.denyPairRequest:

                Toast.makeText(getApplicationContext(), "Pair request declined", Toast.LENGTH_SHORT).show();
                break;
        }

    }


}
