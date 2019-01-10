package frc3824.rscout2018.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frc3824.rscout2018.R;
import frc3824.rscout2018.custom_charts.MainOption;
import frc3824.rscout2018.custom_charts.SecondaryOption;
import frc3824.rscout2018.custom_charts.bar.BarEntryWithTeamNumber;
import frc3824.rscout2018.custom_charts.bar.BarMarkerView;
import frc3824.rscout2018.custom_charts.lld.LLDChart;
import frc3824.rscout2018.custom_charts.lld.LLDData;
import frc3824.rscout2018.custom_charts.lld.LLDDataSet;
import frc3824.rscout2018.custom_charts.lld.LLDEntry;
import frc3824.rscout2018.custom_charts.lld.LLDMarkerView;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.Team;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.utilities.Constants;


public class EventChartsActivityBase extends RScoutActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{
    Spinner mMainDropdown;
    Spinner mSecondaryDropdown;
    protected Map<String, MainOption> mOptions = new HashMap<>();

    LLDChart mLld;
    BarChart mBar;
    XAxis mXAxis;
    YAxis mYAxis;

    ListView mDrawerList;
    EventChartDrawListViewAdapter mDrawerAdapter;

    private ArrayList<Integer> mTeamNumbers;
    private SparseArray< ArrayList<TeamMatchData> > mTeamMatches;
    private ArrayList<TeamNumberCheck> mTeamNumbersSelect;
    private ArrayList<String> mCurrentTeamNumbers;
    private ArrayList<Integer> mSortedTeamNumbers;

