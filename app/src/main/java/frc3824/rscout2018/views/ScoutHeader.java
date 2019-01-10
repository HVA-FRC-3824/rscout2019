package frc3824.rscout2018.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rscout2018.R;

/**
 * @class ScoutHeader
 *
 * Header for the various scout activities
 */
public class ScoutHeader extends LinearLayout implements View.OnClickListener
{
    ScoutHeaderInterface mInterface;

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public ScoutHeader(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.header_scout, this);

        // Set each of the buttons to use the onClick function down below
        findViewById(R.id.previous).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.list).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
    }

    /**
     * Sets the title for the header
     *
     * @param title The title to display
     */
    public void setTitle(String title)
    {
        ((TextView) findViewById(R.id.title)).setText(title);
    }

    /**
     * Sets the interface with the functions for the various header buttons
     *
     * @param interface_ The interface with the functions for each header button
     */
    public void setInterface(ScoutHeaderInterface interface_)
    {
        mInterface = interface_;
    }

    /**
     * Removes the previous button from the header
     */
    public void removePrevious()
    {
        findViewById(R.id.previous).setVisibility(INVISIBLE);
    }

    /**
     * Displays the previous button in the header
     */
    public void addPrevious() {
        findViewById(R.id.previous).setVisibility(VISIBLE);
    }

    /**
     * Removes the next button from the header
     */
    public void removeNext()
    {
        findViewById(R.id.next).setVisibility(INVISIBLE);
    }

    /**
     * Displays the next button in the header
     */
    public void addNext() {
        findViewById(R.id.next).setVisibility(VISIBLE);
    }

    /**
     * Removes the save button from the header
     */
    public void removeSave()
    {
        findViewById(R.id.save).setVisibility(INVISIBLE);
    }

    /**
     * Removes the list button from the header
     */
    public void removeList()
    {
        findViewById(R.id.list).setVisibility(INVISIBLE);
    }

    /**
     * A button has been clicked
     *
     * @param view The button
     */
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.previous:
                mInterface.previous();
                break;
            case R.id.next:
                mInterface.next();
                break;
            case R.id.home:
                mInterface.home();
                break;
            case R.id.list:
                mInterface.list();
                break;
            case R.id.save:
                mInterface.save();
                break;
        }
    }
}
