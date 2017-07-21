package monster.fr.wcs.monsterwikilegend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewMonster extends AppCompatActivity {


    TextView nameViewMonsterView;
    TextView lifeOverView;
    TextView powerOverView;
    TextView speedOverView;
    TextView staminaOverView;
    ImageView typeViewMonsterView;
    TextView typeTextMonsterView;
    String[] typeNameString;
    Button evolve;
    Monster m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monster);

        Intent intentFromList = getIntent();
        m = intentFromList.getParcelableExtra("monster");


        lifeOverView = (TextView) findViewById(R.id.lifeOverView);
        powerOverView = (TextView) findViewById(R.id.powerOverView);
        speedOverView = (TextView) findViewById(R.id.speedOverView);
        staminaOverView = (TextView) findViewById(R.id.staminaOverview);
        nameViewMonsterView = (TextView) findViewById(R.id.nameViewMonsterView);
        typeViewMonsterView = (ImageView) findViewById(R.id.typeViewMonsterView);
        typeTextMonsterView = (TextView) findViewById(R.id.typeTextMonsterView);
        typeNameString = getResources().getStringArray(R.array.typeNameString);
        evolve = (Button) findViewById(R.id.evolve);


        lifeOverView.setText(Integer.toString(m.getmLife()));
        powerOverView.setText(Integer.toString(m.getmStrenght()));
        speedOverView.setText(Integer.toString(m.getmSpeed()));
        staminaOverView.setText(Integer.toString(m.getmStamina()));

        nameViewMonsterView.setText(m.getmMonsterName());
        typeTextMonsterView.setText(typeNameString[m.getmTypeChoice()]);

        if (m.getmGrowState() < 3) {
            evolve.setText(R.string.evolve);
        }
        else{
            evolve.setText(R.string.back_egg);
        }

        evolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m.getmGrowState() == 2) {
                    evolve.setText(R.string.back_egg);
                }
                else{
                    evolve.setText(R.string.evolve);
                }

                m.grow();

                switch (m.getmGrowState()) {
                    case 0:
                        switch (m.getmTypeChoice()) {
                            case 1:
                                typeViewMonsterView.setImageResource(R.drawable.turtle_0);
                                break;

                            case 2:
                                typeViewMonsterView.setImageResource(R.drawable.fire_lion_0);
                                break;

                            case 3:
                                typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_0);
                                break;

                            case 4:
                                typeViewMonsterView.setImageResource(R.drawable.rockilla_0);
                                break;

                            case 5:
                                typeViewMonsterView.setImageResource(R.drawable.light_spirit_0);
                                break;

                            case 6:
                                typeViewMonsterView.setImageResource(R.drawable.genie_0);
                                break;

                            case 7:
                                typeViewMonsterView.setImageResource(R.drawable.metalsaur_0);
                                break;

                            case 8:
                                typeViewMonsterView.setImageResource(R.drawable.panda_0);
                                break;

                            case 9:
                                typeViewMonsterView.setImageResource(R.drawable.tyrannoking_0);
                                break;

                            case 10:
                                typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                                break;

                        }
                        break;

                    case 1:
                        switch (m.getmTypeChoice()) {
                            case 1:
                                typeViewMonsterView.setImageResource(R.drawable.turtle_1);
                                break;

                            case 2:
                                typeViewMonsterView.setImageResource(R.drawable.fire_lion_1);
                                break;

                            case 3:
                                typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_1);
                                break;

                            case 4:
                                typeViewMonsterView.setImageResource(R.drawable.rockilla_1);
                                break;

                            case 5:
                                typeViewMonsterView.setImageResource(R.drawable.light_spirit_1);
                                break;

                            case 6:
                                typeViewMonsterView.setImageResource(R.drawable.genie_1);
                                break;

                            case 7:
                                typeViewMonsterView.setImageResource(R.drawable.metalsaur_1);
                                break;

                            case 8:
                                typeViewMonsterView.setImageResource(R.drawable.panda_1);
                                break;

                            case 9:
                                typeViewMonsterView.setImageResource(R.drawable.tyrannoking_1);
                                break;

                            case 10:
                                typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                                break;

                        }
                        break;

                    case 2:
                        switch (m.getmTypeChoice()) {
                            case 1:
                                typeViewMonsterView.setImageResource(R.drawable.turtle_2);
                                break;

                            case 2:
                                typeViewMonsterView.setImageResource(R.drawable.fire_lion_2);
                                break;

                            case 3:
                                typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_2);
                                break;

                            case 4:
                                typeViewMonsterView.setImageResource(R.drawable.rockilla_2);
                                break;

                            case 5:
                                typeViewMonsterView.setImageResource(R.drawable.light_spirit_2);
                                break;

                            case 6:
                                typeViewMonsterView.setImageResource(R.drawable.genie_2);
                                break;

                            case 7:
                                typeViewMonsterView.setImageResource(R.drawable.metalsaur_2);
                                break;

                            case 8:
                                typeViewMonsterView.setImageResource(R.drawable.panda_2);
                                break;

                            case 9:
                                typeViewMonsterView.setImageResource(R.drawable.tyrannoking_2);
                                break;

                            case 10:
                                typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                                break;

                        }
                        break;

                    case 3:
                        switch (m.getmTypeChoice()) {
                            case 1:
                                typeViewMonsterView.setImageResource(R.drawable.turtle_3);
                                break;

                            case 2:
                                typeViewMonsterView.setImageResource(R.drawable.fire_lion_3);
                                break;

                            case 3:
                                typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_3);
                                break;

                            case 4:
                                typeViewMonsterView.setImageResource(R.drawable.rockilla_3);
                                break;

                            case 5:
                                typeViewMonsterView.setImageResource(R.drawable.light_spirit_3);
                                break;

                            case 6:
                                typeViewMonsterView.setImageResource(R.drawable.genie_3);
                                break;

                            case 7:
                                typeViewMonsterView.setImageResource(R.drawable.metalsaur_3);
                                break;

                            case 8:
                                typeViewMonsterView.setImageResource(R.drawable.panda_3);
                                break;

                            case 9:
                                typeViewMonsterView.setImageResource(R.drawable.tyrannoking_3);
                                break;

                            case 10:
                                typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                                break;

                        }
                        break;
                }

            }
        });

        switch (m.getmGrowState()) {
            case 0:
                switch (m.getmTypeChoice()) {
                    case 1:
                        typeViewMonsterView.setImageResource(R.drawable.turtle_0);
                        break;

                    case 2:
                        typeViewMonsterView.setImageResource(R.drawable.fire_lion_0);
                        break;

                    case 3:
                        typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_0);
                        break;

                    case 4:
                        typeViewMonsterView.setImageResource(R.drawable.rockilla_0);
                        break;

                    case 5:
                        typeViewMonsterView.setImageResource(R.drawable.light_spirit_0);
                        break;

                    case 6:
                        typeViewMonsterView.setImageResource(R.drawable.genie_0);
                        break;

                    case 7:
                        typeViewMonsterView.setImageResource(R.drawable.metalsaur_0);
                        break;

                    case 8:
                        typeViewMonsterView.setImageResource(R.drawable.panda_0);
                        break;

                    case 9:
                        typeViewMonsterView.setImageResource(R.drawable.tyrannoking_0);
                        break;

                    case 10:
                        typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                        break;

                }
                break;

            case 1:
                switch (m.getmTypeChoice()) {
                    case 1:
                        typeViewMonsterView.setImageResource(R.drawable.turtle_1);
                        break;

                    case 2:
                        typeViewMonsterView.setImageResource(R.drawable.fire_lion_1);
                        break;

                    case 3:
                        typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_1);
                        break;

                    case 4:
                        typeViewMonsterView.setImageResource(R.drawable.rockilla_1);
                        break;

                    case 5:
                        typeViewMonsterView.setImageResource(R.drawable.light_spirit_1);
                        break;

                    case 6:
                        typeViewMonsterView.setImageResource(R.drawable.genie_1);
                        break;

                    case 7:
                        typeViewMonsterView.setImageResource(R.drawable.metalsaur_1);
                        break;

                    case 8:
                        typeViewMonsterView.setImageResource(R.drawable.panda_1);
                        break;

                    case 9:
                        typeViewMonsterView.setImageResource(R.drawable.tyrannoking_1);
                        break;

                    case 10:
                        typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                        break;

                }
                break;

            case 2:
                switch (m.getmTypeChoice()) {
                    case 1:
                        typeViewMonsterView.setImageResource(R.drawable.turtle_2);
                        break;

                    case 2:
                        typeViewMonsterView.setImageResource(R.drawable.fire_lion_2);
                        break;

                    case 3:
                        typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_2);
                        break;

                    case 4:
                        typeViewMonsterView.setImageResource(R.drawable.rockilla_2);
                        break;

                    case 5:
                        typeViewMonsterView.setImageResource(R.drawable.light_spirit_2);
                        break;

                    case 6:
                        typeViewMonsterView.setImageResource(R.drawable.genie_2);
                        break;

                    case 7:
                        typeViewMonsterView.setImageResource(R.drawable.metalsaur_2);
                        break;

                    case 8:
                        typeViewMonsterView.setImageResource(R.drawable.panda_2);
                        break;

                    case 9:
                        typeViewMonsterView.setImageResource(R.drawable.tyrannoking_2);
                        break;

                    case 10:
                        typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                        break;

                }
                break;

            case 3:
                switch (m.getmTypeChoice()) {
                    case 1:
                        typeViewMonsterView.setImageResource(R.drawable.turtle_3);
                        break;

                    case 2:
                        typeViewMonsterView.setImageResource(R.drawable.fire_lion_3);
                        break;

                    case 3:
                        typeViewMonsterView.setImageResource(R.drawable.thunder_eagle_3);
                        break;

                    case 4:
                        typeViewMonsterView.setImageResource(R.drawable.rockilla_3);
                        break;

                    case 5:
                        typeViewMonsterView.setImageResource(R.drawable.light_spirit_3);
                        break;

                    case 6:
                        typeViewMonsterView.setImageResource(R.drawable.genie_3);
                        break;

                    case 7:
                        typeViewMonsterView.setImageResource(R.drawable.metalsaur_3);
                        break;

                    case 8:
                        typeViewMonsterView.setImageResource(R.drawable.panda_3);
                        break;

                    case 9:
                        typeViewMonsterView.setImageResource(R.drawable.tyrannoking_3);
                        break;

                    case 10:
                        typeViewMonsterView.setImageResource(R.drawable.oeuf_inconnu);
                        break;

                }
                break;
        }


    }
}
