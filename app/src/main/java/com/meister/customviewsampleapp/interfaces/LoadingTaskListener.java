package com.meister.customviewsampleapp.interfaces;

/**
 * LoadingTaskListener used as a callback for the loading task to communicate with our activity.
 * Created by mark.meister on 4/19/15.
 */
public interface LoadingTaskListener {
    void loadingComplete();
    void loadingInterrupted();
}
