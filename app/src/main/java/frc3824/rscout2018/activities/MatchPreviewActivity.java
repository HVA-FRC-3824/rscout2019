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

import java.util.Locale;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.fragments.match_preview.MatchAlliancePreviewFragment;
import frc3824.rscout2018.fragments.match_preview.MatchBlueAlliancePreviewFragment;
import frc3824.rscout2018.fragments.match_preview.MatchRedAlliancePreviewFragment;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;

/**
 * @class MatchPreviewActivity
 * @brief Activity for previewing the teams in a given match
 */
@MakeActivityStarter
public class MatchPreviewActivity extends RScoutActivity
{
    @Arg
    protected int mMatchNumber;

    MatchPreviewFragmentPagerActivity mFPA;
    ViewPager mViewPager;
    ScoutHeader mHeader;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        ActivityStarter.fill(this);
        setContentView(R.layout.activity_scout);

        mHeader = findViewById(R.id.header);
        mHeader.setInterface(new MatchPreviewHeader());
        mHeader.removeSave();

        // Setup the TABS and fragment pages
        mFPA = new MatchPreviewFragmentPagerActivity(getFragmentManager());

        // Setup view pager
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mFPA);
        mViewPager.setOffscreenPageLimit(mFPA.getCount());

        setup();

        SmartTabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setViewPager(mViewPager);
    }

    private void setup()
    {
        mHeader.setTitle(format(Locale.US, "Match Number: %d", mMatchNumber));

        // If on the first match then the previous button should be hidden
        if (mMatchNumber == 1)
        {
            mHeader.removePrevious();
        }
        else
        {
            mHeader.addPrevious();
        }

        // If on the final match then next button should be hidden
        if (mMatchNumber == Database.getInstance().numberOfMatches())
        {
            mHeader.removeNext();
        }
        else
        {
            mHeader.addNext();
        }

        mFPA.update();
        mViewPager.setCurrentItem(0);
    }


    private class MatchPreviewHeader implements ScoutHeaderInterface
    {

        @Override
        public void previous()
        {
            mMatchNumber--;
            setup();
        }

        @Override
        public void next()
        {
            mMatchNumber++;
            setup();
        }

        @Override
        public void home()
        {
            HomeActivityStarter.startWithFlags(MatchPreviewActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        @Override
        public void list()
        {
            onBackPressed();
        }

        @Override
        public void save()
        {
            //should never be called
        }
    }


    private class MatchPreviewFragmentPagerActivity extends FragmentPagerAdapter
    {
        SparseArray<MatchAlliancePreviewFragment> mFragments;

        public MatchPreviewFragmentPagerActivity(FragmentManager fm)
        {
            super(fm);
            mFragments = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position)
        {
            if(mFragments.get(position) != null)
            {
                return mFragments.get(position);
            }
            MatchAlliancePreviewFragment f;
            switch (position)
            {
                case 0:
                    f = new MatchBlueAlliancePreviewFragment();
                    break;
                case 1:
                    f = new MatchRedAlliancePreviewFragment();
                    break;
                default:
                    throw new AssertionError();
            }
            f.setMatchNumber(mMatchNumber);
            mFragments.put(position, f);
            return f;
        }

        @Override
        public int getCount()
        {
            return Constants.MatchPreview.TABS.length;
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
            if (!(position >= 0 && position < Constants.MatchPreview.TABS.length))
            {
                throw new AssertionError();
            }
            return Constants.MatchPreview.TABS[position];
        }

        public void update()
        {
            for(int i = 0, end = mFragments.size(); i < end; i++)
            {
                MatchAlliancePreviewFragment fragment = mFragments.valueAt(i);
                fragment.setMatchNumber(mMatchNumber);
            }
        }
    }

}
