package com.wcs.hackaton20170427;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonPlay = (Button) findViewById(R.id.buttonPlay);
        mButtonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPlay:
                Intent intentToSign = new Intent(MainActivity.this, ChooseEmplacement.class);
                startActivity(intentToSign);
        }
    }
}
