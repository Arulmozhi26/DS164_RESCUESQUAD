package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    TextView name, mail;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);


        logout = findViewById(R.id.logout);
        name = findViewById(R.id.Name);
        mail = findViewById(R.id.Mail);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                Toast.makeText(LogoutActivity.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
            }
        });


    }
}
