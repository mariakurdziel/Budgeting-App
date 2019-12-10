package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.example.budgetapp.ExpenseDB.DATABASE_TABLE;
import static com.example.budgetapp.ExpenseDB.KEY_CAT;
import static com.example.budgetapp.ExpenseDB.KEY_DESC;
import static com.example.budgetapp.ExpenseDB.KEY_EXPENSE;
import static com.example.budgetapp.ExpenseDB.KEY_IS_BUDGET;
import static com.example.budgetapp.ExpenseDB.KEY_ROWID;

public class StatsActivity extends AppCompatActivity {
    private static String TAG = "StatsActivity";
    PieChart pieChart;
    TextView tvSpentMoney, tvSavedMoney, tvSpentCategory, tvSavedCategory;
    private float[] yData;
    private String[] xData = {"Food", "Clothes" , "Bills" , "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        tvSpentMoney = (TextView) findViewById(R.id.spentMoney);
        tvSavedMoney = (TextView) findViewById(R.id.savedMoney);
        tvSpentCategory = (TextView) findViewById(R.id.spentCategory);
        tvSavedCategory = (TextView) findViewById(R.id.savedCategory);
        Log.d(TAG, "onCreate: starting to create chart");
        yData = initializeExpenses();
        initializeStats();
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        Description desc = new Description();
        desc.setText("Sales by employee (In Thousands $) ");
        pieChart.setDescription(desc);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Money spent");
        pieChart.setCenterTextSize(14);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String money = e.toString().substring(pos1 + 7);
                System.out.println("Money: "+money);
                for(int i = 0; i < yData.length; i++){
                    String[]  tmp = money.split(" ");
                    money = tmp[tmp.length-1];
                    if(yData[i] == Float.parseFloat(money)){
                        pos1 = i;
                        break;
                    }
                }
                String category = xData[pos1];
                Toast.makeText(StatsActivity.this, "Category: " + category + "\n" + "Money spent: " + money, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void initializeStats() {
        int maxIndex=0;
        float counter=0;
        float val=0;
        for(int i=0; i<yData.length;i++) {
            counter+=yData[i];
            if(yData[i]>val) {
                val = yData[i];
                maxIndex=i;
            }
        }
        this.tvSpentCategory.setText(xData[maxIndex]);
        this.tvSpentMoney.setText(new Double(counter).toString());

        String[] columns = new String[]{KEY_ROWID, KEY_EXPENSE, KEY_CAT, KEY_DESC, KEY_IS_BUDGET};
        ExpenseDB db = new ExpenseDB(this);
        db.open();
        Cursor c = db.getOurDatabase().query(DATABASE_TABLE, columns, null, null, null, null, null);

        Double overallBudget=0.0;
        Double foodBudget = 0.0;
        Double clothesBudget = 0.0;
        Double  billsBudget = 0.0;
        Double othersBudget = 0.0;

        int iExpense = c.getColumnIndex(KEY_EXPENSE);
        int iCategory = c.getColumnIndex(KEY_CAT);
        int iIsBudget = c.getColumnIndex(KEY_IS_BUDGET);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(c.getString(iCategory).equals("Food") && c.getInt(iIsBudget)==1) {
                overallBudget+=c.getDouble(iExpense);
                foodBudget = c.getDouble(iExpense);
            } else if (c.getString(iCategory).equals("Clothes") && c.getInt(iIsBudget)==1) {
                overallBudget+=c.getDouble(iExpense);
                clothesBudget = c.getDouble(iExpense);
            } else if (c.getString(iCategory).equals("Bills") && c.getInt(iIsBudget)==1) {
                overallBudget+=c.getDouble(iExpense);
                billsBudget = c.getDouble(iExpense);
            } else if (c.getString(iCategory).equals("Others") && c.getInt(iIsBudget)==0) {
                overallBudget+=c.getDouble(iExpense);
                othersBudget+=c.getFloat(iExpense);
            }
        }

        c.close();
        db.close();

        Double[] savedMoney = {foodBudget-yData[0], clothesBudget-yData[1], billsBudget-yData[2], othersBudget-yData[3]};
        Double savedSum = overallBudget-counter;

        this.tvSavedMoney.setText(savedSum.toString());
        Double value=0.0;
        for (int i=0; i<savedMoney.length;i++) {
            if(savedMoney[i]>value) {
                value = savedMoney[i];
                maxIndex=i;
            }
        }
        this.tvSavedCategory.setText(xData[maxIndex]);

    }

    private  float[] initializeExpenses() {

        String[] columns = new String[]{KEY_ROWID, KEY_EXPENSE, KEY_CAT, KEY_DESC, KEY_IS_BUDGET};
        ExpenseDB db = new ExpenseDB(this);
        db.open();
        Cursor c = db.getOurDatabase().query(DATABASE_TABLE, columns, null, null, null, null, null);

        int iExpense = c.getColumnIndex(KEY_EXPENSE);
        int iCategory = c.getColumnIndex(KEY_CAT);
        int iIsBudget = c.getColumnIndex(KEY_IS_BUDGET);

        float moneyFood = 0.0f;
        float moneyClothes = 0.0f;
        float moneyBills = 0.0f;
        float moneyOthers = 0.0f;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
           if(c.getString(iCategory).equals("Food") && c.getInt(iIsBudget)==0) {
               moneyFood+=c.getFloat(iExpense);
           } else if (c.getString(iCategory).equals("Clothes") && c.getInt(iIsBudget)==0) {
               moneyClothes+=c.getFloat(iExpense);
           } else if (c.getString(iCategory).equals("Bills") && c.getInt(iIsBudget)==0) {
               moneyBills+=c.getFloat(iExpense);
           } else if (c.getString(iCategory).equals("Others") && c.getInt(iIsBudget)==0) {
               moneyOthers+=c.getFloat(iExpense);
           }
        }
        c.close();
        db.close();

        float[] expenses = {moneyFood, moneyClothes, moneyBills, moneyOthers};

        return expenses;
    }

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, " Categories of expenses");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.rgb(102, 255, 153));
        colors.add(Color.rgb(255, 128, 128));
        colors.add(Color.rgb(128, 159, 255));

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(16f);
        LegendEntry l1=new LegendEntry("Food",Legend.LegendForm.CIRCLE,10f,2f,null,Color.GRAY);
        LegendEntry l2=new LegendEntry("Clothes", Legend.LegendForm.CIRCLE,10f,2f,null,Color.rgb(102, 255, 153));
        LegendEntry l3=new LegendEntry("Bills", Legend.LegendForm.CIRCLE,10f,2f,null,Color.rgb(255, 128, 128));
        LegendEntry l4=new LegendEntry("Others",Legend.LegendForm.CIRCLE,10f,2f,null,Color.rgb(128, 159, 255));
        legend.setCustom(new LegendEntry[]{l1,l2,l3,l4});

        legend.setEnabled(true);
        legend.setTextSize(14);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}
