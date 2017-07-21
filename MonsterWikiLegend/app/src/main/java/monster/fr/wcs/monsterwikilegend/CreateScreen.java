package monster.fr.wcs.monsterwikilegend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateScreen extends AppCompatActivity implements View.OnClickListener {

    EditText nameEdit;
    ImageView typeViewCreateScreen;
    int typeChoice;
    ImageButton eau;
    ImageButton feu;
    ImageButton foudre;
    ImageButton terre;
    ImageButton lumiere;
    ImageButton magie;
    ImageButton metal;
    ImageButton nature;
    ImageButton mort;
    ImageButton special;
    static String extraType = "extraType";
    static String extraName = "extraName";
    TextView typeTextCreateScreen;
    String[] typeNameString;

    Button validateCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_screen);

        nameEdit = (EditText) findViewById(R.id.nameEdit);
        typeViewCreateScreen = (ImageView) findViewById(R.id.typeViewCreateScreen);
        typeTextCreateScreen = (TextView) findViewById(R.id.typeTextCreateScreen);
        eau = (ImageButton) findViewById(R.id.button_eau);
        eau.setOnClickListener(this);
        feu = (ImageButton) findViewById(R.id.button_feu);
        feu.setOnClickListener(this);
        foudre = (ImageButton) findViewById(R.id.button_foudre);
        foudre.setOnClickListener(this);
        terre = (ImageButton) findViewById(R.id.button_terre);
        terre.setOnClickListener(this);
        lumiere = (ImageButton) findViewById(R.id.button_lumiere);
        lumiere.setOnClickListener(this);
        magie = (ImageButton) findViewById(R.id.button_magie);
        magie.setOnClickListener(this);
        metal = (ImageButton) findViewById(R.id.button_metal);
        metal.setOnClickListener(this);
        nature = (ImageButton) findViewById(R.id.button_nature);
        nature.setOnClickListener(this);
        mort = (ImageButton) findViewById(R.id.button_mort);
        mort.setOnClickListener(this);
        special = (ImageButton) findViewById(R.id.button_special);
        special.setOnClickListener(this);

        validateCreation = (Button) findViewById(R.id.validateCreation);
        validateCreation.setOnClickListener(this);

        typeChoice = 0;
        typeNameString = getResources().getStringArray(R.array.typeNameString);

    }

    public void onClick(View v){

        switch (v.getId()) {
            case R.id.button_eau:
                typeViewCreateScreen.setImageResource(R.drawable.turtle_0);
                typeChoice = 1;
                break;

            case R.id.button_feu:
                typeViewCreateScreen.setImageResource(R.drawable.fire_lion_0);
                typeChoice = 2;
                break;

            case R.id.button_foudre:
                typeViewCreateScreen.setImageResource(R.drawable.thunder_eagle_0);
                typeChoice = 3;
                break;

            case R.id.button_terre:
                typeViewCreateScreen.setImageResource(R.drawable.rockilla_0);
                typeChoice = 4;
                break;

            case R.id.button_lumiere:
                typeViewCreateScreen.setImageResource(R.drawable.light_spirit_0);
                typeChoice = 5;
                break;

            case R.id.button_magie:
                typeViewCreateScreen.setImageResource(R.drawable.genie_0);
                typeChoice = 6;
                break;

            case R.id.button_metal:
                typeViewCreateScreen.setImageResource(R.drawable.metalsaur_0);
                typeChoice = 7;
                break;

            case R.id.button_nature:
                typeViewCreateScreen.setImageResource(R.drawable.panda_0);
                typeChoice = 8;
                break;

            case R.id.button_mort:
                typeViewCreateScreen.setImageResource(R.drawable.tyrannoking_0);
                typeChoice = 9;
                break;

            case R.id.button_special:
                typeViewCreateScreen.setImageResource(R.drawable.oeuf_inconnu);
                typeChoice = 10;
                break;

            case R.id.validateCreation:
                if (typeChoice > 0 && nameEdit.length() != 0){
                    Intent intentGoFeatures = new Intent(this, CreateScreenFeatures.class);
                    intentGoFeatures.putExtra(extraType, typeChoice);
                    intentGoFeatures.putExtra(extraName, nameEdit.getText().toString());
                    startActivity(intentGoFeatures);
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.type_toast), Toast.LENGTH_SHORT).show();
                }
                break;


        }
        typeTextCreateScreen.setText(typeNameString[typeChoice]);


    }
}
