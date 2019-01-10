package frc3824.rscout2018.database.data_models;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.firebase.database.Exclude;

import frc3824.rscout2018.views.SavableCounter;

public class SuperMatchData extends DataModel
{
    //region Logistics
    //region Match Number
    int matchNumber;

    /**
     * Getter function for match number
     * @returns The match number
     */
    @Bindable
    public int getMatchNumber()
    {
        return matchNumber;
    }

    /**
     * Setter function for match number
     * @param matchNumber The match number
     */
    public void setMatchNumber(int matchNumber)
    {
        this.matchNumber = matchNumber;
        notifyChange();
    }
    //endregion
    //region Scout Name
    String scoutName = "";

    /**
     * Getter function for scout name
     * @returns The name of the scout who recorded the information for {@link TeamMatchData#teamNumber} in {@link TeamMatchData#matchNumber}
     */
    @Bindable
    public String getScoutName()
    {
        return scoutName;
    }

    /**
     * Setter function for scout name
     * @param scoutName The name of the scout who recorded the information for {@link TeamMatchData#teamNumber} in {@link TeamMatchData#matchNumber}
     */
    public void setScoutName(String scoutName)
    {
        this.scoutName = scoutName;
        notifyChange();
    }

    @Exclude
    @Bindable
    public TextWatcher getScoutNameWatcher()
    {
        return new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setScoutName(s.toString());
            }
        };
    }
    //endregion
    //endregion

    //region Power Ups
    //region Force
    //region Red
    int forceRed;

    /**
     *
     * Returns the number of cubes that were placed in the force column by the red alliance
     */
    @Bindable
    public int getForceRed()
    {
        return forceRed;
    }

    /**
     * Sets the number of cubes that were placed in the force column by the red alliance
     */
    public void setForceRed(int forceRed)
    {
        this.forceRed = forceRed;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getForceRedListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setForceRed(value);
            }
        };
    }
    //endregion
    //region Blue
    int forceBlue;

    /**
     *
     * Returns the number of cubes that were placed in the force column by the blue alliance
     */
    @Bindable
    public int getForceBlue()
    {
        return forceBlue;
    }

    /**
     * Sets the number of cubes that were placed in the force column by the blue alliance
     */
    public void setForceBlue(int forceBlue)
    {
        this.forceBlue = forceBlue;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getForceBlueListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setForceBlue(value);
            }
        };
    }
    //endregion
    //endregion
    //region Levitate
    //region Red
    int levitateRed;

    /**
     * Returns the number of cubes that were placed in the levitate column by the red alliance
     */
    @Bindable
    public int getLevitateRed()
    {
        return levitateRed;
    }

    /**
     * Sets the number of cubes that were placed in the levitate column by the red alliance
     */
    public void setLevitateRed(int levitateRed)
    {
        this.levitateRed = levitateRed;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getLevitateRedListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setLevitateRed(value);
            }
        };
    }
    //endregion
    //region Blue
    int levitateBlue;

    /**
     * Returns the number of cubes that were placed in the levitate column by the blue alliance
     */
    @Bindable
    public int getLevitateBlue()
    {
        return levitateBlue;
    }

    /**
     * Sets the number of cubes that were placed in the levitate column by the blue alliance
     */
    public void setLevitateBlue(int levitateBlue)
    {
        this.levitateBlue = levitateBlue;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getLevitateBlueListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setLevitateBlue(value);
            }
        };
    }
    //endregion
    //endregion
    //region Boost
    //region Red
    int boostRed;

    /**
     * Returns the number of cubes that were placed in the boost column by the red alliance
     */
    @Bindable
    public int getBoostRed()
    {
        return boostRed;
    }

    /**
     * Sets the number of cubes that were placed in the boost column by the red alliance
     */
    public void setBoostRed(int boostRed)
    {
        this.boostRed = boostRed;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getBoostRedListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setBoostRed(value);
            }
        };
    }
    //endregion
    //region Blue
    int boostBlue;

    /**
     * Returns the number of cubes that were placed in the boost column by the blue alliance
     */
    @Bindable
    public int getBoostBlue ()
    {
        return boostBlue ;
    }

    /**
     * Sets the number of cubes that were placed in the boost column by the blue alliance
     */
    public void setBoostBlue (int boostBlue )
    {
        this.boostBlue = boostBlue ;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getBoostBlueListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setBoostBlue(value);
            }
        };
    }
    //endregion
    //endregion
    //endregion

    //region Misc
    //region Notes
    String notes = "";

    /**
     * Returns the notes taken by the super scout for this match
     */
    @Bindable
    public String getNotes()
    {
        return notes;
    }

    /**
     * Sets the notes taken by the super scout for this match
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
        notifyChange();
    }

    @Exclude
    @Bindable
    public TextWatcher getNotesWatcher()
    {
        return new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setNotes(s.toString());
            }
        };
    }
    //endregion
    //endregion

    //region Constructors

    public SuperMatchData(int matchNumber)
    {
        this.matchNumber = matchNumber;
        mDirty = false;
    }

    public SuperMatchData()
    {}
    //endregion
    //endregion
}
