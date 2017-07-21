package com.ab.wildchallenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class RankingFragment extends ModelFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_ranking, container, false);

        TextView textViewListTitle = fragmentView.findViewById(R.id.textViewListTitle);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseListAdapter rankingAdapter;
        if (mParameter == MainActivity.RANKING_FRAGMENT_ALL) {
            textViewListTitle.setText("Classement des joueurs");
            rankingAdapter = new PlayerRankingAdapter(databaseReference.child(LogActivity.DATABASE_REF_USER).orderByChild(UserModel.CLICK_REF), UserModel.class, R.layout.rank_item, getActivity());
        }
        else{
            //TODO: change the way of requesting ranking by http request.
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Random random = new Random();
            ref.child("requestRanking").setValue(random.nextLong());

            textViewListTitle.setText("Classement des équipes");
            rankingAdapter = new TeamRankingAdapter(databaseReference.child(TeamModel.DATABASE_TEAM_REF).orderByChild(TeamModel.DATABASE_MEAN_SCORE_REF), TeamModel.class, R.layout.rank_item, getActivity());
        }
        ListView listView = fragmentView.findViewById(R.id.listViewRanking);
        listView.setAdapter(rankingAdapter);
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
