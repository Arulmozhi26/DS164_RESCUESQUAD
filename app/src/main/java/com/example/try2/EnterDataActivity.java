package com.example.try2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnterDataActivity extends AppCompatActivity {
    private static final String TAG = "EnterDataActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd;
    private ImageButton back;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        back=(ImageButton) findViewById(R.id.imageButton);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if (editText.length() == 10) {
                    AddData(newEntry);
                    editText.setText("");
                    Intent intent = new Intent(EnterDataActivity.this, ListDataActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterDataActivity.this, ListDataActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}