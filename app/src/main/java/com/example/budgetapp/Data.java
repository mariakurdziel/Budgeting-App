package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Data extends AppCompatActivity {
    //TextView tvData;
    ListView lvData;
    List<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        listItem = new ArrayList<>();
        String savedExtra = getIntent().getStringExtra("value1");
      /*  tvData= (TextView) findViewById(R.id.tvData);
        String savedExtra = getIntent().getStringExtra("value1");
        try{

            ExpenseDB db=new ExpenseDB(this);
            db.open();
            tvData.setText(db.getData(savedExtra));
            db.close();

        }catch(SQLException e){
            Toast.makeText(Data.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

       */

        lvData = (ListView) findViewById(R.id.lvData);


        try {
            ExpenseDB db = new ExpenseDB(this);
            db.open();

            listItem = db.getData(savedExtra);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            lvData.setAdapter(adapter);
            db.close();
        } catch (SQLException e) {
            Toast.makeText(Data.this, e.getMessage(), Toast.LENGTH_SHORT).show();


        }


    }
}
