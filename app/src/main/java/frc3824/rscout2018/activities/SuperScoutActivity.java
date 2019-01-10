package frc3824.rscout2018.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.database.data_models.SuperMatchData;
import frc3824.rscout2018.fragments.super_scout.SuperNotesFragment;
import frc3824.rscout2018.fragments.super_scout.SuperPowerUpFragment;
import frc3824.rscout2018.fragments.super_scout.SuperScoutFragment;
import frc3824.rscout2018.services.CommunicationService;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;


public class SuperScoutActivity extends RScoutActivity
{
    @Arg
    protected int mMatchNumber = -1;
    private boolean mPractice = false;
    SuperScoutFragmentPagerAdapter mFPA;
    ViewPager mViewPager;
    ScoutHeader mHeader;

    private SuperMatchData mSuperMatchData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        ActivityStarter.fill(this);


        mHeader = findViewById(R.id.header);
        mHeader.setInterface(new SuperScoutActivity.SuperScoutHeader());

        // Keep screen on while scouting
        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Setup the TABS and fragment pages
        mFPA = new SuperScoutActivity.SuperScoutFragmentPagerAdapter(getFragmentManager());

        // Setup view pager
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mFPA);
        mViewPager.setOffscreenPageLimit(mFPA.getCount());

        // Normal match
        if (mMatchNumber > 0)
        {
            setup();
        }
        // Practice Match
        else
        {
            mPractice = true;
            mHeader.setTitle("Practice Match");
            mSuperMatchData = new SuperMatchData(-1);
            mHeader.removeSave();
        }

        SmartTabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setViewPager(mViewPager);
    }

    private void setup()
    {
        if (mMatchNumber > 0)
        {
            MatchLogistics m = new MatchLogistics(mMatchNumber);
            mHeader.setTitle(format(Locale.US, "Match Number: %d",
                                    mMatchNumber));

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

            mSuperMatchData = Database.getInstance().getSuperMatchData(mMatchNumber);
            if(mSuperMatchData == null)
            {
                mSuperMatchData = new SuperMatchData(mMatchNumber);
            }
            mFPA.update();
            mViewPager.setCurrentItem(0);
        }
    }


    /**
     * Inner class that handles pressing the buttons on the header
     */
    private class SuperScoutHeader implements ScoutHeaderInterface
    {

        /**
         * Previous button press handler
         */
        public void previous()
        {
            if (mPractice) // Don't need to worry about saving for practice
            {
                setup();
            }
            else
            {
                if (mSuperMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuperScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateSuperMatchData(mSuperMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(SuperScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    startService(intent);

                                    mMatchNumber --;
                                    setup();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    mMatchNumber --;
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
                    mMatchNumber --;
                    setup();
                }
            }
        }

        /**
         * Next button press handler
         */
        public void next()
        {
            if (mPractice) // Don't need to worry about saving for practice
            {
                setup();
            }
            else
            {
                if (mSuperMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuperScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateSuperMatchData(mSuperMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(SuperScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    startService(intent);

                                    mMatchNumber ++;
                                    setup();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    mMatchNumber ++;
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
                else // Don't need to worry about saving if nothing has changed
                {
                    mMatchNumber ++;
                    setup();
                }
            }
        }

        /**
         * Home button press handler
         */
        public void home()
        {
            if (mPractice) // Don't need to worry about saving for practice
            {
                HomeActivityStarter.startWithFlags(SuperScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
            else
            {
                if (mSuperMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuperScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            // Set action for clicking yes
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateSuperMatchData(mSuperMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(SuperScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    startService(intent);

                                    HomeActivityStarter.startWithFlags(SuperScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    HomeActivityStarter.startWithFlags(SuperScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                else // Don't need to worry about saving if nothing has changed
                {
                    HomeActivityStarter.startWithFlags(SuperScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
            }
        }

        /**
         * List button press handler
         */
        public void list()
        {
            if (mPractice) // Don't need to worry about saving for practice
            {
                onBackPressed();
            }
            else
            {
                if (mSuperMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuperScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateSuperMatchData(mSuperMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(SuperScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    startService(intent);

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
                else // Don't need to worry about saving if nothing has changed
                {
                    onBackPressed();
                }
            }
        }

        /**
         * Save button press handler
         */
        public void save()
        {
            if(!mPractice) // Don't need to worry about saving for practice
            {
                // Save to local database
                Database.getInstance().updateSuperMatchData(mSuperMatchData);

                // Send to the background service to be sent to server
                Intent intent = new Intent(SuperScoutActivity.this, CommunicationService.class);
                intent.putExtra(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING, true);
                intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                startService(intent);
            }
        }
    }

    /**
     * @class MatchScoutFragmentPagerAdapter
     *
     * Creates multiple fragments based for the match scout activity
     */
    private class SuperScoutFragmentPagerAdapter extends FragmentPagerAdapter
    {
        static final String TAG = "MatchScoutFragmentPagerAdapter";

        SparseArray<SuperScoutFragment> mFragments;


        public SuperScoutFragmentPagerAdapter(FragmentManager fm)
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
            if (!(position >= 0 && position < Constants.MatchScouting.TABS.length))
            {
                throw new AssertionError();
            }
            if(mFragments.get(position) != null)
            {
                return mFragments.get(position);
            }

            SuperScoutFragment f;
            switch (position)
            {
                case 0:
                    f = new SuperPowerUpFragment();
                    break;
                case 1:
                    f = new SuperNotesFragment();
                    break;
                default:
                    throw new AssertionError();
            }
            f.setSuperMatchData(mSuperMatchData);
            mFragments.put(position, f);
            return f;
        }

        /**
         * @returns The number of fragments
         */
        @Override
        public int getCount()
        {
            return Constants.SuperScouting.TABS.length;
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
            if (!(position >= 0 && position < Constants.SuperScouting.TABS.length))
            {
                throw new AssertionError();
            }
            return Constants.SuperScouting.TABS[position];
        }

        public void update()
        {
            for(int i = 0, end = mFragments.size(); i < end; i++)
            {
                SuperScoutFragment fragment = mFragments.valueAt(i);
                fragment.setSuperMatchData(mSuperMatchData);
            }
        }

    }
}
