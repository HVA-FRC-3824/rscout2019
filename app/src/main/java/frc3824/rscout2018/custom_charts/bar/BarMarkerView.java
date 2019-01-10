package frc3824.rscout2018.custom_charts.bar;


import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import frc3824.rscout2018.R;
import frc3824.rscout2018.utilities.Utilities;

public class BarMarkerView extends MarkerView
{
    TextView mTeamNumber;
    TextView mTotal;

    int mScreenWidth;
    Context mContext;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public BarMarkerView(Context context)
    {
        super(context, R.layout.marker_bar);
        mContext = context;

        mTeamNumber = findViewById(R.id.team_number);
        mTotal = findViewById(R.id.total);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
    }

    public void refreshContent(Entry e, Highlight highlight)
    {
        BarEntryWithTeamNumber b = (BarEntryWithTeamNumber) e;
        mTeamNumber.setText(String.valueOf(b.getTeamNumber()));
        mTotal.setText(String.valueOf(b.getVal()));
    }

    @Override
    public int getXOffset(float xpos)
    {
        if(xpos + getWidth() > mScreenWidth)
        {
            return (int)(mScreenWidth - xpos -getWidth() - Utilities.dpToPixels(mContext, 16));
        }

        return 0;
    }

    @Override
    public int getYOffset(float ypos)
    {
        return -getHeight();
    }


}
