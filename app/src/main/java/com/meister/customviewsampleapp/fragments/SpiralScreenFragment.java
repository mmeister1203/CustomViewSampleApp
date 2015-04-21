package com.meister.customviewsampleapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meister.customviewsampleapp.R;

/**
 * Displays our spiral view. Basically is just a container for the SpiralView class.
 * Created by mark.meister on 4/19/15.
 */
public class SpiralScreenFragment extends BaseFragment {

    public static SpiralScreenFragment newInstance() {
        final SpiralScreenFragment fragment = new SpiralScreenFragment();
        fragment.mName = SpiralScreenFragment.class.getName();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spiral_fragment_layout, container, false);
    }
}
