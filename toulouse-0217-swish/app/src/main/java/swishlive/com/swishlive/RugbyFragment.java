package swishlive.com.swishlive;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.grafika.CameraCaptureActivity;

/**
 * Created by keru on 28/06/17.
 */

public class RugbyFragment extends SportFragment implements View.OnClickListener{

    TextView textViewTeamOneName;
    TextView textViewTeamTwoName;
    TextView textViewTeamOneScore;
    TextView textViewTeamTwoScore;

    Button buttonMinusFiveScoreOne;
    Button buttonPlusFiveScoreOne;
    Button buttonMinusTwoScoreOne;
    Button buttonPlusTwoScoreOne;
    Button buttonMinusThreeScoreOne;
    Button buttonPlusThreeScoreOne;

    Button buttonMinusFiveScoreTwo;
    Button buttonPlusFiveScoreTwo;
    Button buttonMinusTwoScoreTwo;
    Button buttonPlusTwoScoreTwo;
    Button buttonMinusThreeScoreTwo;
    Button buttonPlusThreeScoreTwo;

    Button buttonStreaming;

    Button buttonChrono;

    Button buttonFirstHalf;
    Button buttonSecondHalf;

    GameModel gameModel;
    RugbyModel rugbyModel;
    TeamModel teamOne;
    TeamModel teamTwo;

    long timeWhenChronoStopped;

