package com.example.camp_proj1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumofTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numofTabs) {
        super(fm);
        this.mNumofTabs = numofTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                Fragment1 tab1 = new Fragment1();
                return tab1;
            case 1 :
                Gallery tab2 = new Gallery();
                return tab2;
            case 2 :
                Fragment3 tab3 = new Fragment3();
                return tab3;
            default :
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumofTabs;
    }
}
