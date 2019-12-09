package com.example.budgetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDB {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_EXPENSE = "expense";
    public static final String KEY_CAT = "category";
    public static final String KEY_DESC = "description";
    public static final String KEY_IS_BUDGET = "is_budget";

    public static final String DATABASE_NAME = "ExpenseDB";
    public static final String DATABASE_TABLE = "AllExpense";
    public static final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private Context ourContext;
    private SQLiteDatabase ourDatabase;

    public ExpenseDB(Context context) {
        ourContext = context;
    }

    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_EXPENSE + " NUMERIC(9,2), " +
                    KEY_CAT + " TEXT, " +
                    KEY_DESC + " TEXT, " +
                    KEY_IS_BUDGET + " BOOLEAN " +
                    ");";

            db.execSQL(query);
            db.execSQL("INSERT INTO " + DATABASE_TABLE + "(expense, category, is_budget)" +
                    "VALUES ('0', 'Food', 'true'); ");
            db.execSQL("INSERT INTO " + DATABASE_TABLE + "(expense, category, is_budget)" +
                    "VALUES ('0', 'Bills', 'true'); ");
            db.execSQL("INSERT INTO " + DATABASE_TABLE + "(expense, category, is_budget)" +
                    "VALUES ('0', 'Clothes', 'true'); ");
            db.execSQL("INSERT INTO " + DATABASE_TABLE + " (expense, category, is_budget)" +
                    "VALUES ('0', 'Others', 'true'); ");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public ExpenseDB open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(double expense, String category, String desc, boolean isBudget) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAT, category);
        values.put(KEY_DESC, desc);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_IS_BUDGET, isBudget);
        return ourDatabase.insert(DATABASE_TABLE, null, values);
    }

    public String getData() {
        String[] columns = new String[]{KEY_ROWID, KEY_EXPENSE, KEY_CAT, KEY_DESC, KEY_IS_BUDGET};


        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        String result = "";

        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iExpense = c.getColumnIndex(KEY_EXPENSE);
        int iCategory = c.getColumnIndex(KEY_CAT);
        int iDescription = c.getColumnIndex(KEY_DESC);
        int iIsBudget = c.getColumnIndex(KEY_IS_BUDGET);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = result + c.getString(iRowID) + ": " + c.getString(iExpense) + ": " + c.getString(iCategory) + ": " + c.getString(iDescription) + ": " + c.getString(iIsBudget) + "\n";
        }
        c.close();
        return result;
    }

    public long deleteEntry(String rowId) {
        return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=?", new String[]{rowId});
    }

    public long updateBudget(String category, double expense) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_EXPENSE,expense);

        return ourDatabase.update(DATABASE_TABLE,cv,KEY_IS_BUDGET + "=? and " + KEY_CAT + "=?", new String[]{"true", category});
    }

    public long updateEntry(String rowId, String expense, String category) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_EXPENSE,expense);
        cv.put(KEY_CAT,category);

        return ourDatabase.update(DATABASE_TABLE,cv,KEY_ROWID+ "=?", new String[]{rowId});
    }
}
