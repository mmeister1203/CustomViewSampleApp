package com.meister.customviewsampleapp.fragments;

import android.app.Fragment;

/**
 * Base fragment used for all fragment instances. Contains abstract method getName which must be
 * implemented by fragments that inherit from BaseFragment. This string is set as the fragments tag
 * during our fragment transaction.
 * Created by mark.meister on 4/18/15.
 */
public abstract class BaseFragment extends Fragment {
    public abstract String getName();
}
