package com.statyja.emojimusic;

import android.content.Context;
import android.content.SharedPreferences;


public class Money {

    private Money() {
        throw new AssertionError();
    }

    static final String PREF_COINS = "pref_coins";

    static final String ADMOB_ID = "pub-4978839534324790";
    static final String AD_MOB_REWARDED_VIDEO_ID = "ca-app-pub-4978839534324790/4097169094";

    static final int REWARD_FOR_VIDEO = 25;


    // public void saveText(String key, String save) {//Сохранение текста
    //     sPref = getSharedPreferences(App_Preference, Context.MODE_PRIVATE);
    //     SharedPreferences.Editor ed = sPref.edit();
    //     ed.putString(key, save);
    //     ed.apply();
    // }

    // public String loadText(String key) {//Загрузка текста
    //     sPref = getSharedPreferences(App_Preference, Context.MODE_PRIVATE);
    //     return sPref.getString(key, "");
    // }
}
