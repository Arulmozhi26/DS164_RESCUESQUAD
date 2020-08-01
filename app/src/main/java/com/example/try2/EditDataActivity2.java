package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditDataActivity2 extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave,btnDelete,sync;
    private EditText editable_item2,editable_item1;
    ImageButton imageButton;
    TextView textView;
    DatabaseHelper02 mDatabaseHelper02;


    private int selectedID;
    private String title,desc,latlng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout2);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        sync = (Button) findViewById(R.id.sync);
        editable_item1 = (EditText) findViewById(R.id.editable_item1);
        editable_item2 = (EditText) findViewById(R.id.editable_item2);
        imageButton=(ImageButton) findViewById(R.id.imageButton1);
        textView=(TextView) findViewById(R.id.txtv) ;
        mDatabaseHelper02 = new DatabaseHelper02(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value
        title=receivedIntent.getStringExtra("title");
        desc=receivedIntent.getStringExtra("desc");
        latlng=receivedIntent.getStringExtra("latlng");

        editable_item1.setText(title);
        editable_item2.setText(desc);
        textView.setText(latlng);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item1 = editable_item1.getText().toString();
                String item2 = editable_item2.getText().toString();
                if(!item1.equals("") && !item2.equals((""))){
                    boolean isUpdate= mDatabaseHelper02.updateName(selectedID,item1,item2);
                    if(isUpdate==true){
                        toastMessage("updated");
                    }
                    else {
                        toastMessage("not updated");
                    }

                    Intent intent = new Intent(EditDataActivity2.this, ListDataActivity2.class);
                    startActivity(intent);
                }else{
                    toastMessage("You must enter a Title and Description");

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deleterows = mDatabaseHelper02.deleteName(selectedID);
                if(deleterows>0){
                    toastMessage("removed from database");
                }else {
                    toastMessage("not removed from database");
                }

                Intent intent = new Intent(EditDataActivity2.this, ListDataActivity2.class);
                startActivity(intent);
                finish();
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent =new Intent(EditDataActivity2.this,PostActivity.class);
                newintent.putExtra("title",title);
                newintent.putExtra("desc",desc);
                newintent.putExtra("latlng",latlng);
                startActivity(newintent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDataActivity2.this, ListDataActivity2.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}