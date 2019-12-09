package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatsActivity extends AppCompatActivity {


    TextView tvBudget, tvMoneyLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statsview);

        tvBudget = (TextView) findViewById(R.id.tvBudget);
        tvMoneyLeft = (TextView) findViewById(R.id.tvMoneyLeft);


        tvBudget.setVisibility(View.GONE);
        tvMoneyLeft.setVisibility(View.GONE);

        String savedExtra = getIntent().getStringExtra("value1");
        TextView myText = (TextView) findViewById(R.id.titleID);
        myText.setText(savedExtra);

    }


}


