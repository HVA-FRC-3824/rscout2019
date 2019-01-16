package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.Team;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.powered_up.CubeEvent;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.utilities.Utilities;

import static java.lang.String.format;

/**
 * Created by frc3824
 */
public class CubesView extends ConstraintLayout implements RadioGroup.OnCheckedChangeListener
{
    CubesInnerView mCubes;
    RadioGroup mOption;

    SparseArray<TeamMatchData> mMatches;

    ArrayList<Pair<Float, Float>> mAutoPickUp;
    ArrayList<Pair<Float, Float>> mAutoPlaced;
    ArrayList<Pair<Float, Float>> mAutoDropped;
    //ArrayList<Pair<Float, Float>> mAutoLaunchSuccess;
    ArrayList<Pair<Float, Float>> mAutoLaunchFailure;

    ArrayList<Pair<Float, Float>> mTeleopPlaced;
    ArrayList<Pair<Float, Float>> mTeleopPickUp;
    ArrayList<Pair<Float, Float>> mTeleopDropped;
    //ArrayList<Pair<Float, Float>> mTeleopLaunchSuccess;
    ArrayList<Pair<Float, Float>> mTeleopLaunchFailure;

    TextView mVaultCycleTime;
    TextView mSwitchCycleTime;
    TextView mScaleCycleTime;

