package com.statyja.emojimusic;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import static com.statyja.emojimusic.Money.AD_MOB_REWARDED_VIDEO_ID;
import static com.statyja.emojimusic.Money.REWARD_FOR_VIDEO;

public class BuyCoin extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    String App_Preference = "App_Preference";
    private TextView moneyText;

    Button fistBuy;
    Button secondBuy;
    Button thirdBuy;
    Button fouthBuy;
    Button fifthBuy;
    Button freeCoin;

    private RewardedVideoAd mRewardedVideoAd;
    private AdRequest mAdRequest;
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.buy_coin_layout);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        fistBuy=findViewById(R.id.first_btn_buy_coin);
        fistBuy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

            }
        });
        secondBuy=findViewById(R.id.second_btn_buy_coin);
        secondBuy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

            }
        });
        thirdBuy=findViewById(R.id.third_btn_buy_coin);
        thirdBuy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

            }
        });
        fouthBuy=findViewById(R.id.fourth_btn_buy_coin);
        fouthBuy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

            }
        });
        fifthBuy=findViewById(R.id.fifth_btn_buy_coin);
        fifthBuy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

            }
        });

        coins = Integer.parseInt(loadText("coin"));
        moneyText = (TextView) findViewById(R.id.money_textView);
        freeCoin=findViewById(R.id.adMob_button_in_buy);

        moneyText.setText("" + coins);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        mAdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        loadRewardVideo();

        freeCoin.setEnabled(false);
        
        freeCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });

    }

    private void loadRewardVideo() {
        mRewardedVideoAd.loadAd(AD_MOB_REWARDED_VIDEO_ID, mAdRequest);
    }

    private RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            freeCoin.setEnabled(true);
        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {

        }

        @Override
        public void onRewardedVideoAdClosed() {
            freeCoin.setEnabled(false);
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