package frc3824.rscout2018.views.horizontal_bar;

public interface ValueTextFormatter
{
    String getValueText(float value, float minimum, float maximum);
    String getMinimum(float minimum);
    String getMaximum(float maximum);
}
