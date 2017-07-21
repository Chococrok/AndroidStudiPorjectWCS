package com.wcs.blablabof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mFirebaseAuth;

    private TextView mTextViewToSighupActivity;
    private EditText mEditTextSignInEmail;
    private EditText mEditTextSignInPassword;
    private Button mButtonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEditTextSignInEmail = (EditText) findViewById(R.id.editTextSignInEmail);
        mEditTextSignInPassword = (EditText) findViewById(R.id.editTextSignInPassword);

        mButtonSignIn = (Button) findViewById(R.id.buttonSignIn);
        mButtonSignIn.setOnClickListener(this);

        mTextViewToSighupActivity = (TextView) findViewById(R.id.textViewToSighupActivity);
        mTextViewToSighupActivity.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewToSighupActivity:
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
                finish();
                break;
            case R.id.buttonSignIn:
                if (mEditTextSignInEmail.length() != 0 && mEditTextSignInPassword.length() != 0){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(getString(R.string.loading));
                    progressDialog.show();
                    mFirebaseAuth.signInWithEmailAndPassword(mEditTextSignInEmail.getText().toString(),
                            mEditTextSignInPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                SigninActivity.this.finish();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(SigninActivity.this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        }
    }
}
