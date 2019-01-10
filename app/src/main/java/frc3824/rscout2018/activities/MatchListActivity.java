package frc3824.rscout2018.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;

/**
 * @class MatchListActivity
 * @brief Activity that displays all the matches to select from. Determines which activity to start
 *        based on the intent extra {@link MatchListActivity#mNextPage}passed to it.
 */
@MakeActivityStarter
public class MatchListActivity extends Activity implements View.OnClickListener
{
    @Arg
    protected String mNextPage;

    int mMatchScoutPosition;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_list);

        // Fill next page
        ActivityStarter.fill(this);

        // Get the position of the match scout (blue 1, blue 2, red 3, etc) from the preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try
        {
            mMatchScoutPosition = Integer.parseInt(sharedPreferences.getString(Constants.Settings.MATCH_SCOUT_POSITION,
                                                                               ""));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        if(mNextPage.equals(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING) || mNextPage.equals(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING))
        {
            findViewById(R.id.practice).setOnClickListener(this);
        }
        else
        {
            findViewById(R.id.practice).setVisibility(View.GONE);
        }

        ScoutHeader header = findViewById(R.id.header);
        header.removeSave();
        header.removePrevious();
        header.removeNext();
        header.removeList();
        header.setTitle("Match List");
        header.setInterface(new MatchListHeader());

        // Setup list of buttons for the individual matches
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new MatchListAdapter());
    }

    /**
     * Practice button clicked
     * {@inheritDoc}
     */
    @Override
    public void onClick(View view)
    {
        switch(mNextPage)
        {
            case Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING:
                MatchScoutActivityStarter.start(this, -1);
                break;
            case Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING:
                SuperScoutActivityStarter.start(this, -1);
                break;
        }
    }

    private class MatchListHeader implements ScoutHeaderInterface
    {

        @Override
        public void previous()
        {
            // Should never be called
        }

        @Override
        public void next()
        {
            // Should never be called
        }

        @Override
        public void home()
        {
            HomeActivityStarter.startWithFlags(MatchListActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        @Override
        public void list()
        {
            // Should never be called
        }

        @Override
        public void save()
        {
            // Should never be called
        }
    }

    /**
     * @class MatchListAdapter
     * @brief The {@link ListAdapter} for showing the list of matches
     */
    private class MatchListAdapter implements ListAdapter, View.OnClickListener
    {
        LayoutInflater mLayoutInflator;
        int mNumberOfMatches;
        SparseIntArray mTeamNumbers;

        /**
         * Constructor
         */
        MatchListAdapter()
        {
            mLayoutInflator = getLayoutInflater();
            if (mNextPage.equals(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING))
            {
                mTeamNumbers = new SparseIntArray();
            }
            mNumberOfMatches = Database.getInstance().numberOfMatches();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isEnabled(int i)
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver)
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount()
        {
            return mNumberOfMatches;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getItem(int i)
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getItemId(int i)
        {
            return i;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            // Inflate the view if it is null
            if (view == null)
            {
                switch (mNextPage)
                {
                    case Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING:
                        if (mMatchScoutPosition < 6)
                        {
                            view = mLayoutInflator.inflate(R.layout.list_item_fbutton, null);
                        }
                        // mMatchScoutPosition == 6 (all)
                        // TODO: 9/20/17  Admin
                        break;
                    case Constants.IntentExtras.NextPageOptions.MATCH_PREVIEW:
                    case Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING:
                        view = mLayoutInflator.inflate(R.layout.list_item_fbutton, null);
                        break;
                }
            }

            switch (mNextPage)
            {
                case Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING:
                    if (mMatchScoutPosition < 6)
                    {
                        int teamNumber;
                        if (mTeamNumbers.get(position + 1, -1) != -1)
                        {
                            teamNumber = mTeamNumbers.get(position + 1);
                        }
                        else
                        {
                            MatchLogistics m = Database.getInstance().getMatchLogistics(position + 1);
                            teamNumber = m.getTeamNumber(mMatchScoutPosition);
                            mTeamNumbers.put(position + 1, teamNumber);
                        }
                        ((TextView) view).setText(format(Locale.US, "Match: %d Team: %d",
                                                         position + 1,
                                                         teamNumber));
                    }
                    // TODO: 9/20/17  Admin
                    break;
                case Constants.IntentExtras.NextPageOptions.MATCH_PREVIEW:
                case Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING:
                    ((TextView) view).setText(format(Locale.US, "Match: %d", position + 1));
                    break;
            }

            view.setId(position + 1);
            if (!view.hasOnClickListeners())
            {
                view.setOnClickListener(this);
            }

            return view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getItemViewType(int i)
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getViewTypeCount()
        {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isEmpty()
        {
            return mNumberOfMatches == 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View view)
        {
            switch (mNextPage)
            {
                case Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING:
                    MatchScoutActivityStarter.start(MatchListActivity.this, view.getId());
                    break;
                case Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING:
                    SuperScoutActivityStarter.start(MatchListActivity.this, view.getId());
                    break;
                case Constants.IntentExtras.NextPageOptions.MATCH_PREVIEW:
                    MatchPreviewActivityStarter.start(MatchListActivity.this, view.getId());
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
}