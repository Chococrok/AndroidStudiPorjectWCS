package monster.fr.wcs.monsterwikilegend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.concurrent.Delayed;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CreateOrViewList extends AppCompatActivity  implements View.OnClickListener {

    ImageButton buttonCreate;
    ImageButton buttonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_view_list);

        buttonCreate = (ImageButton) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(this);
        buttonList = (ImageButton) findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

    }

    public void onClick(View v){

        switch (v.getId()){

            case R.id.buttonCreate:
                Intent intentGoCreateScreen = new Intent(this, CreateScreen.class);
                startActivity(intentGoCreateScreen);
                break;

            case R.id.buttonList:
                Intent intentGoListScreen = new Intent(this, ListScreen.class);
                startActivity(intentGoListScreen);
                break;

        }
    }


}
