package com.exedos.livingwaterplant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Navigation Bar Hiding
       // getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        TextView textView = findViewById(R.id.splash_screen_text);
        textView.animate().translationX(-1000).setDuration(2000).setStartDelay(3000);
        Thread thread = new Thread(){
            public void run(){
                try{
                    Thread.sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                finally {
                    Intent intent = new Intent(SplashScreen.this,RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };

        thread.start();


    }
}