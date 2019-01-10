package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TextView;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamMatchData;

/**
 * Created by frc3824.
 */

public class IndividualFouls extends TableLayout
{
    TextView mFouls;
    TextView mTechFouls;

    TeamMatchData mTeamMatchData;

    public IndividualFouls(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_individual_fouls, this, true);

        mFouls = findViewById(R.id.inner_fouls);
        mTechFouls = findViewById(R.id.inner_tech_fouls);
    }

    public void setTeamMatchData(TeamMatchData teamMatchData)
    {
        mTeamMatchData = teamMatchData;
    }

    public void update()
    {
        if(mTeamMatchData != null)
        {
            mFouls.setText(String.valueOf(mTeamMatchData.getFouls()));
            mTechFouls.setText(String.valueOf(mTeamMatchData.getTechFouls()));
        }
    }
}
