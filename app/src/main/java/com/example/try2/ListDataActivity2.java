package com.example.try2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListDataActivity2 extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper02 mDatabaseHelper02;

    private ListView mListView;
    Button add;
    FloatingActionButton back2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout2);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper02 = new DatabaseHelper02(this);

        add = (Button) findViewById(R.id.add);
        back2=(FloatingActionButton) findViewById(R.id.floatingActionButton3);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActivity2.this, EnterDataActivity2.class);
                startActivity(intent);
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActivity2.this, MapsActivityMain.class);
                startActivity(intent);
            }
        });

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper02.getData();
        ArrayList<String> listData = new ArrayList<>();
        final ArrayList<String> listData2 = new ArrayList<>();
        final ArrayList<String> listData3 = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
            listData2.add(data.getString(2));
            listData3.add(data.getString(3));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                String data2=listData2.get(i);
                String data3=listData3.get(i);
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data1 = mDatabaseHelper02.getItemID(name);//get the id associated with that name
                int itemID = -1;
                while(data1.moveToNext()){
                    itemID = data1.getInt(0);

                }

                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ListDataActivity2.this,EditDataActivity2.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("title",name);

                    editScreenIntent.putExtra("desc", data2);
                    editScreenIntent.putExtra("latlng",data3);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });

    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

