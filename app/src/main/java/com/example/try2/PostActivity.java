package com.example.try2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    private ImageButton img1;
    private TextView txt1,txt2,txt3;
    private Button post1;
    private Uri filepath=null;

    private String title,desc,latlng,Url,time_stamp,ID;
    Member member;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        ID=signInAccount.getId();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member2");
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseStorage=firebaseStorage.getInstance();
        img1=(ImageButton) findViewById(R.id.imageButton);
        txt1=(TextView) findViewById(R.id.edittxt1);
        txt2=(TextView) findViewById(R.id.edittxt2);
        txt3=(TextView) findViewById(R.id.txt3);
        post1=(Button) findViewById(R.id.post1);
        member=new Member();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("hh-mm-ss");
        String date=simpleDateFormat1.format(calendar.getTime());
        String time=simpleDateFormat2.format(calendar.getTime());
        time_stamp="DATE:"+date+"   TIME: "+time;



        ActivityCompat.requestPermissions(PostActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }

        });

        post1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        Intent recIntent = getIntent();
        title=recIntent.getStringExtra("title");
        desc=recIntent.getStringExtra("desc");
        latlng=recIntent.getStringExtra("latlng");

        txt1.setText(title);
        txt2.setText(desc);
        txt3.setText(latlng);
    }

    private void chooseImage() {

        Intent intent1=new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent1,"Select Image"),1);

    }

    private void uploadImage() {
        final String title = txt1.getText().toString().trim();
        final String desc = txt2.getText().toString().trim();
        final String latlong = txt3.getText().toString().trim();
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("UPLOADING...");
        pd.show();
        final String randomkey= UUID.randomUUID().toString();
        final StorageReference riversRef = storageReference.child("POSTS").child("images/"+randomkey);

        riversRef.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(PostActivity.this, "data entered successfully", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Url=uri.toString();
                                member.setUrl(Url);
                                member.setTitle(title);
                                member.setDesc(desc);
                                member.setLatlong(latlong);
                                member.setTime_stamp(time_stamp);
                                databaseReference.child(ID).child(time_stamp).setValue(member);
                                Toast.makeText(PostActivity.this,"data entered successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PostActivity.this, ListDataActivity2.class));
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(PostActivity.this,"un",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double pp=(100.00*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        pd.setMessage("percent ..."+(int) pp +"%");
                    }
                });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filepath=data.getData();
            img1.setImageURI(filepath);

        }
    }
}
