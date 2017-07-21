package com.ab.wildchallenge;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by apprenti on 13/07/17.
 */

public class ModelFragment extends Fragment {

    public static final String PARAMETER = "parameter";

    protected int mParameter;


    public static Fragment newInstance(int parameter) {
        Fragment fragment;
        switch (parameter){
            case MainActivity.CLIKER_FRAGMENT:
                fragment = new ClickerFragment();
                break;
            case MainActivity.RANKING_FRAGMENT_ALL:
                fragment = new RankingFragment();
                break;
            case MainActivity.RANKING_FRAGMENT_TEAM:
                fragment = new RankingFragment();
                break;
            default:
                fragment = new ClickerFragment();
        }
        Bundle args = new Bundle();
        args.putInt(PARAMETER, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParameter = getArguments().getInt(PARAMETER);
        }
    }

}
