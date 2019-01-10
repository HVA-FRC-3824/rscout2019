package frc3824.rscout2018.fragments.pit_scout;

import android.app.Fragment;

import frc3824.rscout2018.database.data_models.TeamPitData;

/**
 * Created by frc3824
 */
public abstract class PitScoutFragment extends Fragment
{
    protected TeamPitData mTeamPitData;

    public void setTeamPitData(TeamPitData teamPitData)
    {
        mTeamPitData = teamPitData;
        if(mTeamPitData != null)
        {
            bind();
        }
    }

    protected abstract void bind();
}