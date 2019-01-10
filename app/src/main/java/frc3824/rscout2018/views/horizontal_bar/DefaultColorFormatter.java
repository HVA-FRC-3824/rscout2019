package frc3824.rscout2018.views.horizontal_bar;

/**
 * Default BarColorFormatter class that supports a single color.
 */
public class DefaultColorFormatter implements BarColorFormatter
{
    private int mColor;

    public DefaultColorFormatter(int color)
    {
        mColor = color;
    }

    @Override
    public int getColor(float value, float maxVal, float minVal)
    {
        return mColor;
    }
}
