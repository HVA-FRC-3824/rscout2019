package frc3824.rscout2018.views.horizontal_bar;

/**
 * Listener for callbacks when selecting values on the ValueBar by touch
 * gesture.
 */
public interface BarSelectionListener
{
    /**
     * Called every time the user moves the finger on the ValueBar.
     *
     * @param value
     * @param minimum
     * @param maximum
     * @param bar
     */
    void onSelectionUpdate(float value, float minimum, float maximum, HorizontalBar bar);

    /**
     * Called when the user releases his finger from the ValueBar.
     *
     * @param value
     * @param minimum
     * @param maximum
     * @param bar
     */
    void onValueSelected(float value, float minimum, float maximum, HorizontalBar bar);
}
