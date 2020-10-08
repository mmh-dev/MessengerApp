package com.example.messengerapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.messengerapp.R;
import com.example.messengerapp.fragments.ChatsFragment;
import com.example.messengerapp.fragments.LoginFragment;
import com.example.messengerapp.fragments.PeopleFragment;
import com.example.messengerapp.fragments.ProfileFragment;
import com.example.messengerapp.fragments.RegisterFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterMain extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_main_1, R.string.tab_main_2, R.string.tab_main_3};
    private final Context mContext;

    public SectionsPagerAdapterMain(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new ChatsFragment();
                break;
            case 1:
                fragment = new PeopleFragment();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}