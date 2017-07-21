package matterless.fr.wcs.matterless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    private ArrayList<String> mTutorialList;
    private TutoListAdapter mAdapter;
    private ListView mListViewTutorial;
    private Button mBackMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mBackMenuButton = (Button) findViewById(R.id.buttonBackMenuTuto);
        mTutorialList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tutorialArray)));
        mListViewTutorial = (ListView) findViewById(R.id.listViewTutorial);

        mAdapter = new TutoListAdapter(this, R.layout.text_view_tuto_item, mTutorialList);
        mListViewTutorial.setAdapter(mAdapter);

        mBackMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
