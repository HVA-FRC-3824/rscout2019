package frc3824.rscout2018.fragments.match_scout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.databinding.FragmentMatchFoulsBinding;
import frc3824.rscout2018.utilities.Utilities;

/**
 * @class MatchFoulsFragment
 * @brief Fragment used to record information about the fouls a team causes
 */
public class MatchFoulsFragment extends MatchScoutFragment
{
    FragmentMatchFoulsBinding mBinding = null;

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_fouls, container, false);
        bind();
        View view = mBinding.getRoot();

        // Add touch listeners
        Utilities.setupUi(getActivity(), view);

        return view;
    }
}
