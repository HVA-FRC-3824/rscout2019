package frc3824.rscout2018.fragments.pick_list;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamPickListItem;
import frc3824.rscout2018.views.TeamPickListItemView;

public class PickListFragment extends Fragment
{
    ArrayList<TeamPickListItem> mPickList = null;
    ListView mListView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pick_list, container, false);

        mListView = view.findViewById(R.id.list_view);

        if(mPickList != null)
        {
            mListView.setAdapter(new PickListAdapter(getContext(), mPickList));
        }

        return view;
    }

    public void setList(ArrayList<TeamPickListItem> list)
    {
        mPickList = list;
        if(mListView != null)
        {
            mListView.setAdapter(new PickListAdapter(getContext(), mPickList));
        }
    }

    private class PickListAdapter extends ArrayAdapter<TeamPickListItem>
    {
        Context mContext;

        public PickListAdapter(@NonNull Context context, ArrayList<TeamPickListItem> objects)
        {
            super(context, R.layout.list_item_pick_list, objects);
            mContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_pick_list, null);
            }

            TeamPickListItem team = getItem(position);

            ((TeamPickListItemView)convertView).setData(team);

            return convertView;
        }
    }
}
