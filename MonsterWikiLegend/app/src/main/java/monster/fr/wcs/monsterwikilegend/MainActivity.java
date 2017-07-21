package monster.fr.wcs.monsterwikilegend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewTapStart;
    ImageView imageViewLogo;
    Animation fadeInShort;
    Animation fadeInLong;
    Animation fadeOut;
    ProgressBar progressBar1;
    android.os.Handler handler;
    RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new android.os.Handler();
        textViewTapStart = (TextView) findViewById(R.id.textViewTapStart);
        imageViewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1) ;
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        fadeInShort = new AlphaAnimation(0,1);
        fadeInShort.setDuration(4000);
        fadeInLong = new AlphaAnimation(0,1);
        fadeInLong.setDuration(4000);
        fadeOut = new AlphaAnimation(1,0);
        fadeOut.setDuration(2000);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                imageViewLogo.setImageResource(R.drawable.entete);
                imageViewLogo.startAnimation(fadeInShort);

                progressBar1.startAnimation(fadeInShort);
                progressBar1.setVisibility(View.VISIBLE);

                textViewTapStart.setText(R.string.tapText);
                textViewTapStart.startAnimation(fadeInLong);

                activity_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentStart = new Intent(getApplicationContext(), CreateOrViewList.class);
                        startActivity(intentStart);
                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);



                    }
                });


            }
        };

        handler.postDelayed(runnable, 1000);


    }
}
