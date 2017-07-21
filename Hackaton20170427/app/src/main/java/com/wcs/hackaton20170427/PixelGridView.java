package com.wcs.hackaton20170427;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by apprenti on 27/04/17.
 */

public class PixelGridView extends View {


    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private Paint redPaint = new Paint();
    private Paint whitePaint = new Paint();
    private int choosenBoat;
    private BattleField mBattleField;
    private MyListener mMyListener;
    private Context mContext;

    public PixelGridView(Context context) {
        this(context, null);
        this.choosenBoat = - 1;
        mMyListener = null;
        mContext = context;
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redPaint.setColor(Color.RED);
        redPaint.setAlpha(150);
        whitePaint.setColor(Color.WHITE);
    }

    public void setmMyListener(MyListener mMyListener) {
        this.mMyListener = mMyListener;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public void setBattleIsOn(boolean battleIsOn) {
        mBattleField.setBattleIsOn(battleIsOn);

    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    public BattleField getmBattleField() {
        return mBattleField;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        if (mBattleField == null) {
            mBattleField = new BattleField(numColumns, numRows);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.BLUE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                switch (mBattleField.getBattleFieldState()[i][j]) {
                    /*case BattleField.HAS_BOAT:
                        canvas.drawRect(i * cellWidth, j * cellHeight,
                                (i + 1) * cellWidth, (j + 1) * cellHeight,
                                blackPaint);
                        break;*/
                    case BattleField.SMALLBOAT:
                        Bitmap smallBoat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.small_boat);
                        Bitmap scalledSmallBoat = Bitmap.createScaledBitmap(smallBoat,
                        cellWidth *2,
                        cellHeight,
                        false);
                        canvas.drawBitmap(scalledSmallBoat,i * cellWidth, j * cellHeight,
                                blackPaint);
                        break;
                    case BattleField.BIGBOAT:
                        Bitmap bigBoat1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.big_boat1);
                        Bitmap scalledbigBoat1 = Bitmap.createScaledBitmap(bigBoat1,
                                cellWidth *3,
                                cellHeight,
                                false);
                        canvas.drawBitmap(scalledbigBoat1,i * cellWidth, j * cellHeight,
                                blackPaint);
                        break;
                    case BattleField.BIGBOAT2:
                        Bitmap bigBoat2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.big_boat2);
                        Bitmap scalledbigBoat2 = Bitmap.createScaledBitmap(bigBoat2,
                                cellWidth *3,
                                cellHeight,
                                false);
                        canvas.drawBitmap(scalledbigBoat2,i * cellWidth, j * cellHeight,
                                blackPaint);
                        break;
                    case BattleField.LBOAT:
                        Bitmap lBoat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.l_boat);
                        Bitmap scalledlBoat = Bitmap.createScaledBitmap(lBoat,
                                cellWidth *3,
                                cellHeight *2,
                                false);
                        canvas.drawBitmap(scalledlBoat,i * cellWidth, (j - 1) * cellHeight,
                                blackPaint);
                        break;
                    case BattleField.UBOAT:
                        Bitmap uBoat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.u_boat);
                        Bitmap scalleduBoat = Bitmap.createScaledBitmap(uBoat,
                                cellWidth *3,
                                cellHeight *2,
                                false);
                        canvas.drawBitmap(scalleduBoat,i * cellWidth, (j - 1) * cellHeight,
                                blackPaint);
                        break;
                    case BattleField.BOUM:
                        canvas.drawRect(i * cellWidth, j * cellHeight,
                                (i + 1) * cellWidth, (j + 1) * cellHeight,
                                redPaint);
                        break;
                    case BattleField.SPLASH:
                        canvas.drawRect(i * cellWidth, j * cellHeight,
                                (i + 1) * cellWidth, (j + 1) * cellHeight,
                                whitePaint);
                        break;
                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            if(i == 10){
                blackPaint.setStrokeWidth(5);
                canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
                blackPaint.setStrokeWidth(1);
            }
            else {
                canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
            }
        }
    }

    public void setChoosenBoat(int i){
        this.choosenBoat = i;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            if (column > 9){
                column = 9;
            }
            int row = (int)(event.getY() / cellHeight);

            if (mBattleField.isBattleOn()){
                mBattleField.shoot(column, row);
                if (mBattleField.getBattleFieldState()[column][row] == BattleField.BOUM){
                    mBattleField.player2hasBeenTouched();
                }
                Random ran = new Random();
                int randomX = ran.nextInt(numColumns);
                int randomY = ran.nextInt(numRows/2) + numRows/2;
                mBattleField.shoot(randomX, randomY);
                if (mBattleField.getBattleFieldState()[randomX][randomY] == BattleField.BOUM){
                    mBattleField.player1hasBeenTouched();
                }
                mBattleField.updateMap();
                mBattleField.setmMyListener(new MyListener() {
                                                @Override
                                                public void onPlayer1Touched(int life) {
                                                    mMyListener.onPlayer1Touched(mBattleField.getmLifePlayer1());

                                                }

                                                @Override
                                                public void onPlayer2Touched(int life) {
                                                    mMyListener.onPlayer2Touched(mBattleField.getmLifePlayer2());

                                                }
                                            });
                invalidate();
            }
            else {
                switch (this.choosenBoat) {
                    case BattleField.SMALLBOAT:
                        mBattleField.setBoat(column, row, BattleField.SMALLBOAT);
                        break;

                    case BattleField.BIGBOAT:
                        mBattleField.setBoat(column, row, BattleField.BIGBOAT);
                        break;

                    case BattleField.BIGBOAT2:
                        mBattleField.setBoat(column, row, BattleField.BIGBOAT2);
                        break;

                    case BattleField.LBOAT:
                        mBattleField.setBoat(column, row, BattleField.LBOAT);
                        break;

                    case BattleField.UBOAT:
                        mBattleField.setBoat(column, row, BattleField.UBOAT);
                        break;
                }

                mBattleField.updateMap();
                invalidate();
            }
        }

        return true;
    }

    public void reset() {
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                mBattleField.getmSpaceFull()[i][j] = false;
                mBattleField.getBattleFieldState()[i][j] = BattleField.EMPTY;

            }
        }
        choosenBoat = -1;
        for(int i = 0; i < mBattleField.getBoatNotSet().length; i++){
            mBattleField.getBoatNotSet()[i] = true;
        }
        invalidate();
    }
}