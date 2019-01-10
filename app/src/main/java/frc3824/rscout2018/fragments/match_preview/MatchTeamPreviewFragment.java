package frc3824.rscout2018.fragments.match_preview;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import activitystarter.Arg;
import frc3824.rscout2018.R;
import frc3824.rscout2018.activities.TeamStatsActivityStarter;
import frc3824.rscout2018.database.data_models.MatchPreviewTeam;
import frc3824.rscout2018.database.data_models.Team;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.TeamPitData;
import frc3824.rscout2018.database.data_models.powered_up.CubeEvent;
import frc3824.rscout2018.databinding.FragmentMatchTeamPreviewBinding;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.utilities.Utilities;

/**
 * @class MatchTeamPreviewFragment
 * @brief The fragment which displays the highlights for a specific
 *        team during the preview of a match
 */
public class MatchTeamPreviewFragment extends Fragment implements OnClickListener, ImageListener
{
    @Arg
    boolean mRed;
    Team mTeam;

    FragmentMatchTeamPreviewBinding mBinding;
    MatchPreviewTeam mPreviewTeam;
    View mView;

    TeamPitData mTeamPitData = null;
    ArrayList<String> mPictureFilepaths = null;
    CarouselView mCarouselView = null;

    public void setTeamNumber(int teamNumber)
    {
        mTeam = new Team(teamNumber);
        mPreviewTeam = new MatchPreviewTeam();
        mPreviewTeam.setTeamNumber(teamNumber);

        mTeamPitData = mTeam.getPit();
        setupCarousel();


        if (mView != null)
        {
            mBinding.setTeam(mPreviewTeam);
            new UpdateTask().execute();
        }
    }

