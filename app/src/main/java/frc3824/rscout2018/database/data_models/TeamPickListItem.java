package frc3824.rscout2018.database.data_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


/**
 * Data model for a team's placement in the pick list
 */
public class TeamPickListItem extends BaseObservable
{
    //region Team Number
    int teamNumber;

    /**
     * Returns the team number for this pick list item
     * @return
     */
    @Bindable
    public int getTeamNumber()
    {
        return teamNumber;
    }

    /**
     * Sets the team number for this pick list item
     * @param teamNumber
     */
    public void setTeamNumber(int teamNumber)
    {
        this.teamNumber = teamNumber;
        notifyChange();
    }
    //endregion
    //region Nickname
    String nickname;

    /**
     * Returns the nickname for this pick list item
     */
    @Bindable
    public String getNickname()
    {
        return nickname;
    }

    /**
     * Sets the nickname for this pick list item
     */
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
        notifyChange();
    }
    //endregion
    //region Picked
    boolean picked;


    /**
     * Getter function for if the team has been picked
     */
    @Bindable
    public boolean isPicked()
    {
        return picked;
    }

    public void setPicked(boolean picked)
    {
        this.picked = picked;
        notifyChange();
    }
    //endregion
    //region Sort Value
    float sortValue;

    @Bindable
    public float getSortValue()
    {
        return sortValue;
    }

    public void setSortValue(float sortValue)
    {
        this.sortValue = sortValue;
        notifyChange();
    }
    //endregion
}