    public CubesView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_cubes, this, true);

        mCubes = findViewById(R.id.locations);

        mOption = findViewById(R.id.options);
        mOption.setOnCheckedChangeListener(this);

        mVaultCycleTime = findViewById(R.id.vault_cycle);
        mSwitchCycleTime = findViewById(R.id.switch_cycle);
        mScaleCycleTime = findViewById(R.id.scale_cycle);

        if(mMatches != null)
        {
            new UpdateTask().execute();
        }
    }

    public void setTeam(Team team)
    {
        mMatches = team.getMatches();
        if(mCubes != null)
        {
            new UpdateTask().execute();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if(checkedId == -1)
        {
            return;
        }

        switch (checkedId)
        {
            case R.id.auto_pickup:
                if(mAutoPickUp != null)
                {
                    mCubes.setData(mAutoPickUp);
                }
                return;
            case R.id.auto_placed:
                if(mAutoPlaced != null)
                {
                    mCubes.setData(mAutoPlaced);
                }
                return;
            case R.id.auto_dropped:
                if(mAutoDropped != null)
                {
                    mCubes.setData(mAutoDropped);
                }
                return;
                /*
            case R.id.auto_launch_success:
                if(mAutoLaunchSuccess != null)
                {
                    mCubes.setData(mAutoLaunchSuccess);
                }
                return;
                */
            case R.id.auto_launch_failure:
                if(mAutoLaunchFailure != null)
                {
                    mCubes.setData(mAutoLaunchFailure);
                }
                return;
            case R.id.teleop_pickup:
                if(mTeleopPickUp != null)
                {
                    mCubes.setData(mTeleopPickUp);
                }
                return;
            case R.id.teleop_placed:
                if(mTeleopPlaced != null)
                {
                    mCubes.setData(mTeleopPlaced);
                }
                return;
            case R.id.teleop_dropped:
                if(mTeleopDropped != null)
                {
                    mCubes.setData(mTeleopDropped);
                }
                return;
                /*
            case R.id.teleop_launch_success:
                if(mTeleopLaunchSuccess != null)
                {
                    mCubes.setData(mTeleopLaunchSuccess);
                }
                return;
                */
            case R.id.teleop_launch_failure:
                if(mTeleopLaunchFailure != null)
                {
                    mCubes.setData(mTeleopLaunchFailure);
                }
                return;
            default:
                throw new AssertionError();
        }
    }

    private class UpdateTask extends AsyncTask
    {
        long vaultCycleSum = 0;
        long switchCycleSum = 0;
        long scaleCycleSum = 0;
        int vaultCycleNum = 0;
        int switchCycleNum = 0;
        int scaleCycleNum = 0;

        @Override
        protected Object doInBackground(Object[] objects)
        {
            mAutoPickUp = new ArrayList<>();
            mAutoPlaced = new ArrayList<>();
            mAutoDropped = new ArrayList<>();
            //mAutoLaunchSuccess = new ArrayList<>();
            mAutoLaunchFailure = new ArrayList<>();

            mTeleopPickUp = new ArrayList<>();
            mTeleopPlaced = new ArrayList<>();
            mTeleopDropped = new ArrayList<>();
            //mTeleopLaunchSuccess = new ArrayList<>();
            mTeleopLaunchFailure = new ArrayList<>();

            for(int matchIndex = 0, end = mMatches.size(); matchIndex < end; matchIndex++)
            {
                TeamMatchData tmd = mMatches.valueAt(matchIndex);
                // Shouldn't ever happen
                if(tmd == null)
                {
                    continue;
                }

                for(CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    switch (cubeEvent.getEvent())
                    {
                        case Constants.MatchScouting.CubeEvents.PICK_UP_CARGO:
                        case Constants.MatchScouting.CubeEvents.PICK_UP_HATCH:
                            mAutoPickUp.add(new Pair<>(cubeEvent.getLocationX(), cubeEvent.getLocationY()));
                            break;

                        case Constants.MatchScouting.CubeEvents.PLACED_HIGH:
                        case Constants.MatchScouting.CubeEvents.PLACED_MEDIUM:
                        case Constants.MatchScouting.CubeEvents.PLACED_LOW:
                            mAutoPlaced.add(new Pair<>(cubeEvent.getLocationX(), cubeEvent.getLocationY()));
                            break;

                        case Constants.MatchScouting.CubeEvents.DROPPED:
                            mAutoDropped.add(new Pair<>(cubeEvent.getLocationX(), cubeEvent.getLocationY()));
                            break;

                        default:
                            throw new AssertionError();
                    }
                }

                long time;
                double distance;
                boolean first = true;
                CubeEvent pickup = null;
                ArrayList<CubeEvent> teleopCubeEvents = tmd.getTeleopCubeEvents();
                for (int i = 0; i < teleopCubeEvents.size(); i++)
                {
                    CubeEvent cubeEvent = teleopCubeEvents.get(i);

                    if (i == 0 && (cubeEvent.getEvent()
                                           .equals(Constants.MatchScouting.CubeEvents.PICK_UP_CARGO) || cubeEvent.getEvent()
                            .equals(Constants.MatchScouting.CubeEvents.PICK_UP_HATCH)))
                    {
                        first = false;
                    }
                    switch (cubeEvent.getEvent())
                    {
                        case Constants.MatchScouting.CubeEvents.PICK_UP_CARGO:
                        case Constants.MatchScouting.CubeEvents.PICK_UP_HATCH:
                            mTeleopPickUp.add(new Pair<>(cubeEvent.getLocationX(), cubeEvent.getLocationY()));
                            if (pickup == null)
                            {
                                pickup = cubeEvent;
                            }
                            break;
                        case Constants.MatchScouting.CubeEvents.PLACED_LOW:
                        case Constants.MatchScouting.CubeEvents.PLACED_MEDIUM:
                        case Constants.MatchScouting.CubeEvents.PLACED_HIGH:
                            mTeleopPlaced.add(new Pair<>(cubeEvent.getLocationX(), cubeEvent.getLocationY()));
                            if (!first)
                            {

                                time = cubeEvent.getTime() - pickup.getTime();

                                if (Utilities.isExchange(cubeEvent.getLocationX()))
                                {
                                    vaultCycleSum += time;
                                    vaultCycleNum++;
                                }
                                else if (Utilities.isSwitch(cubeEvent.getLocationX()))
                                {
                                    switchCycleSum += time;
                                    switchCycleNum++;
                                }
                                else if(Utilities.isScale(cubeEvent.getLocationX()))
                                {
                                    scaleCycleSum += time;
                                    scaleCycleNum++;
                                }
                                else
                                {
                                    // Should not be possible
                                    assert(false);
                                }
                            }
                            pickup = null;
                            first = false;
                            break;
                        case Constants.MatchScouting.CubeEvents.DROPPED:
                            mTeleopDropped.add(new Pair<>(cubeEvent.getLocationX(), cubeEvent.getLocationY()));
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
            }

            publishProgress();

            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects)
        {
            if(vaultCycleNum == 0)
            {
                mVaultCycleTime.setText("N/A");
            }
            else
            {
                mVaultCycleTime.setText(format(Locale.US, "%.2fs", (double) vaultCycleSum / 1000.0f / (double) vaultCycleNum));
            }

            if(switchCycleNum == 0)
            {
                mSwitchCycleTime.setText("N/A");
            }
            else
            {
                mSwitchCycleTime.setText(format(Locale.US, "%.2fs", (double) switchCycleSum / 1000.0f / (double) switchCycleNum));
            }

            if(scaleCycleNum == 0)
            {
                mScaleCycleTime.setText("N/A");
            }
            else
            {
                mScaleCycleTime.setText(format(Locale.US, "%.2fs", (double) scaleCycleSum / 1000.0f / (double) scaleCycleNum));
            }

            onCheckedChanged(mOption, mOption.getCheckedRadioButtonId());
        }
    }
}
