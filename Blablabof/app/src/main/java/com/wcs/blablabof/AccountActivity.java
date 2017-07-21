package com.wcs.blablabof;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.Permission;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MY_PERMISSION_REQUEST_READ_EXTERNALSTORAGE = 1;
    public static final int GET_PICTURE_PROFILE = 1;

    private FirebaseUser mUser;
    private StorageReference mStorageRef;

    private Button mButtonLogOut;
    private TextView mTextViewAccountUsername;
    private TextView mTextViewAccountEmail;
    private ImageView mImageViewimageViewAccountPictureProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mUser =FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("picture_profile");

        mTextViewAccountUsername = (TextView) findViewById(R.id.textViewAccountUsername);
        mTextViewAccountEmail = (TextView) findViewById(R.id.textViewAccountEmail);
        mButtonLogOut = (Button) findViewById(R.id.buttonLogOut);
        mImageViewimageViewAccountPictureProfile = (ImageView) findViewById(R.id.imageViewAccountPictureProfile);

        mTextViewAccountUsername.setOnClickListener(this);
        mButtonLogOut.setOnClickListener(this);
        mImageViewimageViewAccountPictureProfile.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null) {
            startActivity(new Intent(AccountActivity.this, SigninActivity.class));
        }
        else if (mTextViewAccountEmail.getText().toString().equals("TextView")){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
            mTextViewAccountUsername.setText(mUser.getDisplayName());
            mTextViewAccountEmail.setText(mUser.getEmail());
            mStorageRef.child(mUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(AccountActivity.this).load(task.getResult()).into(mImageViewimageViewAccountPictureProfile);
                        progressDialog.dismiss();
                    }
                    else{
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.textViewAccountUsername:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                final EditText input = new EditText(AccountActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                input.setSingleLine(true);
                input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(input.getText().toString())
                                    .build();
                            mUser.updateProfile(profileUpdates);
                            mTextViewAccountUsername.setText(input.getText().toString());
                            alertDialog.dismiss();

                        }
                        return false;
                    }
                });
                alertDialog.setView(input);
                alertDialog.show();
                break;
            case R.id.imageViewAccountPictureProfile:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNALSTORAGE);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_EXTERNALSTORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), GET_PICTURE_PROFILE);

                }
                return;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PICTURE_PROFILE && resultCode == RESULT_OK){
            Glide.with(this).load(data.getData()).into(mImageViewimageViewAccountPictureProfile);
            mStorageRef.child(mUser.getUid()).putFile(data.getData());
        }
    }
}
