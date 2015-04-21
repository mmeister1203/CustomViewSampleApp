package com.meister.customviewsampleapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.meister.customviewsampleapp.R;

/**
 * Custom progress view to display the loading progress on our splash screen. Just to note, we could
 * have just used Androids ProgressBar class, however that would have been far less fun.
 * Created by mark.meister on 4/19/15.
 */
public class ProgressView extends View {
    private Paint mBackgroundPaint;
    private Paint mProgressPaint;

    private int mCapRadius;

    private float mPercentComplete;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final int lineWidth = (int)getResources().getDimension(R.dimen.progress_view_line_width);

        mCapRadius = lineWidth / 2;

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStrokeWidth(lineWidth);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mBackgroundPaint.setShader(new LinearGradient(0, 0, 0, 2 * mCapRadius, Color.WHITE, Color.GRAY, Shader.TileMode.CLAMP));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(lineWidth);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setShader(new LinearGradient(0, 0, 0, 2 * mCapRadius, getResources().getColor(R.color.loading_blue_primary), getResources().getColor(R.color.loading_blue_secondary), Shader.TileMode.CLAMP));
    }

    @Override
    public void onDraw(Canvas canvas) {
        float currentPosition = (float)getWidth() * mPercentComplete + mCapRadius;

        if (currentPosition > getWidth() - mCapRadius) {
            currentPosition = getWidth() - mCapRadius;
        }

        // Draw the background line from the current position to the width of the view.
        canvas.drawLine(currentPosition, mCapRadius, getWidth() - mCapRadius, mCapRadius, mBackgroundPaint);

        // Draw the progress line from the start of the view to the current position.
        canvas.drawLine(mCapRadius, mCapRadius, currentPosition, mCapRadius, mProgressPaint);
    }

    /**
     * Update the progress view by feeding in a progress value.
     * @param progress - value expected to be between [0, 100]
     */
    public void updateProgress(int progress) {
        mPercentComplete = (float)progress / 100f;
        invalidate();
    }
}
