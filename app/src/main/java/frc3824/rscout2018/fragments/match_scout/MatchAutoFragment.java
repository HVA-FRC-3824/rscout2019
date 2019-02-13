package frc3824.rscout2018.fragments.match_scout;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import frc3824.rscout2018.R;
import frc3824.rscout2018.databinding.FragmentMatchAutoBinding;
import frc3824.rscout2018.utilities.Utilities;
import frc3824.rscout2018.views.powered_up.SavableCubes;

/**
 * @class MatchAutoFragment
 * @brief Fragment used to record information about how a team performed during the autonomous period
 */
public class MatchAutoFragment extends MatchScoutFragment
{
    FragmentMatchAutoBinding mBinding = null;
    SavableCubes mCubes = null;


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
        // Inflate layout and bind the data model
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_auto, container, false);
        bind();
        View view = mBinding.getRoot();

        // Inflate the cubes view
        mCubes = view.findViewById(R.id.cubes);
        mCubes.setAuto(true);

        // Inflate the undo button and pass it to the cubes view
        Button undo = view.findViewById(R.id.undo);
        undo.setVisibility(View.GONE);
        mCubes.setUndoButton(undo);

        // Add touch listeners
        Utilities.setupUi(getActivity(), view);

        return view;
    }

}
