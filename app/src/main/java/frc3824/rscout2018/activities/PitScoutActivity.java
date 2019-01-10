package frc3824.rscout2018.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Locale;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.TeamPitData;
import frc3824.rscout2018.fragments.pit_scout.PitDimensionsFragment;
import frc3824.rscout2018.fragments.pit_scout.PitMiscFragment;
import frc3824.rscout2018.fragments.pit_scout.PitPictureFragment;
import frc3824.rscout2018.fragments.pit_scout.PitScoutFragment;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;

/**
 * @class PitScoutActivity
 * @brief The page for scouting an individual team
 */
@MakeActivityStarter
public class PitScoutActivity extends RScoutActivity
{
    private final static String TAG = "MatchScoutActivity";

    @Arg
    protected int mTeamNumber = -1;
    boolean mPractice = false;
    int mScoutPosition;

    ScoutHeader mHeader;
    PitScoutFragmentPagerAdapter mFPA;
    ViewPager mViewPager;
    TeamPitData mTeamPitData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        ActivityStarter.fill(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mScoutPosition = Integer.parseInt(sharedPreferences.getString(Constants.Settings.PIT_SCOUT_POSITION,
                                                                    "-1"));
        if (mScoutPosition == -1)
        {
            Log.e(TAG, "Error: impossible pit scout position");
            return;
        }

        // Setup header
        mHeader = findViewById(R.id.header);
        mHeader.setInterface(new PitScoutHeader());

        // Keep screen on while scouting
        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Setup the TABS and fragment pages
        mFPA = new PitScoutFragmentPagerAdapter(getFragmentManager());

        // Setup view pager
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mFPA);
        mViewPager.setOffscreenPageLimit(mFPA.getCount());

        // Real pit scouting
        if (mTeamNumber > 0)
        {
            setup();
        }
        // Practice
        else
        {
            mPractice = true;
            mHeader.setTitle("Practice Match");
            mTeamPitData = new TeamPitData(-1);
            mHeader.removeSave();
        }

        SmartTabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setViewPager(mViewPager);
    }

    private void setup()
    {
        if(mTeamNumber > 0)
        {
            mHeader.setTitle(format(Locale.US, "Team Number: %d",
                                    mTeamNumber));

            // todo: handle admin
            // If first team then remove the previous button
            ArrayList<Integer> teamNumbers = Database.getInstance().getTeamNumbers();
            int index = teamNumbers.indexOf(mTeamNumber);
            if (index - (teamNumbers.size() / 6 * mScoutPosition) <= 0)
            {
                mHeader.removePrevious();
            }
            else
            {
                mHeader.addPrevious();
            }

            // todo: handle admin
            // If last team then remove the next button
            index = teamNumbers.indexOf(mTeamNumber);
            if (index >= (teamNumbers.size() / 6 * (mScoutPosition + 1)) - 1)
            {
                mHeader.removeNext();
            }
            else
            {
                mHeader.addNext();
            }

            mTeamPitData = Database.getInstance().getTeamPitData(mTeamNumber);
            if(mTeamPitData == null)
            {
                mTeamPitData = new TeamPitData(mTeamNumber);
            }

            mFPA.update();
            mViewPager.setCurrentItem(0);
        }
    }


    private class PitScoutHeader implements ScoutHeaderInterface
    {

        @Override
        public void previous()
        {
            if (mPractice)
            {
                setup();
            }
            else
            {
                final ArrayList<Integer> teamNumbers = Database.getInstance().getTeamNumbers();
                final int index = teamNumbers.indexOf(mTeamNumber);
                if (mTeamPitData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PitScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamPitData(mTeamPitData);

                                    mTeamNumber = teamNumbers.get(index - 1);
                                    setup();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    mTeamNumber = teamNumbers.get(index - 1);
                                    setup();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Nothing goes here
                                }
                            });
                    builder.create().show();
                }
                else
                {
                    if (index > 0) // If null then this function shouldn't be possible to call as the button should have been hidden
                    {
                        mTeamNumber = teamNumbers.get(index - 1);
                        setup();
                    }
                }
            }
        }

        @Override
        public void next()
        {
            if (mPractice)
            {
                setup();
            }
            else
            {
                final ArrayList<Integer> teamNumbers = Database.getInstance().getTeamNumbers();
                final int index = teamNumbers.indexOf(mTeamNumber);

                if (mTeamPitData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PitScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamPitData(mTeamPitData);

                                    mTeamNumber = teamNumbers.get(index + 1);
                                    setup();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    mTeamNumber = teamNumbers.get(index + 1);
                                    setup();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Nothing goes here
                                }
                            });
                    builder.create().show();
                }
                else
                {
                    mTeamNumber = teamNumbers.get(index + 1);
                    setup();
                }
            }
        }

        @Override
        public void home()
        {
            if (mPractice)
            {
                HomeActivityStarter.startWithFlags(PitScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
            else
            {
                if (mTeamPitData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PitScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamPitData(mTeamPitData);

                                    HomeActivityStarter.startWithFlags(PitScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    HomeActivityStarter.startWithFlags(PitScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Nothing goes here
                                }
                            });
                    builder.create().show();
                }
                else
                {
                    HomeActivityStarter.startWithFlags(PitScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                }
            }
        }

        @Override
        public void list()
        {
            if (mPractice)
            {
                TeamListActivityStarter.start(PitScoutActivity.this, Constants.IntentExtras.NextPageOptions.PIT_SCOUTING);
            }
            else
            {
                if (mTeamPitData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PitScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamPitData(mTeamPitData);

                                    onBackPressed();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    onBackPressed();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Nothing goes here
                                }
                            });
                    builder.create().show();
                }
                else
                {
                    onBackPressed();
                }
            }
        }

        @Override
        public void save()
        {
            if (!mPractice && mTeamPitData.isDirty())
            {
                Database.getInstance().updateTeamPitData(mTeamPitData);
            }
        }
    }

    /**
     * @class PitScoutFragmentPagerAdapter
     * @brief Creates and manages the fragments that are displayed for pit scouting
     */
    private class PitScoutFragmentPagerAdapter extends FragmentPagerAdapter
    {
        static final String TAG = "MatchScoutFragmentPagerAdapter";

        SparseArray<PitScoutFragment> mFragments;


        public PitScoutFragmentPagerAdapter(FragmentManager fm)
        {
            super(fm);
            mFragments = new SparseArray<>();
        }

        /**
         * Gets the fragment at the specified position for display
         *
         * @param position Position of the fragment wanted
         */
        @Override
        public Fragment getItem(int position)
        {
            if (!(position >= 0 && position < Constants.PitScouting.TABS.length))
            {
                throw new AssertionError();
            }
            if(mFragments.get(position) != null)
            {
                return mFragments.get(position);
            }
            PitScoutFragment f;
            switch (position)
            {
                case 0:
                    f = new PitPictureFragment();
                    break;
                case 1:
                    f = new PitDimensionsFragment();
                    break;
                case 2:
                    f = new PitMiscFragment();
                    break;
                default:
                    throw new AssertionError();
            }
            f.setTeamPitData(mTeamPitData);
            mFragments.put(position, f);
            return f;
        }

        /**
         * Returns the number of fragments
         */
        @Override
        public int getCount()
        {
            return Constants.PitScouting.TABS.length;
        }

        /**
         * Returns the title of the fragment at the specified position
         *
         * @param position Position of the fragment
         *
         * @return The title of the fragment
         */
        @Override
        public String getPageTitle(int position)
        {
            if (!(position >= 0 && position < Constants.PitScouting.TABS.length))
            {
                throw new AssertionError();
            }
            return Constants.PitScouting.TABS[position];
        }

        public void update()
        {
            for (int i = 0, end = mFragments.size(); i < end; i++)
            {
                PitScoutFragment fragment = mFragments.valueAt(i);
                fragment.setTeamPitData(mTeamPitData);
            }
        }

    }
}
