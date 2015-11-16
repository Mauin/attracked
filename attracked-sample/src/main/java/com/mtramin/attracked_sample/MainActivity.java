package com.mtramin.attracked_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mtramin.attracked.Attracked;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Attracked
                .initialize()
                .registerCrashlyticsAnswers(this);

        trackSomething();
    }

    private void trackSomething() {
        AttrackedTestEvent.trackTest("yes", 9001);
    }
}
