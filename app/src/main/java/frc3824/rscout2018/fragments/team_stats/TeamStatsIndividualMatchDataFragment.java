package frc3824.rscout2018.fragments.team_stats;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.views.powered_up.IndividualClimb;
import frc3824.rscout2018.views.powered_up.IndividualCubes;
import frc3824.rscout2018.views.powered_up.IndividualFouls;
import frc3824.rscout2018.views.powered_up.IndividualStart;

/**
 * Created by frc3824
 */
public class TeamStatsIndividualMatchDataFragment extends Fragment
{
    int mTeamNumber;
    int mMatchNumber;
    TeamMatchData mTeamMatchData;

    View mView;
    IndividualStart mIndividualStart;
    IndividualCubes mIndividualCubes;
    IndividualClimb mIndividualClimb;
    IndividualFouls mIndividualFouls;


    public void setTeamMatchNumber(int teamNumber, int matchNumber)
    {
        mTeamNumber = teamNumber;
        mMatchNumber = matchNumber;
        mTeamMatchData = Database.getInstance().getTeamMatchData(teamNumber, matchNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_team_stats_individual_match_data, null);

        mIndividualStart = mView.findViewById(R.id.start);
        mIndividualCubes = mView.findViewById(R.id.cubes);
        mIndividualClimb = mView.findViewById(R.id.climb);
        mIndividualFouls = mView.findViewById(R.id.fouls);

        new UpdateTask().execute();

        return mView;
    }

    private class UpdateTask extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects)
        {
            if(mTeamMatchData != null && mIndividualStart != null)
            {
                mIndividualStart.setTeamMatchData(mTeamMatchData);
                mIndividualCubes.setTeamMatchData(mTeamMatchData);
                mIndividualClimb.setTeamMatchData(mTeamMatchData);
                mIndividualFouls.setTeamMatchData(mTeamMatchData);

                publishProgress();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects)
        {
            if(mTeamMatchData.isRedCard())
            {
                mView.setBackgroundColor(Color.RED);
                mView.invalidate();
            }
            else if(mTeamMatchData.isYellowCard())
            {
                mView.setBackgroundColor(Color.YELLOW);
                mView.invalidate();
            }
            mIndividualFouls.update();
        }
    }
}
