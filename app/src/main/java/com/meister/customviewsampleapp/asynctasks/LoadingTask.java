package com.meister.customviewsampleapp.asynctasks;

import android.os.AsyncTask;

import com.meister.customviewsampleapp.fragments.SplashScreenFragment;
import com.meister.customviewsampleapp.interfaces.LoadingTaskListener;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Loading AsyncTask. This is started when we show our splash screen when the user opens the app.
 * This task would responsible for initializing anything needed by our application, but in this case
 * is really only used to publish progress to our custom progress view.
 * Created by mark.meister on 4/19/15.
 */
public class LoadingTask extends AsyncTask<Void, Integer, Boolean> {
    private WeakReference<SplashScreenFragment> mSplashFragment;
    private WeakReference<LoadingTaskListener> mCallbacks;

    private Random mRandom;

    public LoadingTask(SplashScreenFragment splashScreenFragment, LoadingTaskListener loadingTaskListener) {
        mSplashFragment = new WeakReference<>(splashScreenFragment);
        mCallbacks = new WeakReference<>(loadingTaskListener);

        mRandom = new Random();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        mSplashFragment.get().updateProgress(progress[0]);
    }

    // We're really just creating some way to create progress and publish it periodically here.
    @Override
    protected Boolean doInBackground(Void... params) {
        int progress = 0;

        while (progress < 100) {
            // Since an AsyncTasks lifecycle is not tied to the Activity's, we cancel the async task
            // when the activity is destroyed, then we can test for this and stop processing.
            if (isCancelled()) {
                break;
            }

            try {
                // Put this background thread to sleep for 600 ms.
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Increment our progress and publish it to onProgressUpdate.
            progress += mRandom.nextInt(14);
            publishProgress(progress);
        }

        publishProgress(100);

        return true;
    }

    @Override
    protected void onCancelled(Boolean result) {
        // For this basic application, we don't need to do any clean up if the task is cancelled,
        // but if we wanted to, this is where we'd do it. When an AsyncTask is cancelled, it calls
        // onCancelled instead of onPostExecute once doInBackground is complete.
        mCallbacks.get().loadingInterrupted();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mCallbacks.get().loadingComplete();
        }
    }
}
