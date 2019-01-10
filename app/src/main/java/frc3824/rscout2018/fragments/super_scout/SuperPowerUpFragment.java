package frc3824.rscout2018.fragments.super_scout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.databinding.FragmentSuperPowerupsBinding;
import frc3824.rscout2018.utilities.Utilities;

/**
 * Created by frc3824.
 */
public class SuperPowerUpFragment extends SuperScoutFragment
{
    FragmentSuperPowerupsBinding mBinding = null;
    private final static String TAG = "SuperNotesFragment";

    public SuperPowerUpFragment()
    {}

    protected void bind()
    {
        if(mSuperMatchData != null && mBinding != null)
        {
            mBinding.setSmd(mSuperMatchData);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate layout and bind the realm object
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_super_powerups, container, false);
        bind();
        View view = mBinding.getRoot();

        Utilities.setupUi(getActivity(), view);

        return view;
    }

}
