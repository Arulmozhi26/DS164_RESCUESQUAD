package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormEditActivity extends AppCompatActivity {
    EditText a, b, c, d, e, f, g, h, i, j, k, l, m;
    ImageButton btn;
    Button btn2;
    FirebaseDatabase database;
    Member2 member2;
    DatabaseReference reference,reference2;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_edit);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        ID=signInAccount.getId();

        member2 =new Member2();
        btn2=(Button) findViewById(R.id.save02);
        btn=(ImageButton) findViewById(R.id.img);
        a =findViewById(R.id.name_tv);
        b =findViewById(R.id.Address_tv);
        c =findViewById(R.id.city_tv);
        d =findViewById(R.id.state_tv);
        e =findViewById(R.id.pin_tv);
        f =findViewById(R.id.Email_tv);
        g =findViewById(R.id.Gender_tv);
        h =findViewById(R.id.PhoneNumber_tv);
        i =findViewById(R.id.Height_tv);
        j =findViewById(R.id.Weight_tv);
        k =findViewById(R.id.Medical_history_tv);
        l =findViewById(R.id.DOB_tv);
        m =findViewById(R.id.BloodGroup_tv);

        reference2= database.getInstance().getReference().child("Member2").child(ID);
        reference= FirebaseDatabase.getInstance().getReference().child("Member2").child(ID);
        reference.addValueEventListener(new ValueEventListener() {
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
                Intent intent = new Intent(FormEditActivity.this, MapsActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                member2.setName(a.getText().toString());
                member2.setAddress(b.getText().toString());
                member2.setCity(c.getText().toString());
                member2.setState(d.getText().toString());
                member2.setPincode(e.getText().toString());
                member2.setEmail(f.getText().toString());
                member2.setGender(g.getText().toString());
                member2.setPhoneNumber(h.getText().toString());
                member2.setHeight(i.getText().toString());
                member2.setWeight(j.getText().toString());
                member2.setMedicalHistory(k.getText().toString());
                member2.setDOB(l.getText().toString());
                member2.setBloodGroup(m.getText().toString());
                reference2.setValue(member2);
                Toast.makeText(FormEditActivity.this,"Data updated successfully",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(FormEditActivity.this, MapsActivityMain.class));
            }
        });

    }
}