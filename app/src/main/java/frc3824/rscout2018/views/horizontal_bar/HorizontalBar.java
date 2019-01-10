package frc3824.rscout2018.views.horizontal_bar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.mikephil.charting.utils.Utils;

/**
 * @class HorizontalBar
 * @brief Custom view for display values as a horizontal bar
 *
 * Based on PhilJay's ValueBar
 */
public class HorizontalBar extends View implements ValueAnimator.AnimatorUpdateListener
{
    private static final String TAG = "HorizontalBar";

    float mMinimum;
    float mMaximum;
    float mValue;
    float mInterval;

    RectF mBar;

    //region Paints
    Paint mBarPaint;
    Paint mBorderPaint;
    Paint mValueTextPaint;
    Paint mMinMaxTextPaint;
    Paint mOverlayPaint;
    //endregion

    ObjectAnimator mAnimator;

    boolean mDrawBorder;
    boolean mDrawValueText;
    boolean mDrawMinMaxText;
    boolean mTouchEnabled;

    BarColorFormatter mColorFormatter;
    ValueTextFormatter mValueTextFormatter;
    BarSelectionListener mSelectionListener;
    GestureDetector mGestureDetector;

    public HorizontalBar(Context context)
    {
        super(context);
        init();
    }

    public HorizontalBar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public HorizontalBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialize
     */
    private void init()
    {
        Utils.init(getContext());

        mBar = new RectF();
        mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPaint.setStyle(Paint.Style.FILL);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(2f));

        mValueTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValueTextPaint.setColor(Color.WHITE);
        mValueTextPaint.setTextSize(Utils.convertDpToPixel(18f));

        mMinMaxTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinMaxTextPaint.setColor(Color.WHITE);
        mMinMaxTextPaint.setTextSize(Utils.convertDpToPixel(18f));

        mOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayPaint.setStyle(Paint.Style.FILL);
        mOverlayPaint.setColor(Color.WHITE);
        mOverlayPaint.setAlpha(120);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        prepareBarSize();

        if (mDrawMinMaxText)
        {
            drawMinMaxText(canvas);
        }

        mBarPaint.setColor(mColorFormatter.getColor(mValue, mMinimum, mMaximum));

        canvas.drawRect(mBar, mBarPaint);

