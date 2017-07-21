package patrimoine.wcs.fr.toulousemonuments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static patrimoine.wcs.fr.toulousemonuments.R.id.imageView;

public class AddPhotos extends BaseFragment implements View.OnClickListener{

    //private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST=255;
    private static final int REQUEST_IMAGE_CAPTURE = 234;

    ImageView showPhoto;
    private Uri imageUri;
    private ImageButton buttonTakePicture;
    private ImageButton buttonSelectFromGallery;
    private ImageView buttonUpload, buttonRotate;
    private String mCurrentPhotoPath;
    private StorageReference mStorageRef;
    private EditText mEditTextLegende;


    private String uploadId;
    private String legend;
    private String urlSock;
    private String idUser;
    private String displayName;
    public Bitmap bitmapOrg;
    private static int position;
    private static MonumentModel monumentModel;



    private DatabaseReference mDatabase;

    public AddPhotos(int mPosition, MonumentModel mMonumentModel) {
        super(mPosition, mMonumentModel);
        position = mPosition;
        monumentModel = mMonumentModel;
    }

    public AddPhotos(){
        super (0,null);

    }

    public static AddPhotos newInstance() {
        AddPhotos fragment = new AddPhotos(position,monumentModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStorageRef= FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        View view = inflater.inflate(R.layout.fragment_add_photos, container, false);

        showPhoto = (ImageView) view.findViewById(imageView);
        buttonSelectFromGallery = (ImageButton) view.findViewById(R.id.galleryButton);
        buttonTakePicture=(ImageButton) view.findViewById(R.id.cameraButton);
        buttonUpload=(ImageView) view.findViewById(R.id.buttonUpload);
        buttonRotate=(ImageView) view.findViewById(R.id.rotateButton);
        mEditTextLegende = (EditText) view.findViewById(R.id.editsockName);
        uploadId = mDatabase.push().getKey();

        buttonTakePicture.setOnClickListener(this);
        buttonSelectFromGallery.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonRotate.setOnClickListener(this);

        buttonRotate.setClickable(false);


        return view;
    }



    private void openGallery() {
        Intent gallery = new Intent();

        gallery.setType("image/*");

        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    private void dispatchTakePictureIntent() {



        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /**Ensure there is a camera activity to handle the Intent*/
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            /** Create the file where the photo should go
             */
            File photoFile=null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //Error occurred while creating the File
            }
            // Continue only if the File was successfully created
          /*  if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(getActivity(),"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            }*/


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        buttonRotate.setClickable(true);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {

            imageUri = data.getData();

            try {
                bitmapOrg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                bitmapOrg = getResizedBitmap(bitmapOrg,300,300);
                showPhoto.setImageBitmap(bitmapOrg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imageUri = data.getData();
            Bundle extras = data.getExtras();

            bitmapOrg = (Bitmap) extras.get("data");
            bitmapOrg = getResizedBitmap(bitmapOrg,300,300);
            showPhoto.setImageBitmap(bitmapOrg);


            galleryAddPic();
            return;
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + sdf + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void uploadFile(){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Chargement en cours...");
        progressDialog.show();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference picRef = mStorageRef.child(Constants.STORAGE_PATH_UPLOADS + uploadId );
        UploadTask uploadTask = picRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Upload successfull", Toast.LENGTH_LONG);

                //adding an upload to firebase database

                urlSock = taskSnapshot.getDownloadUrl().toString();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded :" + (int) progress + "%");
            }
        });


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        float scale = Math.min(scaleHeight,scaleWidth);
        matrix.postScale(scale, scale);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap rotateBitmap(){
        Matrix matrix = new Matrix();
        matrix.setRotate(Constants.BITMAP_ROTATE);
        bitmapOrg = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(),bitmapOrg.getHeight(), matrix, true);
        showPhoto.setImageBitmap(bitmapOrg);
        return bitmapOrg;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v == buttonTakePicture) {
            if (checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        Constants.MY_REQUEST_CODE);
            }else{
                dispatchTakePictureIntent();
            }
        }
        if (v == buttonSelectFromGallery) {
            openGallery();
        }

        if(v==buttonRotate){
            rotateBitmap();
        }
       /* if (v == buttonUpload) {
            legend=mEditTextLegende.getText().toString().trim();
            mChaussette.setmLegende(legend);
            uploadFile();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.pick_category)
                    .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    mChaussette.setmCategory(Constants.DATABASE_PATH_CATEGORY_1);
                                    mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(Constants.DATABASE_PATH_CATEGORY_1).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(mChaussette);
                                    mDatabase.child(mChaussette.getmIdUser()).child(Constants.DATABASE_PATH_UPLOADS).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    break;
                                case 1:
                                    mChaussette.setmCategory(Constants.DATABASE_PATH_CATEGORY_2);
                                    mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(Constants.DATABASE_PATH_CATEGORY_2).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(mChaussette);
                                    mDatabase.child(mChaussette.getmIdUser()).child(Constants.DATABASE_PATH_UPLOADS).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    break;
                                case 2:
                                    mChaussette.setmCategory(Constants.DATABASE_PATH_CATEGORY_3);
                                    mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(Constants.DATABASE_PATH_CATEGORY_3).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(mChaussette);
                                    mDatabase.child(mChaussette.getmIdUser()).child(Constants.DATABASE_PATH_UPLOADS).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    break;
                                default:
                                    mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(mChaussette);
                                    mDatabase.child(mChaussette.getmIdUser()).child(Constants.DATABASE_PATH_UPLOADS).child(mChaussette.getmIdChaussette()).setValue(mChaussette);
                                    break;
                            }
                        }
                    });
            builder.show();
            mEditTextLegende.setEnabled(false);

        } */

    }



}
