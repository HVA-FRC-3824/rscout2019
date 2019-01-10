package frc3824.rscout2018.custom_charts.lld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.LineScatterCandleRadarRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class LLDRenderer extends LineScatterCandleRadarRenderer
{
    LLDDataProvider mChart;

    float[] mShadowBuffers = new float[8];
    float[] mBodyBuffers = new float[4];
    float[] mAvgBuffers = new float[6];

    public LLDRenderer(LLDDataProvider chart, ChartAnimator animator,
                       ViewPortHandler viewPortHandler)
    {
        super(animator, viewPortHandler);
        mChart = chart;
    }


    @Override
    public void initBuffers()
    {

    }

    @Override
    public void drawData(Canvas c)
    {
        LLDData lldData = mChart.getLLDData();

        for (ILLDDataSet set : lldData.getDataSets())
        {
            if (set.isVisible())
            {
                drawDrawSet(c, set);
            }
        }
    }

    protected void drawDrawSet(Canvas c, ILLDDataSet dataSet)
    {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));
        float phaseY = mAnimator.getPhaseY();
        float barSpace = dataSet.getBarSpace();

        int minx = Math.max(mMinX, 0);
        int maxx = Math.min(mMaxX + 1, dataSet.getEntryCount());

        mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());

        // draw the body
        for (int j = minx,
             count = (int) Math.ceil((maxx - minx) * phaseX + (float) minx);
             j < count;
             j++)
        {

            // get the entry
            LLDEntry e = dataSet.getEntryForIndex(j);

            if (e == null)
            {
                continue;
            }

            final float xPos = e.getXIndex();

            final float avg = e.getAvg();
            final float std = e.getStd();
            final float avg_plus = avg + std;
            final float avg_minus = avg - std;
            final float max = e.getMax();
            final float min = e.getMin();

            // calculate the shadow
            mShadowBuffers[0] = xPos;
            mShadowBuffers[2] = xPos;
            mShadowBuffers[4] = xPos;
            mShadowBuffers[6] = xPos;

            mShadowBuffers[1] = max * phaseY;
            mShadowBuffers[3] = avg_plus * phaseY;
            mShadowBuffers[5] = avg_minus * phaseY;
            mShadowBuffers[7] = min * phaseY;

            trans.pointValuesToPixel(mShadowBuffers);

            // draw the shadows
            mRenderPaint.setColor(Color.BLACK);
            mRenderPaint.setStyle(Paint.Style.STROKE);

            c.drawLines(mShadowBuffers, mRenderPaint);

            // calculate the body
            mBodyBuffers[0] = xPos - 0.5f + barSpace;
            mBodyBuffers[1] = avg_minus * phaseY;
            mBodyBuffers[2] = xPos + 0.5f - barSpace;
            mBodyBuffers[3] = avg_plus * phaseY;

            trans.pointValuesToPixel(mBodyBuffers);

            // draw the body
            mRenderPaint.setColor(dataSet.getColor(j));
            mRenderPaint.setStyle(Paint.Style.FILL);

            // left, top, right, bottom
            c.drawRect(
                    mBodyBuffers[0], mBodyBuffers[3],
                    mBodyBuffers[2], mBodyBuffers[1],
                    mRenderPaint);

            // outline the body
            mRenderPaint.setColor(Color.BLACK);
            mRenderPaint.setStyle(Paint.Style.STROKE);

            // left, top, right, bottom
            c.drawRect(
                    mBodyBuffers[0], mBodyBuffers[3],
                    mBodyBuffers[2], mBodyBuffers[1],
                    mRenderPaint);

            // calculate average line
            mAvgBuffers[0] = xPos - 0.5f + barSpace;
            mAvgBuffers[1] = avg;
            mAvgBuffers[2] = xPos + 0.5f - barSpace;
            mAvgBuffers[3] = avg;
            mAvgBuffers[4] = xPos;
            mAvgBuffers[5] = avg;

            trans.pointValuesToPixel(mAvgBuffers);

            // draw the average line
            c.drawLine(mAvgBuffers[0],
                       mAvgBuffers[1],
                       mAvgBuffers[2],
                       mAvgBuffers[3],
                       mRenderPaint);
        }
    }

    @Override
    public void drawValues(Canvas c)
    {

    }

    @Override
    public void drawExtras(Canvas c)
    {

    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices)
    {
        LLDData lld_data = mChart.getLLDData();

        for (Highlight high : indices)
        {

            final int minDataSetIndex = high.getDataSetIndex() == -1 ? 0 : high.getDataSetIndex();
            final int maxDataSetIndex = high.getDataSetIndex() == -1 ? lld_data.getDataSetCount() : (high
                    .getDataSetIndex() + 1);
            if (maxDataSetIndex - minDataSetIndex < 1)
            {
                continue;
            }

            for (int dataSetIndex = minDataSetIndex; dataSetIndex < maxDataSetIndex; dataSetIndex++)
            {

                int xIndex = high.getXIndex(); // get the
                // x-position

                ILLDDataSet set = mChart.getLLDData().getDataSetByIndex(dataSetIndex);

                if (set == null || !set.isHighlightEnabled())
                {
                    continue;
                }

                LLDEntry e = set.getEntryForXIndex(xIndex);

                if (e == null || e.getXIndex() != xIndex)
                {
                    continue;
                }

                float lowValue = e.getMin() * mAnimator.getPhaseY();
                float highValue = e.getMax() * mAnimator.getPhaseY();
                float y = (lowValue + highValue) / 2f;

                float[] pts = new float[]
                        {
                                xIndex, y
                        };

                mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);

                // draw the lines
                drawHighlightLines(c, pts, set);
            }
        }

    }
}
