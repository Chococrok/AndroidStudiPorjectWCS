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

public class VolleyFragment extends SportFragment implements View.OnClickListener {

    TextView textViewTeamOneName;
    TextView textViewTeamTwoName;
    TextView textViewTeamOneScore;
    TextView textViewTeamTwoScore;
    TextView textViewTeamOneSetScore;
    TextView textViewTeamTwoSetScore;

    Button buttonMinusScoreOne;
    Button buttonPlusScoreOne;
    Button buttonMinusScoreTwo;
    Button buttonPlusScoreTwo;
    Button buttonPlusSetScoreOne;
    Button buttonMinusSetScoreOne;
    Button buttonPlusSetScoreTwo;
    Button buttonMinusSetScoreTwo;
    Button buttonChrono;
    Button buttonStreaming;

    GameModel gameModel;
    VolleyModel volleyModel;
    TeamModel teamOne;
    TeamModel teamTwo;

    int teamOneSetScore;
    int teamTwoSetScore;

    long timeWhenChronoStopped;

    boolean chronoRunning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_volley, container, false);

        textViewTeamOneName = (TextView) v.findViewById(R.id.textViewTeamOneName);
        textViewTeamTwoName = (TextView) v.findViewById(R.id.textViewTeamTwoName);
        textViewTeamOneScore = (TextView) v.findViewById(R.id.textViewTeamOneScore);
        textViewTeamTwoScore = (TextView) v.findViewById(R.id.textViewTeamTwoScore);
        textViewTeamOneSetScore = (TextView) v.findViewById(R.id.textViewTeamOneSetScore);
        textViewTeamTwoSetScore = (TextView) v.findViewById(R.id.textViewTeamTwoSetScore);

        chronometer = (Chronometer) v.findViewById(R.id.chronometerFootball);

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
        buttonPlusSetScoreOne = (Button) v.findViewById(R.id.buttonPlusSetScoreOne);
        buttonPlusSetScoreOne.setOnClickListener(this);
        buttonPlusSetScoreTwo = (Button) v.findViewById(R.id.buttonPlusSetScoreTwo);
        buttonPlusSetScoreTwo.setOnClickListener(this);
        buttonMinusSetScoreOne = (Button) v.findViewById(R.id.buttonMinusSetScoreOne);
        buttonMinusSetScoreOne.setOnClickListener(this);
        buttonMinusSetScoreTwo = (Button) v.findViewById(R.id.buttonMinusSetScoreTwo);
        buttonMinusSetScoreTwo.setOnClickListener(this);
        buttonStreaming = (Button) v.findViewById(R.id.buttonStreeam);
        buttonStreaming.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        gameModel = CameraCaptureActivity.getGameModel();
        teamOne = gameModel.getTeamOne();
        teamTwo = gameModel.getTeamTwo();
        volleyModel = new VolleyModel();

        textViewTeamOneName.setText(teamOne.getName());
        textViewTeamTwoName.setText(teamTwo.getName());

        teamOneSetScore = 0;
        teamOneSetScore = 0;

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

            case R.id.buttonMinusScoreOne :

                if (teamOne.getBasicScore() > 0) {
                    volleyModel.decrementScore(VolleyModel.SET, teamOne);
                    updateScoreUi(textViewTeamOneScore, teamOne);
                }
                break;

            case R.id.buttonPlusScoreOne :

                volleyModel.incrementScore(VolleyModel.SET, teamOne);
                updateScoreUi(textViewTeamOneScore, teamOne);
                break;

            case R.id.buttonMinusSetScoreOne :

                if (teamOneSetScore > 0) {

                    teamOneSetScore --;
                    updateSetUi(textViewTeamOneSetScore, teamOneSetScore);
                }
                break;

            case R.id.buttonPlusSetScoreOne :

                teamOneSetScore ++;
                updateSetUi(textViewTeamOneSetScore, teamOneSetScore);
                break;

            case R.id.buttonMinusScoreTwo :

                if (teamTwo.getBasicScore() > 0) {
                    volleyModel.decrementScore(VolleyModel.SET, teamTwo);
                    updateScoreUi(textViewTeamTwoScore, teamTwo);
                }
                break;

            case R.id.buttonPlusScoreTwo:

                volleyModel.incrementScore(VolleyModel.SET, teamTwo);
                updateScoreUi(textViewTeamTwoScore, teamTwo);
                break;

            case R.id.buttonMinusSetScoreTwo :

                if (teamTwoSetScore > 0) {

                    teamTwoSetScore --;
                    updateSetUi(textViewTeamTwoSetScore, teamTwoSetScore);
                }
                break;

            case R.id.buttonPlusSetScoreTwo :

                teamTwoSetScore ++;
                updateSetUi(textViewTeamTwoSetScore, teamTwoSetScore);
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

            case R.id.buttonStreeam:
                if (mListener != null) {
                    mListener.onFragmentClickerInteraction();
                }
                break;
        }

    }

    public void updateScoreUi(TextView textView, TeamModel teamModel){

        textView.setText(String.valueOf(teamModel.getBasicScore()));
    }


    public void updateSetUi(TextView textView, int score) {

        textView.setText(String.valueOf(score));
    }
}

