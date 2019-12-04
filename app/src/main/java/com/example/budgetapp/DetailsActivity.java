package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        String savedExtra = getIntent().getStringExtra("value1");
        TextView myText = (TextView) findViewById(R.id.titleID);
        myText.setText(savedExtra);
    }
}
