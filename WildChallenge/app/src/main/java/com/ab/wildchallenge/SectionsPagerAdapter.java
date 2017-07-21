package com.ab.wildchallenge;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by apprenti on 13/07/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm, String userId) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ModelFragment.newInstance(position);
            case 1:
                return ModelFragment.newInstance(position);
            case 2:
                return ModelFragment.newInstance(position);
            default:
                return ModelFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}