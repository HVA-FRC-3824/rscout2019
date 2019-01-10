package frc3824.rscout2018.custom_charts.lld;

import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface LLDDataProvider extends BarLineScatterCandleBubbleDataProvider
{
    LLDData getLLDData();
}
