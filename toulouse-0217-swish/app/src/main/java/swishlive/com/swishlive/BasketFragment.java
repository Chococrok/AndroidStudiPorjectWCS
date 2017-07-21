package swishlive.com.swishlive;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.grafika.CameraCaptureActivity;

public class BasketFragment extends SportFragment implements View.OnClickListener{

    TextView textViewTeamOneName;
    TextView textViewTeamTwoName;
    TextView textViewTeamOneScore;
    TextView textViewTeamTwoScore;

    Button buttonMinusScoreOne;
    Button buttonPlusScoreOne;
    Button buttonMinusTwoScoreOne;
    Button buttonPlusTwoScoreOne;
    Button buttonMinusThreeScoreOne;
    Button buttonPlusThreeScoreOne;
    Button buttonMinusScoreTwo;
    Button buttonPlusScoreTwo;
    Button buttonMinusTwoScoreTwo;
    Button buttonPlusTwoScoreTwo;
    Button buttonMinusThreeScoreTwo;
    Button buttonPlusThreeScoreTwo;
    Button buttonChrono;
    Button buttonFirstQuarter;
    Button buttonSecondQuarter;
    Button buttonThirdQuarter;
    Button buttonFourthQuarter;
    Button buttonStreaming;

    GameModel gameModel;
    BasketballModel basketballModel;
    TeamModel teamOne;
    TeamModel teamTwo;

    long timeWhenChronoStopped;

