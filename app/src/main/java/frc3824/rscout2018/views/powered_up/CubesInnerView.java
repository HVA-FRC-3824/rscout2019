package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Pair;

import java.util.ArrayList;

import frc3824.rscout2018.R;
import frc3824.rscout2018.utilities.Utilities;
import frc3824.rscout2018.views.heatmap.HeatMap;
import frc3824.rscout2018.views.heatmap.HeatMapMarkerCallback;

/**
 * Created by frc3824
 */
public class CubesInnerView extends HeatMap
{
    Bitmap mBackgroundBitmap;
    Paint mCanvasPaint;
    int mWidth;
    int mHeight;
    ArrayList<Pair<Float, Float>> mData;

    public CubesInnerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setMinimum(0);
        setMaximum(100);
        setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(0xff9400D3));
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setData(ArrayList<Pair<Float, Float>> data)
    {
        mData = data;
        new CubesInnerView.UpdateTask().execute();
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mWidth = width;
        mHeight = height;
        mBackgroundBitmap = Utilities.decodeSampledBitmapFromResource(getResources(), R.drawable.field_top_down,
                                                                      mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, mCanvasPaint);
        super.onDraw(canvas);
    }

    private class UpdateTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects)
        {
            if(mData != null)
            {
                clearData();

                for (Pair<Float, Float> pair: mData)
                {
                    addData(new HeatMap.DataPoint(pair.first,
                                                  pair.second,
                                                  100));
                }
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects)
        {
            forceRefresh();
        }

    }
}
