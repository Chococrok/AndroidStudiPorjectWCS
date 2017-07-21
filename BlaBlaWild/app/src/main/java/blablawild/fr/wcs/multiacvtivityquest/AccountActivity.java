package blablawild.fr.wcs.multiacvtivityquest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    public static final String TAG = "AccountActivity";
    static final int PICK_CONTACT_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    private Button buttonLogOut;
    private Button buttonEditProfile;
    private TextView textViewUserName;
    private TextView textViewEmail;
    private ImageView imageViewProfilPicture;
    private EditText editTextUserName;
    private EditText editTextProfilePicture;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        this.buttonLogOut = (Button) findViewById(R.id.buttonLogOut) ;
        this.buttonLogOut.setOnClickListener(this);
        this.buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);
        this.buttonEditProfile.setOnClickListener(this);
        this.textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        this.textViewUserName = (TextView) findViewById(R.id.textViewUserName);
        this.imageViewProfilPicture = (ImageView) findViewById(R.id.imageViewProfilPicture);
        this.editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        this.editTextUserName.setOnEditorActionListener(this);
        this.editTextProfilePicture = (EditText) findViewById(R.id.editTextPictureProfile);
        this.editTextProfilePicture.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog));


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mFirebaseUser = firebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {

                    mStorageRef = FirebaseStorage.getInstance().getReference(mFirebaseUser.getUid()).child("avatar");
                    textViewUserName.setText(mFirebaseUser.getDisplayName());
                    textViewEmail.setText(mFirebaseUser.getEmail());
                    donwloadImage();

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
                } else {

                    Intent intentToSignInActivity = new Intent(AccountActivity.this, SigninActivity.class);
                    startActivity(intentToSignInActivity);
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.show();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogOut:
                mAuth.signOut();
                break;

            case R.id.buttonEditProfile:
                this.buttonLogOut.setVisibility(View.GONE);
                this.buttonEditProfile.setVisibility(View.GONE);
                this.editTextUserName.setVisibility(View.VISIBLE);
                this.editTextProfilePicture.setVisibility(View.VISIBLE);
                break;

            case R.id.editTextPictureProfile:

                progressDialog.show();

                ActivityCompat.requestPermissions(AccountActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {

            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(editTextUserName.getText().toString()).build();
            mFirebaseUser.updateProfile(userProfileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                textViewUserName.setText(mFirebaseUser.getDisplayName());

                                AccountActivity.this.buttonLogOut.setVisibility(View.VISIBLE);
                                AccountActivity.this.buttonEditProfile.setVisibility(View.VISIBLE);
                                AccountActivity.this.editTextUserName.setVisibility(View.GONE);
                                AccountActivity.this.editTextProfilePicture.setVisibility(View.GONE);
                            }
                            else{
                                Log.d(TAG, "User profile failed.");
                            }
                        }
                    });


        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == AccountActivity.RESULT_OK){


            mStorageRef.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                        textViewUserName.setText(mFirebaseUser.getDisplayName());

                        AccountActivity.this.buttonLogOut.setVisibility(View.VISIBLE);
                        AccountActivity.this.buttonEditProfile.setVisibility(View.VISIBLE);
                        AccountActivity.this.editTextUserName.setVisibility(View.GONE);
                        AccountActivity.this.editTextProfilePicture.setVisibility(View.GONE);
                        donwloadImage();

                    }
                    else{
                        Log.d(TAG, "User profile failed.");
                    }

                }
            });

        }
    }

    public void donwloadImage(){

        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso.with(AccountActivity.this).load(uri).into(imageViewProfilPicture);
                            Glide.with(AccountActivity.this)
                                    .load(uri)
                                    .into(imageViewProfilPicture);
                progressDialog.cancel();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, PICK_CONTACT_REQUEST);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
