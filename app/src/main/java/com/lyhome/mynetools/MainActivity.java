package com.lyhome.mynetools;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);
        Intent intent = new Intent(MainActivity.this, com.lyhome.ETNews.MainActivity.class);
        startActivity(intent);
    }
}