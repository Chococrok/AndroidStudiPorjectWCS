package com.wcs.hackaton20170427;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ChooseEmplacement extends AppCompatActivity implements View.OnClickListener {

    public static final String PLAYER1_LIFE = "PLAYER1_LIFE";
    public static final String PLAYER2_LIFE = "PLAYER2_LIFE";

    private ImageButton mImageButtonSmallBoat;
    private ImageButton mImageButtonBigBoat;
    private ImageButton mImageButtonBigBoat2;
    private ImageButton mImageButtonLBoat;
    private ImageButton mImageButtonUBoat;
    private ImageButton mImageButtonReset;
    private ImageButton mImageButtonFight;

    private PixelGridView mPixelGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_emplacement);
        ViewGroup LinearLayoutGame  = (ViewGroup) findViewById(R.id.LinearLayoutGame);

        mPixelGridView = new PixelGridView(this);

        mPixelGridView.setNumColumns(10);
        mPixelGridView.setNumRows(20);


        mPixelGridView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        LinearLayoutGame.addView(mPixelGridView);

        mImageButtonSmallBoat = (ImageButton) findViewById(R.id.imageButtonSmallBoat);
        mImageButtonBigBoat = (ImageButton) findViewById(R.id.imageButtonBigBoat);
        mImageButtonBigBoat2 = (ImageButton) findViewById(R.id.imageButtonBigBoat2);
        mImageButtonLBoat = (ImageButton) findViewById(R.id.imageButtonBigBoatL);
        mImageButtonUBoat = (ImageButton) findViewById(R.id.imageButtonBigBoatU);
        mImageButtonReset = (ImageButton) findViewById(R.id.imageButtonReset);
        mImageButtonFight = (ImageButton) findViewById(R.id.imageButtonFight);

        mImageButtonSmallBoat.setOnClickListener(this);
        mImageButtonBigBoat.setOnClickListener(this);
        mImageButtonBigBoat2.setOnClickListener(this);
        mImageButtonLBoat.setOnClickListener(this);
        mImageButtonUBoat.setOnClickListener(this);
        mImageButtonReset.setOnClickListener(this);
        mImageButtonFight.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButtonSmallBoat:
                mPixelGridView.setChoosenBoat(BattleField.SMALLBOAT);
                break;
            case R.id.imageButtonBigBoat:
                mPixelGridView.setChoosenBoat(BattleField.BIGBOAT);
                break;
            case R.id.imageButtonBigBoat2:
                mPixelGridView.setChoosenBoat(BattleField.BIGBOAT2);
                break;
            case R.id.imageButtonBigBoatL:
                mPixelGridView.setChoosenBoat(BattleField.LBOAT);
                break;
            case R.id.imageButtonBigBoatU:
                mPixelGridView.setChoosenBoat(BattleField.UBOAT);
                break;
            case R.id.imageButtonReset:
                mPixelGridView.reset();
                break;
            case R.id.imageButtonFight:
                if(mPixelGridView.getmBattleField().isBoatAllSet()){
                    mPixelGridView.setBattleIsOn(true);
                    mPixelGridView.setmMyListener(new MyListener() {
                        @Override
                        public void onPlayer1Touched(int life) {
                            if(mPixelGridView.getmBattleField().getmLifePlayer1() == 0){
                                Intent intentToRestult = new Intent(ChooseEmplacement.this, Result.class);
                                intentToRestult.putExtra(PLAYER1_LIFE, mPixelGridView.getmBattleField().getmLifePlayer1());
                                intentToRestult.putExtra(PLAYER2_LIFE, mPixelGridView.getmBattleField().getmLifePlayer2());
                                finish();
                                startActivity(intentToRestult);
                            }
                        }

                        @Override
                        public void onPlayer2Touched(int life) {
                            if(mPixelGridView.getmBattleField().getmLifePlayer2() == 0){
                                Intent intentToRestult = new Intent(ChooseEmplacement.this, Result.class);
                                intentToRestult.putExtra(PLAYER1_LIFE, mPixelGridView.getmBattleField().getmLifePlayer1());
                                intentToRestult.putExtra(PLAYER2_LIFE, mPixelGridView.getmBattleField().getmLifePlayer2());
                                finish();
                                startActivity(intentToRestult);
                            }

                        }
                    });

                    ViewGroup linearLayoutBoat = (ViewGroup) findViewById(R.id.LinearLayoutBoat);
                    ViewGroup linearLayoutFight = (ViewGroup) findViewById(R.id.LinearLayoutResetFight);
                    linearLayoutBoat.setVisibility(View.GONE);
                    linearLayoutFight.setVisibility(View.GONE);
                }

                break;

        }
    }
}
