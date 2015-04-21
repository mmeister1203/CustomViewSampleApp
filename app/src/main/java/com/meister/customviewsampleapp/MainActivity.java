package com.meister.customviewsampleapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.meister.customviewsampleapp.asynctasks.LoadingTask;
import com.meister.customviewsampleapp.fragments.BaseFragment;
import com.meister.customviewsampleapp.fragments.SpiralScreenFragment;
import com.meister.customviewsampleapp.fragments.SplashScreenFragment;
import com.meister.customviewsampleapp.interfaces.LoadingTaskListener;

/**
 * Main Activity used for this application. Since we're using fragments, it handles application lifecycle
 * and non-view related tasks. Our fragments are responsible for handling their view component lifecycle
 * events.
 * Created by mark.meister on 4/17/15.
 */
public class MainActivity extends Activity implements LoadingTaskListener {

    private LoadingTask mLoadingTask;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_layout);

        // Android will handle most things during a configuration change, however, we want the splash
        // screen to re-load on a configuration change, while if the user has already completed the loading
        // then we can take them directly to the SpiralView.
        if (savedInstanceState == null || getFragmentManager().getBackStackEntryCount() == 0) {
            initializeApplication();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // If the application is put in the background or killed while the loading task is running, we
        // cancel the task. So, in these cases we need to recreate the task.
        final Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (mLoadingTask != null && mLoadingTask.isCancelled() && currentFragment instanceof SplashScreenFragment) {
            (mLoadingTask = new LoadingTask((SplashScreenFragment)currentFragment, this)).execute();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mLoadingTask != null) {
            mLoadingTask.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        final Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);

        // If we're on the Splash or Spiral screens, the back button should finish our activity. If
        // we had more fragments, we'd still want to call super.onBackPressed for those, since that
        // will pop the top fragment off the stack, showing the one underneath.
        if (currentFragment instanceof SplashScreenFragment ||
                currentFragment instanceof SpiralScreenFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void loadingComplete() {
        // Once our loading is complete, we can show our primary screen. In our case it's SpiralScreenFragment.
        setFragment(SpiralScreenFragment.newInstance(), true, true);
    }

    @Override
    public void loadingInterrupted() {
        // This gives us a place to decide what we want to do if the user closes the app while the loading
        // splash screen is showing. In this case we cancel the background work, so it would make sense
        // to just let the app finish and have to user have to go through the loading screen next time
        // they open the app. In other cases though, it might make more sense for our LoadingTask to complete,
        // then when they bring the app back from that background we have whatever we loaded ready to go.
//        finish();
    }

    public void setFragment(Fragment fragment, boolean animate, boolean addToBackStack) {
        String tag = fragment.getTag();

        final Fragment fragmentFromTag = getFragmentManager().findFragmentByTag(tag);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Here we set a custom fragment transition animation using object animators.
        if (animate) {
            transaction.setCustomAnimations(
                    R.animator.fade_in,
                    R.animator.fade_out,
                    R.animator.fade_in,
                    R.animator.fade_out);
        }

        // We need to check if there is a fragment on the back stack with the same tag, since adding
        // one would throw an exception.
        if (fragmentFromTag == null && addToBackStack) {
            tag = ((BaseFragment)fragment).getName();
            transaction.replace(R.id.fragment_container, fragment, tag).addToBackStack(null).commit();
        } else {
            transaction.replace(R.id.fragment_container, fragment, tag).commit();
        }
    }

    private void initializeApplication() {
        final SplashScreenFragment splashScreenFragment = SplashScreenFragment.newInstance();

        // Because we'd never want the user to be able to return the splash screen, we don't add it
        // to our fragment managers back stack.
        setFragment(splashScreenFragment, false, false);
        (mLoadingTask = new LoadingTask(splashScreenFragment, this)).execute();
    }
}
