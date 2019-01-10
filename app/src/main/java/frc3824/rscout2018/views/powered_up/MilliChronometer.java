package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;

import java.text.DecimalFormat;

/**
 * Created by frc3824
 *
 * Variant of the android chronometer so that it shows millisecond
 */
public class MilliChronometer extends android.support.v7.widget.AppCompatTextView
{
    public interface OnChronometerTickListener
    {
        void onChronometerTick(MilliChronometer chronometer);
    }

    long mBase;
    boolean mVisible;
    boolean mStarted;
    boolean mRunning;
    private static final int TICK_WHAT = 2;
    long timeElapsed;
    OnChronometerTickListener mOnChronometerTickListener;
    private Handler mHandler;

    public MilliChronometer(Context context)
    {
        this(context, null, 0);
    }

    public MilliChronometer(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MilliChronometer(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init();
    }

    private void init()
    {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
        mHandler = new Handler()
        {
            public void handleMessage(Message m)
            {
                if (mRunning)
                {
                    updateText(SystemClock.elapsedRealtime());
                    dispatchChronometerTick();
                    sendMessageDelayed(Message.obtain(this, TICK_WHAT),
                                       100);
                }
            }
        };
    }

    public void setBase(long base)
    {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase()
    {
        return mBase;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener)
    {
        mOnChronometerTickListener = listener;
    }

    public OnChronometerTickListener getOnChronometerTickListener()
    {
        return mOnChronometerTickListener;
    }

    public void start()
    {
        mStarted = true;
        updateRunning();
    }

    public void stop()
    {
        mStarted = false;
        updateRunning();
    }


    public void setStarted(boolean started)
    {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility)
    {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    public void setTime(long time)
    {
        if (!mRunning)
        {
            timeElapsed = time;

            DecimalFormat df = new DecimalFormat("00");

            int hours = (int) (timeElapsed / (3600 * 1000));
            int remaining = (int) (timeElapsed % (3600 * 1000));

            int minutes = remaining / (60 * 1000);
            remaining = remaining % (60 * 1000);

            int seconds = remaining / 1000;
            remaining = remaining % (1000);

            // We are only printing the hundred millseconds
            int milliseconds = remaining / 100;

            String text = "";

            if (hours > 0)
            {
                text += df.format(hours) + ":";
            }

            if (minutes > 0)
            {
                text += df.format(minutes) + ":";
            }

            text += df.format(seconds) + ".";
            text += Integer.toString(milliseconds);

            setText(text);
        }
    }

    private synchronized void updateText(long now)
    {
        timeElapsed = now - mBase;

        DecimalFormat df = new DecimalFormat("00");

        int hours = (int) (timeElapsed / (3600 * 1000));
        int remaining = (int) (timeElapsed % (3600 * 1000));

        int minutes = remaining / (60 * 1000);
        remaining = remaining % (60 * 1000);

        int seconds = remaining / 1000;
        remaining = remaining % (1000);

        // We are only printing the hundred millseconds
        int milliseconds = remaining / 100;

        String text = "";

        if (hours > 0)
        {
            text += df.format(hours) + ":";
        }

        if (minutes > 0)
        {
            text += df.format(minutes) + ":";
        }

        text += df.format(seconds) + ".";
        text += Integer.toString(milliseconds);

        setText(text);
    }

    private void updateRunning()
    {
        boolean running = mVisible && mStarted;
        if (running != mRunning)
        {
            if (running)
            {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 100);
            }
            else
            {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    void dispatchChronometerTick()
    {
        if (mOnChronometerTickListener != null)
        {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    public long getTimeElapsed()
    {
        return timeElapsed;
    }
}
