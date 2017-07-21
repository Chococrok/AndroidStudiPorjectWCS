package com.ab.wildchallenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by apprenti on 18/07/17.
 */

public class ProfileFragment extends ModelFragment implements View.OnClickListener, OnChoosenProfilePicture {

    public static final String URI_PICTURE_PROFILE = "pictureProfile";

    OnFragmentProfileInteractionListener mListener;

    private TextView textViewNickName;
    private TextView textViewEmail;
    private TextView textViewTeam;
    private ImageView imageViewProfilePicture;
    private Uri mUriPictureProfile;

    public static ProfileFragment newInstance(Uri uri) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        if (uri != null) {
            args.putString(URI_PICTURE_PROFILE, uri.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getString(URI_PICTURE_PROFILE) != null) {
            mUriPictureProfile = Uri.parse(getArguments().getString(URI_PICTURE_PROFILE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        textViewNickName = v.findViewById(R.id.textViewNickName);
        textViewEmail = v.findViewById(R.id.textViewEmail);
        textViewTeam = v.findViewById(R.id.textViewTeam);
        imageViewProfilePicture = v.findViewById(R.id.imageViewProfilePicture);
        if (mUriPictureProfile != null) {
            Glide.with(this).load(mUriPictureProfile).into(imageViewProfilePicture);
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(LogActivity.DATABASE_REF_USER).child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel currentUserData = dataSnapshot.getValue(UserModel.class);
                textViewNickName.setText(currentUserData.getNickName());
                textViewEmail.setText(currentUserData.getEmail());
                textViewTeam.setText(currentUserData.getTeam());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textViewNickName.setOnClickListener(this);
        textViewTeam.setOnClickListener(this);
        imageViewProfilePicture.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragment.OnFragmentSignUpInteractionListener) {
            mListener = (ProfileFragment.OnFragmentProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.textViewNickName:
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                final EditText input = new EditText(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                input.setSingleLine(true);
                input.setOnEditorActionListener(
                        new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    String inputString = input.getText().toString().trim();

                                    mListener.onFragmentProfileInteraction(inputString, null);
                                    textViewNickName.setText(inputString);
                                    alertDialog.dismiss();

                                    return true;

                                }
                                return false;
                            }
                        });


                alertDialog.setView(input);
                alertDialog.show();
                break;
            case R.id.textViewTeam:
                final AlertDialog alertDialogChoice = new AlertDialog.Builder(getActivity()).create();
                ListView listViewChoices = new ListView(getActivity());
                final CharSequence[] choices = new CharSequence[]{UserModel.RED, UserModel.BLUE, UserModel.YELLOW};
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, choices);
                listViewChoices.setAdapter(adapter);
                listViewChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        textViewTeam.setText(choices[i].toString());
                        mListener.onFragmentProfileInteraction(null, choices[i].toString().trim());
                        alertDialogChoice.dismiss();
                    }
                });
                alertDialogChoice.setView(listViewChoices);
                alertDialogChoice.show();
                break;
            case R.id.imageViewProfilePicture:
                mListener.onFragmentProfileInteraction(null, null);
                break;
        }
    }

    @Override
    public void onChoosenProfilePicture(Uri data) {
        Glide.with(this).load(data).into(imageViewProfilePicture);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentProfileInteractionListener {
        // TODO: Update argument type and name
        void onFragmentProfileInteraction(String nickName, String team);
    }
}
