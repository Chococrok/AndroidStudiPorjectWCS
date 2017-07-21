package com.wcs.hackaton20170427;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private TextView textViewSignIn;
    private RadioGroup radioGroup;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //On instancie ici l'authentification avec FB
        auth = FirebaseAuth.getInstance();


        btnSignUp = (Button) findViewById(R.id.buttonRegister);
        inputEmail = (EditText) findViewById(R.id.editTextEmailUser);
        inputPassword = (EditText) findViewById(R.id.editTextPasswordUser);

        //progress dialog

        progressDialog = new ProgressDialog(this);

        // On rattache le listener au boutton
        btnSignUp.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

    }


    //On récupère les mots de passes etemails des edittexts
    private void registerUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        //String ecole = radioGroup.toString().getCheckedRadioButtonId();

        //emails et mots de passes sont renseignes
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Renseigne ton email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Renseigne ton mot de passe", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Inscription en cours...");
        progressDialog.show();


        //Creation d'un user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //si la tâche est ok
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest profilUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName)
                                    .build();
                            user.updateProfile(profilUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                    startActivity(new Intent(CreateAccount.this, ChooseEmplacement.class));
                                }
                            });

                            //l'activity sign up démarre
                            finish();
                            startActivity(new Intent(CreateAccount.this, ChooseEmplacement.class));

                        } else {
                            //sinon le message d'erreur s'affiche
                            Toast.makeText(CreateAccount.this, "Email et mot de passe requis", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                });

    }

    public void onClick(View view) {

        if (view == btnSignUp) {
            registerUser();
        }

        if (view == textViewSignIn) {
            //On ira à Login Activity si l'utilisateur est déjà enregistré
            startActivity(new Intent(CreateAccount.this, ChooseEmplacement.class));
        }


    }
}

