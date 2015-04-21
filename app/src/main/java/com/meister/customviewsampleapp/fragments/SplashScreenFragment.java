package com.meister.customviewsampleapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meister.customviewsampleapp.R;
import com.meister.customviewsampleapp.views.ProgressView;

/**
 * Splash screen shown when the application is started. We also start an loading async task that
 * is expected to initialize any longer loading components and publish progress information to this
 * view.
 * Created by mark.meister on 4/19/15.
 */
public class SplashScreenFragment extends BaseFragment {

    private ProgressView mProgressView;

    public static SplashScreenFragment newInstance() {
        final SplashScreenFragment fragment = new SplashScreenFragment();
        fragment.mName = SplashScreenFragment.class.getName();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mProgressView = (ProgressView) view.findViewById(R.id.progress_view);
    }

    public void updateProgress(int progress) {
        mProgressView.updateProgress(progress);
    }
}
