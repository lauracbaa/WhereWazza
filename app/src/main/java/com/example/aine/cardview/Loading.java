package com.example.aine.cardview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
        Thread time = new Thread() {
            @Override
            public void run() {
                // run a loading page, the progress bar will spin and after (2500) the Main Activity page will open
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent popIntent = new Intent(Loading.this, MainActivity.class ); // opens the Popularactivity page
                    popIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    Loading.this.startActivity(popIntent);
                }
            }
        };
        time.start();
    }
}
