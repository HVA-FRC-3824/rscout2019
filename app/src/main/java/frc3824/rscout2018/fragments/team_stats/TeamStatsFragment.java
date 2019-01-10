package frc3824.rscout2018.fragments.team_stats;

import android.app.Fragment;

/**
 * Created by frc3824.
 */
public abstract class TeamStatsFragment extends Fragment
{
    protected int mTeamNumber = -1;

    public void setTeamNumber(int teamNumber)
    {
        mTeamNumber = teamNumber;
        bind();
    }

    protected abstract void bind();
}
