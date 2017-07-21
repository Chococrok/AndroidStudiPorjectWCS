package blablawild.fr.wcs.multiacvtivityquest;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener {

    public static final String TAG = "SignupActivity";
    static final int PICK_CONTACT_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Uri muri;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonConnexion;
    private EditText editTextUserName;
    private EditText editTextimage_URL;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        this.editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        this.buttonConnexion = (Button) findViewById(R.id.buttonConnexionSignupActivity);
        this.buttonConnexion.setOnClickListener(this);
        this.editTextUserName= (EditText) findViewById(R.id.editTextUserName);
        this.editTextimage_URL = (EditText) findViewById(R.id.editTextURL);
        this.editTextimage_URL.setOnClickListener(this);

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage(getResources().getString(R.string.progress_dialog));



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonConnexionSignupActivity:
                if(editTextPassword.length() >= 6 && editTextEmail.length() != 0) {

                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {

                                    } else {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(editTextUserName.getText().toString())
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "User profile updated.");
                                                        }
                                                    }
                                                });

                                        mStorageRef = FirebaseStorage.getInstance().getReference(user.getUid()).child("avatar");
                                        mStorageRef.putFile(muri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                progressDialog.cancel();

                                                finish();

                                            }
                                        });
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignupActivity.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextURL:

                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == AccountActivity.RESULT_OK){

            muri = data.getData();
            SignupActivity.this.editTextimage_URL.setHint(getText(R.string.image_choosen));

        }
    }
}
