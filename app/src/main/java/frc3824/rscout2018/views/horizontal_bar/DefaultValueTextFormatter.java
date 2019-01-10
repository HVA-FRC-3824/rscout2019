package frc3824.rscout2018.views.horizontal_bar;

import java.text.DecimalFormat;

/**
 * Default ValueTextFormatter that simply returns the value as a string.
 */
public class DefaultValueTextFormatter implements ValueTextFormatter
{
    private DecimalFormat mFormat;

    public DefaultValueTextFormatter()
    {
        mFormat = new DecimalFormat("###,###,##0.00");
    }

    @Override
    public String getValueText(float value, float maxVal, float minVal)
    {
        return mFormat.format(value);
    }

    @Override
    public String getMinimum(float minVal)
    {
        return mFormat.format(minVal);
    }

    @Override
    public String getMaximum(float maxVal)
    {
        return mFormat.format(maxVal);
    }
}
