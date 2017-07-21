package com.ab.wildchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Random;


public class ClickerFragment extends ModelFragment implements SurfaceHolder.Callback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ClickerFragment";


    private OnFragmentClickerInteractionListener mListener;

    private SurfaceHolder mSurfaceHolder;
    private Paint mBlackPaint;
    private int count;
    private int shWidth;
    private int shHeight;
    private int mPosition;


    public ClickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View clickerFragmentView = inflater.inflate(R.layout.fragment_clicker, container, false);
        final SurfaceView surfaceViewClicker = clickerFragmentView.findViewById(R.id.surfaceViewClicker);
        surfaceViewClicker.getHolder().addCallback(this);
        final DatabaseReference databaseClick = FirebaseDatabase.getInstance()
                .getReference(LogActivity.DATABASE_REF_USER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(UserModel.CLICK_REF);
        surfaceViewClicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    databaseClick.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            if (mutableData.getValue() == null) {
                                return Transaction.success(mutableData);
                            }
                            mutableData.setValue(mutableData.getValue(Integer.class) + 1);
                            ;
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            if (databaseError != null) {
                                Log.d(TAG, databaseError.getMessage());
                            }
                            count = dataSnapshot.getValue(Integer.class);
                        }
                    });

                    if (mSurfaceHolder != null) {
                        draw();
                    }
                }

                return true;

            }
        });

        return clickerFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentClickerInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentClickerInteractionListener) {
            mListener = (OnFragmentClickerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        mBlackPaint = new Paint();
        mBlackPaint.setColor(Color.argb(255, 0, 0, 0));
        mBlackPaint.setTextAlign(Paint.Align.CENTER);
        mBlackPaint.setFakeBoldText(true);
        Canvas c = surfaceHolder.lockCanvas();
        c.drawColor(Color.argb(255, 255, 255, 255));
        surfaceHolder.unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
        shWidth = width;
        shHeight = height;
        mBlackPaint.setTextSize(shWidth/3);
        draw();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentClickerInteractionListener {
        // TODO: Update argument type and name
        void onFragmentClickerInteraction(Uri uri);
    }

    private void draw(){
        Canvas c = mSurfaceHolder.lockCanvas();
        Random r = new Random();
        c.drawColor(Color.argb(255, r.nextInt(100) + 155, r.nextInt(100) + 155, r.nextInt(100) + 155));
        if (count == 0){
            c.drawText("Click !", shWidth/2, shHeight/2, mBlackPaint);
        }
        else{
            c.drawText(String.valueOf(count + 1), shWidth/2, shHeight/2, mBlackPaint);
        }
        mSurfaceHolder.unlockCanvasAndPost(c);
    }
}
