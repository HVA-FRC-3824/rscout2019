package frc3824.rscout2018.custom_charts.lld;

import android.graphics.Paint;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LLDDataSet extends LineScatterCandleRadarDataSet<LLDEntry> implements ILLDDataSet
{
    /**
     * the width of the shadow of the candle
     */
    private float mShadowWidth = 3f;

    /**
     * should the candle bars show?
     * when false, only "ticks" will show
     * <p/>
     * - default: true
     */
    private boolean mShowCandleBar = true;

    /**
     * the space between the candle entries, default 0.1f (10%)
     */
    private float mBarSpace = 0.1f;

    /**
     * use candle color for the shadow
     */
    private boolean mShadowColorSameAsCandle = false;

    /**
     * paint style when open < close
     * increasing candlesticks are traditionally hollow
     */
    protected Paint.Style mIncreasingPaintStyle = Paint.Style.STROKE;

    /**
     * paint style when open > close
     * descreasing candlesticks are traditionally filled
     */
    protected Paint.Style mDecreasingPaintStyle = Paint.Style.FILL;

    /**
     * color for open == close
     */
    protected int mNeutralColor = ColorTemplate.COLOR_SKIP;

    /**
     * color for open < close
     */
    protected int mIncreasingColor = ColorTemplate.COLOR_SKIP;

    /**
     * color for open > close
     */
    protected int mDecreasingColor = ColorTemplate.COLOR_SKIP;

    /**
     * shadow line color, set -1 for backward compatibility and uses default
     * color
     */
    protected int mShadowColor = ColorTemplate.COLOR_SKIP;

    public LLDDataSet(List<LLDEntry> yVals, String label)
    {
        super(yVals, label);
    }

    @Override
    public DataSet<LLDEntry> copy()
    {
        List<LLDEntry> yVals = new ArrayList<>();
        yVals.clear();

        for (int i = 0; i < mYVals.size(); i++)
        {
            yVals.add(mYVals.get(i).copy());
        }

        LLDDataSet copied = new LLDDataSet(yVals, getLabel());
        copied.mColors = mColors;
        copied.mShadowWidth = mShadowWidth;
        copied.mShowCandleBar = mShowCandleBar;
        copied.mBarSpace = mBarSpace;
        copied.mHighLightColor = mHighLightColor;
        copied.mIncreasingPaintStyle = mIncreasingPaintStyle;
        copied.mDecreasingPaintStyle = mDecreasingPaintStyle;
        copied.mShadowColor = mShadowColor;

        return copied;
    }

    @Override
    public void calcMinMax(int start, int end) {
        // super.calculate();

        if (mYVals == null)
            return;

        if (mYVals.size() == 0)
            return;

        int endValue;

        if (end == 0 || end >= mYVals.size())
            endValue = mYVals.size() - 1;
        else
            endValue = end;

        mYMin = Float.MAX_VALUE;
        mYMax = -Float.MAX_VALUE;

        for (int i = start; i <= endValue; i++) {

            LLDEntry e = mYVals.get(i);

            if (e.getMin() < mYMin)
                mYMin = e.getMin();

            if (e.getMax() > mYMax)
                mYMax = e.getMax();
        }
    }

    /**
     * Sets the space that is left out on the left and right side of each
     * candle, default 0.1f (10%), max 0.45f, min 0f
     *
     * @param space
     */
    public void setBarSpace(float space) {

        if (space < 0f)
            space = 0f;
        if (space > 0.45f)
            space = 0.45f;

        mBarSpace = space;
    }

    @Override
    public float getBarSpace() {
        return mBarSpace;
    }


    /**
     * Sets whether the candle bars should show?
     *
     * @param showCandleBar
     */
    public void setShowCandleBar(boolean showCandleBar)
    {
        mShowCandleBar = showCandleBar;
    }

    @Override
    public boolean getShowCandleBar()
    {
        return mShowCandleBar;
    }

    /**
     * Sets the width of the candle-shadow-line in pixels. Default 3f.
     *
     * @param width
     */
    public void setShadowWidth(float width)
    {
        mShadowWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public float getShadowWidth()
    {
        return mShadowWidth;
    }


    @Override
    public int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Sets shadow color for all entries
     *
     * @param shadowColor
     */
    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open == close.
     *
     * @param color
     */
    public void setNeutralColor(int color)
    {
        mNeutralColor = color;
    }

    @Override
    public int getNeutralColor()
    {
        return mNeutralColor;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open <= close.
     *
     * @param color
     */
    public void setIncreasingColor(int color)
    {
        mIncreasingColor = color;
    }

    @Override
    public int getIncreasingColor()
    {
        return mIncreasingColor;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open > close.
     *
     * @param color
     */
    public void setDecreasingColor(int color)
    {
        mDecreasingColor = color;
    }

    @Override
    public int getDecreasingColor()
    {
        return mDecreasingColor;
    }

    @Override
    public Paint.Style getIncreasingPaintStyle()
    {
        return mIncreasingPaintStyle;
    }

    /**
     * Sets paint style when open < close
     *
     * @param paintStyle
     */
    public void setIncreasingPaintStyle(Paint.Style paintStyle)
    {
        this.mIncreasingPaintStyle = paintStyle;
    }


    @Override
    public Paint.Style getDecreasingPaintStyle()
    {
        return mDecreasingPaintStyle;
    }

    /**
     * Sets paint style when open > close
     *
     * @param decreasingPaintStyle
     */
    public void setDecreasingPaintStyle(Paint.Style decreasingPaintStyle)
    {
        this.mDecreasingPaintStyle = decreasingPaintStyle;
    }

    @Override
    public boolean getShadowColorSameAsCandle() {
        return mShadowColorSameAsCandle;
    }

    /**
     * Sets shadow color to be the same color as the candle color
     *
     * @param shadowColorSameAsCandle
     */
    public void setShadowColorSameAsCandle(boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }
}
