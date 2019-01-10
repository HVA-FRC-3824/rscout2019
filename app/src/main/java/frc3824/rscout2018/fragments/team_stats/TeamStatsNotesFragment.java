package frc3824.rscout2018.fragments.team_stats;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.NotesStorage;
import frc3824.rscout2018.database.data_models.SuperMatchData;
import frc3824.rscout2018.database.data_models.Team;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.databinding.FragmentTeamStatsNotesBinding;

import static java.lang.String.format;

/**
 * @class TeamStatsNotesFragment
 * @brief A fragment for displaying the notes taken during a team's matches about its performance
 */
public class TeamStatsNotesFragment extends TeamStatsFragment
{
    FragmentTeamStatsNotesBinding mBinding;
    NotesStorage mNotes = new NotesStorage();

    protected void bind()
    {
        new UpdateTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_team_stats_notes,
                                           null,
                                           false);
        mBinding.setNotes(mNotes);
        bind();
        return mBinding.getRoot();
    }

    private class UpdateTask extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects)
        {
            Team team = new Team(mTeamNumber);

            StringBuilder matchNotesText = new StringBuilder();
            SparseArray<TeamMatchData> matches = team.getMatches();
            ArrayList<Integer> matchNumbers = new ArrayList();
            for (int i = 0, end = matches.size(); i < end; i++)
            {
                matchNumbers.add(matches.keyAt(i));
            }
            Collections.sort(matchNumbers);

            for (int matchNumber : matchNumbers)
            {
                TeamMatchData tmd = matches.get(matchNumber);
                if (tmd.getNotes() != null && !tmd.getNotes().isEmpty())
                {
                    matchNotesText.append(format(Locale.US,
                                                 "Match %d:\n\t%s\n",
                                                 tmd.getMatchNumber(),
                                                 tmd.getNotes()));
                }
            }
            mNotes.setMatchNotes(matchNotesText.toString());

            StringBuilder superNotesText = new StringBuilder();
            SparseArray<SuperMatchData> superMatches = team.getSuperMatches();
            matchNumbers = new ArrayList();
            for (int i = 0, end = matches.size(); i < end; i++)
            {
                matchNumbers.add(superMatches.keyAt(i));
            }
            Collections.sort(matchNumbers);
            for (int matchNumber : matchNumbers)
            {
                SuperMatchData smd = superMatches.get(matchNumber);
                if (smd.getNotes() != null && !smd.getNotes().isEmpty())
                {
                    superNotesText.append(format(Locale.US,
                                                 "Match %d:\n\t%s\n",
                                                 smd.getMatchNumber(),
                                                 smd.getNotes()));
                }
            }
            mNotes.setSuperNotes(superNotesText.toString());

            return null;
        }
    }

}
