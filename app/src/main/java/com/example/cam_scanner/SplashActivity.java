package com.example.cam_scanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    final Intent i = new Intent(SplashActivity.this, MainActivity.class);

    Thread thread = new Thread()
    {
      public void run() {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          startActivity(i);
          finish();
        }
      }
    };
    thread.start();
  }
}