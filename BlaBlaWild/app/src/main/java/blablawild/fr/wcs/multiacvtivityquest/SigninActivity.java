package blablawild.fr.wcs.multiacvtivityquest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SigninActivity";

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private Button buttonSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignup = (Button) findViewById(R.id.buttonSignUp);
        buttonSignup.setOnClickListener(this);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonSignUp:
                Intent intentToSignupActivity = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intentToSignupActivity);
                finish();
                break;
            case R.id.buttonSignIn:
                if(editTextPassword.length() != 0 && editTextEmail.length() != 0) {
                    firebaseAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(SigninActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                /*Intent intentToAccountActivity = new Intent(SigninActivity.this, AccountActivity.class);
                                startActivity(intentToAccountActivity);*/
                                finish();
                            }

                        }
                    });
                }
                break;

        }
    }
}
