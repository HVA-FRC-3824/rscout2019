package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import frc3824.rscout2018.R;

/**
 * Created by frc3824.
 */

public class Timer extends LinearLayout implements View.OnClickListener
{
    public interface ButtonListener
    {
        void onStart();
        void onStop(long time);
        void onReset();
    }


    MilliChronometer mTimer;
    Button mStartButton;
    Button mStopButton;
    Button mResetButton;
    ButtonListener mListener = null;


    public Timer(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timer, this, true);

        mTimer = findViewById(R.id.chronometer);
        mStartButton = findViewById(R.id.start);
        mStartButton.setOnClickListener(this);
        mStopButton = findViewById(R.id.stop);
        mStopButton.setOnClickListener(this);
        mResetButton = findViewById(R.id.reset);
        mResetButton.setOnClickListener(this);
    }

    public void setTime(long time)
    {
        mTimer.setTime(time);
        mStartButton.setVisibility(GONE);
        mStopButton.setVisibility(GONE);
        mResetButton.setVisibility(VISIBLE);
    }

    public long getTime()
    {
        return mTimer.getTimeElapsed();
    }

    public void setButtonListener(ButtonListener listener)
    {
        mListener = listener;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.start:
                mTimer.setBase(SystemClock.elapsedRealtime());
                mTimer.start();
                if(mListener != null)
                {
                    mListener.onStart();
                }
                mStopButton.setVisibility(VISIBLE);
                mStartButton.setVisibility(GONE);
                break;
            case R.id.stop:
                mTimer.stop();
                if(mListener != null)
                {
                    mListener.onStop(getTime());
                }
                mStopButton.setVisibility(GONE);
                mResetButton.setVisibility(VISIBLE);
                break;
            case R.id.reset:
                mTimer.setBase(SystemClock.elapsedRealtime());
                mStartButton.setVisibility(VISIBLE);
                mResetButton.setVisibility(GONE);
                if(mListener != null)
                {
                    mListener.onReset();
                }
                break;
        }
    }
}
