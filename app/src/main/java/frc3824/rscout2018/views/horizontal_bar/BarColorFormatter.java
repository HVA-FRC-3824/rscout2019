package frc3824.rscout2018.views.horizontal_bar;

/**
 * Interface for providing custom volors for the HorizontalBar
 */
public interface BarColorFormatter
{
    /**
     * Use this method to return whatever color you would like the HorizontalBar to have.
     *
     * @param value
     * @param minimum
     * @param maximum
     * @return
     */
    int getColor(float value, float minimum, float maximum);
}
