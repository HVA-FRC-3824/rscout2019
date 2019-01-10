package frc3824.rscout2018.database.data_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


public class TeamLLDItem extends BaseObservable
{
    //region Constructor
    public TeamLLDItem(int teamNumber, double min, double max, double average, double std)
    {
        this.teamNumber = teamNumber;
        this.minimum = min;
        this.maximum = max;
        this.average = average;
        this.std = std;
    }
    //endregion

    //region Team Number
    int teamNumber;

    public int getTeamNumber()
    {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber)
    {
        this.teamNumber = teamNumber;
    }
    //endregion

    //region Average
    double average;

    /**
     * Getter function for the average
     * @returns The average
     */
    @Bindable
    public double getAverage()
    {
        return average;
    }

    /**
     * Setter function for the average
     * @param average The average
     */
    public void setAverage(double average)
    {
        this.average = average;
    }
    //endregion
    //region Standard Deviation
    double std;

    /**
     * Getter function for the standard deviation
     * @return The standard deviation
     */
    @Bindable
    public double getStd()
    {
        return std;
    }

    /**
     * Setter function for the standard deviation
     * @param std The standard deviation
     */
    public void setStd(double std)
    {
        this.std = std;
    }
    //endregion
    //region Minimum
    double minimum;

    /**
     * Getter function for the minimum
     * @returns The minimum
     */
    @Bindable
    public double getMinimum()
    {
        return minimum;
    }

    /**
     * Setter function for the minimum
     * @param minimum The minimum
     */
    public void setMinimum(double minimum)
    {
        this.minimum = minimum;
    }
    //endregion
    //region Maximum
    double maximum;

    /**
     * Getter function for the maximum
     * @return The maximum
     */
    @Bindable
    public double getMaximum()
    {
        return maximum;
    }

    /**
     * Setter function for the maximum
     * @param maximum The maximum
     */
    public void setMaximum(double maximum)
    {
        this.maximum = maximum;
    }
    //endregion
}
