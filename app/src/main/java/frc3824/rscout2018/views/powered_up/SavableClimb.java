package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.Arrays;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.utilities.Constants;

/**
 * Created by frc3824
 */
public class SavableClimb extends RelativeLayout implements RadioGroup.OnCheckedChangeListener
{
    Timer mTimer;
    RadioGroup mStatus;
    RadioGroup mMethod;
    TeamMatchData mTeamMatchData;

    public SavableClimb(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.savable_climb, this, true);

        mTimer = findViewById(R.id.timer);
        mTimer.setButtonListener(new ClimbTimerButtonListener());
        mStatus = findViewById(R.id.status);
        mStatus.setOnCheckedChangeListener(this);

        for (int i = 0; i < Constants.MatchScouting.Climb.Status.OPTIONS.length; i++)
        {
            RadioButton radioButton = new RadioButton(context, attrs);
            radioButton.setText(Constants.MatchScouting.Climb.Status.OPTIONS[i]);
            radioButton.setId(i);
            mStatus.addView(radioButton);
        }

        mMethod = findViewById(R.id.method);
        mMethod.setOnCheckedChangeListener(this);
        for (int i = 0; i < Constants.MatchScouting.Climb.Method.OPTIONS.length; i++)
        {
            RadioButton radioButton = new RadioButton(context, attrs);
            radioButton.setText(Constants.MatchScouting.Climb.Method.OPTIONS[i]);
            radioButton.setId(i);
            radioButton.setEnabled(false);
            mMethod.addView(radioButton);
        }
    }

    public void setData(TeamMatchData teamMatchData)
    {
        mStatus.setOnCheckedChangeListener(null);
        mMethod.setOnCheckedChangeListener(null);
        mTeamMatchData = teamMatchData;
        mTimer.setTime(mTeamMatchData.getClimbTime());
        int statusIndex = Arrays.asList(Constants.MatchScouting.Climb.Status.OPTIONS)
                                .indexOf(mTeamMatchData.getClimbStatus());

        mStatus.check(statusIndex);
        if (statusIndex < Constants.MatchScouting.Climb.Status.OPTIONS.length - 1)
        {
            for (int i = 0; i < mMethod.getChildCount(); i++)
            {
                mMethod.getChildAt(i).setEnabled(false);
            }
            mMethod.check(-1);
        }
        else
        {
            for (int i = 0; i < mMethod.getChildCount(); i++)
            {
                mMethod.getChildAt(i).setEnabled(true);
            }
        }
        //mMethod.clearCheck();
        mMethod.check(Arrays.asList(Constants.MatchScouting.Climb.Method.OPTIONS)
                            .indexOf(mTeamMatchData.getClimbMethod()));

        mStatus.setOnCheckedChangeListener(this);
        mMethod.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (group == mStatus && checkedId >= 0)
        {
            mTeamMatchData.setClimbStatus(Constants.MatchScouting.Climb.Status.OPTIONS[checkedId]);

            if (checkedId < Constants.MatchScouting.Climb.Status.OPTIONS.length - 1)
            {
                for (int i = 0; i < mMethod.getChildCount(); i++)
                {
                    mMethod.getChildAt(i).setEnabled(false);
                }
                mMethod.check(-1);
                mTeamMatchData.setClimbTime(-1);
            }
            else
            {
                for (int i = 0; i < mMethod.getChildCount(); i++)
                {
                    mMethod.getChildAt(i).setEnabled(true);
                }
            }
        }
        else if (checkedId >= 0)
        {
            mTeamMatchData.setClimbMethod(Constants.MatchScouting.Climb.Method.OPTIONS[checkedId]);
        }
    }

    private class ClimbTimerButtonListener implements Timer.ButtonListener
    {

        @Override
        public void onStart()
        {
            mStatus.check(Constants.MatchScouting.Climb.Status.OPTIONS.length - 1);
            for (int i = 0; i < mStatus.getChildCount(); i++)
            {
                mStatus.getChildAt(i).setEnabled(false);
            }
            mTeamMatchData.setClimbStatus(Constants.MatchScouting.Climb.Status.CLIMB);
        }

        @Override
        public void onStop(long time)
        {
            mTeamMatchData.setClimbTime(time);
        }

        @Override
        public void onReset()
        {
            mTeamMatchData.setClimbTime(-1);
            mTeamMatchData.setClimbStatus("");
            mTeamMatchData.setClimbMethod("");

            mStatus.check(-1);
            for (int i = 0; i < mStatus.getChildCount(); i++)
            {
                mStatus.getChildAt(i).setEnabled(true);
            }
            for (int i = 0; i < mMethod.getChildCount(); i++)
            {
                mMethod.getChildAt(i).setEnabled(false);
            }
            mMethod.check(-1);
        }
    }
}
