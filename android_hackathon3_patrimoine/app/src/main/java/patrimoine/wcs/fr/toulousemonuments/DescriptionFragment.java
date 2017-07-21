package patrimoine.wcs.fr.toulousemonuments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import patrimoine.wcs.fr.toulousemonuments.models.Fields;
import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;


public class DescriptionFragment extends BaseFragment {

    private static final String SCORE = "score";
    private static final String FILENAME = "myfile";
    private static final String ID = "id";

    private DatabaseReference mDatabaseReference;

    private TextView textViewTitle;
    private ImageView imageViewDescriptionMain;
    private TextView textViewDescription;
    private TextView textViewScore;
    private ImageView mImageButtonRating;

    private Integer mId = null;
    private ArrayList<Integer> mLike = new ArrayList<>();



    public DescriptionFragment(int position, MonumentModel model) {
        super(position, model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if(container == null){
            return null;
        }

        View v = inflater.inflate(R.layout.fragment_desciption, container, false);

        final Fields currentFields = mMonumentModel.getRecords().get(mPosition).getFields();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(currentFields.getNomCdt());
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mId = sharedPref.getInt(FILENAME, -1);


        textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
        imageViewDescriptionMain = (ImageView) v.findViewById(R.id.imageViewDescriptionMain);
        textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        textViewDescription.setMovementMethod(new ScrollingMovementMethod());
        textViewScore = (TextView) v.findViewById(R.id.textViewScore);
        mImageButtonRating = (ImageView) v.findViewById(R.id.imageButtonRating);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    textViewScore.setText(String.valueOf(dataSnapshot.child(SCORE).getValue(Integer.class)));

                    for (DataSnapshot data : dataSnapshot.child(ID).getChildren()){
                        mLike.add(data.getValue(Integer.class));
                        if (data.getValue(Integer.class).equals(mId)){
                            Glide.with(container).load(R.drawable.fullheart).into(mImageButtonRating);
                        }
                    }
                }
                else{
                    textViewScore.setText(String.valueOf(0));
                }

                if (mId.equals(-1)){
                    Random random = new Random();
                    mId = random.nextInt();
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(FILENAME, mId);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mImageButtonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference child = mDatabaseReference.child(ID);
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null){
                            child.push().setValue(mId);
                            mDatabaseReference.child(SCORE).runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    if (mutableData.getValue() == null) {
                                        mutableData.setValue(1);
                                        return Transaction.success(mutableData);
                                    }
                                    int value = mutableData.getValue(Integer.class);
                                    mutableData.setValue(value + 1);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                }
                            });
                        }
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if (!data.getValue(Integer.class).equals(mId)){
                                child.push().setValue(mId);
                                mDatabaseReference.child(SCORE).runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        if (mutableData.getValue() == null) {
                                            mutableData.setValue(1);
                                            return Transaction.success(mutableData);
                                        }
                                        int value = mutableData.getValue(Integer.class);
                                        mutableData.setValue(value + 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        textViewTitle.setText(currentFields.getNom());
        textViewDescription.setText(currentFields.getDescriptif());

        new ImageDownloadTask(imageViewDescriptionMain).execute(currentFields.getNomCdt());
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            AssetManager assetManager = getContext().getAssets();
            try {
                InputStream inputStream = assetManager.open( params[0] + ".jpg", AssetManager.ACCESS_BUFFER);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
        public ImageDownloadTask(ImageView iv){
            imageViewDescriptionMain = iv;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageViewDescriptionMain.setImageBitmap(bitmap);
        }
    }
}
