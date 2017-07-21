package com.wcs.hackaton20170427;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Result extends AppCompatActivity {

    private int mLifePlayer1;
    private int mLifePlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayoutResult);

        Intent intentFromBattle = getIntent();
        mLifePlayer1 = intentFromBattle.getIntExtra(ChooseEmplacement.PLAYER1_LIFE, -1);
        mLifePlayer2 = intentFromBattle.getIntExtra(ChooseEmplacement.PLAYER2_LIFE, -1);

        if (mLifePlayer1 == 0){
            constraintLayout.setBackgroundResource(R.drawable.rip);
        }

        else {
            constraintLayout.setBackgroundResource(R.drawable.win);
        }
    }
}
