package com.ab.wildchallenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LogActivity extends AppCompatActivity implements SignInFragment.OnSignInFragmentInteractionListener,
        SignUpFragment.OnFragmentSignUpInteractionListener,
        ProfileFragment.OnFragmentProfileInteractionListener{

    public static final String TAG = "MainActivity";
    public static final String STORAGE_REF_PROFILE_PICTURE = "profile_picture";
    public static final String DATABASE_REF_USER = "user";
    public static final String DATABASE_REF_USER_NICKNAME = "nickName";
    public static final String DATABASE_REF_USER_TEAM = "team";
    public static final int ACTION_UPDATE_PICTURE = 0;
    public static final int ACTION_SIGN_UP = 1;
    public static final int MY_PERMISSION_REQUEST_READ_EXTERNALSTORAGE = 1;
    public static final int GET_PICTURE_PROFILE = 1;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutLogActivity, SignInFragment.newInstance()).commit();

            } else if (mFirebaseUser == null){
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                mStorageRef = FirebaseStorage.getInstance().getReference(STORAGE_REF_PROFILE_PICTURE).child(mFirebaseUser.getUid());
                mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_REF_USER).child(mFirebaseUser.getUid());

                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            ProfileFragment profileFragment = ProfileFragment.newInstance(task.getResult());
                            mListener = profileFragment;
                            transaction.replace(R.id.frameLayoutLogActivity, profileFragment, "prof").commit();
                        }
                        else{
                            ProfileFragment profileFragment = ProfileFragment.newInstance(null);
                            mListener = profileFragment;
                            transaction.replace(R.id.frameLayoutLogActivity,profileFragment).commit();
                        }
                    }
                });

            }
        }
    };
    private OnChoosenProfilePicture mListener;
    private Uri mUri;
    private LogginHandler mLogginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mLogginHandler = new LogginHandler();

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onSignInFragmentInteractionListener(String email, String password, boolean goSignUp) {
        if (goSignUp){
            SignUpFragment signUpFragment = SignUpFragment.newInstance();
            mListener = signUpFragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLogActivity, signUpFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
        if (!email.isEmpty() && ! password.isEmpty()) {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                startActivity(new Intent(LogActivity.this, MainActivity.class));
                                LogActivity.this.finish();
                            } else{
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                            }

                            // ...
                        }
                    });
        }
        else {
            Toast.makeText(this, "Entre ton email et ton mot de passe", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentSignUpInteraction(int action, final String nickName, String email, String password) {
        switch (action){
            case ACTION_UPDATE_PICTURE:
                updatePictureProfile();
                break;
            case ACTION_SIGN_UP:
                signUp(nickName, email, password);
                break;
        }

    }

    private void signUp(final String nickName, final String email, String password){
        if (!email.isEmpty() && ! password.isEmpty()) {
            mProgressDialog.show();
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
                                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                updateProfile(nickName);
                                UserModel userData;
                                if (nickName.isEmpty()){
                                    userData = new UserModel(0, UserModel.ANONYMOUS, email, UserModel.UNKNOWN);
                                } else {
                                    userData = new UserModel(0, nickName, email, UserModel.UNKNOWN);
                                }
                                mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_REF_USER).child(mFirebaseUser.getUid());
                                mDatabaseRef.setValue(userData);

                            } else{
                                Log.w(TAG, "signInWithEmail:failed ", task.getException());
                                mProgressDialog.dismiss();
                            }

                            // ...
                        }
                    });
        }
        else {
            Toast.makeText(this, "Entre ton email et ton mot de passe", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile(String nickName){
        if (nickName.isEmpty()) {
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(UserModel.ANONYMOUS)
                    .build();
            mFirebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mLogginHandler.sendEmptyMessage(0);
                    } else {
                        Log.w(TAG, "upadateProfile failed: ", task.getException());
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nickName)
                    .build();
            mFirebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mLogginHandler.sendEmptyMessage(0);
                    } else {
                        Log.w(TAG, "upadateProfile failed: ", task.getException());
                        mProgressDialog.dismiss();
                    }
                }
            });
            mLogginHandler.sendEmptyMessage(0);
        }
        if (mUri != null) {
            mStorageRef = FirebaseStorage.getInstance().getReference(STORAGE_REF_PROFILE_PICTURE).child(mFirebaseUser.getUid());
            mStorageRef.putFile(mUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mLogginHandler.sendEmptyMessage(0);
                    } else {
                        Log.w(TAG, "upadateProfile failed: ", task.getException());
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            mLogginHandler.sendEmptyMessage(0);
        }

    }

    private void updatePictureProfile(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNALSTORAGE);
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
            mUri = data.getData();
            if (mListener != null) {
                mListener.onChoosenProfilePicture(mUri);
            } else {
                Log.wtf(TAG, "The listener is null ??");
                if (getSupportFragmentManager().findFragmentByTag("prof") != null) {
                    Log.wtf(TAG, getSupportFragmentManager().findFragmentByTag("prof").toString());
                } else{
                    Log.wtf(TAG, "The fragment is null");
                }
            }
            if (mFirebaseUser != null){
                mStorageRef.putFile(mUri);
            }
        }
    }

    @Override
    public void onFragmentProfileInteraction(String nickName, String team) {
        if (nickName != null){
            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nickName)
                    .build();
            mFirebaseUser.updateProfile(changeRequest);
            mDatabaseRef.child(DATABASE_REF_USER_NICKNAME).setValue(nickName);
        } else if (team != null){
            mDatabaseRef.child(DATABASE_REF_USER_TEAM).setValue(team);
        } else{
            updatePictureProfile();
        }



    }

    private class LogginHandler extends Handler{
        boolean firstMessageReceived = false;

        @Override
        public void handleMessage(Message msg) {
            if (firstMessageReceived){
                LogActivity.this.finish();
                mProgressDialog.dismiss();
                return;
            }
            firstMessageReceived = true;
        }
    }

}


