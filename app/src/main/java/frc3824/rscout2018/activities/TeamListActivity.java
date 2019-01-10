package frc3824.rscout2018.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.ScoutHeader;
import frc3824.rscout2018.views.ScoutHeaderInterface;

import static java.lang.String.format;

/**
 * @class TeamListActivity
 * @brief Activity that displays all the teams to select from. Determines which activity to start
 *        based on the intent extra {@link TeamListActivity#nextPage} passed to it.
 */
@MakeActivityStarter
public class TeamListActivity extends Activity implements View.OnClickListener
{
    @Arg
    String nextPage;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_list);
        ActivityStarter.fill(this);

        if(nextPage.equals(Constants.IntentExtras.NextPageOptions.PIT_SCOUTING))
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
        header.setTitle("Team List");
        header.setInterface(new TeamListHeader());

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new TeamListAdapter(this));
    }

    @Override
    public void onClick(View view)
    {
        PitScoutActivityStarter.start(this, -1);
    }

    private class TeamListHeader implements ScoutHeaderInterface
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
            HomeActivityStarter.startWithFlags(TeamListActivity.this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
     * @class TeamListAdapter
     * @brief The {@link ListAdapter} for showing the list of teams
     */
    private class TeamListAdapter extends ArrayAdapter<Integer> implements View.OnClickListener
    {
        LayoutInflater mLayoutInflator;

        /**
         * Constructor
         */
        TeamListAdapter(Context context)
        {
            super(context, R.layout.list_item_fbutton, Database.getInstance().getTeamNumbers());
            mLayoutInflator = getLayoutInflater();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View getView(int i, View view, ViewGroup parent)
        {
            if(view == null)
            {
                view = mLayoutInflator.inflate(R.layout.list_item_fbutton, null);
            }

            int teamNumber = getItem(i);

            ((TextView)view).setText(format(Locale.US, "Team: %d", teamNumber));

            view.setId(teamNumber);
            view.setOnClickListener(this);

            return view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View view)
        {
            switch (nextPage)
            {
                case Constants.IntentExtras.NextPageOptions.PIT_SCOUTING:
                    PitScoutActivityStarter.start(TeamListActivity.this, view.getId());
                    break;
                case Constants.IntentExtras.NextPageOptions.TEAM_STATS:
                    TeamStatsActivityStarter.start(TeamListActivity.this, view.getId());
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }

    /**
     * Removes back pressed option
     */
    @Override
    public void onBackPressed()
    {
        return;
    }
}
