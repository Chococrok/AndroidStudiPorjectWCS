package com.ab.wildchallenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignInFragment extends Fragment {
    private OnSignInFragmentInteractionListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
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
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        final EditText editTextEmail = v.findViewById(R.id.editTextEmailSignIn);
        final EditText editTextPassword = v.findViewById(R.id.editTextPasswordSignIn);
        v.findViewById(R.id.buttonSingIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSignInFragmentInteractionListener(editTextEmail.getText().toString(), editTextPassword.getText().toString(), false);
            }
        });
        v.findViewById(R.id.textViewCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSignInFragmentInteractionListener(null, null, true);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignInFragmentInteractionListener) {
            mListener = (OnSignInFragmentInteractionListener) context;
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
    public interface OnSignInFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignInFragmentInteractionListener(String email, String password, boolean goSignUp);
    }
}
