package com.statyja.emojimusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by olegk on 18.06.2018.
 */

public class ChouseLvlActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    String App_Preference = "App_Preference";
    int lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            lvl = Integer.parseInt(loadText("lvl"));
        } catch (Exception e) {
            lvl = 1;
            saveText("lvl", "" + lvl);
        }


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.chouse_lvl_activity);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        DatabaseHelper s = new DatabaseHelper(this);
        SQLiteDatabase database = s.getReadableDatabase();
        Cursor cursor = database.query("Lvls", null, null, null, null, null, null);
        cursor.moveToLast();
        int idIndex = cursor.getColumnIndex("_id");

        int BOOKSHELF_COLUMNS = 4;

        int x = cursor.getInt(idIndex);
        int BOOKSHELF_ROWS = x;

        cursor.close();
        database.close();
        s.close();

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        // while
        boolean locFlag = false;
        for (int i = 0; i < BOOKSHELF_ROWS; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT));
            //tableRow.setBackgroundResource(R.drawable.shelf);

            for (int j = 0; j < BOOKSHELF_COLUMNS; j++) {


                final Button imageView = new Button(this);
                //imageView.setImageResource(R.drawable.book);
                int local = (i * BOOKSHELF_ROWS + j + 1 - i);

                if (local > lvl) {
                    imageView.setEnabled(false);
                }
                imageView.setText(String.valueOf(local));
                //imageView.setEnabled(false);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChouseLvlActivity.this, GameActivity.class);
                        Button b = (Button) v;
                        Log.d("TAG", "onClick: " + b.getText());
                        intent.putExtra("Lvl", b.getText());

                        startActivity(intent);
                    }
                });
                tableRow.addView(imageView, j);
                if (local == x) {
                    locFlag = true;
                    break;
                }
                //break;
            }

            tableLayout.addView(tableRow, i);
            if (locFlag)
                break;
        }
    }

    public void saveText(String key, String save) {//Сохранение текста
        mSharedPreferences = getSharedPreferences(App_Preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(key, save);
        ed.apply();
    }

    public String loadText(String key) {//Загрузка текста
        mSharedPreferences = getSharedPreferences(App_Preference, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, "");

    }
}