        if (mDrawBorder)
        {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mBorderPaint);
        }

        if (mDrawValueText)
        {
            drawValueText(canvas);
        }
    }

    /**
     * Draws all text on the HorizontalBar
     *
     * @param canvas
     */
    private void drawValueText(Canvas canvas)
    {
        if (mValue <= mMinimum && mDrawMinMaxText)
        {
            return;
        }

        String text = mValueTextFormatter.getValueText(mValue, mMinimum, mMaximum);

        float textHeight = Utils.calcTextHeight(mValueTextPaint, text) * 1.5f;
        float textWidth = Utils.calcTextWidth(mValueTextPaint, text);

        float x = mBar.right - textHeight / 2f;
        float y = getHeight() / 2f + textWidth / 2f;

        if (x < textHeight)
        {
            x = textHeight;
        }

        // Draw overlay
        canvas.drawRect(x - textHeight / 1.5f - textHeight / 2f,
                        0,
                        mBar.right,
                        getHeight(),
                        mOverlayPaint);

        drawTextVertical(canvas, text, x, y, mValueTextPaint);
    }

    /**
     * Draws the minimum and maximum text values
     *
     * @param canvas
     */
    private void drawMinMaxText(Canvas canvas)
    {
        String max = mValueTextFormatter.getMaximum(mMaximum);
        String min = mValueTextFormatter.getMinimum(mMinimum);

        float textHeight = Utils.calcTextHeight(mValueTextPaint, min) * 1.5f;

        // Draw max
        drawTextVertical(canvas,
                         max,
                         getWidth() - textHeight / 2f,
                         getHeight() / 2f + Utils.calcTextWidth(mMinMaxTextPaint, min) / 2f,
                         mMinMaxTextPaint);

        if (!mDrawValueText || mValue <= mMinimum)
        {
            drawTextVertical(canvas,
                             min,
                             textHeight,
                             getHeight() / 2f + Utils.calcTextWidth(mMinMaxTextPaint, min) / 2f,
                             mMinMaxTextPaint);
        }
    }

    /**
     * Draws the text vertically at the provided position
     *
     * @param canvas
     * @param text
     * @param x
     * @param y
     * @param p
     */
    private void drawTextVertical(Canvas canvas, String text, float x, float y, Paint p)
    {
        canvas.save();
        canvas.rotate(270, x, y);
        canvas.drawText(text, x, y, p);
        canvas.restore();
    }

    /**
     * Prepares the bar according to the current value
     */
    private void prepareBarSize()
    {
        float length = ((float) getWidth() / (mMaximum - mMinimum)) * (mValue - mMinimum);
        mBar.set(0, 0, length, getHeight());
    }

    /**
     * Sets the minimum and maximum value the bar can display
     *
     * @param min
     * @param max
     */
    public void setMinMax(float min, float max)
    {
        mMinimum = min;
        mMaximum = max;
    }

    /**
     * Returns the maximum value the bar can display
     */
    public float getMax()
    {
        return mMaximum;
    }

    /**
     * Returns the minimum value the bar can display
     */
    public float getMin()
    {
        return mMinimum;
    }

    /**
     * Sets the actual value the bar displays. Do not forget to set a minimum
     * and maximum value
     *
     * @param value
     */
    public void setValue(float value)
    {
        mValue = value;
    }

    /**
     * Returns the currently displayed value
     *
     * @return
     */
    public float getValue()
    {
        return mValue;
    }

    /**
     * Sets the interval in which the values can be chosen and displayed on
     * the ValueBar. If interval <=, there is no interval.
     *
     * @param interval
     */
    public void setInterval(float interval)
    {
        mInterval = interval;
    }

    /**
     * Returns the interval in which value can be chosen and displayed
     *
     * @return
     */
    public float getInterval()
    {
        return mInterval;
    }

    /**
     * Returns the bar that represents the value
     *
     * @return
     */
    public RectF getBar()
    {
        return mBar;
    }

    /**
     * Animates the bar from a specific value to a specific value
     *
     * @param from
     * @param to
     * @param durationMillis
     */
    public void animate(float from, float to, int durationMillis)
    {
        if (from < mMinimum)
        {
            from = mMinimum;
        }

        if (from > mMaximum)
        {
            from = mMaximum;
        }

        if (to < mMinimum)
        {
            to = mMinimum;
        }

        if (to > mMaximum)
        {
            to = mMaximum;
        }

        mValue = from;
        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, to);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    /**
     * Animates the bar up from it's minimum value to the specified value
     *
     * @param to
     * @param durationMillis
     */
    public void animateUp(float to, int durationMillis)
    {
        if (to > mMaximum)
        {
            to = mMaximum;
        }

        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, to);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    /**
     * Animates the bar down from it's current value to the specified value
     *
     * @param to
     * @param durationMillis
     */
    public void animateDown(float to, int durationMillis)
    {
        if (to < mMinimum)
        {
            to = mMinimum;
        }

        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, to);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator)
    {
        invalidate();
    }

    /**
     * Set this to true to enable drawing the border around the bar, or false to disable it
     *
     * @param enabled
     */
    public void setDrawBorder(boolean enabled)
    {
        mDrawBorder = enabled;
    }

    /**
     * Sets the width of the border around the bar (if drawn)
     *
     * @param width
     */
    public void setBorderWidth(float width)
    {
        mBorderPaint.setStrokeWidth(width);
    }

    /**
     * Sets the color of the border around the bar (if drawn).
     *
     * @param color
     */
    public void setBorderColor(int color)
    {
        mBorderPaint.setColor(color);
    }

    /**
     * Sets a custom formatter that formats the value-text. Provide null to
     * reset all changes and use the default formatter.
     *
     * @param formatter
     */
    public void setValueTextFormatter(ValueTextFormatter formatter)
    {

        if (formatter == null)
        {
            formatter = new DefaultValueTextFormatter();
        }
        mValueTextFormatter = formatter;
    }

    /**
     * Sets a custom BarColorFormatter for the ValueBar. Implement the
     * BarColorFormatter interface in your own formatter class and return
     * whatever color you like from the getColor(...) method. You can for
     * example make the color depend on the current value of the bar. Provide
     * null to reset all changes.
     *
     * @param formatter
     */
    public void setColorFormatter(BarColorFormatter formatter)
    {

        if (formatter == null)
        {
            formatter = new DefaultColorFormatter(Color.rgb(39, 140, 230));
        }
        mColorFormatter = formatter;
    }

    /**
     * Sets the color the ValueBar should have.
     *
     * @param color
     */
    public void setColor(int color)
    {
        mColorFormatter = new DefaultColorFormatter(color);
    }

    /**
     * Returns the paint object that is used for drawing the bar.
     *
     * @return
     */
    public Paint getBarPaint()
    {
        return mBarPaint;
    }

    /**
     * Returns the Paint object used for drawing the value-text.
     *
     * @return
     */
    public Paint getValueTextPaint()
    {
        return mValueTextPaint;
    }

    /**
     * Returns the Paint object used for drawing min an max text.
     *
     * @return
     */
    public Paint getMinMaxTextPaint()
    {
        return mMinMaxTextPaint;
    }

    /**
     * Sets the size of the value-text in density pixels.
     *
     * @param size
     */
    public void setValueTextSize(float size)
    {
        mValueTextPaint.setTextSize(Utils.convertDpToPixel(size));
    }

    /**
     * Sets the Typeface of the value-text.
     *
     * @param tf
     */
    public void setValueTextTypeface(Typeface tf)
    {
        mValueTextPaint.setTypeface(tf);
    }

    /**
     * Sets the size of the min-max text in density pixels.
     *
     * @param size
     */
    public void setMinMaxTextSize(float size)
    {
        mMinMaxTextPaint.setTextSize(Utils.convertDpToPixel(size));
    }

    /**
     * Sets the Typeface of the min-max text.
     *
     * @param tf
     */
    public void setMinMaxTextTypeface(Typeface tf)
    {
        mMinMaxTextPaint.setTypeface(tf);
    }

    /**
     * Sets the color of the overlay that is placed below the value-text.
     *
     * @param color
     */
    public void setOverlayColor(int color)
    {
        int alpha = mOverlayPaint.getAlpha();
        mOverlayPaint.setColor(color);
        mOverlayPaint.setAlpha(alpha);
    }

    /**
     * Set this to true to enable touch gestures on the ValueBar.
     *
     * @param enabled
     */
    public void setTouchEnabled(boolean enabled)
    {
        mTouchEnabled = enabled;
    }

    /**
     * Set this to true to enable drawing the actual value that is currently
     * displayed onto the bar.
     *
     * @param enabled
     */
    public void setDrawValueText(boolean enabled)
    {
        mDrawValueText = enabled;
    }

    /**
     * Returns true if drawing the text that describes the actual value is
     * enabled.
     *
     * @return
     */
    public boolean isDrawValueTextEnabled()
    {
        return mDrawValueText;
    }

    /**
     * Set this to true to enable drawing the minimum and maximum labels below
     * the bar.
     *
     * @param enabled
     */
    public void setDrawMinMaxText(boolean enabled)
    {
        mDrawMinMaxText = enabled;
    }

    /**
     * Returns true if drawing the minimum and maximum label is enabled.
     *
     * @return
     */
    public boolean isDrawMinMaxTextEnabled()
    {
        return mDrawMinMaxText;
    }

    /**
     * Returns the corresponding value for a pixel-position on the horizontal
     * axis.
     *
     * @param xPos
     *
     * @return
     */
    public float getValueForPosition(int xPos)
    {

        float factor = xPos / getWidth();
        return mMaximum * factor;
    }

    /**
     * Sets a GestureDetector for the ValueBar to receive callbacks on gestures.
     *
     * @param gd
     */
    public void setGestureDetector(GestureDetector gd)
    {
        mGestureDetector = gd;
    }

    /**
     * Sets a selectionlistener for callbacks when selecting values on the
     * ValueBar.
     *
     * @param l
     */
    public void setValueBarSelectionListener(BarSelectionListener l)
    {
        mSelectionListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        if (mTouchEnabled)
        {
            if (mSelectionListener == null)
            {
                Log.w(TAG,
                      "No SelectionListener specified. Use setSelectionlistener(...) to set a listener for callbacks when selecting values.");
            }

            // If the detector reconized a gesture, consume it
            if (mGestureDetector != null && mGestureDetector.onTouchEvent(e))
            {
                return true;
            }

            float x = e.getX();
            float y = e.getY();

            switch (e.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    updateValue(x, y);
                    invalidate();
                case MotionEvent.ACTION_MOVE:
                    updateValue(x, y);
                    invalidate();
                    if (mSelectionListener != null)
                    {
                        mSelectionListener.onSelectionUpdate(mValue, mMinimum, mMaximum, this);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    updateValue(x, y);
                    invalidate();
                    if (mSelectionListener != null)
                    {
                        mSelectionListener.onValueSelected(mValue, mMinimum, mMaximum, this);
                    }
                    break;
            }
            return true;
        }
        else
        {
            return super.onTouchEvent(e);
        }
    }

    private void updateValue(float x, float y)
    {
        float newValue = 0f;

        if (x <= 0)
        {
            newValue = mMinimum;
        }
        else if (x > getWidth())
        {
            newValue = mMaximum;
        }
        else
        {
            float factor = x / getWidth();
            newValue = (mMaximum - mMinimum) * factor + mMinimum;
        }

        if (mInterval > 0f)
        {
            float remainder = newValue % mInterval;

            // Check if the new value is closer to the next or the previous
            if (remainder <= mInterval / 2f)
            {
                newValue -= remainder;
            }
            else
            {
                newValue -= remainder;
                newValue += mInterval;
            }
        }

        mValue = newValue;
    }
}
