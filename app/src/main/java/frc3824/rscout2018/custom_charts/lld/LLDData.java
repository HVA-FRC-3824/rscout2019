package frc3824.rscout2018.custom_charts.lld;

import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

import java.util.ArrayList;
import java.util.List;

public class LLDData extends BarLineScatterCandleBubbleData<ILLDDataSet>
{
    public LLDData(List<String> xVals, List<ILLDDataSet> datasets)
    {
        super(xVals, datasets);
    }

    public LLDData(List<String> xVals, ILLDDataSet dataset)
    {
        super(xVals, toList(dataset));
    }

    private static List<ILLDDataSet> toList(ILLDDataSet dataset)
    {
        List<ILLDDataSet> sets = new ArrayList<>();
        sets.add(dataset);
        return sets;
    }

}
