package com.wcs.hackaton20170427;

import java.util.Random;

/**
 * Created by apprenti on 27/04/17.
 */

public class BattleField {

    public static final int SMALLBOAT = 4;
    public static final int BIGBOAT = 5;
    public static final int BIGBOAT2 = 6;
    public static final int LBOAT = 7;
    public static final int UBOAT = 8;
    public static final int NUMBER_OF_SHIP = 5;


    public static final int HIDDEN = -1;
    public static final int EMPTY = 0;
    public static final int HAS_BOAT = 1;
    public static final int SPLASH = 2;
    public static final int BOUM = 3;

    private int[][] mBattleField;
    private boolean[][] mSpaceFull;
    private int mWidth;
    private int mHeight;
    private boolean[] mBoatNotSet = new boolean[NUMBER_OF_SHIP + 5];
    private boolean mBattleIsOn;
    private int mNumberBoatOpponent;
    private int mLifePlayer1 = 5 + 4 + (3 * 2) + 2;
    private int mLifePlayer2 = 5 + 4 + (3 * 2) + 2;
    private MyListener mMyListener;

    public BattleField(int x, int y){
        mBattleIsOn = false;
        mWidth = x;
        mHeight = y;
        mMyListener = null;

        mBattleField = new int[mWidth][mHeight];
        mSpaceFull = new boolean[mWidth][mHeight];
        for (int i = 0; i < mWidth; i++){
            for (int j = 0; j < mHeight; j++) {
                mBattleField[i][j] = EMPTY;
                mSpaceFull[i][j] = false;
            }
        }
        for(int i = 4; i < mBoatNotSet.length -1; i++){
            this.mBoatNotSet[i] = true;
        }
        mNumberBoatOpponent = 4;
        Random ran = new Random();
        while( mNumberBoatOpponent < mBoatNotSet.length -1){
            int randomX = ran.nextInt(mWidth);
            int randomY = ran.nextInt(mHeight/2);
            setBoatTop(randomX, randomY, mNumberBoatOpponent);
        }
    }

    public void setmMyListener(MyListener mMyListener) {
        this.mMyListener = mMyListener;
    }

    public void player1hasBeenTouched() {
        this.mLifePlayer1 --;
        mMyListener.onPlayer1Touched(this.mLifePlayer1);
    }

    public int getmLifePlayer1() {
        return mLifePlayer1;
    }

    public void player2hasBeenTouched() {
        this.mLifePlayer2 --;
        mMyListener.onPlayer2Touched(this.mLifePlayer2);
    }

    public int getmLifePlayer2() {
        return mLifePlayer2;
    }

    public int[][] getmBattleField() {
        return mBattleField;
    }

    public boolean isBattleOn() {
        return mBattleIsOn;
    }

    public void setBattleIsOn(boolean battleIsOn) {
        this.mBattleIsOn = battleIsOn;
    }

    public boolean[] getBoatNotSet(){
        return mBoatNotSet;
    }

    public int[][] getBattleFieldState(){
        return mBattleField;
    }

    public boolean[][] getmSpaceFull() {
        return mSpaceFull;
    }

    public void setBoat(int x, int y, int boatType){
        switch (boatType){
            case SMALLBOAT:
                if (x + 1 < mWidth
                        && y >= mHeight/2
                        && mBoatNotSet[SMALLBOAT]
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]) {
                    mSpaceFull[x][y] = true;
                    mBattleField[x][y] = SMALLBOAT;
                    mSpaceFull[x + 1][y] = true;
                    mBoatNotSet[SMALLBOAT] = false;
                }
                break;

            case BIGBOAT:
                if (x + 2 < mWidth
                        && y >= mHeight/2
                        && mBoatNotSet[BIGBOAT]
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]) {
                    mSpaceFull[x][y] = true;
                    mBattleField[x][y] = BIGBOAT;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mBoatNotSet[BIGBOAT] = false;
                }
                break;

