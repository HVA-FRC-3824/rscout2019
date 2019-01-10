package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.utilities.Utilities;

/**
 * Created by frc3824
 */

public class IndividualStartLocation extends View
{
    Bitmap mBackgroundBitmap;
    Paint mCanvasPaint;
    Paint mPointPaint;
    int mWidth = -1;
    int mHeight;
    TeamMatchData mTeamMatchData = null;

    public IndividualStartLocation(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
        mPointPaint = new Paint();
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setColor(getResources().getColor(R.color.LightGreen));
        mPointPaint.setStrokeWidth(25);
    }


    public void setData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
        if (mTeamMatchData != null)
        {
            loadBackground();
        }
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mWidth = width;
        mHeight = height;
        loadBackground();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(mTeamMatchData != null && mBackgroundBitmap != null)
        {
            canvas.drawBitmap(mBackgroundBitmap, 0, 0, mCanvasPaint);
            if (mTeamMatchData != null)
            {
                canvas.drawPoint((float) mTeamMatchData.getStartLocationX() * mWidth,
                                 (float) mTeamMatchData.getStartLocationY() * mHeight,
                                 mPointPaint);
            }
        }
    }

    private void loadBackground()
    {
        if(mWidth > 0 && mTeamMatchData != null)
        {
            MatchLogistics match = Database.getInstance()
                                           .getMatchLogistics(mTeamMatchData.getMatchNumber());
            if(match != null)
            {
                int position = match.getTeamNumbers().indexOf(mTeamMatchData.getTeamNumber());
                if (position < 3) // Blue
                {
                    Bitmap temp = Utilities.decodeSampledBitmapFromResource(getResources(),
                                                                            R.drawable.blue_top_down,
                                                                            mWidth, mHeight);
                    if (mTeamMatchData.getStartLocationX() < 0.5)
                    {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(180);
                        mBackgroundBitmap = Bitmap.createBitmap(temp,
                                                           0,
                                                           0,
                                                           temp.getWidth(),
                                                           temp.getHeight(),
                                                           matrix,
                                                           true);
                        temp.recycle();
                    }
                    else
                    {
                        mBackgroundBitmap = temp;
                    }
                }
                else // Red
                {
                    Bitmap temp = Utilities.decodeSampledBitmapFromResource(getResources(),
                                                                            R.drawable.red_top_down,
                                                                            mWidth, mHeight);
                    if (mTeamMatchData.getStartLocationX() > 0.5)
                    {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(180);
                        mBackgroundBitmap = Bitmap.createBitmap(temp,
                                                           0,
                                                           0,
                                                           temp.getWidth(),
                                                           temp.getHeight(),
                                                           matrix,
                                                           true);
                        temp.recycle();
                    }
                    else
                    {
                        mBackgroundBitmap = temp;
                    }
                }
            }
        }
    }
}
