package frc3824.rscout2018.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rscout2018.R;

/**
 * @class SavableEditText
 * @brief A savable widget that has a label and an EditText
 */
public class SavableEditText extends LinearLayout implements TextWatcher
{
    EditText mEditText;
    String mText = "";

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public SavableEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.savable_edittext, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        // Set label
        TextView label = findViewById(R.id.label);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        typedArray.recycle();

        mEditText = findViewById(R.id.edittext);
        mEditText.addTextChangedListener(this);
    }

    /**
     * Sets the text in the internal EditText
     * @param text
     */
    public void setText(String text)
    {
        if(!mText.equals(text))
        {
            mText = text;
            mEditText.setText(text);
        }
    }

    /**
     * Returns the text from the internal EditText
     */
    public String getText()
    {
        mText = mEditText.toString();
        return mText;
    }

    /**
     * Adds a watcher that detects the text being editted
     * in the internal EditText
     */
    public void addListener(TextWatcher textWatcher)
    {
        mEditText.addTextChangedListener(textWatcher);
    }

    /**
     * Removes the specified watcher
     */
    public void removeListener(TextWatcher textWatcher)
    {
        mEditText.removeTextChangedListener(textWatcher);
    }

    /**
     * Needed for the data binding
     */
    @InverseBindingAdapter(attribute = "text", event = "textAttrChanged")
    public static String getText(SavableEditText savableEditText)
    {
        return savableEditText.getText();
    }

    /**
     * Needed for the data binding
     */
    @BindingAdapter("text")
    public static void setText(SavableEditText savableEditText, String text)
    {
        savableEditText.setText(text);
    }

    /**
     * Binds the "textAttrChanged" attribute value to the edit text
     *
     * In short it binds the listener for the specific value to the internal edit text
     * so it will update the variable in the data model.
     */
    @BindingAdapter(value = {"textAttrChanged"})
    public static void setTextWatcher(SavableEditText savableEditText,
                                      TextWatcher oldListener,
                                      TextWatcher newListener)
    {
        if (oldListener != null)
        {
            savableEditText.removeListener(oldListener);
        }
        if (newListener != null)
        {
            savableEditText.addListener(newListener);
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
