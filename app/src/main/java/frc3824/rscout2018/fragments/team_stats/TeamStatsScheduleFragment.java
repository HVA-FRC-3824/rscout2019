package frc3824.rscout2018.fragments.team_stats;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.database.data_models.TeamLogistics;

/**
 * A fragment for display a team's schedule
 * @author frc3824
 */
public class TeamStatsScheduleFragment extends TeamStatsFragment
{
    TeamLogistics mTeamLogistics;
    ListView mListView;

    protected void bind()
    {
        mTeamLogistics = Database.getInstance().getTeamLogistics(mTeamNumber);
        if(mTeamLogistics != null && mListView != null)
        {
            mListView.setAdapter(new ScheduleListAdapter(
                    getContext(),
                    mTeamLogistics.getMatchNumbers()));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_team_stats_schedule, null);

        mListView = view.findViewById(R.id.list_view);

        bind();

        return view;
    }

    private class ScheduleListAdapter extends ArrayAdapter<Integer>
    {
        Context mContext;
        public ScheduleListAdapter(@NonNull Context context,@NonNull ArrayList<Integer> objects)
        {
            super(context, R.layout.list_item_schedule, objects);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_schedule, null);
            }

            MatchLogistics match = Database.getInstance().getMatchLogistics(getItem(position));

            if(match != null)
            {
                ((TextView)convertView.findViewById(R.id.match_number)).setText(String.valueOf(match.getMatchNumber()));
                ((TextView)convertView.findViewById(R.id.blue1)).setText(String.valueOf(match.getBlue1()));
                ((TextView)convertView.findViewById(R.id.blue2)).setText(String.valueOf(match.getBlue2()));
                ((TextView)convertView.findViewById(R.id.blue3)).setText(String.valueOf(match.getBlue3()));
                ((TextView)convertView.findViewById(R.id.red1)).setText(String.valueOf(match.getRed1()));
                ((TextView)convertView.findViewById(R.id.red2)).setText(String.valueOf(match.getRed2()));
                ((TextView)convertView.findViewById(R.id.red3)).setText(String.valueOf(match.getRed3()));
            }

            return  convertView;
        }
    }
}
