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

public class DetailsActivity extends AppCompatActivity {


    EditText etExpense, etChangeBudget, etCategory;

    Button btnAdd, btnChange, btnShow;

    TextView tvBudget, tvMoneyLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsview);

        etExpense = (EditText) findViewById(R.id.etExpense);
        etChangeBudget = (EditText)findViewById(R.id.etChangeBudget);
        etCategory =(EditText) findViewById(R.id.etCategory);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnChange =  (Button) findViewById(R.id.btnChange);
        btnShow =  (Button) findViewById(R.id.btnShow);

        tvBudget = (TextView)findViewById(R.id.tvBudget);
        tvMoneyLeft = (TextView) findViewById(R.id.tvMoneyLeft);


        tvBudget.setVisibility(View.GONE);
        tvMoneyLeft.setVisibility(View.GONE);

        String savedExtra = getIntent().getStringExtra("value1");
        TextView myText = (TextView) findViewById(R.id.titleID);
        myText.setText(savedExtra);
    }

    public void btnAdd(View v) {
        String expense = etExpense.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        try {
            ExpenseDB db = new ExpenseDB(this);
            db.open();
            db.createEntry(expense, category);
            db.close();
            Toast.makeText(DetailsActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
            etExpense.setText("");
            etCategory.setText("");
        } catch (SQLException e) {
            Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void BtnShow(View v) {
        startActivity(new Intent(this, Data.class));
    }

    /*public void setBtnEdit(View v) {
        try {

            ExpenseDB db = new ExpenseDB(this);
            db.open();
            db.updateEntry()
            db.close();

        } catch (SQLException) {
            Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }


