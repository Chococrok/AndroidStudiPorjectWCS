package com.ab.wildchallenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements OnChoosenProfilePicture {

    private OnFragmentSignUpInteractionListener mListener;
    private ImageView mImageViewProfilePicture;
    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        final EditText editTextNickName = v.findViewById(R.id.editTextNickName);
        final EditText editTextEmail = v.findViewById(R.id.editTextEmailSignUp);
        final EditText editTextPassword = v.findViewById(R.id.editTextPasswordSignUp);
        v.findViewById(R.id.buttonSingUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentSignUpInteraction(
                        LogActivity.ACTION_SIGN_UP,
                        editTextNickName.getText().toString(),
                        editTextEmail.getText().toString(),
                        editTextPassword.getText().toString());
            }
        });

        mImageViewProfilePicture = v.findViewById(R.id.imageViewProfilePicture);
        mImageViewProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentSignUpInteraction(
                        LogActivity.ACTION_UPDATE_PICTURE,
                        null,
                        null,
                        null);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSignUpInteractionListener) {
            mListener = (OnFragmentSignUpInteractionListener) context;
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
    public void onChoosenProfilePicture(Uri data) {
        Glide.with(this).load(data).into(mImageViewProfilePicture);

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
    public interface OnFragmentSignUpInteractionListener {
        // TODO: Update argument type and name
        void onFragmentSignUpInteraction(int action, String nickName, String email, String password);
    }
}
