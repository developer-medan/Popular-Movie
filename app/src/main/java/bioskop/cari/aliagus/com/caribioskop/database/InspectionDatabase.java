package bioskop.cari.aliagus.com.caribioskop.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import bioskop.cari.aliagus.com.caribioskop.R;

/**
 * Created by ali on 26/01/18.
 */

public class InspectionDatabase extends AppCompatActivity {

    private static final String TAG = InspectionDatabase.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String mMainTitle = "Db Inspection";
    private boolean isMainView = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspection_layout);
        setTitle(mMainTitle);
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        getTableName();
    }

    private void getTableName() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",
                null
        );
        int i = 0;
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                ArrayList<String> rowList = new ArrayList<>();
                i++;
                rowList.add(cursor.getString(0));
                rows.add(rowList);
                cursor.moveToNext();
            }
        }
        ArrayList<String> column = new ArrayList<>();
        column.add("Table Name");
        showTable(column, rows, true);
    }

    private void showTable(ArrayList<String> column, ArrayList<ArrayList<String>> rows, boolean clicked) {
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(2, 2, 2, 2);
        tableRowParams.weight = 1;

        //textview layout param
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        params.setMargins(5, 2, 15, 2);

        //create header field
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.GRAY);
        for (int i = 0; i < column.size(); i++) {
            TextView columnField = new TextView(getApplicationContext());

            columnField.setGravity(Gravity.CENTER);
            columnField.setTextColor(Color.WHITE);
            columnField.setText(column.get(i));
            columnField.setLayoutParams(params);
            tableRow.addView(columnField, tableRowParams);

        }
        tableLayout.addView(tableRow, tableLayoutParams);

        for (int i = 0; i < rows.size(); i++) {
            TableRow tableRowValue = new TableRow(this);
            tableRowValue.setBackgroundColor(Color.GRAY);
            ArrayList<String> row = rows.get(i);

            for (int j = 0; j < row.size(); j++) {
                final String value = row.get(j) != null ? row.get(j) : "null";
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.BLACK);
                textView.setText(value);
                textView.setLayoutParams(params);
                textView.setMaxWidth(400);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                if (clicked) {
                    textView.setTextColor(Color.BLUE);
                    textView.setTag(value);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setTitle(mMainTitle + " : " + value);
                            loadTable(value);
                            isMainView = false;
                        }
                    });
                }
                tableRowValue.addView(textView, tableRowParams);
            }
            tableLayout.addView(tableRowValue, tableLayoutParams);
        }

        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView sv = new ScrollView(this);
        TextView tvRowCount = new TextView(getApplicationContext());
        tvRowCount.setText("Row :" + String.valueOf(rows.size()));
        linearLayout.addView(tvRowCount);

        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.addView(tableLayout);
        sv.addView(hsv);
        linearLayout.addView(sv);
        setContentView(linearLayout);
    }

    private void loadTable(String tableName) {
        ArrayList<String> column = getColumnFromTable(tableName);
        ArrayList<ArrayList<String>> row = getTableValue(tableName);
        showTable(column, row, false);
    }

    private ArrayList<ArrayList<String>> getTableValue(String tableName) {
        String query = "SELECT * FROM " + tableName;
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> columns = getColumnFromTable(tableName);
        ArrayList<ArrayList<String>> arrayRow = new ArrayList<>();
        if (c.moveToFirst()) {
            while ((!c.isAfterLast())) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 0; i < columns.size(); i++) {
                    if (tableName.equals("kv")) {
                        String value = c.getString(c.getColumnIndex(columns.get(i)));
                        row.add(value);
                    } else {
                        row.add(c.getString(c.getColumnIndex(columns.get(i))));
                    }
                }
                arrayRow.add(row);
                c.moveToNext();
            }
        }
        return arrayRow;
    }

    private ArrayList<String> getColumnFromTable(String tableName) {
        Cursor ti = sqLiteDatabase.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        ArrayList<String> column = new ArrayList<>();
        if (ti.moveToFirst()) {
            do {
                System.out.println("col: " + ti.getString(1));
                column.add(ti.getString(1));
            } while (ti.moveToNext());
        }
        ti.close();
        return column;
    }

    @Override
    public void onBackPressed() {
        if (isMainView) {
            super.onBackPressed();
        } else {
            getTableName();
            isMainView = true;
        }
    }
}
