package frc3824.rscout2018.custom_charts.bar;

import com.github.mikephil.charting.data.BarEntry;

public class BarEntryWithTeamNumber extends BarEntry
{
    int mTeamNumber;

    public BarEntryWithTeamNumber(int x, int teamNumber, float y)
    {
        super(y, x);
        mTeamNumber = teamNumber;
    }

    public int getTeamNumber()
    {
        return mTeamNumber;
    }
}
