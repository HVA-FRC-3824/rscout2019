package frc3824.rscout2018.fragments.super_scout;

import android.app.Fragment;

import frc3824.rscout2018.database.data_models.SuperMatchData;

/**
 * Created by frc3824
 */
public abstract class SuperScoutFragment extends Fragment
{
    protected SuperMatchData mSuperMatchData;

    public void setSuperMatchData(SuperMatchData superMatchData)
    {
        mSuperMatchData = superMatchData;
        if(mSuperMatchData != null)
        {
            bind();
        }
    }

    protected abstract void bind();
}
