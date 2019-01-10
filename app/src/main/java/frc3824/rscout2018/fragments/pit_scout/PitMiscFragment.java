package frc3824.rscout2018.fragments.pit_scout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.databinding.FragmentPitMiscBinding;
import frc3824.rscout2018.utilities.Utilities;

/**
 * @class PitMiscFragment
 * @brief A fragment used to record miscellaneous information about a team's robot
 */
public class PitMiscFragment extends PitScoutFragment
{
    FragmentPitMiscBinding mBinding = null;

    /**
     * Sets the data model for binding
     */
    @Override
    protected void bind()
    {
        if(mTeamPitData != null && mBinding != null)
        {
            mBinding.setTpd(mTeamPitData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate layout and bind the realm object
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pit_misc, container, false);
        bind();
        View view = mBinding.getRoot();

        // Add touch listeners
        Utilities.setupUi(getActivity(), view);

        return view;
    }
}
