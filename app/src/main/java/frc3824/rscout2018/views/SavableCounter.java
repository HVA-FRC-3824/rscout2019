package frc3824.rscout2018.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rscout2018.R;

/**
 * @class SavableCounter
 * @brief
 */
public class SavableCounter extends LinearLayout implements View.OnClickListener, View.OnLongClickListener
{
    public interface CountListener
    {
        void onChange(int value);
    }

    Button mButton;
    Integer mCount;
    int mMax;
    int mMin;
    CountListener mListener = null;

    public SavableCounter(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.savable_counter, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);

        // Set label
        TextView label = findViewById(R.id.label);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableCounter);
        mMin = typedArray.getInt(R.styleable.SavableCounter_min, Integer.MIN_VALUE);
        mMax = typedArray.getInt(R.styleable.SavableCounter_max, Integer.MAX_VALUE);
        typedArray.recycle();

        mButton = findViewById(R.id.button);

        mCount = 0;
        mButton.setText(mCount.toString());
        mButton.setOnClickListener(this);
        mButton.setOnLongClickListener(this);
    }

    public void setCount(int count)
    {
        if(mCount != count)
        {
            mCount = count;
            mButton.setText(mCount.toString());
        }
    }

    public int getCount()
    {
        return mCount;
    }

    @Override
    public void onClick(View view)
    {
        if (mCount < mMax)
        {
            mCount++;
            mButton.setText(mCount.toString());
            if (mListener != null)
            {
                mListener.onChange(mCount);
            }
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        if (mCount > mMin)
        {
            mCount--;
            mButton.setText(mCount.toString());
            if (mListener != null)
            {
                mListener.onChange(mCount);
            }
        }
        return true;
    }

    public void setCountListener(CountListener countListener)
    {
        mListener = countListener;
    }

    @BindingAdapter("count")
    public static void setCount(SavableCounter savableCounter, int count)
    {
        savableCounter.setCount(count);
    }

    @InverseBindingAdapter(attribute = "count")
    public static int getCount(SavableCounter savableCounter)
    {
        return savableCounter.getCount();
    }

    @BindingAdapter("countAttrChanged")
    public static void setListener(SavableCounter savableCounter,
                                   CountListener oldListener,
                                   CountListener newListener)
    {
        savableCounter.setCountListener(newListener);
    }

}
