package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.Team;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.utilities.Constants;

/**
 * Created by frc3824.
 */
public class ClimbView extends ConstraintLayout
{
    LineChart mTimeChart;
    YAxis mYAxis;
    PieChart mStatusChart;
    PieChart mMethodChart;

    Team mTeam;
    SparseArray<TeamMatchData> mMatches;

    public ClimbView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_climb, this, true);

        mTimeChart = findViewById(R.id.time);
        mStatusChart = findViewById(R.id.status);
        mMethodChart = findViewById(R.id.method);

        setup();

        if(mTeam != null)
        {
            new UpdateTask().execute();
        }
    }

    public void setTeam(Team team)
    {
        mTeam = team;
        mMatches = mTeam.getMatches();
        if(mTimeChart != null)
        {
            new UpdateTask().execute();
        }
    }

    public void setup()
    {
        mStatusChart.setUsePercentValues(true);
        mStatusChart.setDescription(null);
        mStatusChart.setDrawHoleEnabled(false);
        mStatusChart.setRotationEnabled(false);
        mStatusChart.setHighlightPerTapEnabled(true);
        Legend statusLegend = mStatusChart.getLegend();
        statusLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        statusLegend.setWordWrapEnabled(true);
        statusLegend.setTextSize(14);

        mMethodChart.setUsePercentValues(true);
        mMethodChart.setDescription(null);
        mMethodChart.setDrawHoleEnabled(false);
        mMethodChart.setRotationEnabled(false);
        mMethodChart.setHighlightPerTapEnabled(true);
        Legend methodLegend = mMethodChart.getLegend();
        methodLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        methodLegend.setWordWrapEnabled(true);
        methodLegend.setTextSize(14);

        mTimeChart.getLegend().setEnabled(false);
        mTimeChart.setDescription(null);
        XAxis xaxis = mTimeChart.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setAvoidFirstLastClipping(true);
        mYAxis = mTimeChart.getAxisLeft();
        mYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mTimeChart.getAxisRight().setEnabled(false);
        mTimeChart.setDoubleTapToZoomEnabled(false);
        mTimeChart.setPinchZoom(false);
    }

    private class UpdateTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects)
        {
            ArrayList<Entry> statusEntries = new ArrayList<>();
            ArrayList<Entry> methodEntries = new ArrayList<>();
            ArrayList<Entry> timeEntries = new ArrayList<>();

            List statusOptions = Arrays.asList(Constants.MatchScouting.Climb.Status.OPTIONS);
            List methodOptions = Arrays.asList(Constants.MatchScouting.Climb.Method.OPTIONS);

            int[] statusOptionsFrequency = new int[statusOptions.size()];
            int[] methodOptionsFrequency = new int[methodOptions.size()];

            int j = 0;
            float timeSum = 0;
            float timeNum = 0;
            ArrayList<String> matchLabels = new ArrayList<>();
            for (int i = 0, end = mMatches.size(); i < end; i++)
            {
                TeamMatchData tmd = mMatches.valueAt(i);
                int statusIndex = statusOptions.indexOf(tmd.getClimbStatus());
                if(statusIndex > -1)
                {
                    statusOptionsFrequency[statusIndex]++;
                    int methodIndex = methodOptions.indexOf(tmd.getClimbMethod());
                    if (methodIndex > -1)
                    {
                        methodOptionsFrequency[methodIndex]++;
                    }
                    if (tmd.getClimbTime() > 0)
                    {
                        timeSum += (float) tmd.getClimbTime() / 1000.0f;
                        timeNum++;
                    }
                    timeEntries.add(new Entry((float) tmd.getClimbTime() / 1000.0f, j));
                    j++;

                    matchLabels.add(String.valueOf(tmd.getMatchNumber()));
                }
            }
            LineDataSet lineDataSet = new LineDataSet(timeEntries, "Times");
            lineDataSet.setCircleColor(Color.GREEN);
            lineDataSet.setColor(Color.GREEN);
            LineData data = new LineData(matchLabels);
            data.addDataSet(lineDataSet);

            float timeAvg = timeNum == 0 ? 0 : timeSum / timeNum;
            ArrayList<Entry> timeAverageEntries = new ArrayList<>();
            for(int i = 0; i < mMatches.size(); i++)
            {
                timeAverageEntries.add(new Entry(timeAvg, i));
            }
            LineDataSet lineAverages = new LineDataSet(timeAverageEntries, "Average");
            lineAverages.setCircleColor(Color.RED);
            lineAverages.setColor(Color.RED);
            data.addDataSet(lineAverages);
            mTimeChart.setData(data);


            ArrayList<Integer> statusColors = new ArrayList<>();
            ArrayList<String> statusLabels = new ArrayList<>();

            for (int i = 0; i < statusOptions.size(); i++)
            {
                if(statusOptionsFrequency[i] > 0)
                {
                    statusEntries.add(new Entry(statusOptionsFrequency[i], i));
                    statusColors.add(Constants.TeamStats.Climb.STATUS_COLORS[i]);
                    statusLabels.add(Constants.MatchScouting.Climb.Status.OPTIONS[i]);
                }
            }
            PieDataSet statusDataSet = new PieDataSet(statusEntries, "");
            statusDataSet.setColors(statusColors);
            mStatusChart.setData(new PieData(statusLabels, statusDataSet));

            ArrayList<Integer> methodColors = new ArrayList<>();
            ArrayList<String> methodLabels = new ArrayList<>();
            for (int i = 0; i < methodOptions.size(); i++)
            {
                if(methodOptionsFrequency[i] > 0)
                {
                    methodEntries.add(new Entry(methodOptionsFrequency[i], i));
                    methodColors.add(Constants.TeamStats.Climb.METHOD_COLORS[i]);
                    methodLabels.add(Constants.MatchScouting.Climb.Method.OPTIONS[i]);
                }
            }
            PieDataSet methodDataSet = new PieDataSet(methodEntries, "");
            methodDataSet.setColors(methodColors);
            mMethodChart.setData(new PieData(methodLabels, methodDataSet));

            publishProgress();

            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects)
        {
            mTimeChart.notifyDataSetChanged();
            mTimeChart.invalidate();

            mStatusChart.notifyDataSetChanged();
            mStatusChart.invalidate();

            mMethodChart.notifyDataSetChanged();
            mMethodChart.invalidate();
        }
    }
}
