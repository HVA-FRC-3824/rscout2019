package frc3824.rscout2018.custom_charts;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Map;

import frc3824.rscout2018.custom_charts.bar.BarEntryWithTeamNumber;
import frc3824.rscout2018.custom_charts.lld.LLDEntry;
import frc3824.rscout2018.database.data_models.LowLevelStats;
import frc3824.rscout2018.database.data_models.TeamMatchData;

/**
 * Created by frc3824
 */
public class SecondaryOption
{
    String mKey;
    boolean mBar;
    Filter mFilter;

    public SecondaryOption(String key, boolean bar, Filter filter)
    {
        mKey = key;
        mBar = bar;
        mFilter = filter;
    }

    public String getKey()
    {
        return mKey;
    }

    public boolean getBar()
    {
        return mBar;
    }

    public Filter getFiller()
    {
        return mFilter;
    }

    public Object fill(int i, int teamNumber, ArrayList<TeamMatchData> tmds)
    {
        if(mFilter != null)
        {
            if(mBar)
            {
                return new BarEntryWithTeamNumber(i, teamNumber, mFilter.createBarValue(tmds));
            }
            else
            {
                LowLevelStats lls = mFilter.createLlsValue(tmds);
                if(lls != null)
                {
                    return new LLDEntry(i,
                                        teamNumber,
                                        (float) lls.getMaximum(),
                                        (float) lls.getMinimum(),
                                        (float) lls.getAverage(),
                                        (float) lls.getStd());
                }
                else
                {
                    return new LLDEntry(i,
                                        teamNumber, 0, 0, 0, 0);
                }
                }
        }
        return null;
    }

    public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
    {
        if(mFilter != null)
        {
            return mFilter.sort(map);
        }
        return null;
    }

    public interface Filter
    {
        float createBarValue(ArrayList<TeamMatchData> tmds);

        LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds);

        ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map);
    }

}
