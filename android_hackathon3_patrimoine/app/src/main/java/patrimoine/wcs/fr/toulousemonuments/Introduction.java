package patrimoine.wcs.fr.toulousemonuments;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Introduction extends AppCompatActivity implements View.OnTouchListener{

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        findViewById(R.id.constraintLayoutIntroduction).setOnTouchListener(this);
        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Introduction.this, MainActivity.class);
                startActivity(mainIntent);
                Introduction.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Intent mainIntent = new Intent(Introduction.this, MainActivity.class);
        startActivity(mainIntent);
        Introduction.this.finish();
        return false;
    }
}
