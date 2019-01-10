package frc3824.rscout2018.database.data_models;

import android.databinding.Bindable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Data model containing the logistics information for a specific match
 */
@IgnoreExtraProperties
public class MatchLogistics extends DataModel
{
    //region Match Number
    int matchNumber;

    @Exclude
    @Bindable
    public int getMatchNumber()
    {
        return matchNumber;
    }

    @Exclude
    public void setMatchNumber(int matchNumber)
    {
        this.matchNumber = matchNumber;
        notifyChange();
    }
    //endregion

    //region Team Numbers
    ArrayList<Integer> teamNumbers = new ArrayList<>();

    @Exclude
    public int getTeamNumber(int position)
    {
        if (!(position >= 0 && position < teamNumbers.size()))
        {
            throw new AssertionError();
        }
        return teamNumbers.get(position);
    }

    /**
     * Getter function for the numbers of the teams in a given match
     */
    @Exclude
    @Bindable
    public ArrayList<Integer> getTeamNumbers()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers;
    }

    /**
     * Setter function for the numbers of the teams in a given match
     */
    @Exclude
    public void setTeamNumbers(ArrayList<Integer> teamNumbers)
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        this.teamNumbers = teamNumbers;
        notifyChange();
    }
    //endregion

    @Exclude
    public boolean isRed(int teamNumber)
    {
        if (teamNumbers.size() != 6 || teamNumbers.indexOf(teamNumber) == -1)
        {
            throw new AssertionError();
        }
        return this.teamNumbers.indexOf(teamNumber) >= 3;
    }

    @Exclude
    public boolean isBlue(int teamNumber)
    {
        if (teamNumbers.size() != 6 || teamNumbers.indexOf(teamNumber) == -1)
        {
            throw new AssertionError();
        }
        return this.teamNumbers.indexOf(teamNumber) < 3;
    }

    @Exclude
    public int getBlue1()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers.get(0);
    }

    @Exclude
    public int getBlue2()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers.get(1);
    }

    @Exclude
    public int getBlue3()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers.get(2);
    }

    @Exclude
    public int getRed1()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers.get(3);
    }

    @Exclude
    public int getRed2()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers.get(4);
    }

    @Exclude
    public int getRed3()
    {
        if (teamNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        return teamNumbers.get(5);
    }


    //region Constructors
    public MatchLogistics(int matchNumber)
    {
        this.matchNumber = matchNumber;
    }

    public MatchLogistics()
    {}
    //endregion
}