    private void setupCarousel()
    {
        if(mTeamPitData != null)
        {
            mPictureFilepaths = mTeamPitData.getPictureFilepaths();

            if(mCarouselView != null)
            {
                // If there are pictures then display the default image
                if (mTeamPitData.numberOfPictures() > 0)
                {
                    String defaultFilepath = mTeamPitData.getDefaultPictureFilepath();
                    mCarouselView.setPageCount(mPictureFilepaths.size());

                    // If there is a default image
                    if (!defaultFilepath.isEmpty())
                    {
                        int index = mPictureFilepaths.indexOf(defaultFilepath);
                        // Show the default image
                        if (index > -1)
                        {
                            mCarouselView.setCurrentItem(index);
                        }
                        // if the default picture isn't in the list then show the first one
                        else
                        {
                            mCarouselView.setCurrentItem(0);
                        }
                    }
                    // if there is no default picture then show the first one
                    else
                    {
                        mCarouselView.setCurrentItem(0);
                    }
                }
                // Otherwise hide the carousel and N/A buttons
                else
                {
                    mCarouselView.setVisibility(View.GONE);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {

        mBinding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_match_team_preview,
                                           container,
                                           false);
        if (mPreviewTeam != null)
        {
            mBinding.setTeam(mPreviewTeam);
            new UpdateTask().execute();
        }
        mView = mBinding.getRoot();

        // Inflate the carousel
        mCarouselView = mView.findViewById(R.id.carousel);
        mCarouselView.setImageListener(this);


       setupCarousel();

        mView.findViewById(R.id.view_team).setOnClickListener(this);

        // Add touch listeners
        Utilities.setupUi(getActivity(), mView);

        return mView;
    }

    @Override
    public void onClick(View v)
    {
        TeamStatsActivityStarter.start(getActivity(), mTeam.getTeamNumber());
    }

    @Override
    public void setImageForPosition(int position, ImageView imageView)
    {
        if(position >= 0 && position < mPictureFilepaths.size())
        {
            Glide.with(this).load(mPictureFilepaths.get(position)).into(imageView);
        }
    }

    private class UpdateTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects)
        {
            SparseArray<TeamMatchData> matches = mTeam.getMatches();
            for (int matchIndex = 0, end = matches.size(); matchIndex < end; matchIndex++)
            {
                TeamMatchData teamMatchData = matches.valueAt(matchIndex);
                mPreviewTeam.setYellowCard(teamMatchData.isYellowCard());
                mPreviewTeam.setRedCard(teamMatchData.isRedCard());

                if(teamMatchData.getCrossedAutoLine())
                {
                    mPreviewTeam.incrementAutoCross();
                }

                mPreviewTeam.addToFouls(teamMatchData.getFouls());
                mPreviewTeam.addToTechFouls(teamMatchData.getTechFouls());

                for (CubeEvent cubeEvent : teamMatchData.getAutoCubeEvents())
                {
                    switch (cubeEvent.getEvent())
                    {
                        case Constants.MatchScouting.CubeEvents.DROPPED:
                            mPreviewTeam.incrementNumDrops();
                            break;
                        case Constants.MatchScouting.CubeEvents.LAUNCH_FAILURE:
                            mPreviewTeam.incrementNumLaunchFails();
                            if (cubeEvent.getLocationX() < Constants.TeamStats.Cubes.EXCHANGE_THESHOLD ||
                                    cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.EXCHANGE_THESHOLD)
                            {
                                // exchange
                            }
                            else if (cubeEvent.getLocationX() < Constants.TeamStats.Cubes.SWITCH_THRESHOlD ||
                                    cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.SWITCH_THRESHOlD)
                            {
                                mPreviewTeam.incrementAutoSwitchFailures();
                            }
                            else
                            {
                                mPreviewTeam.incrementAutoScaleFailures();
                            }
                            break;
                        case Constants.MatchScouting.CubeEvents.PLACED:
                        case Constants.MatchScouting.CubeEvents.LAUNCH_SUCCESS:
                            if (cubeEvent.getLocationX() < Constants.TeamStats.Cubes.EXCHANGE_THESHOLD ||
                                    cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.EXCHANGE_THESHOLD)
                            {
                                // exchange
                            }
                            else if (cubeEvent.getLocationX() < Constants.TeamStats.Cubes.SWITCH_THRESHOlD ||
                                    cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.SWITCH_THRESHOlD)
                            {
                                mPreviewTeam.incrementAutoSwitchSuccesses();
                            }
                            else
                            {
                                mPreviewTeam.incrementAutoScaleSuccesses();
                            }
                            break;
                    }
                }

                ArrayList<CubeEvent> teleopCubeEvents = teamMatchData.getTeleopCubeEvents();

                long time;
                double distance;
                boolean first = true;
                CubeEvent pickup = null;
                for (int i = 0; i < teleopCubeEvents.size(); i++)
                {
                    CubeEvent cubeEvent = teleopCubeEvents.get(i);

                    if (i == 0 && cubeEvent.getEvent()
                                           .equals(Constants.MatchScouting.CubeEvents.PICK_UP))
                    {
                        first = false;
                    }

                    switch (cubeEvent.getEvent())
                    {
                        case Constants.MatchScouting.CubeEvents.DROPPED:
                            mPreviewTeam.incrementNumDrops();
                            break;
                        case Constants.MatchScouting.CubeEvents.LAUNCH_FAILURE:
                            mPreviewTeam.incrementNumLaunchFails();
                            if (cubeEvent.getLocationX() < Constants.TeamStats.Cubes.EXCHANGE_THESHOLD ||
                                    cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.EXCHANGE_THESHOLD)
                            {
                                // exchange
                            }
                            else if (cubeEvent.getLocationX() < Constants.TeamStats.Cubes.SWITCH_THRESHOlD ||
                                    cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.SWITCH_THRESHOlD)
                            {
                                mPreviewTeam.incrementTeleopSwitchFailures();
                            }
                            else
                            {
                                mPreviewTeam.incrementTeleopScaleFailures();
                            }
                            break;
                        case Constants.MatchScouting.CubeEvents.PLACED:
                        case Constants.MatchScouting.CubeEvents.LAUNCH_SUCCESS:
                            if (Utilities.isExchange(cubeEvent.getLocationX()))
                            {
                                mPreviewTeam.incrementTeleopExchangeSuccesses();
                            }
                            else if (Utilities.isSwitch(cubeEvent.getLocationX()))
                            {
                                mPreviewTeam.incrementTeleopSwitchSuccesses();
                            }
                            else if(Utilities.isScale(cubeEvent.getLocationX()))
                            {
                                mPreviewTeam.incrementTeleopScaleSuccesses();
                            }
                            else
                            {
                                assert (false);
                            }

                            if (!first)
                            {
                                time = cubeEvent.getTime() - pickup.getTime();

                                if (Utilities.isExchange(cubeEvent.getLocationX()))
                                {
                                    mPreviewTeam.addToVaultCycleSum(time);
                                }
                                else if (Utilities.isSwitch(cubeEvent.getLocationX()))
                                {
                                    mPreviewTeam.addToSwitchCycleSum(time);
                                }
                                else if(Utilities.isScale(cubeEvent.getLocationX()))
                                {
                                    mPreviewTeam.addToScaleCycleSum(time);
                                }
                                else
                                {
                                    assert (false);
                                }
                            }
                            pickup = null;
                            first = false;
                            break;
                        case Constants.MatchScouting.CubeEvents.PICK_UP:
                            if (pickup == null)
                            {
                                pickup = cubeEvent;
                            }
                            break;
                    }
                }

            }

            mPreviewTeam.setNumMatches(mTeam.getMatches().size());

            return null;
        }
    }
}