    boolean chronoRunning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rugby, container, false);

        textViewTeamOneName = (TextView) v.findViewById(R.id.textViewTeamOneName);
        textViewTeamTwoName = (TextView) v.findViewById(R.id.textViewTeamTwoName);
        textViewTeamOneScore = (TextView) v.findViewById(R.id.textViewTeamOneScore);
        textViewTeamTwoScore = (TextView) v.findViewById(R.id.textViewTeamTwoScore);

        chronometer = (Chronometer) v.findViewById(R.id.chronometerFootball);

        buttonChrono = (Button) v.findViewById(R.id.buttonChrono);
        buttonChrono.setOnClickListener(this);

        buttonFirstHalf= (Button) v.findViewById(R.id.buttonFirstHalf);
        buttonFirstHalf.setOnClickListener(this);
        buttonSecondHalf = (Button) v.findViewById(R.id.buttonSecondHalf);
        buttonSecondHalf.setOnClickListener(this);


        buttonMinusTwoScoreOne = (Button) v.findViewById(R.id.buttonMinusTwoScoreOne);
        buttonMinusTwoScoreOne.setOnClickListener(this);
        buttonPlusTwoScoreOne = (Button) v.findViewById(R.id.buttonPlusTwoScoreOne);
        buttonPlusTwoScoreOne.setOnClickListener(this);
        buttonMinusThreeScoreOne = (Button) v.findViewById(R.id.buttonMinusThreeScoreOne);
        buttonMinusThreeScoreOne.setOnClickListener(this);
        buttonPlusThreeScoreOne = (Button) v.findViewById(R.id.buttonPlusThreeScoreOne);
        buttonPlusThreeScoreOne.setOnClickListener(this);
        buttonMinusFiveScoreOne  = (Button) v.findViewById(R.id.buttonMinusFiveScoreOne);
        buttonMinusFiveScoreOne.setOnClickListener(this);
        buttonPlusFiveScoreOne = (Button) v.findViewById(R.id.buttonPlusFiveScoreOne);
        buttonPlusFiveScoreOne.setOnClickListener(this);


        buttonMinusTwoScoreTwo = (Button) v.findViewById(R.id.buttonMinusTwoScoreTwo);
        buttonMinusTwoScoreTwo.setOnClickListener(this);
        buttonPlusTwoScoreTwo = (Button) v.findViewById(R.id.buttonPlusTwoScoreTwo);
        buttonPlusTwoScoreTwo.setOnClickListener(this);
        buttonMinusThreeScoreTwo = (Button) v.findViewById(R.id.buttonMinusThreeScoreTwo);
        buttonMinusThreeScoreTwo.setOnClickListener(this);
        buttonPlusThreeScoreTwo = (Button) v.findViewById(R.id.buttonPlusThreeScoreTwo);
        buttonPlusThreeScoreTwo.setOnClickListener(this);
        buttonMinusFiveScoreTwo = (Button) v.findViewById(R.id.buttonMinusFiveScoreTwo);
        buttonMinusFiveScoreTwo.setOnClickListener(this);
        buttonPlusFiveScoreTwo = (Button) v.findViewById(R.id.buttonPlusFiveScoreTwo);
        buttonPlusFiveScoreTwo.setOnClickListener(this);

        buttonStreaming = (Button) v.findViewById(R.id.buttonStreeam);
        buttonStreaming.setOnClickListener(this);

        super.sportSequence = buttonFirstHalf.getText().toString();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        gameModel = CameraCaptureActivity.getGameModel();
        teamOne = gameModel.getTeamOne();
        teamTwo = gameModel.getTeamTwo();
        rugbyModel = new RugbyModel();

        textViewTeamOneName.setText(teamOne.getName());
        textViewTeamTwoName.setText(teamTwo.getName());

        timeWhenChronoStopped = 0;

        chronoRunning = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonMinusTwoScoreOne :

                if (teamOne.getBasicScore() > 2) {
                    rugbyModel.decrementScore(RugbyModel.TRANSFORMATION, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusTwoScoreOne :

                rugbyModel.incrementScore(RugbyModel.TRANSFORMATION, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusTwoScoreTwo :

                if (teamTwo.getBasicScore() > 2) {
                    rugbyModel.decrementScore(RugbyModel.TRANSFORMATION, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusTwoScoreTwo:

                rugbyModel.incrementScore(RugbyModel.TRANSFORMATION, teamTwo);
                updateUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonMinusThreeScoreOne:

                if (teamOne.getBasicScore() >= 3) {
                    rugbyModel.decrementScore(RugbyModel.PENALITE_DROP, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusThreeScoreOne :

                rugbyModel.incrementScore(RugbyModel.PENALITE_DROP, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusThreeScoreTwo :

                if (teamTwo.getBasicScore() >= 3) {
                    rugbyModel.decrementScore(RugbyModel.PENALITE_DROP, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusThreeScoreTwo:

                rugbyModel.incrementScore(RugbyModel.PENALITE_DROP, teamTwo);
                updateUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonMinusFiveScoreOne:

                if (teamOne.getBasicScore() >= 5) {
                    rugbyModel.decrementScore(RugbyModel.ESSAI, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusFiveScoreOne :

                rugbyModel.incrementScore(RugbyModel.ESSAI, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusFiveScoreTwo :

                if (teamTwo.getBasicScore() >= 5) {
                    rugbyModel.decrementScore(RugbyModel.ESSAI, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusFiveScoreTwo:

                rugbyModel.incrementScore(RugbyModel.ESSAI, teamTwo);
                updateUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonChrono :

                if (chronoRunning) {

                    timeWhenChronoStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    buttonChrono.setBackgroundResource(R.drawable.play_button);
                    chronoRunning = false;
                } else {

                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenChronoStopped);
                    chronometer.start();
                    buttonChrono.setBackgroundResource(R.drawable.pause_button);
                    chronoRunning = true;
                }
                break;

            case R.id.buttonFirstHalf :
                buttonFirstHalf.setBackgroundResource(R.drawable.chrono_shape);
                buttonSecondHalf.setBackgroundResource(R.drawable.half_time_checked_button);
                super.sportSequence = buttonFirstHalf.getText().toString();
                break;

            case R.id.buttonSecondHalf :
                buttonFirstHalf.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonSecondHalf.setBackgroundResource(R.drawable.chrono_shape);
                super.sportSequence = buttonSecondHalf.getText().toString();
                break;

            case R.id.buttonStreeam:
                if (mListener != null) {
                    mListener.onFragmentClickerInteraction();
                }
                break;
        }

    }

    public void updateUi(TextView textView, TeamModel teamModel){

        textView.setText(String.valueOf(teamModel.getBasicScore()));
    }

}

