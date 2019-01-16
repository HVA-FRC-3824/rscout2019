package frc3824.rscout2018.views.powered_up;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.powered_up.CubeEvent;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.utilities.Utilities;

/**
 * Created by frc3824
 */
public class SavableCubes extends View implements View.OnClickListener
{
    static boolean mPickedUp;
    static boolean mFirst = true;
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
    Button mUndoButton = null;

    TeamMatchData mTeamMatchData;
    ArrayList<CubeEvent> mCubeEvents = null;
    CubeEvent mTempCubeEvent;

    EventSelect mEventSelect;
    PickUpOk mPickUpOk;


    public SavableCubes(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);

        mEventSelect = new EventSelect();
        mPickUpOk = new PickUpOk();
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
        }
    }

    public TeamMatchData getData()
    {
        return mTeamMatchData;
    }

    public void setData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
        if(mAuto != null)
        {
            if (mAuto)
            {
                mCubeEvents = mTeamMatchData.getAutoCubeEvents();
                if(mCubeEvents.isEmpty() && mTeamMatchData.getTeleopCubeEvents().isEmpty())
                {
                    mFirst = true;

                }
            }
            else
            {
                mCubeEvents = mTeamMatchData.getTeleopCubeEvents();
            }
        }
        invalidate();
    }

    public void setUndoButton(Button undo)
    {
        mUndoButton = undo;
        mUndoButton.setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mWidth = width;
        mHeight = height; SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
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
                    case Constants.MatchScouting.CubeEvents.PICK_UP_HATCH:
                        canvas.drawBitmap(mPickedUpHatchBitmap,
                                          x - mPickedUpHatchBitmap.getWidth() / 2,
                                          y - mPickedUpHatchBitmap.getHeight() / 2,
                                          mCanvasPaint);
                        break;
                    case Constants.MatchScouting.CubeEvents.PICK_UP_CARGO:
                        canvas.drawBitmap(mPickedUpCargoBitmap,
                                x - mPickedUpCargoBitmap.getWidth() / 2,
                                y - mPickedUpCargoBitmap.getHeight() / 2,
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

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        if (e.getAction() == MotionEvent.ACTION_UP)
        {
            float x = e.getX();
            float y = e.getY();

            if (mFirst)
            {
                mPickedUp = mTeamMatchData.getStartedWithCube();
            }

            mTempCubeEvent = new CubeEvent();
            long time = Calendar.getInstance().getTimeInMillis();
            mTempCubeEvent.setTime(time);
            mTempCubeEvent.setLocationX(x / (float) mWidth);
            mTempCubeEvent.setLocationY(y / (float) mHeight);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle("Event");

            if (mPickedUp)
            {
                builder.setItems(Constants.MatchScouting.CubeEvents.EVENT_OPTIONS,
                                 mEventSelect);
            }
            else
            {
                builder.setItems(new String[]{Constants.MatchScouting.CubeEvents.PICK_UP_CARGO, Constants.MatchScouting.CubeEvents.PICK_UP_HATCH},
                                 mPickUpOk);
            }

            builder.create().show();
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        // Remove the last cube event
        if (!mCubeEvents.isEmpty())
        {
            mCubeEvents.remove(mCubeEvents.size() - 1);
            if (mCubeEvents.isEmpty())
            {
                mUndoButton.setVisibility(GONE);

                if(mAuto)
                {
                    mFirst = true;
                }
                else // Teleop
                {
                    if(mTeamMatchData.getAutoCubeEvents().isEmpty())
                    {
                        mFirst = true;
                    }
                    else
                    {
                        mPickedUp = !(mTeamMatchData.getAutoCubeEvents()
                                                   .get(mTeamMatchData.getAutoCubeEvents()
                                                                      .size() - 1)
                                                   .getEvent()
                                                   .equals(Constants.MatchScouting.CubeEvents.PICK_UP_HATCH) ||
                                mTeamMatchData.getAutoCubeEvents()
                                        .get(mTeamMatchData.getAutoCubeEvents()
                                                .size() - 1)
                                        .getEvent()
                                        .equals(Constants.MatchScouting.CubeEvents.PICK_UP_CARGO));
                    }
                }
            }
            else mPickedUp = (mCubeEvents.get(mCubeEvents.size() - 1)
                                        .getEvent()
                                        .equals(Constants.MatchScouting.CubeEvents.PICK_UP_CARGO) ||
                    mCubeEvents.get(mCubeEvents.size() - 1)
                            .getEvent()
                            .equals(Constants.MatchScouting.CubeEvents.PICK_UP_HATCH));
            invalidate();
        }
    }

    private class EventSelect implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            mFirst = false;
            mTempCubeEvent.setEvent(Constants.MatchScouting.CubeEvents.EVENT_OPTIONS[which]);
            mCubeEvents.add(mTempCubeEvent);
            mTempCubeEvent = null;
            mUndoButton.setVisibility(VISIBLE);
            mPickedUp = !mPickedUp;
            invalidate();
        }
    }

    private class PickUpOk implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            mFirst = false;
            mTempCubeEvent.setEvent(Constants.MatchScouting.CubeEvents.PICK_UP_EVENT_OPTIONS[which]);
            mCubeEvents.add(mTempCubeEvent);
            mTempCubeEvent = null;
            mUndoButton.setVisibility(VISIBLE);
            mPickedUp = !mPickedUp;
            invalidate();
        }
    }
}
