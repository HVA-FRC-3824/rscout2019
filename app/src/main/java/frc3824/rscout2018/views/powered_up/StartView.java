package frc3824.rscout2018.views.powered_up;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.Team;
import frc3824.rscout2018.database.data_models.TeamMatchData;

/**
 * Created by frc3824
 */
public class StartView extends ConstraintLayout
{
    // int mTeamNumber;

    SparseArray<TeamMatchData> mMatches = null;

    StartLocationView mStartLocationView = null;
    PieChart mStartWithCubeChart = null;
    PieChart mAutoCrossChart = null;

    public StartView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_start, this, true);

        mStartLocationView = findViewById(R.id.start_location_heatmap);
        mStartWithCubeChart = findViewById(R.id.start_with_cube_pie);
        mAutoCrossChart = findViewById(R.id.auto_cross_pie);

        // Setup pie charts
        mStartWithCubeChart.setUsePercentValues(true);
        mStartWithCubeChart.setDescription(null);
        mStartWithCubeChart.setDrawHoleEnabled(false);
        mStartWithCubeChart.setRotationEnabled(false);
        mStartWithCubeChart.setHighlightPerTapEnabled(true);

        mAutoCrossChart.setUsePercentValues(true);
        mAutoCrossChart.setDescription(null);
        mAutoCrossChart.setDrawHoleEnabled(false);
        mAutoCrossChart.setRotationEnabled(false);
        mAutoCrossChart.setHighlightPerTapEnabled(true);

        if(mMatches != null)
        {
            new UpdateTask().execute();
        }
    }

    public void setTeam(Team team)
    {
        mMatches = team.getMatches();
        if(mStartLocationView != null)
        {
            new UpdateTask().execute();
        }
    }

    public class UpdateTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects)
        {
            if(mStartLocationView != null && mMatches != null)
            {
                mStartLocationView.setData(mMatches);

                // Populate the chart
                ArrayList<Entry> entries = new ArrayList<>();
                int yes = 0;
                int no = 0;
                TeamMatchData tmd;
                for(int i = 0, end = mMatches.size(); i < end; i++)
                {
                   tmd  = mMatches.valueAt(i);
                   if(tmd.getStartedWithCube())
                   {
                       yes++;
                   }
                   else
                   {
                       no++;
                   }
                }

                entries.add(new Entry(yes, 0));
                entries.add(new Entry(no, 1));
                PieDataSet dataset = new PieDataSet(entries, "");
                dataset.setColors(new int[]{
                    Color.GREEN,
                    Color.RED
                });
                mStartWithCubeChart.setData(new PieData(new String[]{"Yes", "No"}, dataset));

                entries = new ArrayList<>();
                yes = 0;
                no = 0;
                for(int i = 0, end = mMatches.size(); i < end; i++)
                {
                    tmd  = mMatches.valueAt(i);
                    if(tmd.getCrossedAutoLine())
                    {
                        yes++;
                    }
                    else
                    {
                        no++;
                    }
                }

                entries.add(new Entry(yes, 0));
                entries.add(new Entry(no, 1));
                dataset = new PieDataSet(entries, "");
                dataset.setColors(new int[]{
                        Color.GREEN,
                        Color.RED
                });
                mAutoCrossChart.setData(new PieData(new String[]{"Yes", "No"}, dataset));

                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects)
        {
            mStartWithCubeChart.notifyDataSetChanged();
            mStartWithCubeChart.invalidate();
            mAutoCrossChart.notifyDataSetChanged();
            mAutoCrossChart.invalidate();
        }
    }
}
