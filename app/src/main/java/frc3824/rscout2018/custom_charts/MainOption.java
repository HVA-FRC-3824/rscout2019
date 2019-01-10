package frc3824.rscout2018.custom_charts;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frc3824
 */
public class MainOption
{
    String mKey;
    ArrayAdapter<String> mAdapter;
    Map<String, SecondaryOption> mOptions = new HashMap<>();

    public MainOption(Context context, String key, ArrayList<SecondaryOption> options)
    {
        mKey = key;

        ArrayList<String> list = new ArrayList<>();
        for(SecondaryOption option : options)
        {
            mOptions.put(option.getKey(), option);
            list.add(option.getKey());
        }
        mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, list);
    }

    public ArrayAdapter<String> getAdapter()
    {
        return mAdapter;
    }

    public String getOption(int position)
    {
        return mAdapter.getItem(position);
    }

    public SecondaryOption getSecondary(String key)
    {
        if(mOptions.containsKey(key))
        {
            return mOptions.get(key);
        }
        return null;
    }
}
