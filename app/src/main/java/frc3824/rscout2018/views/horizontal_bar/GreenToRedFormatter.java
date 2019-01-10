package frc3824.rscout2018.views.horizontal_bar;


import android.graphics.Color;

public class GreenToRedFormatter implements BarColorFormatter
{
    @Override
    public int getColor(float value, float minimum, float maximum)
    {
        float hsv[] = new float[] {((120f * ((maximum - minimum) - (value - minimum))) / (maximum - minimum)), 1f, 1f};
        return Color.HSVToColor(hsv);
    }
}
