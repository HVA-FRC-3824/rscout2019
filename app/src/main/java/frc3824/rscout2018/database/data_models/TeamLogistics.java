package frc3824.rscout2018.database.data_models;

import android.databinding.Bindable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class TeamLogistics extends DataModel
{
    //region Team Number
    int teamNumber;

    @Exclude
    public int getTeamNumber()
    {
        return teamNumber;
    }

    @Exclude
    public void setTeamNumber(int teamNumber)
    {
        this.teamNumber = teamNumber;
        notifyChange();
    }
    //endregion

    //region Nickname
    String nickname = "";

    /**
     * Getter function for {@link TeamLogistics#nickname}
     */
    @Exclude
    @Bindable
    public String getNickname()
    {
        return nickname;
    }

    /**
     * Setter function for {@link TeamLogistics@nickname}
     * @param
     */
    @Exclude
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
        notifyChange();
    }
    //endregion

    //region Match Numbers
    ArrayList<Integer> matchNumbers = new ArrayList<>();

    /**
     * Getter for the numbers of the matches a specific team is in
     */
    @Exclude
    @Bindable
    public ArrayList<Integer> getMatchNumbers()
    {
        return matchNumbers;
    }

    /**
     * Setter for the numbers of the matches a specific team is in
     */
    @Exclude
    public void setMatchNumbers(ArrayList<Integer> matchNumbers)
    {
        if (matchNumbers.size() != 6)
        {
            throw new AssertionError();
        }
        this.matchNumbers = matchNumbers;
        notifyChange();
    }
    //endregion

    //region Constructors
    public TeamLogistics(int teamNumber)
    {
        this.teamNumber = teamNumber;
        mDirty = false;
    }

    public TeamLogistics()
    {}
    //endregion
}
