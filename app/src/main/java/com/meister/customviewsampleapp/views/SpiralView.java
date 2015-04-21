package com.meister.customviewsampleapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.meister.customviewsampleapp.R;
import com.meister.customviewsampleapp.drawables.SpiralDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.meister.customviewsampleapp.drawables.SpiralDrawable.DrawableState.*;

/**
 * The view used for our spiral screen. The reason a custom view was used here was so that we could
 * include custom attributes set in our spiral_fragment_layout.xml
 * Created by mark.meister on 4/17/15.
 */
public class SpiralView extends RelativeLayout implements View.OnTouchListener {

    private enum SpinnerType {
        SpiralColor,
        BackgroundColor
    }

    private final SpiralDrawable mSpiralDrawable;

    public SpiralView(Context context) {
        this(context, null);
    }

    public SpiralView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiralView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Inflate our layout and attach it to our root view.
        LayoutInflater.from(context).inflate(R.layout.spiral_view_layout, this, true);

        // Get a reference to our child-view buttons, and set OnTouchListeners for each.
        findViewById(R.id.spiral_play_btn).setOnTouchListener(this);
        findViewById(R.id.spiral_pause_btn).setOnTouchListener(this);
        findViewById(R.id.spiral_reset_btn).setOnTouchListener(this);

        // Setup spinner used to select spiral color.
        final Spinner spiralColorSpinner = (Spinner) findViewById(R.id.spiral_color_spinner);
        final ArrayAdapter<String> spiralColorAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_list_item,
                makeSpinnerArray(getResources().getStringArray(R.array.color_options), SpinnerType.SpiralColor));
        spiralColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiralColorSpinner.setAdapter(spiralColorAdapter);
        spiralColorSpinner.setOnItemSelectedListener(mOnColorSelected);

        // Setup spinner used to select view background color.
        final Spinner backgroundColorSpinner = (Spinner) findViewById(R.id.background_color_spinner);
        final ArrayAdapter<String> bgColorAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_list_item,
                makeSpinnerArray(getResources().getStringArray(R.array.color_options), SpinnerType.BackgroundColor));
        bgColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backgroundColorSpinner.setAdapter(bgColorAdapter);
        backgroundColorSpinner.setOnItemSelectedListener(mOnColorSelected);

        // Obtain view attributes.
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SpiralView);

        // Instantiate our background drawable.
        mSpiralDrawable = new SpiralDrawable(getContext());

        // We obtain specific attributes set in main_activity_layout.xml
        mSpiralDrawable.setSpiralColor(attributes.getColor(R.styleable.SpiralView_spiralColor, Color.WHITE));
        mSpiralDrawable.setBgColor(attributes.getColor(R.styleable.SpiralView_backgroundColor, Color.BLACK));

        // Recycle our attributes, allowing for them to be garbage collected.
        attributes.recycle();

        setBackground(mSpiralDrawable);
    }

    // In general View.OnClickListener is appropriate for most buttons, however, because these are
    // controls for the animation, it makes more sense for the buttons action to occur as soon as
    // the user presses down on the button. (Instead of when the user lifts their finger up which is
    // how View.OnClickListener functions).
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()) {
                    case R.id.spiral_play_btn:
                        mSpiralDrawable.setDrawableState(Play);
                        postInvalidate();
                        return true;
                    case R.id.spiral_pause_btn:
                        mSpiralDrawable.setDrawableState(Pause);
                        return true;
                    case R.id.spiral_reset_btn:
                        mSpiralDrawable.resetAndPlay();
                        postInvalidate();
                        return true;
                }
                break;
        }

        return false;
    }

    private AdapterView.OnItemSelectedListener mOnColorSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            }

            final int color;

            switch (position) {
                case 1:
                    color = Color.GREEN;
                    break;
                case 2:
                    color = Color.BLUE;
                    break;
                case 3:
                    color = Color.RED;
                    break;
                case 4:
                    color = Color.rgb(128, 0, 128);
                    break;
                case 5:
                    color = Color.rgb(255, 255, 0);
                    break;
                case 6:
                    color = Color.rgb(255, 165, 0);
                    break;
                case 7:
                    color = Color.BLACK;
                    break;
                default:
                    color = Color.BLACK;
                    break;
            }

            if (parent.getId() == R.id.background_color_spinner) {
                mSpiralDrawable.setBgColor(color);
            } else if (parent.getId() == R.id.spiral_color_spinner) {
                mSpiralDrawable.setSpiralColor(color);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private String[] makeSpinnerArray(String[] baseArray, SpinnerType type) {
        final List<String> spinnerList = new ArrayList<>();

        if (type == SpinnerType.BackgroundColor) {
            spinnerList.add(getResources().getString(R.string.bg_color_spinner_top_text));
        } else if (type == SpinnerType.SpiralColor) {
            spinnerList.add(getResources().getString(R.string.spiral_color_spinner_top_text));
        }

        spinnerList.addAll(Arrays.asList(baseArray));

        return spinnerList.toArray(new String[baseArray.length]);
    }
}
