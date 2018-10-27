package com.demo.mdb.spring2017finalassessment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final Button login = (Button) findViewById(R.id.login);
        final Button register = (Button) findViewById(R.id.register);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAccount(email.getText().toString(), password.getText().toString(), mAuth);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        /* TODO Part 1
         * Implement login. When the login button is pressed, attempt to login the user, and if
         * that is successful, go to the TabbedActivity. If the register button is pressed, go to
         * the RegisterActivity. Check the layout files for the IDs of the views used in this part
         */

    }

    private void loginAccount(String email, String password, final FirebaseAuth auth) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("BOB", "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, TabbedActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("BOB", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
