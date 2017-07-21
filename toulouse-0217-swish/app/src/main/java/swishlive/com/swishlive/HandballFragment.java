package swishlive.com.swishlive;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.grafika.CameraCaptureActivity;

public class HandballFragment extends SportFragment implements View.OnClickListener{

    TextView textViewTeamOneName;
    TextView textViewTeamTwoName;
    TextView textViewTeamOneScore;
    TextView textViewTeamTwoScore;

    long timeWhenStopped;

    Button buttonMinusScoreOne;
    Button buttonPlusScoreOne;
    Button buttonMinusScoreTwo;
    Button buttonPlusScoreTwo;
    Button buttonChrono;
    Button buttonFirstHalf;
    Button buttonSecondHalf;
    Button buttonStreeam;

    GameModel gameModel;
    HandballModel handballModel;
    TeamModel teamOne;
    TeamModel teamTwo;

    boolean chronoRunning;
    boolean firstHalf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_football, container, false);

        textViewTeamOneName = (TextView) v.findViewById(R.id.textViewTeamOneName);
        textViewTeamTwoName = (TextView) v.findViewById(R.id.textViewTeamTwoName);
        textViewTeamOneScore = (TextView) v.findViewById(R.id.textViewTeamOneScore);
        textViewTeamTwoScore = (TextView) v.findViewById(R.id.textViewTeamTwoScore);

        chronometer = (Chronometer) v.findViewById(R.id.chronometerFootball);
        timeWhenStopped = 0;

        buttonMinusScoreOne  = (Button) v.findViewById(R.id.buttonMinusScoreOne);
        buttonMinusScoreOne.setOnClickListener(this);
        buttonPlusScoreOne = (Button) v.findViewById(R.id.buttonPlusScoreOne);
        buttonPlusScoreOne.setOnClickListener(this);
        buttonMinusScoreTwo = (Button) v.findViewById(R.id.buttonMinusScoreTwo);
        buttonMinusScoreTwo.setOnClickListener(this);
        buttonPlusScoreTwo = (Button) v.findViewById(R.id.buttonPlusScoreTwo);
        buttonPlusScoreTwo.setOnClickListener(this);
        buttonChrono = (Button) v.findViewById(R.id.buttonChrono);
        buttonChrono.setOnClickListener(this);
        buttonFirstHalf = (Button) v.findViewById(R.id.buttonFirstHalf);
        buttonFirstHalf.setOnClickListener(this);
        buttonSecondHalf = (Button) v.findViewById(R.id.buttonSecondHalf);
        buttonSecondHalf.setOnClickListener(this);
        buttonStreeam = (Button) v.findViewById(R.id.buttonStreeam);
        buttonStreeam.setOnClickListener(this);

        super.sportSequence = buttonFirstHalf.getText().toString();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        gameModel = CameraCaptureActivity.getGameModel();
        teamOne = gameModel.getTeamOne();
        teamTwo = gameModel.getTeamTwo();
        handballModel = new HandballModel();

        textViewTeamOneName.setText(teamOne.getName());
        textViewTeamTwoName.setText(teamTwo.getName());

        chronoRunning = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonMinusScoreOne :

                if (teamOne.getBasicScore() > 0) {
                    handballModel.decrementScore(FootballModel.BUT, teamOne);
                    updateUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusScoreOne :

                handballModel.incrementScore(FootballModel.BUT, teamOne);
                updateUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusScoreTwo :

                if (teamTwo.getBasicScore() > 0) {
                    handballModel.decrementScore(FootballModel.BUT, teamTwo);
                    updateUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusScoreTwo:

                handballModel.incrementScore(FootballModel.BUT, teamTwo);
                updateUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonChrono :

                if (chronoRunning) {

                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    buttonChrono.setBackgroundResource(R.drawable.play_button);
                    chronoRunning = false;
                } else {

                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                    buttonChrono.setBackgroundResource(R.drawable.pause_button);
                    chronoRunning = true;
                }
                break;

            case R.id.buttonFirstHalf :
                buttonFirstHalf.setBackgroundResource(R.drawable.chrono_shape);
                buttonSecondHalf.setBackgroundResource(R.drawable.half_time_checked_button);
                super.sportSequence = buttonFirstHalf.getText().toString();
                firstHalf = true;
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
