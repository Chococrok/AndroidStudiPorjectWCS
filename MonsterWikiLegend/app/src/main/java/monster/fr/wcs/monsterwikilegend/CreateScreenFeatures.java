package monster.fr.wcs.monsterwikilegend;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CreateScreenFeatures extends AppCompatActivity {

    int typeChoice;
    Intent intentFromCreateScreen;
    ImageView typeViewCreateScreenFeatures;
    TextView nameViewCreateScreenFeatures;
    TextView typeTextCreateScreenFeatures;
    String monsterName;
    String[] typeNameString;
    Button validateButtonCreateScreenFeatures;
    TextView resultLife;
    TextView resultPower;
    TextView resultSpeed;
    TextView resultStamina;
    SeekBar seekBarLife;
    SeekBar seekBarPower;
    SeekBar seekBarSpeed;
    SeekBar seekBarStamina;
    TextView sommeFeatures;
    int lifeValue;
    int powerValue;
    int speedValue;
    int staminaValue;
    int total;
    int budget = 200;
    static String extraLife = "extraLife";
    static String extraStrength = "extraStrength";
    static String extraSpeed = "extraSpeed";
    static String extraStamina = "extraStamina";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_screen_features);

        seebbar();

        typeViewCreateScreenFeatures = (ImageView) findViewById(R.id.typeViewCreateScreenFeatures);
        nameViewCreateScreenFeatures = (TextView) findViewById(R.id.nameViewCreateScreenFeatures);
        typeTextCreateScreenFeatures = (TextView) findViewById(R.id.typeTextCreateScreenFeatures);
        typeNameString = getResources().getStringArray(R.array.typeNameString);
        validateButtonCreateScreenFeatures = (Button) findViewById(R.id.validateButtonCreateScreenFeatures);

        intentFromCreateScreen = getIntent();
        typeChoice = intentFromCreateScreen.getIntExtra(CreateScreen.extraType, 0);
        monsterName = intentFromCreateScreen.getStringExtra(CreateScreen.extraName);

        nameViewCreateScreenFeatures.setText(monsterName);
        switch (typeChoice) {
            case 1:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.turtle_0);
                break;

            case 2:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.fire_lion_0);
                break;

            case 3:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.thunder_eagle_0);
                break;

            case 4:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.rockilla_0);
                break;

            case 5:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.light_spirit_0);
                break;

            case 6:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.genie_0);
                break;

            case 7:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.metalsaur_0);
                break;

            case 8:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.panda_0);
                break;

            case 9:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.tyrannoking_0);
                break;

            case 10:
                typeViewCreateScreenFeatures.setImageResource(R.drawable.oeuf_inconnu);
                break;

        }
        typeTextCreateScreenFeatures.setText(typeNameString[typeChoice]);

        validateButtonCreateScreenFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total > budget){
                    Toast.makeText(getApplicationContext(), getString(R.string.too_much_point), Toast.LENGTH_SHORT).show();
                }

                else if (lifeValue != 0 && powerValue != 0 && speedValue != 0 && staminaValue != 0) {

                    Intent intentGoResume = new Intent(getApplicationContext(), ListScreen.class);
                    intentGoResume.putExtra(CreateScreen.extraName, monsterName);
                    intentGoResume.putExtra(CreateScreen.extraType, typeChoice);
                    intentGoResume.putExtra(extraLife, lifeValue);
                    intentGoResume.putExtra(extraStrength, powerValue);
                    intentGoResume.putExtra(extraSpeed, speedValue);
                    intentGoResume.putExtra(extraStamina, staminaValue);
                    startActivity(intentGoResume);
                }

                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.feature_toast), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void seebbar() {

        resultLife = (TextView) findViewById(R.id.resultLife);
        resultPower = (TextView) findViewById(R.id.resultPower);
        resultSpeed = (TextView) findViewById(R.id.resultSpeed);
        resultStamina = (TextView) findViewById(R.id.resultStamina);
        seekBarLife = (SeekBar) findViewById(R.id.seekBarLife);
        seekBarPower = (SeekBar) findViewById(R.id.seekBarPower);
        seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
        seekBarStamina = (SeekBar) findViewById(R.id.seekBarStamina);
        sommeFeatures = (TextView) findViewById(R.id.sommeFeatures);

        seekBarLife.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lifeValue = progress;
                resultLife.setText(" " + lifeValue + "/" + seekBarLife.getMax());
                totalDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                resultLife.setText(" " + lifeValue + "/" + seekBarLife.getMax());

            }
        });

        seekBarPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                powerValue = progress;
                resultPower.setText(" " + powerValue + "/" + seekBarPower.getMax());
                totalDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                resultPower.setText(" " + powerValue + "/" + seekBarPower.getMax());

            }
        });

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedValue = progress;
                resultSpeed.setText(" " + speedValue + "/" + seekBarSpeed.getMax());
                totalDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                resultSpeed.setText(" " + speedValue + "/" + seekBarSpeed.getMax());
            }
        });

        seekBarStamina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                staminaValue = progress;
                resultStamina.setText(" " + staminaValue + "/" + seekBarStamina.getMax());
                totalDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                resultStamina.setText(" " + staminaValue + "/" + seekBarStamina.getMax());

            }
        });
    }

    public void totalDisplay(){

        total = lifeValue + powerValue + speedValue + staminaValue;
        sommeFeatures.setText(total + " / " + budget);
        if (total > budget) {
            sommeFeatures.setTypeface(null, Typeface.BOLD);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sommeFeatures.setTextColor(getColor(R.color.red));
            }
        }
        else {
            sommeFeatures.setTypeface(null, Typeface.NORMAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sommeFeatures.setTextColor(getColor(R.color.black));
            }
        }
    }
}
