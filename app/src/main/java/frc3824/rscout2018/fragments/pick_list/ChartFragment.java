package frc3824.rscout2018.fragments.pick_list;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import frc3824.rscout2018.R;
import frc3824.rscout2018.custom_charts.bar.BarEntryWithTeamNumber;
import frc3824.rscout2018.custom_charts.bar.BarMarkerView;
import frc3824.rscout2018.custom_charts.lld.LLDChart;
import frc3824.rscout2018.custom_charts.lld.LLDData;
import frc3824.rscout2018.custom_charts.lld.LLDDataSet;
import frc3824.rscout2018.custom_charts.lld.LLDEntry;
import frc3824.rscout2018.custom_charts.lld.LLDMarkerView;
import frc3824.rscout2018.database.data_models.TeamLLDItem;
import frc3824.rscout2018.database.data_models.TeamPickListItem;

public class ChartFragment extends Fragment
{
    LLDChart mLLDChart;
    BarChart mBarChart;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        XAxis xAxis;
        YAxis yAxis;

        //region Low Level Data Chart Setup
        mLLDChart = view.findViewById(R.id.lld_chart);
        mLLDChart.setMarkerView(new LLDMarkerView(getContext()));
        mLLDChart.setDoubleTapToZoomEnabled(false);
        mLLDChart.setPinchZoom(false);
        mLLDChart.setDescription("");

        xAxis = mLLDChart.getXAxis();
        xAxis.setLabelRotationAngle(90);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        yAxis = mLLDChart.getAxisRight();
        yAxis.setEnabled(false);
        //endregion

        //region Bar Chart Setup
        mBarChart = view.findViewById(R.id.bar_chart);
        mBarChart.setMarkerView(new BarMarkerView(getContext()));
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDescription("");

        xAxis = mBarChart.getXAxis();
        xAxis.setLabelRotationAngle(90);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        yAxis = mBarChart.getAxisRight();
        yAxis.setEnabled(false);
        //endregion
        return view;
    }

    public void setLLDChart(ArrayList<TeamLLDItem> list)
    {
        mBarChart.setVisibility(View.GONE);
        mLLDChart.setVisibility(View.VISIBLE);

        Collections.sort(list, new Comparator<TeamLLDItem>()
        {
            @Override
            public int compare(TeamLLDItem lhs, TeamLLDItem rhs)
            {
                return Integer.compare(lhs.getTeamNumber(), rhs.getTeamNumber());
            }
        });

        ArrayList<String> teamNumbers = new ArrayList<>();
        ArrayList<LLDEntry> entries = new ArrayList<>();
        int i = 0;
        for (TeamLLDItem item : list)
        {
            teamNumbers.add(String.valueOf(item.getTeamNumber()));
            entries.add(new LLDEntry(i,
                                     item.getTeamNumber(),
                                     (float) item.getMaximum(),
                                     (float) item.getMinimum(),
                                     (float) item.getAverage(),
                                     (float) item.getStd()));
        }
        LLDDataSet dataset = new LLDDataSet(entries, "");
        LLDData data = new LLDData(teamNumbers, dataset);
        mLLDChart.setData(data);

        XAxis xAxis = mLLDChart.getXAxis();
        xAxis.setLabelsToSkip(0);

        YAxis yAxis = mLLDChart.getAxisLeft();
        yAxis.setAxisMinValue(0.0f);

        mLLDChart.notifyDataSetChanged();
        mLLDChart.invalidate();
    }

    public void setBarChart(ArrayList<TeamPickListItem> list)
    {
        mLLDChart.setVisibility(View.GONE);
        mBarChart.setVisibility(View.VISIBLE);

        Collections.sort(list, new Comparator<TeamPickListItem>()
        {
            @Override
            public int compare(TeamPickListItem lhs, TeamPickListItem rhs)
            {
                return Integer.compare(lhs.getTeamNumber(), rhs.getTeamNumber());
            }
        });

        ArrayList<String> teamNumbers = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (TeamPickListItem item : list)
        {
            teamNumbers.add(String.valueOf(item.getTeamNumber()));
            entries.add(new BarEntryWithTeamNumber(i, item.getTeamNumber(), item.getSortValue()));
            i++;
        }

        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setDrawValues(false);
        BarData data = new BarData(teamNumbers, dataset);
        mBarChart.setData(data);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setLabelsToSkip(0);

        YAxis yAxis = mBarChart.getAxisLeft();
        yAxis.setAxisMinValue(0.0f);

        mBarChart.notifyDataSetChanged();
        mBarChart.invalidate();

    }

}