    private final int ALL = 0;
    private final int TOP_5 = 1;
    private final int TOP_10 = 2;
    private final int TOP_24 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_charts);

        //region Setup LLD Chart
        mLld = findViewById(R.id.lld_chart);
        mLld.setMarkerView(new LLDMarkerView(this));
        mLld.setDoubleTapToZoomEnabled(false);
        mLld.setPinchZoom(false);
        mLld.setDescription("");
        //endregion

        //region Setup Bar Chart
        mBar = findViewById(R.id.bar_chart);
        mBar.setMarkerView(new BarMarkerView(this));
        mBar.setDoubleTapToZoomEnabled(false);
        mBar.setPinchZoom(false);
        mBar.setDescription("");
        //endregion

        //region Y-Axis Setup
        YAxis yAxis = mLld.getAxisRight();
        yAxis.setEnabled(false);
        yAxis = mBar.getAxisRight();
        yAxis.setEnabled(false);
        //endregion

        //region X-Axis Setup
        mXAxis = mLld.getXAxis();
        mXAxis.setLabelRotationAngle(90);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setDrawGridLines(false);

        mXAxis = mBar.getXAxis();
        mXAxis.setLabelRotationAngle(90);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setDrawGridLines(false);
        //endregion

        //region Legend
        Legend legend = mLld.getLegend();
        legend.setEnabled(false);
        legend = mBar.getLegend();
        legend.setEnabled(false);
        //endregion

        mMainDropdown = findViewById(R.id.main_dropdown);
        mMainDropdown.setAdapter(new ArrayAdapter<>(this,
                                                    android.R.layout.simple_dropdown_item_1line,
                                                    Constants.PickList.MainDropdown.OPTIONS));

        mSecondaryDropdown = findViewById(R.id.secondary_dropdown);

        mTeamNumbers = Database.getInstance().getTeamNumbers();
        mTeamMatches = new SparseArray<>();
        for (int teamNumber : mTeamNumbers)
        {
            Team team = new Team(teamNumber);
            ArrayList<TeamMatchData> matches_list = new ArrayList<>();
            SparseArray<TeamMatchData> matches = team.getMatches();
            for(int i = 0, end = matches.size(); i < end; i++)
            {
                matches_list.add(matches.valueAt(i));
            }
            mTeamMatches.put(teamNumber, matches_list);
        }

        //region Drawer Setup
        mDrawerList = findViewById(R.id.drawer_list);
        mTeamNumbersSelect = new ArrayList<>();
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // All
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // Top 5
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // Top 10
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // Top 24
        for (Integer team_number : mTeamNumbers)
        {
            mTeamNumbersSelect.add(new TeamNumberCheck(team_number, true));
        }
        mDrawerAdapter = new EventChartDrawListViewAdapter();
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(this);

        mMainDropdown.setOnItemSelectedListener(this);
        mSecondaryDropdown.setOnItemSelectedListener(this);
        //endregion
    }

    private void updateChart()
    {
        String main = Constants.PickList.MainDropdown.OPTIONS[mMainDropdown.getSelectedItemPosition()];
        MainOption mainOption = mOptions.get(main);
        String secondary = mainOption.getOption(mSecondaryDropdown.getSelectedItemPosition());
        SecondaryOption secondaryOption = mainOption.getSecondary(secondary);
        if (secondaryOption.getBar())
        {
            new BarUpdateTask().execute(secondaryOption);
        }
        else
        {
            new LldUpdateTask().execute(secondaryOption);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getId() == R.id.main_dropdown)
        {
            mSecondaryDropdown.setAdapter(mOptions.get(Constants.PickList.MainDropdown.OPTIONS[position])
                                                  .getAdapter());
        }
        else
        {
            updateChart();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public ArrayList<Integer> getTop(int top)
    {
        return new ArrayList<>(mSortedTeamNumbers.subList(0, top));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        boolean new_check = !mTeamNumbersSelect.get(position).check;
        ArrayList<Integer> top;
        switch (position)
        {
            case ALL:
                for (int i = 0; i < 4; i++)
                {
                    mTeamNumbersSelect.get(i).setCheck(false);
                }
                mTeamNumbersSelect.get(position).check = new_check;
                if (new_check)
                {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++)
                    {
                        mTeamNumbersSelect.get(i).setCheck(true);
                    }
                }
                mDrawerAdapter.setEnabled(!new_check);
                mDrawerAdapter.notifyDataSetChanged();
                updateChart();
                break;
            case TOP_5:
                top = getTop(5);
                for (int i = 0; i < 4; i++)
                {
                    mTeamNumbersSelect.get(i).setCheck(false);
                }
                mTeamNumbersSelect.get(position).setCheck(new_check);
                if (new_check)
                {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++)
                    {
                        mTeamNumbersSelect.get(i).check = false;
                        for (int j = 0; j < top.size(); j++)
                        {
                            if (mTeamNumbersSelect.get(i).getTeamNumber() == top.get(j))
                            {
                                mTeamNumbersSelect.get(i).setCheck(true);
                                break;
                            }
                        }
                    }
                }
                mDrawerAdapter.setEnabled(!new_check);
                mDrawerAdapter.notifyDataSetChanged();
                updateChart();
                break;
            case TOP_10:
                top = getTop(10);
                for (int i = 0; i < 4; i++)
                {
                    mTeamNumbersSelect.get(i).setCheck(false);
                }
                mTeamNumbersSelect.get(position).setCheck(new_check);
                if (new_check)
                {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++)
                    {
                        mTeamNumbersSelect.get(i).setCheck(false);
                        for (int j = 0; j < top.size(); j++)
                        {
                            if (mTeamNumbersSelect.get(i).getTeamNumber() == top.get(j))
                            {
                                mTeamNumbersSelect.get(i).setCheck(true);
                                break;
                            }
                        }
                    }
                }
                mDrawerAdapter.setEnabled(!new_check);
                mDrawerAdapter.notifyDataSetChanged();
                updateChart();
                break;
            case TOP_24:
                top = getTop(24);
                for (int i = 0; i < 4; i++)
                {
                    mTeamNumbersSelect.get(i).setCheck(false);
                }
                mTeamNumbersSelect.get(position).setCheck(new_check);
                if (new_check)
                {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++)
                    {
                        mTeamNumbersSelect.get(i).setCheck(false);
                        for (int j = 0; j < top.size(); j++)
                        {
                            if (mTeamNumbersSelect.get(i).getTeamNumber() == top.get(j))
                            {
                                mTeamNumbersSelect.get(i).setCheck(true);
                                break;
                            }
                        }
                    }
                }
                mDrawerAdapter.setEnabled(!new_check);
                mDrawerAdapter.notifyDataSetChanged();
                updateChart();
                break;
            default:
                if (mDrawerAdapter.getEnabled())
                {
                    mTeamNumbersSelect.get(position).setCheck(new_check);
                    mDrawerAdapter.notifyDataSetChanged();
                    updateChart();
                }
        }
    }

    private class BarUpdateTask extends AsyncTask<SecondaryOption, Void, Void>
    {

        @Override
        protected Void doInBackground(SecondaryOption... secondaryOptions)
        {
            SecondaryOption secondaryOption = secondaryOptions[0];

            ArrayList<BarEntry> entries = new ArrayList<>();
            mCurrentTeamNumbers = new ArrayList<>();
            int x = 0;
            for (int i = 4; i < mTeamNumbersSelect.size(); i++)
            {
                if (!mTeamNumbersSelect.get(i).check)
                {
                    continue;
                }
                mCurrentTeamNumbers.add(String.valueOf(mTeamNumbersSelect.get(i).getTeamNumber()));
                entries.add((BarEntryWithTeamNumber) secondaryOption.fill(x,
                                                                          mTeamNumbersSelect.get(i)
                                                                                            .getTeamNumber(),
                                                                          mTeamMatches.get(
                                                                                  mTeamNumbersSelect
                                                                                          .get(
                                                                                                  i)
                                                                                          .getTeamNumber())));
                x++;
            }
            BarDataSet dataset = new BarDataSet(entries, "");
            dataset.setDrawValues(false);
            BarData data = new BarData(mCurrentTeamNumbers, dataset);
            mBar.setData(data);

            mXAxis = mBar.getXAxis();
            mXAxis.setLabelsToSkip(0);
            mYAxis = mBar.getAxisLeft();
            mYAxis.setAxisMinValue(0.0f);
            mSortedTeamNumbers = secondaryOption.sort(mTeamMatches);

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mLld.setVisibility(View.GONE);
            mBar.setVisibility(View.VISIBLE);

            mBar.notifyDataSetChanged();
            mBar.invalidate();
        }
    }

    private class LldUpdateTask extends AsyncTask<SecondaryOption, Void, Void>
    {

        @Override
        protected Void doInBackground(SecondaryOption... secondaryOptions)
        {
            SecondaryOption secondaryOption = secondaryOptions[0];

            ArrayList<LLDEntry> entries = new ArrayList<>();
            mCurrentTeamNumbers = new ArrayList<>();
            int x = 0;
            for (int i = 4; i < mTeamNumbersSelect.size(); i++)
            {
                if (!mTeamNumbersSelect.get(i).check)
                {
                    continue;
                }
                mCurrentTeamNumbers.add(String.valueOf(mTeamNumbersSelect.get(i).getTeamNumber()));
                entries.add((LLDEntry) secondaryOption.fill(x,
                                                            mTeamNumbersSelect.get(i)
                                                                              .getTeamNumber(),
                                                            mTeamMatches.get(mTeamNumbersSelect.get(
                                                                    i).getTeamNumber())));
                x++;
            }
            LLDDataSet dataset = new LLDDataSet(entries, "");
            LLDData data = new LLDData(mCurrentTeamNumbers, dataset);
            mLld.setData(data);

            mXAxis = mLld.getXAxis();
            mXAxis.setLabelsToSkip(0);
            mYAxis = mLld.getAxisLeft();
            mYAxis.setAxisMinValue(0.0f);

            mSortedTeamNumbers = secondaryOption.sort(mTeamMatches);

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mLld.setVisibility(View.VISIBLE);
            mBar.setVisibility(View.GONE);

            mLld.notifyDataSetChanged();
            mLld.invalidate();
        }
    }

    private class TeamNumberCheck
    {
        //region Team Number
        int teamNumber;

        public int getTeamNumber()
        {
            return teamNumber;
        }

        public void setTeamNumber(int teamNumber)
        {
            this.teamNumber = teamNumber;
        }

        //endregion
        //region Check
        boolean check;

        public boolean getCheck()
        {
            return check;
        }

        public void setCheck(boolean check)
        {
            this.check = check;
        }
        //endregion

        public TeamNumberCheck(int teamNumber)
        {
            this(teamNumber, false);
        }

        public TeamNumberCheck(int teamNumber, boolean check)
        {
            this.teamNumber = teamNumber;
            this.check = check;
        }
    }

    private class EventChartDrawListViewAdapter extends ArrayAdapter<TeamNumberCheck>
    {
        boolean mEnabled;

        public EventChartDrawListViewAdapter()
        {
            super(EventChartsActivityBase.this,
                  R.layout.list_item_event_drawer,
                  mTeamNumbersSelect);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) EventChartsActivityBase.this.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_event_drawer, null);
            }

            final TeamNumberCheck team = getItem(position);

            String label;
            switch (position)
            {
                case 0:
                    label = "All";
                    break;
                case 1:
                    label = "Top 5";
                    break;
                case 2:
                    label = "Top 10";
                    break;
                case 3:
                    label = "Top 24";
                    break;
                default:
                    label = String.valueOf(team.getTeamNumber());
            }

            ((TextView) convertView.findViewById(R.id.team_number)).setText(label);

            CheckBox checkBox = convertView.findViewById(R.id.checkbox);
            checkBox.setChecked(team.getCheck());

            if (position < 4)
            {
                checkBox.setEnabled(true);
            }
            else
            {
                checkBox.setEnabled(mEnabled);
            }

            return convertView;
        }

        public void setEnabled(boolean enabled)
        {
            mEnabled = enabled;
        }

        public boolean getEnabled()
        {
            return mEnabled;
        }
    }
}
