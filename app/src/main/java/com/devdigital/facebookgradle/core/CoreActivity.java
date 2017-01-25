package com.devdigital.facebookgradle.core;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hitesh on 07-Mar-16.
 */
public abstract class CoreActivity extends AppCompatActivity {
    protected abstract void initUI();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
