package monster.fr.wcs.monsterwikilegend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListScreen extends AppCompatActivity {

    int typeChoice;
    String monsterName;
    int life;
    int strenght;
    int speed;
    int stamina;
    private static List<Monster> mMonsterList = null;;
    private ListView mMonsterListView= null;
    private ArrayAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        final Intent intentFromScreenFeatures = getIntent();
        monsterName = intentFromScreenFeatures.getStringExtra(CreateScreen.extraName);
        typeChoice = intentFromScreenFeatures.getIntExtra(CreateScreen.extraType, 0);
        life = intentFromScreenFeatures.getIntExtra(CreateScreenFeatures.extraLife, 0);
        strenght = intentFromScreenFeatures.getIntExtra(CreateScreenFeatures.extraStrength, 0);
        speed = intentFromScreenFeatures.getIntExtra(CreateScreenFeatures.extraSpeed, 0);
        stamina = intentFromScreenFeatures.getIntExtra(CreateScreenFeatures.extraStamina, 0);


        if (mMonsterList == null){
            mMonsterList = new ArrayList<Monster>();
        }

        Monster m = new Monster(this, monsterName, typeChoice, life, strenght, speed, stamina);

        if (monsterName != null) {


            this.mMonsterList.add(m);
        }
        this.mAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, mMonsterList);
        this.mMonsterListView = (ListView) findViewById(R.id.monsterList);
        this.mMonsterListView.setAdapter(mAdapter);




        Button toMainButton = (Button) findViewById(R.id.toMainButton);
        toMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent(getApplicationContext(), CreateOrViewList.class);
                startActivity(toMain);
            }
        });

        mMonsterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentGoViewMonster = new Intent(getApplicationContext(), ViewMonster.class);
                Monster mm = (Monster) mAdapter.getItem(position);
                intentGoViewMonster.putExtra("monster", mm);
                startActivity(intentGoViewMonster);
            }
        });

    }
}
