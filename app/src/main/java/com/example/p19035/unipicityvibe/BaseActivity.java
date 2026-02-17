package com.example.p19035.unipicityvibe;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

//This Base Activity exists to change the language in every activity using LocaleHelper
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }
}
