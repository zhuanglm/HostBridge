package com.epocal.host4;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * The ic_about screen will go here in future. For now it is just a place holder.
 *
 * Created by Zeeshan A Zakaria on 6/14/2017.
 */

public class PlaceHolderActivity extends AppCompatActivity {

    Button mButton;
    ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.epocal.login.R.layout.activity_about);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActionBar actionBar = getSupportActionBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, com.epocal.login.R.color.colorAlereCranberry));
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, com.epocal.login.R.color.colorAlereCranberry)));
        }
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Epocal Host4");
        mProgressBar = (ProgressBar) findViewById(com.epocal.login.R.id.progress_bar_about);
        mButton = (Button) findViewById(com.epocal.login.R.id.button_go_back);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CloseActivity().execute();
            }
        });

    }

    /**
     * A smoother exit using Progress spinner to give user some feedback.
     */

    private class CloseActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
            return null;
        }
    }
}