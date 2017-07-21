package com.wcs.blablabof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private EditText mEditTextSignUpNickName;
    private EditText mEditTextSignUpEmail;
    private EditText mEditTextSignUpPassword;
    private Button mButtonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEditTextSignUpNickName = (EditText) findViewById(R.id.editTextSignUpNickName);
        mEditTextSignUpEmail = (EditText) findViewById(R.id.editTextSIgnUpEmail);
        mEditTextSignUpPassword = (EditText) findViewById(R.id.editTextSignUpPassWord);
        mButtonSignUp = (Button) findViewById(R.id.buttonSignup);
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditTextSignUpNickName.length() != 0
                        && mEditTextSignUpEmail.length() != 0
                        && mEditTextSignUpPassword.length() != 0){
                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setMessage(getString(R.string.loading));
                    progressDialog.show();
                    mFirebaseAuth.createUserWithEmailAndPassword(mEditTextSignUpEmail.getText().toString(),
                            mEditTextSignUpPassword.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser newUser = mFirebaseAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(mEditTextSignUpNickName.getText().toString())
                                        .build();
                                newUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        SignupActivity.this.finish();
                                    }
                                });
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this, R.string.toast_sign_up_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser != null) {
            finish();
        }
    }
}
