package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.example.budgetapp.ExpenseDB.DATABASE_TABLE;
import static com.example.budgetapp.ExpenseDB.KEY_CAT;
import static com.example.budgetapp.ExpenseDB.KEY_DESC;
import static com.example.budgetapp.ExpenseDB.KEY_EXPENSE;
import static com.example.budgetapp.ExpenseDB.KEY_IS_BUDGET;
import static com.example.budgetapp.ExpenseDB.KEY_ROWID;

public class DetailsActivity extends AppCompatActivity {

    private Double budget, moneyLeft;
    EditText etExpense, etChangeBudget, etCategory, etDescription;

    Button btnAdd, btnChange, btnShow;

    TextView tvBudget, tvMoneyLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsview);

        etExpense = (EditText) findViewById(R.id.etExpense);
        etChangeBudget = (EditText) findViewById(R.id.etChangeBudget);
        etDescription = (EditText) findViewById(R.id.etDescription);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnShow = (Button) findViewById(R.id.btnShow);

        tvBudget = (TextView) findViewById(R.id.tvBudget);
        tvMoneyLeft = (TextView) findViewById(R.id.tvMoneyLeft);

        String savedExtra = getIntent().getStringExtra("value1");
        initializeData(savedExtra);
        TextView myText = (TextView) findViewById(R.id.titleID);
        myText.setText(savedExtra);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    btnAdd(v);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShow(v);
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChange(v);
            }
        });



    }

    public void initializeData(String category) {
        String[] columns = new String[]{KEY_ROWID, KEY_EXPENSE, KEY_CAT, KEY_DESC, KEY_IS_BUDGET};

        String categoryName = category;
        ExpenseDB db = new ExpenseDB(this);
        db.open();
        Cursor c = db.getOurDatabase().query(DATABASE_TABLE, columns, null, null, null, null, null);

        int iExpense = c.getColumnIndex(KEY_EXPENSE);
        int iCategory = c.getColumnIndex(KEY_CAT);
        int iIsBudget = c.getColumnIndex(KEY_IS_BUDGET);
        Double moneySpent = 0.0;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if((c.getString(iCategory)).equals(categoryName)) {
                if(c.getInt(iIsBudget)==0) {
                    moneySpent += c.getDouble(iExpense);
                } else {
                    budget=c.getDouble(iExpense);
                }
            }
        }
        c.close();
        db.close();
        tvBudget.setText(budget.toString());
        moneyLeft = budget - moneySpent;
        tvMoneyLeft.setText(moneyLeft.toString());
    }

    public void btnAdd(View v) {
        double expense = Double.parseDouble(etExpense.getText().toString().trim());
        String savedExtra = getIntent().getStringExtra("value1");
        String category = savedExtra.trim();
        String desc = etDescription.getText().toString().trim();

        try {
            ExpenseDB db = new ExpenseDB(this);
            db.open();
            db.createEntry(expense, category, desc, 0);
            db.close();
            Toast.makeText(DetailsActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
            etExpense.setText("");
            etDescription.setText("");
            finish();
            startActivity(getIntent());
        } catch (SQLException e) {
            Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void btnShow(View v) {
        Intent myIntent;
        myIntent=(new Intent(v.getContext(), Data.class));
        String savedExtra = getIntent().getStringExtra("value1");
        myIntent.putExtra("value1",savedExtra);
        startActivity(myIntent);

    }

    public void btnChange(View v) {
        double expense = Double.parseDouble(etChangeBudget.getText().toString().trim());
        String savedExtra = getIntent().getStringExtra("value1");
        String category = savedExtra.trim();

        try {
            ExpenseDB db = new ExpenseDB(this);
            db.open();
            db.updateBudget(category, expense);
            db.close();
            Toast.makeText(DetailsActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
            etChangeBudget.setText("");
            finish();
            startActivity(getIntent());

        } catch (SQLException e) {
            Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}


