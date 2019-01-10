package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioGroup;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamMatchData;

/**
 * Created by frc3824
 */
public class IndividualCubes extends ConstraintLayout implements RadioGroup.OnCheckedChangeListener
{
    IndividualCubesInner mIndividualCubesInner;
    RadioGroup mRadioGroup;

    TeamMatchData mTeamMatchData;

    public IndividualCubes(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_individual_cubes, this, true);

        mIndividualCubesInner = findViewById(R.id.inner);

        if(mTeamMatchData != null)
        {
            mIndividualCubesInner.setTeamMatchData(mTeamMatchData);
        }

        mRadioGroup = findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    public void setTeamMatchData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
        if(mIndividualCubesInner != null)
        {
            mIndividualCubesInner.setTeamMatchData(mTeamMatchData);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.auto:
                mIndividualCubesInner.setAuto(true);
                return;
            case R.id.teleop:
                mIndividualCubesInner.setAuto(false);
                return;
            default:
                throw new AssertionError();
        }
    }
}
