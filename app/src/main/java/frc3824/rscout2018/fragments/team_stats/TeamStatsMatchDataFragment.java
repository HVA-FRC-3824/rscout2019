package frc3824.rscout2018.fragments.team_stats;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Collections;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.TeamLogistics;

/**
 * A fragment used to display the data collect on this team during each of its matches
 */
public class TeamStatsMatchDataFragment extends TeamStatsFragment
{
    TeamStatsMatchFragmentPagerAdapter mFPA;
    ViewPager mViewPager;

    protected void bind()
    {
        if(mViewPager != null)
        {
            mFPA.update();
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_team_stats_match_data, null);

        mFPA = new TeamStatsMatchFragmentPagerAdapter(getChildFragmentManager());

        // Setup view pager
        mViewPager = view.findViewById(R.id.inner_view_pager);
        mViewPager.setAdapter(mFPA);

        bind();

        SmartTabLayout tabLayout = view.findViewById(R.id.inner_tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setViewPager(mViewPager);

        return view;
    }

    private class TeamStatsMatchFragmentPagerAdapter extends FragmentPagerAdapter
    {
        ArrayList<Integer> mMatchNumbers;

        public TeamStatsMatchFragmentPagerAdapter(FragmentManager fm)
        {
            super(fm);
            update();
        }

        @Override
        public Fragment getItem(int position)
        {
            TeamStatsIndividualMatchDataFragment frag = new TeamStatsIndividualMatchDataFragment();
            frag.setTeamMatchNumber(mTeamNumber, mMatchNumbers.get(position));
            return frag;
        }

        @Override
        public int getCount()
        {
            return mMatchNumbers.size();
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
            return String.valueOf(mMatchNumbers.get(position));
        }

        public void update()
        {
            TeamLogistics teamLogistics = Database.getInstance().getTeamLogistics(mTeamNumber);
            if(teamLogistics == null)
            {
                // No nulls
                mMatchNumbers = new ArrayList<>();
            }
            else
            {
                mMatchNumbers = teamLogistics.getMatchNumbers();
                Collections.sort(mMatchNumbers);
            }
        }
    }
}
