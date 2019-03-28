package frc3824.rscout2018.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.List;

import activitystarter.ActivityStarter;
import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.R;
import frc3824.rscout2018.fragments.pick_list.ChartFragment;
import frc3824.rscout2018.fragments.pick_list.PickListFragment;

@MakeActivityStarter
public class PickListActivity extends RScoutActivity implements AdapterView.OnItemSelectedListener
{
    ChartFragment mChart;
    PickListFragment mList;

    Spinner mMainDropDown;
    Spinner mSecondaryDropDown;

    List<String> mMainOptions;
    List<String> mSecondaryOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);
        ActivityStarter.fill(this);

        /*
        FragmentManager fm = getFragmentManager();
        mChart = (ChartFragment) fm.findFragmentById(R.id.chart);
        mList = (PickListFragment) fm.findFragmentById(R.id.list);

        mMainDropDown = findViewById(R.id.main_dropdown);
        mMainDropDown.setOnItemSelectedListener(this);
        mSecondaryDropDown = findViewById(R.id.secondary_dropdown);
        mSecondaryDropDown.setOnItemSelectedListener(this);

        mMainOptions = Arrays.asList(Constants.PickList.MAIN_SORTING);
        */
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        /*
        int parentId = parent.getId();
        if (parentId == R.id.main_dropdown)
        {
            ArrayAdapter<String> secondaryAdapter = null;
            switch (Constants.PickList.MAIN_SORTING[position])
            {
                case Constants.PickList.CARGO:
                    secondaryAdapter = new ArrayAdapter<>(this,
                                                          android.R.layout.simple_dropdown_item_1line,
                                                          Constants.PickList.PowerCubes.OPTIONS);
                    break;
                case Constants.PickList.CLIMB:
                    break;
                case Constants.PickList.FOULS:
                    break;
                default:
                    assert(false);
            }
            mSecondaryDropDown.setAdapter(secondaryAdapter);
        }
        else if (parentId == R.id.secondary_dropdown)
        {
            updateChart();
        }
        */
    }

    private void updateChart()
    {
        //int mainDropdownPosition = mMainDropDown.getSelectedItemPosition();
        //int secondaryDropdownPosition = mSecondaryDropDown.getSelectedItemPosition();


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
