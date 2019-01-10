package frc3824.rscout2018.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rscout2018.R;

/**
 * @class SavableAutoCompleteTextView
 * @brief A savable widget that has a label and an AutoCompleteTextView
 */
public class SavableAutoCompleteTextView extends LinearLayout implements TextWatcher
{
    AutoCompleteTextView mAutoCompleteTextView;
    String[] mResourceStrings;
    String mText;

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public SavableAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.savable_autocompletetextview, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);

        // Set label
        TextView label = findViewById(R.id.label);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableAutoCompleteTextView);
        int autocompleteValueId = typedArray.getResourceId(R.styleable.SavableAutoCompleteTextView_options, 0);
        mResourceStrings = context.getResources().getStringArray(autocompleteValueId);
        typedArray.recycle();

        mAutoCompleteTextView = findViewById(R.id.autocomplete_textview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mResourceStrings);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.addTextChangedListener(this);
    }

    /**
     * Setter function for the data binding
     * @param text The text to set for the {@link AutoCompleteTextView}
     */
    public void setText(String text)
    {
        if(!text.equals(mText))
        {
            mText = text;
            mAutoCompleteTextView.setText(text);
        }
    }

    /**
     * Getter function for the data binding
     * @returns The text currently in the {@link AutoCompleteTextView}
     */
    public String getText()
    {
        return mAutoCompleteTextView.getText().toString();
    }


    /**
     * Adds a watcher that detects the text being editted
     * in the internal EditText
     */
    public void addListener(TextWatcher textWatcher)
    {
        mAutoCompleteTextView.addTextChangedListener(textWatcher);
    }

    /**
     * Removes the specified watcher
     */
    public void removeListener(TextWatcher textWatcher)
    {
        mAutoCompleteTextView.removeTextChangedListener(textWatcher);
    }

    /**
     * Needed for the data binding
     */
    @InverseBindingAdapter(attribute = "text", event = "textAttrChanged")
    public static String getText(SavableAutoCompleteTextView savableAutoCompleteTextView)
    {
        return savableAutoCompleteTextView.getText();
    }

    /**
     * Needed for the data binding
     */
    @BindingAdapter("text")
    public static void setText(SavableAutoCompleteTextView savableAutoCompleteTextView, String text)
    {
        savableAutoCompleteTextView.setText(text);
    }

    /**
     * Binds the "textAttrChanged" attribute value to the edit text
     *
     * In short it binds the listener for the specific value to the internal edit text
     * so it will update the variable in the data model.
     */
    @BindingAdapter(value = {"textAttrChanged"})
    public static void setTextWatcher(SavableAutoCompleteTextView savableAutoCompleteTextView,
                                      TextWatcher oldListener,
                                      TextWatcher newListener)
    {
        if (oldListener != null)
        {
            savableAutoCompleteTextView.removeListener(oldListener);
        }
        if (newListener != null)
        {
            savableAutoCompleteTextView.addListener(newListener);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        mText = s.toString();
    }
}