            case BIGBOAT2:
                if (x + 2 < mWidth
                        && y >= mHeight/2
                        && mBoatNotSet[BIGBOAT2]
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]) {
                    mSpaceFull[x][y] = true;
                    mBattleField[x][y] = BIGBOAT2;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mBoatNotSet[BIGBOAT2] = false;
                }
                break;

            case LBOAT:
                if (x + 2 < mWidth
                        && y - 1 >= mHeight/2
                        && mBoatNotSet[LBOAT]
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]
                        && !mSpaceFull[x][y - 1]) {
                    mSpaceFull[x][y] = true;
                    mBattleField[x][y] = LBOAT;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mSpaceFull[x][y - 1] = true;
                    mBoatNotSet[LBOAT] = false;
                }
                break;

            case UBOAT:
                if (x + 2 < mWidth
                        && y - 1 >= mHeight/2
                        && mBoatNotSet[UBOAT]
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]
                        && !mSpaceFull[x][y + 1]
                        && !mSpaceFull[x + 2][y + 1]) {
                    mSpaceFull[x][y] = true;
                    mBattleField[x][y] = UBOAT;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mSpaceFull[x][y - 1] = true;
                    mSpaceFull[x + 2][y - 1] = true;
                    mBoatNotSet[UBOAT] = false;
                }
                break;
        }

    }

    public void shoot(int x, int y){
        if (mBattleField[x][y] == EMPTY) {
            mBattleField[x][y] = SPLASH;
        }
        else if (mBattleField[x][y] == HAS_BOAT
                || mBattleField[x][y] == HIDDEN
                || mBattleField[x][y] == SMALLBOAT
                || mBattleField[x][y] == BIGBOAT
                || mBattleField[x][y] == BIGBOAT2
                || mBattleField[x][y] == LBOAT
                || mBattleField[x][y] == UBOAT){
            mBattleField[x][y] = BOUM;
        }
    }

    public void updateMap(){
        for (int i = 0; i < mWidth; i++){
            for (int j = 0; j < mHeight; j++) {
                if (mSpaceFull[i][j] && mBattleField[i][j] == EMPTY && j >= mHeight/2){
                    mBattleField[i][j] = HAS_BOAT;
                }
                else if (mSpaceFull[i][j] && mBattleField[i][j] == EMPTY){
                    mBattleField[i][j] = HIDDEN;
                }

            }
        }
    }

    public boolean isBoatAllSet() {
        for (int i = 0; i < mBoatNotSet.length; i++){
            if(mBoatNotSet[i]){
                return false;
            }
        }
        return true;
    }

    public void setBoatTop(int x, int y, int boatType){
        switch (boatType){
            case SMALLBOAT:
                if (x + 1 < mWidth
                        && y < mHeight/2
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]) {
                    mSpaceFull[x][y] = true;
                    mSpaceFull[x + 1][y] = true;
                    mNumberBoatOpponent++;
                }
                break;

            case BIGBOAT:
                if (x + 2 < mWidth
                        && y < mHeight/2
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]) {
                    mSpaceFull[x][y] = true;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mNumberBoatOpponent++;
                }
                break;

            case BIGBOAT2:
                if (x + 2 < mWidth
                        && y < mHeight/2
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]) {
                    mSpaceFull[x][y] = true;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mNumberBoatOpponent++;
                }
                break;

            case LBOAT:
                if (x + 2 < mWidth
                        && y < mHeight/2
                        && y + 1 <= mHeight/2
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]
                        && !mSpaceFull[x][y + 1]) {
                    mSpaceFull[x][y] = true;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mSpaceFull[x][y + 1] = true;
                    mNumberBoatOpponent++;
                }
                break;

            case UBOAT:
                if (x + 2 < mWidth
                        && y <= 9
                        && y + 1 < mHeight/2
                        && !mSpaceFull[x][y]
                        && !mSpaceFull[x + 1][y]
                        && !mSpaceFull[x + 2][y]
                        && !mSpaceFull[x][y + 1]
                        && !mSpaceFull[x + 2][y + 1]) {
                    mSpaceFull[x][y] = true;
                    mSpaceFull[x + 1][y] = true;
                    mSpaceFull[x + 2][y] = true;
                    mSpaceFull[x][y + 1] = true;
                    mSpaceFull[x + 2][y + 1] = true;
                    mNumberBoatOpponent++;
                }
                break;
        }

    }


}
