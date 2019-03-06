package com.statyja.emojimusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.common.util.CrashUtils;

import static com.statyja.emojimusic.Money.AD_MOB_REWARDED_VIDEO_ID;
import static com.statyja.emojimusic.Money.PREF_COINS;
import static com.statyja.emojimusic.Money.REWARD_FOR_VIDEO;


public class MainMenuActivity extends AppCompatActivity {
    Button play;
    private TextView moneyText;
    Button addCoin;
    Button howPlay;
    Button randomGame;
    private int coins;
    private RewardedVideoAd mRewardedVideoAd;

    private SharedPreferences mSharedPreferences;

    private AdRequest mAdRequest;

    String App_Preference = "App_Preference";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_fullscreen);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        randomGame = (Button) findViewById(R.id.button_randomGame);
        randomGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

                Intent intent = new Intent(MainMenuActivity.this, RandomGame.class);

                startActivity(intent);
            }
        });

        ImageView coinsImage= findViewById(R.id.imageView);
        coinsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, BuyCoin.class);

                startActivity(intent);
            }
        });
        moneyText = (TextView) findViewById(R.id.money_textView);
        moneyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, BuyCoin.class);

                startActivity(intent);
            }
        });



        play = (Button) findViewById(R.id.button_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                DatabaseHelper s = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase database = s.getReadableDatabase();
                Cursor cursor = database.query("Lvls", null, null, null, null, null, null);
                //получаем номер уровня
                //  try {

                String lvl = loadText("lvl");
                int lvlIndex = cursor.getColumnIndex("_id");
                cursor.moveToLast();
                if (cursor.getInt(lvlIndex) <= Integer.parseInt(lvl)) {
                    Intent intent = new Intent(MainMenuActivity.this, finalActivity.class);
                    Log.d("ForHelp", "onCreate: " + lvl + " " + cursor.getInt(lvlIndex));
                    startActivity(intent);
                    cursor.close();
                    database.close();
                } else {
                    Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);

                    startActivity(intent);
                }
            }
        });

        howPlay = (Button) findViewById(R.id.button_how_play);
        howPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(MainMenuActivity.this, HowPlayActivity.class);

                startActivity(intent);
            }
        });

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        addCoin = (Button) findViewById(R.id.adMob_button);
        addCoin.setEnabled(false);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        mAdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        loadRewardVideo();



        try {
            coins = Integer.parseInt(loadText("coin"));
        } catch (Exception e) {
            coins = 500;
            saveText("lvl", "0");

            saveText("coin", "" + coins);
        }
        if (loadText("lvl").equals("0")) {
            randomGame.setEnabled(false);
            randomGame.setAlpha((float) 0.25);
        }
        moneyText.setText("" + coins);

        addCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });
        //howPlay.setWidth(addCoin.getWidth());
        //play.setWidth(addCoin.getWidth());
        //randomGame.setWidth(addCoin.getWidth());

    }


    private void loadRewardVideo() {
        mRewardedVideoAd.loadAd(AD_MOB_REWARDED_VIDEO_ID, mAdRequest);
    }

    private RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            addCoin.setEnabled(true);
        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {

        }

        @Override
        public void onRewardedVideoAdClosed() {
            addCoin.setEnabled(false);
            loadRewardVideo();
        }

        @Override
        public void onRewarded(RewardItem rewardItem) {
            coins += REWARD_FOR_VIDEO;

            saveText("coin", "" + coins);
            moneyText.setText("" + coins);
            loadRewardVideo();
            //String msg = getResources().getQuantityString(R.plurals.congrats, REWARD_FOR_VIDEO,
            //        REWARD_FOR_VIDEO);
            //mSharedPreferences.edit().putInt(PREF_COINS, coins).apply();
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
            Log.d("GOTF", "onRewardedVideoAdFailedToLoad: " + i);
        }

        @Override
        public void onRewardedVideoCompleted() {
            Log.d("GOTF", "onRewardedVideoCompleted: ");
        }


    };

    @Override
    protected void onPause() {
        super.onPause();


        //mSharedPreferences.edit().putInt(PREF_COINS, coins).apply();
        saveText("coin", coins + "");
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.pause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.resume(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy(this);
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

    @Override
    protected void onStart() {
        super.onStart();
        coins = Integer.parseInt(loadText("coin"));
        moneyText.setText("" + coins);
        if (Integer.parseInt(loadText("lvl"))>1){
            randomGame.setAlpha(1);
            randomGame.setEnabled(true);
        }
    }
}
