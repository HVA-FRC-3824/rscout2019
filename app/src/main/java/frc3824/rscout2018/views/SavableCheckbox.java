package frc3824.rscout2018.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rscout2018.R;

/**
 * @class SavableCheckbox
 * @brief Savable widget with a label and a checkbox
 */
public class SavableCheckbox extends LinearLayout
{
    CheckBox mCheckBox;

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public SavableCheckbox(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.savable_checkbox, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);

        // Set label
        TextView label = findViewById(R.id.label);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        typedArray.recycle();

        mCheckBox = findViewById(R.id.checkbox);
    }

    /**
     * Setter function for data binding
     * @param value The value of the checkbox
     */
    public void setBool(boolean value)
    {
        mCheckBox.setChecked(value);
    }

    /**
     * Getter function for data binding
     * @returns The value of the checkbox
     */
    public boolean getBool()
    {
        return mCheckBox.isChecked();
    }

    public void addListener(CompoundButton.OnCheckedChangeListener listener)
    {
        mCheckBox.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("bool")
    public static void setBool(SavableCheckbox savableCheckbox, boolean value)
    {
        savableCheckbox.setBool(value);
    }

    @InverseBindingAdapter(attribute = "bool")
    public static boolean getBool(SavableCheckbox savableCheckbox)
    {
        return savableCheckbox.getBool();
    }

    @BindingAdapter("boolAttrChanged")
    public static void setListener(SavableCheckbox savableCheckbox,
                                   CompoundButton.OnCheckedChangeListener oldListener,
                                   CompoundButton.OnCheckedChangeListener newListener)
    {
        if (newListener != null)
        {
            savableCheckbox.addListener(newListener);
        }
    }
}
