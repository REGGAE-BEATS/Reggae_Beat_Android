package reggae_beats.com.reggaebeats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText LoginEmail, LoginPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getCurrentUser() != null)
        {
            //user already logged in, go to BaseActivity
            startActivity(new Intent(this,  BaseActivity.class));
        }

        progressBar = findViewById(R.id.progresBarId);
        // Initialize and add items to spinner

        Button LoginButton = findViewById(R.id.Login);
        Button RegisterButton = findViewById(R.id.RegisterId);
        LoginEmail = (EditText) findViewById(R.id.editTextAccountLoginId);
        LoginPassword = (EditText) findViewById(R.id.editTextLoginPasswordId);

        //----------LOGIN-----------
        LoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = LoginEmail.getText().toString();
                String pass = LoginPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    startActivity(new Intent(LoginActivity.this,  BaseActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("LOGIN", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this,
                                            "LOGIN FAILED\nCheck internet connection.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //--------REGISTER------------
        RegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,  RegistrationActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }
}
