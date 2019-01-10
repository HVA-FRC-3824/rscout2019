package frc3824.rscout2018.fragments.match_scout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.databinding.FragmentMatchMiscBinding;
import frc3824.rscout2018.utilities.Utilities;

/**
 * @class MatchMiscFragment
 * @brief Fragment used to record information about how a team performed that is not in any of the other fragments
 */
public class MatchMiscFragment extends MatchScoutFragment
{
    FragmentMatchMiscBinding mBinding = null;

    @Override
    protected void bind()
    {
        if(mTeamMatchData != null && mBinding != null)
        {
            mBinding.setTmd(mTeamMatchData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate layout and bind the realm object
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_misc, container, false);
        bind();
        View view = mBinding.getRoot();

        // Add touch listeners
        Utilities.setupUi(getActivity(), view);

        return view;
    }
}
