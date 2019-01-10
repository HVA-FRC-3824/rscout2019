package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Locale;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamMatchData;

import static java.lang.String.format;

/**
 * Created by frc3824
 */

public class IndividualClimb extends TableLayout
{
    TextView mTime;
    TextView mStatus;
    TextView mMethod;

    TeamMatchData mTeamMatchData;

    public IndividualClimb(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_individual_climb, this, true);

        mStatus = findViewById(R.id.status);
        mMethod = findViewById(R.id.method);
        mTime = findViewById(R.id.time);

        update();
    }

    public void setTeamMatchData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
        //update();
    }

    private void update()
    {
        if(mTeamMatchData != null && mStatus != null)
        {
            mStatus.setText(mTeamMatchData.getClimbStatus());
            if (mTeamMatchData.getClimbMethod() != null && !mTeamMatchData.getClimbMethod()
                                                                          .isEmpty())
            {
                mMethod.setText(mTeamMatchData.getClimbMethod());
            }
            if (mTeamMatchData.getClimbTime() > 0)
            {
                mTime.setText(format(Locale.US, "%.2fs", (float)mTeamMatchData.getClimbTime() / 1000.0));
            }
        }
    }

}
