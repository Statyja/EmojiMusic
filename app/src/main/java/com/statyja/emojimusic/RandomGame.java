package com.statyja.emojimusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class RandomGame extends AppCompatActivity {
    //int lvl;
    TextView withLvl;//textView с уровнем
    TextView withAutor;
    TextView withEmoji;//textView с заданием
    Button btnOk;//кнопка OK
    EditText editText;//editText для ответа
    Button whoAutor;
    Button findAnswer;

    TextView wrong;
    TextView coin;

    private int random;

    String answer;//строка с ответом
    String autor;
    //Random random;
    JSONArray subAnswer;//строка с альтернативными ответами
    //String coin;
    int counter;//счётчик неправильных ответов для показа рекламы

    InterstitialAd mInterstitialAd;//какая-то штука для рекламы

    String lvl;//строка с уровнем

    //строки для записи в настройки
    private SharedPreferences mSharedPreferences;
    String App_Preference = "App_Preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);//инициализация для сохранения данных

        //для рекламы
        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-4978839534324790/6020215737");

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-4978839534324790/6020215737");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        counter = 0;//инициализация счётчика для рекламы

        //всё для полноэкранного режима
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//прикольная палитра
        //#69D2E7,#A7DBD8,#E0E4CC,#F38630,#FA6900,#69D2E7,#A7DBD8,#E0E4CC
        //инициализация для активности
        setContentView(R.layout.new_game_activity);

        whoAutor = findViewById(R.id.button_find_autor);

        withLvl = findViewById(R.id.textView_with_lvl);

        withEmoji = findViewById(R.id.textView_with_emoji);

        wrong = findViewById(R.id.wrongTextView);

        coin = findViewById(R.id.coin_textView);

        btnOk = findViewById(R.id.button_OK);

        editText = findViewById(R.id.editText_answer);

        withAutor = findViewById(R.id.textView_with_autor_name);

        findAnswer = findViewById(R.id.button_find_answer);

        coin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RandomGame.this, BuyCoin.class);

                startActivity(intent);
            }
        });
        ImageView coinsImage= findViewById(R.id.imageView);
        coinsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RandomGame.this, BuyCoin.class);

                startActivity(intent);
            }
        });


        findAnswer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int local = Integer.parseInt(coin.getText().toString());
                if (local < 250)
                    findAnswer.setEnabled(false);
                else {
                    local -= 250;
                    saveText("coin", "" + local);
                    coin.setText("" + local);
                    editText.setText(answer);
                    findAnswer.setEnabled(false);
                }
            }
        });

        //получаем номер уровня
        //  try {
        lvl = loadText("lvl");

        random=1 + (int) (Math.random() * Integer.parseInt(lvl));

        coin.setText(loadText("coin"));
        //  } catch (Exception r) {
        //      saveText("lvl", "1");
        //      lvl = "1";
        //  }
        whoAutor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int local = Integer.parseInt(coin.getText().toString());
                if (local < 100)
                    whoAutor.setEnabled(false);
                else {
                    local -= 100;
                    saveText("coin", "" + local);
                    coin.setText("" + local);
                    withAutor.setText(autor);
                    whoAutor.setEnabled(false);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().toLowerCase().equals(answer)|| proverka(editText.getText().toString())) {
                    refresh();
                } else {
                    counter++;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    assert vibrator != null;
                    vibrator.vibrate(300);
                    if (counter == 5) {
                        counter = 0;
                        Log.d("MyTAG", "onClick: Зашёл");

                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                // Code to be executed when an ad request fails.
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                // Load the next interstitial.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }

                        });
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                    wrong.setText("Неверный ответ");

                }
            }
        });

        withLvl.setText(""+random);

        DatabaseHelper s = new DatabaseHelper(this);
        SQLiteDatabase database = s.getReadableDatabase();
        Cursor cursor = database.query("Lvls", null, null, null, null, null, null);
        //random = new Random(Integer.parseInt(lvl));

        cursor.move(random);

        int emojiIndex = cursor.getColumnIndex("emojiName");

        int answerIndex = cursor.getColumnIndex("name");

        int autorIndex = cursor.getColumnIndex("autor");

        int subAnswerIndex = cursor.getColumnIndex("subName");
        try {
            subAnswer = new JSONArray(cursor.getString(subAnswerIndex));
        } catch (JSONException e) {
            Log.d("JSON_HELP", "create: " + e.toString());
        }

        withEmoji.setText(cursor.getString(emojiIndex));


        answer = cursor.getString(answerIndex).toLowerCase();

        autor = cursor.getString(autorIndex);
        cursor.close();
        database.close();
        s.close();


        editText.setOnKeyListener(new View.OnKeyListener() {
                                      public boolean onKey(View v, int keyCode, KeyEvent event) {
                                          if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                                  (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                              InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                              imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                              btnOk.callOnClick();
                                              return true;
                                          }
                                          return false;
                                      }
                                  }
        );

        AdView adView = findViewById(R.id.adViewGame);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


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


    private void refresh() {
        random= 1 + (int) (Math.random() * Integer.parseInt(lvl));
        //lvl = "" +random;

        DatabaseHelper s = new DatabaseHelper(this);
        SQLiteDatabase database = s.getReadableDatabase();
        Cursor cursor = database.query("Lvls", null, null, null, null, null, null);
        cursor.move(random);

        int emojiIndex = cursor.getColumnIndex("emojiName");
        int answerIndex = cursor.getColumnIndex("name");
        int autorIndex = cursor.getColumnIndex("autor");
        int subAnswerIndex = cursor.getColumnIndex("subName");

        try {
            subAnswer = new JSONArray(cursor.getString(subAnswerIndex));
        } catch (JSONException e) {
            Log.d("JSON_HELP", "refresh: " + e.toString() + "");
        }
        autor = cursor.getString(autorIndex);
        withEmoji.setText(cursor.getString(emojiIndex));
        answer = cursor.getString(answerIndex).toLowerCase();

        cursor.close();
        database.close();
        withLvl.setText(""+random);
        wrong.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        whoAutor.setEnabled(true);
        findAnswer.setEnabled(true);
        withAutor.setText("");


        // GameActivity.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        editText.setText("");
    }
    boolean proverka(String userAnswer) {
        if (subAnswer!=null) {
            for (int i = 0; i < subAnswer.length(); i++) {
                try {
                    if (subAnswer.getString(i).toLowerCase().equals(userAnswer.toLowerCase()))
                        return true;
                } catch (JSONException e) {
                    //e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

}
