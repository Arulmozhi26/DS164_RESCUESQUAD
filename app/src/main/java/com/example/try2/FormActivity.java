package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {
    Button b;
    Member2 member2;
    RadioButton male,female;
    FirebaseDatabase database;
    DatabaseReference reference;
    int i=0;
    EditText Name,Address,City,State,Pincode,Email,PhoneNumber,Height,Weight,MedicalHistory,DOB,BloodGroup;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        ID=signInAccount.getId();

        b=findViewById(R.id.save);
        member2 =new Member2();
        male=findViewById(R.id.Male);
        female=findViewById(R.id.Female);
        Name=findViewById(R.id.Name);
        Address=findViewById(R.id.Address);
        City=findViewById(R.id.city);
        State=findViewById(R.id.state);
        Pincode=findViewById(R.id.pincode);
        Email=findViewById(R.id.Email);
        PhoneNumber=findViewById(R.id.PhoneNumber);
        Height=findViewById(R.id.Height);
        Weight=findViewById(R.id.Weight);
        MedicalHistory=findViewById(R.id.Medical_history);
        DOB=findViewById(R.id.DOB);
        BloodGroup=findViewById(R.id.BloodGroup);

        reference = database.getInstance().getReference().child("Member2").child(ID);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m1=male.getText().toString();
                String m2=female.getText().toString();

                member2.setName(Name.getText().toString());
                member2.setAddress(Address.getText().toString());
                member2.setCity(City.getText().toString());
                member2.setState(State.getText().toString());
                member2.setPincode(Pincode.getText().toString());
                member2.setEmail(Email.getText().toString());
                member2.setPhoneNumber(PhoneNumber.getText().toString());
                member2.setHeight(Height.getText().toString());
                member2.setWeight(Weight.getText().toString());
                member2.setMedicalHistory(MedicalHistory.getText().toString());
                member2.setDOB(DOB.getText().toString());
                member2.setBloodGroup(BloodGroup.getText().toString());

                if(male.isChecked()){
                    member2.setGender(m1);
                }
                else{
                    member2.setGender(m2);

                }
                reference.setValue(member2);
                startActivity(new Intent(FormActivity.this, MapsActivityMain.class));
            }

        });
    }
}
