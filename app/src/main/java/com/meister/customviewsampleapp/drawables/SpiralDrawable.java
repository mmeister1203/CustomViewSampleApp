package com.meister.customviewsampleapp.drawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.meister.customviewsampleapp.R;

import static com.meister.customviewsampleapp.drawables.SpiralDrawable.DrawableState.*;

/**
 * Custom drawable for SpiralView background. Handles drawing our spiral.
 * Created by mark.meister on 4/17/15.
 */
public class SpiralDrawable extends Drawable {
    public enum DrawableState {
        Play,
        Pause,
        Done
    }

    private static final float THETA_DELTA = 4f * (float)Math.PI / 180f;

    private DrawableState mDrawableState;
    private Paint mPaint;
    private Path mPath;

    private int mBgColor;
    private int mHalfWidth;
    private int mHalfHeight;
    private int mPortraitShift;

    private float mTheta;

    public SpiralDrawable(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(context.getResources().getDimension(R.dimen.spiral_line_width));
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();

        mDrawableState = Play;
    }

    @Override
    public void draw (Canvas canvas) {
        // First time through, we set our view size to members, then we never have to figure it out again.
        if (mHalfWidth == 0 || mHalfHeight == 0) {
            final Rect bounds = getBounds();
            mHalfWidth = (bounds.right - bounds.left) / 2;
            mHalfHeight = (bounds.bottom - bounds.top) / 2;

            // In portrait mode, we're going to shift the spiral down such that it spans the width.
            // and just touches the bottom of the view.
            mPortraitShift = mHalfWidth > mHalfHeight ? 0 : mHalfHeight - mHalfWidth;

            // Move our math pointer to our initial position.
            mPath.moveTo(mHalfWidth, mHalfHeight + mPortraitShift);
        }

        if (mDrawableState != Done) {
            mTheta += THETA_DELTA;
        }

        // Angle of our control point.
        final float thetaMinusHalf = mTheta - THETA_DELTA / 2;

        final float a1 = 15f * thetaMinusHalf;
        final float a2 = 15f * mTheta;

        // Quadratic control point.
        final float x1 = a1 * (float)Math.cos(thetaMinusHalf) + mHalfWidth;
        final float y1 = a1 * (float)Math.sin(thetaMinusHalf) + mHalfHeight + mPortraitShift;

        // Quadratic end point.
        final float x2 = a2 * (float)Math.cos(mTheta) + mHalfWidth;
        final float y2 = a2 * (float)Math.sin(mTheta) + mHalfHeight + mPortraitShift;

        mPath.quadTo(x1, y1, x2, y2);

        canvas.drawColor(mBgColor);
        canvas.drawPath(mPath, mPaint);

        if (mDrawableState == Play && x2 < mHalfWidth * 2 && y2 < mHalfHeight * 2) {
            invalidateSelf();
        } else {
            mDrawableState = Done;
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter (ColorFilter cf) {}

    @Override
    public int getOpacity () {
        return 0;
    }

    public void setSpiralColor(int color) {
        mPaint.setColor(color);
        invalidateSelf();
    }

    public void setBgColor(int color) {
        mBgColor = color;
        invalidateSelf();
    }

    public void setDrawableState(DrawableState drawableState) {
        mDrawableState = drawableState;
    }

    public void resetAndPlay() {
        mTheta = 0;
        mPath.reset();
        mPath.moveTo(mHalfWidth, mHalfHeight + mPortraitShift);
        mDrawableState = Play;
    }
}
