package reggae_beats.com.reggaebeats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText NewUserName, NewUserPassword;
    Button RegisterButton;
    private Pattern pattern;
    ProgressBar progressBar;
    Matcher matcher;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        progressBar = (ProgressBar) findViewById(R.id.progresBarId);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Initialise the UI Components
        NewUserName = (EditText) findViewById(R.id.NewUserNameId);
        NewUserPassword = (EditText) findViewById(R.id.NewUserPasswordId);
        RegisterButton = findViewById(R.id.RegisterButtonId);

        RegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = NewUserName.getText().toString();
                String pass = NewUserPassword.getText().toString();

                if (!(email).isEmpty() && !(pass).isEmpty()) {
                    //Ensure that password has numbers ,lower case ,upper case and  atleast one symbol
                    String password_pattern = "(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@_#$%^&+=]).{6,}$)";
                    pattern = Pattern.compile(password_pattern);
                    matcher = pattern.matcher(pass);

                    if (matcher.matches()) {
                        //register new user
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            startActivity(new Intent(RegistrationActivity.this,  BaseActivity.class));
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.d("LOGIN", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegistrationActivity.this,
                                                    "SIGN-UP FAILED\nCheck internet connection.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.PASSWORD_VALIDATION_INSTRUCTION,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.RELEVANT_FIELDS,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}



