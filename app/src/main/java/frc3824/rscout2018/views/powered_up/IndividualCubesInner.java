package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.powered_up.CubeEvent;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.utilities.Utilities;

/**
 * Created by frc3824
 */
public class IndividualCubesInner extends View
{
    Context mContext;
    Bitmap mBackgroundBitmap;
    Bitmap mPickedUpHatchBitmap;
    Bitmap mPickedUpCargoBitmap;
    Bitmap mPlacedHighBitmap;
    Bitmap mPlacedMediumBitmap;
    Bitmap mPlacedLowBitmap;
    Bitmap mDroppedBitmap;
    Paint mCanvasPaint;
    int mWidth;
    int mHeight;
    Boolean mAuto = null;

    TeamMatchData mTeamMatchData;
    ArrayList<CubeEvent> mCubeEvents = null;


    public IndividualCubesInner(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setAuto(boolean auto)
    {
        mAuto = auto;
        if (mTeamMatchData != null)
        {
            if (mAuto)
            {
                mCubeEvents = mTeamMatchData.getAutoCubeEvents();
            }
            else
            {
                mCubeEvents = mTeamMatchData.getTeleopCubeEvents();
            }
            invalidate();
        }
    }

    public TeamMatchData getData()
    {
        return mTeamMatchData;
    }

    public void setTeamMatchData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
        if(mAuto != null)
        {
            if (mAuto)
            {
                mCubeEvents = mTeamMatchData.getAutoCubeEvents();
            }
            else
            {
                mCubeEvents = mTeamMatchData.getTeleopCubeEvents();
            }
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mWidth = width;
        mHeight = height;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Resources resources = getResources();
        Bitmap temp = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.field_top_down, width, height);
        if(sharedPreferences.getBoolean(Constants.Settings.BLUE_LEFT, false))
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(180);
            mBackgroundBitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
            temp.recycle();
        }
        else
        {
            mBackgroundBitmap = temp;
        }

        mPickedUpHatchBitmap = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.hatch_panel,
                                                    height / 15,
                                                    height / 15);
        mPickedUpCargoBitmap = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.cargo_ball,
                height / 15,
                height / 15);
        mPlacedHighBitmap = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.placed_high, height / 15, height / 15);
        mPlacedMediumBitmap = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.placed_medium, height / 15, height / 15);
        mPlacedLowBitmap = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.placed_low, height / 15, height / 15);
        mDroppedBitmap = Utilities.decodeSampledBitmapFromResource(resources, R.drawable.dropped, height / 15, height / 15);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, mCanvasPaint);

        if (mCubeEvents != null)
        {
            for (CubeEvent event : mCubeEvents)
            {
                float x = event.getLocationX() * mWidth;
                float y = event.getLocationY() * mHeight;
                switch (event.getEvent())
                {
                    case Constants.MatchScouting.CubeEvents.PICK_UP_CARGO:
                        canvas.drawBitmap(mPickedUpCargoBitmap,
                                x - mPickedUpCargoBitmap.getWidth() / 2,
                                y - mPickedUpCargoBitmap.getHeight() / 2,
                                mCanvasPaint);
                        break;
                    case Constants.MatchScouting.CubeEvents.PICK_UP_HATCH:
                        canvas.drawBitmap(mPickedUpHatchBitmap,
                                          x - mPickedUpHatchBitmap.getWidth() / 2,
                                          y - mPickedUpHatchBitmap.getHeight() / 2,
                                          mCanvasPaint);
                        break;
                    case Constants.MatchScouting.CubeEvents.PLACED_HIGH:
                        canvas.drawBitmap(mPlacedHighBitmap,
                                x - mPlacedHighBitmap.getWidth() / 2,
                                y - mPlacedHighBitmap.getHeight() / 2,
                                mCanvasPaint);
                        break;
                    case Constants.MatchScouting.CubeEvents.PLACED_MEDIUM:
                        canvas.drawBitmap(mPlacedMediumBitmap,
                                x - mPlacedMediumBitmap.getWidth() / 2,
                                y - mPlacedMediumBitmap.getHeight() / 2,
                                mCanvasPaint);
                        break;
                    case Constants.MatchScouting.CubeEvents.PLACED_LOW:
                        canvas.drawBitmap(mPlacedLowBitmap,
                                          x - mPlacedLowBitmap.getWidth() / 2,
                                          y - mPlacedLowBitmap.getHeight() / 2,
                                          mCanvasPaint);
                        break;
                    case Constants.MatchScouting.CubeEvents.DROPPED:
                        canvas.drawBitmap(mDroppedBitmap,
                                          x - mDroppedBitmap.getWidth() / 2,
                                          y - mDroppedBitmap.getHeight() / 2,
                                          mCanvasPaint);
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        }
    }
}
