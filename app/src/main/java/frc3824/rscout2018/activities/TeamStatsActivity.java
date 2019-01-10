package frc3824.rscout2018.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Locale;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.fragments.team_stats.TeamStatsChartsFragment;
import frc3824.rscout2018.fragments.team_stats.TeamStatsFragment;
import frc3824.rscout2018.fragments.team_stats.TeamStatsMatchDataFragment;
import frc3824.rscout2018.fragments.team_stats.TeamStatsNotesFragment;
import frc3824.rscout2018.fragments.team_stats.TeamStatsPitDataFragment;
import frc3824.rscout2018.fragments.team_stats.TeamStatsScheduleFragment;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;

/**
 *
 */
@MakeActivityStarter
public class TeamStatsActivity extends RScoutActivity
{
    @Arg
    protected int mTeamNumber;
    TeamStatsFragmentPagerAdapter mFPA;
    ScoutHeader mHeader;
    ViewPager mViewPager;
    ArrayList<Integer> mTeamNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        ActivityStarter.fill(this);

        mHeader = findViewById(R.id.header);
        mHeader.setInterface(new TeamStatsActivity.TeamStatsHeader());
        mHeader.removeSave();

        // If first team then remove the previous button
        mTeamNumbers = Database.getInstance().getTeamNumbers();

        // Setup the TABS and fragment pages
        mFPA = new TeamStatsActivity.TeamStatsFragmentPagerAdapter(getFragmentManager());

        // Setup view pager
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mFPA);
        //viewPager.setOffscreenPageLimit(mFPA.getCount());

        setup();

        SmartTabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setViewPager(mViewPager);
    }

    private void setup()
    {
        mHeader.setTitle(format(Locale.US, "Team: %d", mTeamNumber));

        int index = mTeamNumbers.indexOf(mTeamNumber);
        if (index <= 0)
        {
            mHeader.removePrevious();
        }

        // If last team then remove the next button
        index = mTeamNumbers.indexOf(mTeamNumber);
        if (index >= mTeamNumbers.size() - 1)
        {
            mHeader.removeNext();
        }

        mFPA.update();
        mViewPager.setCurrentItem(0);
    }

    private class TeamStatsHeader implements ScoutHeaderInterface
    {

        @Override
        public void previous()
        {
            int index = mTeamNumbers.indexOf(mTeamNumber);
            if (index > 0) // If null then this function shouldn't be possible to call as the button should have been hidden
            {
                mTeamNumber = mTeamNumbers.get(index - 1);
                setup();
            }
        }

        @Override
        public void next()
        {
            int index = mTeamNumbers.indexOf(mTeamNumber);
            if (index < mTeamNumbers.size() - 1) // If null then this function shouldn't be possible to call as the button should have been hidden
            {
                mTeamNumber = mTeamNumbers.get(index + 1);
                setup();
            }
        }

        @Override
        public void home()
        {
            HomeActivityStarter.startWithFlags(TeamStatsActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        @Override
        public void list()
        {
            onBackPressed();
        }

        @Override
        public void save()
        {
            // Never called
        }
    }

    private class TeamStatsFragmentPagerAdapter extends FragmentPagerAdapter
    {
        SparseArray<TeamStatsFragment> mFragments;

        public TeamStatsFragmentPagerAdapter(FragmentManager fm)
        {
            super(fm);
            mFragments = new SparseArray<>();
        }

        /**
         * Gets the fragment at the specified position for display
         *
         * @param position Position of the fragment wanted
         * @returns fragment to be displayed
         */
        @Override
        public Fragment getItem(int position)
        {
            if (!(position >= 0 && position < Constants.TeamStats.TABS.length))
            {
                throw new AssertionError();
            }
            if(mFragments.get(position) != null)
            {
                return mFragments.get(position);
            }
            TeamStatsFragment f;
            switch (position)
            {
                case 0:
                    f = new TeamStatsChartsFragment();
                    break;
                case 1:
                    f = new TeamStatsMatchDataFragment();
                    break;
                case 2:
                    f = new TeamStatsPitDataFragment();
                    break;
                case 3:
                    f = new TeamStatsNotesFragment();
                    break;
                case 4:
                    f = new TeamStatsScheduleFragment();
                    break;
                default:
                    throw new AssertionError();
            }
            f.setTeamNumber(mTeamNumber);
            mFragments.put(position, f);
            return f;
        }

        /**
         * @returns The number of fragments
         */
        @Override
        public int getCount()
        {
            return Constants.TeamStats.TABS.length;
        }

        /**
         * Returns the title of the fragment at the specified position
         *
         * @param position Position of the fragment
         * @return The title of the fragment
         */
        @Override
        public String getPageTitle(int position)
        {
            if (!(position >= 0 && position < Constants.TeamStats.TABS.length))
            {
                throw new AssertionError();
            }
            return Constants.TeamStats.TABS[position];
        }

        public void update()
        {
            for(int i = 0, end = mFragments.size(); i < end; i++)
            {
                TeamStatsFragment fragment = mFragments.valueAt(i);
                fragment.setTeamNumber(mTeamNumber);
            }
        }
    }
}
