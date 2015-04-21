package com.meister.customviewsampleapp.fragments;

import android.app.Fragment;

/**
 * Base fragment used for all fragment instances. Contains mName member which should be set during
 * instantiation of the fragment and set as the fragments tag during our transaction.
 * Created by mark.meister on 4/18/15.
 */
public class BaseFragment extends Fragment {
    protected String mName;

    public String getName() {
        return mName;
    }
}
