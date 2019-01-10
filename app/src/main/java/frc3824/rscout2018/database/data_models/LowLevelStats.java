package frc3824.rscout2018.database.data_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.Exclude;

import java.util.List;

/**
 * @class LowLevelStats
 * @brief The aggregated statistics about a particular element of a team's performance
 *
 * Contains average, standard deviation, minimum, maximum, and ranking
 */
public class LowLevelStats extends BaseObservable
{
    //region Total
    double total;

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
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
    //region Ranking
    int ranking;

    /**
     * Getter function for the ranking
     * @returns The ranking
     */
    @Bindable
    public int getRanking()
    {
        return ranking;
    }

    /**
     * Setter function for the ranking
     * @param ranking The ranking
     */
    public void setRanking(int ranking)
    {
        this.ranking = ranking;
    }
    //endregion

    //region Constructors
    public LowLevelStats()
    {
    }
    //endregion

    @Exclude
    public static LowLevelStats fromInt(List<Integer> list) {
        LowLevelStats lls = new LowLevelStats();
        if(list.size() == 0)
        {
            lls.total = 0;
            lls.average = 0.0;
            lls.maximum = 0.0;
            lls.minimum = 0.0;
            lls.std = 0.0;
            return lls;
        }

        lls.total = 0.0;
        lls.maximum = Double.MIN_VALUE;
        lls.minimum = Double.MAX_VALUE;
        for(int i: list)
        {
            lls.maximum = Math.max(i, lls.maximum);
            lls.minimum = Math.min(i, lls.minimum);
            lls.total += i;
        }
        lls.average = lls.total / (double) list.size();

        lls.std = 0.0;
        for(int i: list)
        {
            lls.std += Math.pow((double)i - lls.average, 2);
        }

        lls.std /= list.size();
        lls.std = Math.sqrt(lls.std);

        return lls;
    }

    @Exclude
    public static LowLevelStats fromDouble(List<Double> list) {
        LowLevelStats lls = new LowLevelStats();
        if(list.size() == 0)
        {
            lls.total = 0;
            lls.average = 0.0;
            lls.maximum = 0.0;
            lls.minimum = 0.0;
            lls.std = 0.0;
            return lls;
        }

        lls.total = 0.0;
        lls.maximum = Double.MIN_VALUE;
        lls.minimum = Double.MAX_VALUE;
        for(double i: list)
        {
            lls.maximum = Math.max(i, lls.maximum);
            lls.minimum = Math.min(i, lls.minimum);
            lls.total += i;
        }

        lls.average = lls.total / (double) list.size();

        lls.std = 0.0;
        for(double i: list)
        {
            lls.std += Math.pow(i - lls.average, 2);
        }

        lls.std /= list.size();
        lls.std = Math.sqrt(lls.std);

        return lls;
    }

    @Exclude
    public static LowLevelStats fromBoolean(List<Boolean> list) {
        LowLevelStats lls = new LowLevelStats();
        if(list.size() == 0)
        {
            lls.total = 0;
            lls.average = 0.0;
            lls.maximum = 0.0;
            lls.minimum = 0.0;
            lls.std = 0.0;
            return lls;
        }

        lls.total = 0;
        lls.maximum = Double.MIN_VALUE;
        lls.minimum = Double.MAX_VALUE;
        for(boolean i: list)
        {
            if(i)
            {
                lls.maximum = 1.0;
            }
            else
            {
                lls.minimum = 0.0;
            }
            lls.total += i ? 1.0 : 0.0;
        }
        lls.average = lls.total / (double) list.size();

        lls.std = 0.0;
        for(boolean i: list)
        {
            lls.std += Math.pow((i ? 1.0 : 0.0) - lls.average, 2);
        }

        lls.std /= list.size();
        lls.std = Math.sqrt(lls.std);

        return lls;
    }
}
