package frc3824.rscout2018.fragments.match_scout;

import android.app.Fragment;

import frc3824.rscout2018.database.data_models.TeamMatchData;

/**
 * Created by frc3824
 */
public abstract class MatchScoutFragment extends Fragment
{
    protected TeamMatchData mTeamMatchData;

    /**
     * Sets the Team Match Data and call the abstract bind method
     * @param teamMatchData
     */
    public void setTeamMatchData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
        bind();
    }

    /**
     * Each derivative must implement this to bind the model to the views
     */
    protected abstract void bind();
}
