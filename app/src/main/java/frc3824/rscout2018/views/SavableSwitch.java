package frc3824.rscout2018.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import frc3824.rscout2018.R;

/**
 * @class SavableSwitch
 * @brief A savable widget that contains a switch
 */
public class SavableSwitch extends LinearLayout
{
    Switch mSwitch;

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public SavableSwitch(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.savable_switch, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);

        // Set label
        TextView label = findViewById(R.id.label);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        typedArray.recycle();

        mSwitch = findViewById(R.id.switch1);
    }

    /**
     * Setter function for the binding
     * @param value
     */
    public void setBool(boolean value)
    {
        mSwitch.setChecked(value);
    }

    /**
     * Getter function for the binding
     * @returns
     */
    public boolean getBool()
    {
        return mSwitch.isChecked();
    }

    public void addListener(CompoundButton.OnCheckedChangeListener listener)
    {
        mSwitch.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("bool")
    public static void setBool(SavableSwitch savableSwitch, boolean value)
    {
        savableSwitch.setBool(value);
    }

    @InverseBindingAdapter(attribute = "bool")
    public static boolean getBool(SavableSwitch savableSwitch)
    {
        return savableSwitch.getBool();
    }

    @BindingAdapter("boolAttrChanged")
    public static void setListener(SavableSwitch savableSwitch,
                                   CompoundButton.OnCheckedChangeListener oldListener,
                                   CompoundButton.OnCheckedChangeListener newListener)
    {
        if (newListener != null)
        {
            savableSwitch.addListener(newListener);
        }
    }
}
