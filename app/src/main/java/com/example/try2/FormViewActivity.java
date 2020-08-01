package com.example.try2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormViewActivity extends AppCompatActivity {
    TextView a, b, c, d, e, f, g, h, i, j, k, l, m;
    ImageButton btn;
    DatabaseReference ref;
    String ID;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formview);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        ID=signInAccount.getId();

        btn=(ImageButton) findViewById(R.id.img);
        a = (TextView) findViewById(R.id.name_tv);
        b = (TextView) findViewById(R.id.Address_tv);
        c = (TextView) findViewById(R.id.city_tv);
        d = (TextView) findViewById(R.id.state_tv);
        e = (TextView) findViewById(R.id.pin_tv);
        f = (TextView) findViewById(R.id.Email_tv);
        g = (TextView) findViewById(R.id.Gender_tv);
        h = (TextView) findViewById(R.id.PhoneNumber_tv);
        i = (TextView) findViewById(R.id.Height_tv);
        j = (TextView) findViewById(R.id.Weight_tv);
        k = (TextView) findViewById(R.id.Medical_history_tv);
        l = (TextView) findViewById(R.id.DOB_tv);
        m = (TextView) findViewById(R.id.BloodGroup_tv);


        ref= FirebaseDatabase.getInstance().getReference().child("Member2").child(ID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                String Address=snapshot.child("address").getValue().toString();
                String City=snapshot.child("city").getValue().toString();
                String State=snapshot.child("state").getValue().toString();
                String Pincode=snapshot.child("pincode").getValue().toString();
                String Email=snapshot.child("email").getValue().toString();
                String Gender=snapshot.child("gender").getValue().toString();
                String PhoneNumber=snapshot.child("phoneNumber").getValue().toString();
                String Height=snapshot.child("height").getValue().toString();
                String Weight=snapshot.child("weight").getValue().toString();
                String MedicalHistory=snapshot.child("medicalHistory").getValue().toString();
                String DOB=snapshot.child("dob").getValue().toString();
                String BloodGroup=snapshot.child("bloodGroup").getValue().toString();
                a.setText(name);
                b.setText(Address);
                c.setText(City);
                d.setText(State);
                e.setText(Pincode);
                f.setText(Email);
                g.setText(Gender);
                h.setText(PhoneNumber);
                i.setText(Height);
                j.setText(Weight);
                k.setText(MedicalHistory);
                l.setText(DOB);
                m.setText(BloodGroup);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormViewActivity.this, MapsActivityMain.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