    boolean chronoRunning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_basket, container, false);

        textViewTeamOneName = (TextView) v.findViewById(R.id.textViewTeamOneName);
        textViewTeamTwoName = (TextView) v.findViewById(R.id.textViewTeamTwoName);
        textViewTeamOneScore = (TextView) v.findViewById(R.id.textViewTeamOneScore);
        textViewTeamTwoScore = (TextView) v.findViewById(R.id.textViewTeamTwoScore);

        chronometer = (Chronometer) v.findViewById(R.id.chronometerFootball);

        buttonChrono = (Button) v.findViewById(R.id.buttonChrono);
        buttonChrono.setOnClickListener(this);

        buttonFirstQuarter= (Button) v.findViewById(R.id.buttonFirstQuarter);
        buttonFirstQuarter.setOnClickListener(this);
        buttonSecondQuarter = (Button) v.findViewById(R.id.buttonSecondQuarter);
        buttonSecondQuarter.setOnClickListener(this);
        buttonThirdQuarter = (Button) v.findViewById(R.id.buttonThirdQuarter);
        buttonThirdQuarter.setOnClickListener(this);
        buttonFourthQuarter = (Button) v.findViewById(R.id.buttonFourthQuarter);
        buttonFourthQuarter.setOnClickListener(this);

        buttonMinusScoreOne  = (Button) v.findViewById(R.id.buttonMinusOneScoreOne);
        buttonMinusScoreOne.setOnClickListener(this);
        buttonPlusScoreOne = (Button) v.findViewById(R.id.buttonPlusOneScoreOne);
        buttonPlusScoreOne.setOnClickListener(this);
        buttonMinusTwoScoreOne = (Button) v.findViewById(R.id.buttonMinusTwoScoreOne);
        buttonMinusTwoScoreOne.setOnClickListener(this);
        buttonPlusTwoScoreOne = (Button) v.findViewById(R.id.buttonPlusTwoScoreOne);
        buttonPlusTwoScoreOne.setOnClickListener(this);
        buttonMinusThreeScoreOne = (Button) v.findViewById(R.id.buttonMinusThreeScoreOne);
        buttonMinusThreeScoreOne.setOnClickListener(this);
        buttonPlusThreeScoreOne = (Button) v.findViewById(R.id.buttonPlusThreeScoreOne);
        buttonPlusThreeScoreOne.setOnClickListener(this);

        buttonMinusScoreTwo = (Button) v.findViewById(R.id.buttonMinusOneScoreTwo);
        buttonMinusScoreTwo.setOnClickListener(this);
        buttonPlusScoreTwo = (Button) v.findViewById(R.id.buttonPlusOneScoreTwo);
        buttonPlusScoreTwo.setOnClickListener(this);
        buttonMinusTwoScoreTwo = (Button) v.findViewById(R.id.buttonMinusTwoScoreTwo);
        buttonMinusTwoScoreTwo.setOnClickListener(this);
        buttonPlusTwoScoreTwo = (Button) v.findViewById(R.id.buttonPlusTwoScoreTwo);
        buttonPlusTwoScoreTwo.setOnClickListener(this);
        buttonMinusThreeScoreTwo = (Button) v.findViewById(R.id.buttonMinusThreeScoreTwo);
        buttonMinusThreeScoreTwo.setOnClickListener(this);
        buttonPlusThreeScoreTwo = (Button) v.findViewById(R.id.buttonPlusThreeScoreTwo);
        buttonPlusThreeScoreTwo.setOnClickListener(this);

        buttonStreaming = (Button) v.findViewById(R.id.buttonStreeam);
        buttonStreaming.setOnClickListener(this);

        super.sportSequence = buttonFirstQuarter.getText().toString();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        gameModel = CameraCaptureActivity.getGameModel();
        teamOne = gameModel.getTeamOne();
        teamTwo = gameModel.getTeamTwo();
        basketballModel = new BasketballModel();

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

            case R.id.buttonMinusOneScoreOne :

                if (teamOne.getBasicScore() > 0) {
                    basketballModel.decrementScore(BasketballModel.ONE_POINT, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusOneScoreOne :

                basketballModel.incrementScore(BasketballModel.ONE_POINT, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusOneScoreTwo :

                if (teamTwo.getBasicScore() > 0) {
                    basketballModel.decrementScore(BasketballModel.ONE_POINT, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusOneScoreTwo:

                basketballModel.incrementScore(BasketballModel.ONE_POINT, teamTwo);
                updateUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonMinusTwoScoreOne:

                if (teamOne.getBasicScore() >= 2) {
                    basketballModel.decrementScore(BasketballModel.TWO_POINTS, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusTwoScoreOne :

                basketballModel.incrementScore(BasketballModel.TWO_POINTS, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusTwoScoreTwo :

                if (teamTwo.getBasicScore() >= 2) {
                    basketballModel.decrementScore(BasketballModel.TWO_POINTS, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusTwoScoreTwo:

                basketballModel.incrementScore(BasketballModel.TWO_POINTS, teamTwo);
                updateUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonMinusThreeScoreOne:

                if (teamOne.getBasicScore() >= 3) {
                    basketballModel.decrementScore(BasketballModel.THREE_POINTS, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusThreeScoreOne :

                basketballModel.incrementScore(BasketballModel.THREE_POINTS, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusThreeScoreTwo :

                if (teamTwo.getBasicScore() >= 3) {
                    basketballModel.decrementScore(BasketballModel.THREE_POINTS, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusThreeScoreTwo:

                basketballModel.incrementScore(BasketballModel.THREE_POINTS, teamTwo);
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

            case R.id.buttonFirstQuarter :

                buttonFirstQuarter.setBackgroundResource(R.drawable.chrono_shape);
                buttonSecondQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonThirdQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonFourthQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                super.chronometer.setBase(SystemClock.elapsedRealtime());
                super.sportSequence = buttonFirstQuarter.getText().toString();
                break;

            case R.id.buttonSecondQuarter :

                buttonSecondQuarter.setBackgroundResource(R.drawable.chrono_shape);
                buttonFirstQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonThirdQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonFourthQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                super.sportSequence = buttonSecondQuarter.getText().toString();
                super.chronometer.setBase(SystemClock.elapsedRealtime());
                break;

            case R.id.buttonThirdQuarter :

                buttonThirdQuarter.setBackgroundResource(R.drawable.chrono_shape);
                buttonFirstQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonSecondQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonFourthQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                super.sportSequence = buttonThirdQuarter.getText().toString();
                super.chronometer.setBase(SystemClock.elapsedRealtime());
                break;

            case R.id.buttonFourthQuarter :

                buttonFourthQuarter.setBackgroundResource(R.drawable.chrono_shape);
                buttonFirstQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonSecondQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                buttonThirdQuarter.setBackgroundResource(R.drawable.half_time_checked_button);
                super.sportSequence = buttonFourthQuarter.getText().toString();
                super.chronometer.setBase(SystemClock.elapsedRealtime());

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
