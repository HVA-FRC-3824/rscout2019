package frc3824.rscout2018.fragments.match_preview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.utilities.Utilities;

/**
 * @class MatchAlliancePreviewFragment
 * @brief A fragment for the match preview that shows how how an alliance should stack up
 */
public abstract class MatchAlliancePreviewFragment extends Fragment
{
    int mMatchNumber;

    public void setMatchNumber(int matchNumber)
    {
        mMatchNumber = matchNumber;
    }

    protected abstract int offset();

    protected abstract int color();

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate layout and bind the realm object
        View view = inflater.inflate(R.layout.fragment_match_alliance_preview, container, false);

        FragmentManager fm = getChildFragmentManager();

        view.setBackgroundColor(color());

        MatchTeamPreviewFragment team1 = (MatchTeamPreviewFragment)fm.findFragmentById(R.id.team1);
        MatchTeamPreviewFragment team2 = (MatchTeamPreviewFragment)fm.findFragmentById(R.id.team2);
        MatchTeamPreviewFragment team3 = (MatchTeamPreviewFragment)fm.findFragmentById(R.id.team3);

        MatchLogistics match = Database.getInstance().getMatchLogistics(mMatchNumber);
        team1.setTeamNumber(match.getTeamNumber(offset()));
        team2.setTeamNumber(match.getTeamNumber(1 + offset()));
        team3.setTeamNumber(match.getTeamNumber(2 + offset()));

        // Add touch listeners
        Utilities.setupUi(getActivity(), view);

        return view;
    }
}
