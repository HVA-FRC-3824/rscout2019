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

import java.util.Locale;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.fragments.match_scout.MatchAutoFragment;
import frc3824.rscout2018.fragments.match_scout.MatchEndgameFragment;
import frc3824.rscout2018.fragments.match_scout.MatchFoulsFragment;
import frc3824.rscout2018.fragments.match_scout.MatchMiscFragment;
import frc3824.rscout2018.fragments.match_scout.MatchScoutFragment;
import frc3824.rscout2018.fragments.match_scout.MatchStartFragment;
import frc3824.rscout2018.fragments.match_scout.MatchTeleopFragment;
import frc3824.rscout2018.services.CommunicationService;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;

/**
 * @class MatchScoutActivity
 * @brief The page for scouting an individual team in a single match
 */
@MakeActivityStarter
public class MatchScoutActivity extends RScoutActivity
{
    private final static String TAG = "MatchScoutActivity";

    private int mTeamNumber = -1;
    @Arg
    protected int mMatchNumber = -1;
    boolean mPractice = false;

    ScoutHeader mHeader;
    int mScoutPosition;
    MatchScoutFragmentPagerAdapter mFPA;
    ViewPager mViewPager;
    TeamMatchData mTeamMatchData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        ActivityStarter.fill(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Integer.parseInt(sharedPreferences.getString(Constants.Settings.MATCH_SCOUT_POSITION,
        //                                                                               ""));
        int mScoutPosition = Integer.parseInt(sharedPreferences.getString(Constants.Settings.MATCH_SCOUT_POSITION, "-1"));
        if (mScoutPosition == -1)
        {
            Log.e(TAG, "Error: impossible match scout position");
            return;
        }

        // Inflate header
        mHeader = findViewById(R.id.header);
        mHeader.setInterface(new MatchScoutHeader());

        // Keep screen on while scouting
        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Setup the TABS and fragment pages
        mFPA = new MatchScoutFragmentPagerAdapter(getFragmentManager());

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
            mTeamMatchData = new TeamMatchData(-1, -1);
            mHeader.removeSave();
        }

        SmartTabLayout tabLayout = findViewById(R.id.tab_layout);
        if (mScoutPosition < 3)
        {
            tabLayout.setBackgroundColor(Color.BLUE);
        }
        else
        {
            tabLayout.setBackgroundColor(Color.RED);
        }
        tabLayout.setViewPager(mViewPager);
    }

    public void setup()
    {
        // Normal match
        if (mMatchNumber > 0)
        {
            MatchLogistics m = Database.getInstance().getMatchLogistics(mMatchNumber);
            mTeamNumber = m.getTeamNumber(mScoutPosition);
            mHeader.setTitle(format(Locale.US, "Match Number: %d Team Number: %d",
                                    mMatchNumber,
                                    mTeamNumber));

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

            mTeamMatchData = Database.getInstance().getTeamMatchData(mTeamNumber, mMatchNumber);
            if (mTeamMatchData == null)
            {
                mTeamMatchData = new TeamMatchData(mTeamNumber, mMatchNumber);
            }
            mFPA.update();
            mViewPager.setCurrentItem(0);
        }
        else
        {
            // Clear
            mTeamMatchData = new TeamMatchData(-1, -1);
            mFPA.update();
            mViewPager.setCurrentItem(0);
        }
    }


    /**
     * Inner class that handles pressing the buttons on the header
     */
    private class MatchScoutHeader implements ScoutHeaderInterface
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
                if (mTeamMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamMatchData(mTeamMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(MatchScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    intent.putExtra(Constants.IntentExtras.TEAM_NUMBER, mTeamNumber);
                                    startService(intent);

                                    mMatchNumber--;
                                    setup();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    mMatchNumber--;
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
                    mMatchNumber--;
                    MatchScoutActivity.this.setup();
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
                if (mTeamMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamMatchData(mTeamMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(MatchScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    intent.putExtra(Constants.IntentExtras.TEAM_NUMBER, mTeamNumber);
                                    startService(intent);

                                    mMatchNumber++;
                                    setup();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    mMatchNumber++;
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
                    mMatchNumber++;
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
                HomeActivityStarter.startWithFlags(MatchScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
            else
            {
                if (mTeamMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            // Set action for clicking yes
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamMatchData(mTeamMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(MatchScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    intent.putExtra(Constants.IntentExtras.TEAM_NUMBER, mTeamNumber);
                                    startService(intent);

                                    HomeActivityStarter.startWithFlags(MatchScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    HomeActivityStarter.startWithFlags(MatchScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                else // Don't need to worry about saving if nothing has changed
                {
                    HomeActivityStarter.startWithFlags(MatchScoutActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
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
                if (mTeamMatchData.isDirty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoutActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Would you like to save them?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Save to local database
                                    Database.getInstance().updateTeamMatchData(mTeamMatchData);

                                    // Send to the background service to be sent to server
                                    Intent intent = new Intent(MatchScoutActivity.this, CommunicationService.class);
                                    intent.putExtra(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING, true);
                                    intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                                    intent.putExtra(Constants.IntentExtras.TEAM_NUMBER, mTeamNumber);
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
            if(!mPractice && mTeamMatchData.isDirty()) // Don't need to worry about saving for practice or if there is nothing new
            {
                // Save to local database
                Database.getInstance().updateTeamMatchData(mTeamMatchData);

                // Send to the background service to be sent to server
                Intent intent = new Intent(MatchScoutActivity.this, CommunicationService.class);
                intent.putExtra(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING, true);
                intent.putExtra(Constants.IntentExtras.MATCH_NUMBER, mMatchNumber);
                intent.putExtra(Constants.IntentExtras.TEAM_NUMBER, mTeamNumber);
                startService(intent);
            }
        }
    }

    /**
     * @class MatchScoutFragmentPagerAdapter
     *
     * Creates multiple fragments based for the match scout activity
     */
    private class MatchScoutFragmentPagerAdapter extends FragmentPagerAdapter
    {
        static final String TAG = "MatchScoutFragmentPagerAdapter";

        SparseArray<MatchScoutFragment> mFragments;


        public MatchScoutFragmentPagerAdapter(FragmentManager fm)
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

            MatchScoutFragment f;
            switch (position)
            {
                case 0:
                    f = new MatchStartFragment();
                break;
                case 1:
                    f = new MatchAutoFragment();
                    break;
                case 2:
                    f = new MatchTeleopFragment();
                    break;
                case 3:
                    f = new MatchEndgameFragment();
                    break;
                case 4:
                    f = new MatchFoulsFragment();
                    break;
                case 5:
                    f = new MatchMiscFragment();
                    break;
                default:
                    throw new AssertionError();
            }
            f.setTeamMatchData(mTeamMatchData);
            mFragments.put(position, f);
            return f;
        }

        /**
         * @returns The number of fragments
         */
        @Override
        public int getCount()
        {
            return Constants.MatchScouting.TABS.length;
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
            if (!(position >= 0 && position < Constants.MatchScouting.TABS.length))
            {
                throw new AssertionError();
            }
            return Constants.MatchScouting.TABS[position];
        }

        public void update()
        {
            for(int i = 0, end = mFragments.size(); i < end; i++)
            {
                MatchScoutFragment fragment = mFragments.valueAt(i);
                fragment.setTeamMatchData(mTeamMatchData);
            }
        }
    }
}
