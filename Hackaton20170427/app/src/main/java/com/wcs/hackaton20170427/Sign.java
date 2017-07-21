package com.wcs.hackaton20170427;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign extends AppCompatActivity implements View.OnClickListener{
    private EditText inputEmail;
    private EditText inputPassword;
    private FirebaseAuth auth;
    private Button btnSignup;
    private Button btnLogin;
    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        //Ici est instancié l'authentification via FB
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Sign.this, MainActivity.class));
            finish();
        }



        inputEmail = (EditText) findViewById(R.id.editTextEmailLogin);
        inputPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        btnSignup = (Button) findViewById(R.id.buttonInscription);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

        progressDialog = new ProgressDialog(this);

        //On rattache les bouttons au listener
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

    }

    //Création de la méthode de l'utilisateur
    private void userLogin() {
        String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        //On check si email et mot de passe sont renseignés
        if(TextUtils.isEmpty(email)){
            Toast.makeText(Sign.this,"Renseigne ton email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(Sign.this,"Renseigne ton mot de passe",Toast.LENGTH_LONG).show();
            return;
        }

        //Si email et mots de passe sont renseignés alors apparait la progressDialog
        progressDialog.setMessage("Accès au compte...");
        progressDialog.show();


        // Authentification de l'utilisateur déjà enregistré
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Sign.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        //si la tâche est OK
                        if(task.isSuccessful()){
                            //On est renvoyé à la AccountActivity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ChooseEmplacement.class));

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            userLogin();
        }

        if (view == btnSignup) {
            finish();
            startActivity(new Intent(Sign.this, CreateAccount.class));
        }

    }
}