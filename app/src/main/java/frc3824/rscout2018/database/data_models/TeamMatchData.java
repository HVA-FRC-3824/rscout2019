package frc3824.rscout2018.database.data_models;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;

import com.bumptech.glide.annotation.Excludes;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

import frc3824.rscout2018.database.data_models.powered_up.CubeEvent;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.views.SavableCounter;

import frc3824.rscout2018.database.data_models.powered_up.HatchEvent;


/**
 * Data Model for a single team in a single match
 */
@IgnoreExtraProperties
public class TeamMatchData extends DataModel
{
    //region Logistics
    //region Match Number
    int matchNumber;

    /**
     * Getter function for match number
     */
    @Exclude
    @Bindable
    public int getMatchNumber()
    {
        return matchNumber;
    }

    /**
     * Setter function for match number
     *
     * @param matchNumber The match number
     */
    @Exclude
    public void setMatchNumber(int matchNumber)
    {
        this.matchNumber = matchNumber;
        notifyChange();
    }

    //endregion
    //region Team Number
    int teamNumber;

    /**
     * Getter function for team number
     */
    @Exclude
    @Bindable
    public int getTeamNumber()
    {
        return teamNumber;
    }

    /**
     * Setter function for team number
     *
     * @param teamNumber The team number
     */
    @Exclude
    public void setTeamNumber(int teamNumber)
    {
        this.teamNumber = teamNumber;
        notifyChange();
    }

    //endregion
    //region Scout Name
    String scoutName = "";

    /**
     * Getter function for scout name
     */
    @Exclude
    @Bindable
    public String getScoutName()
    {
        return scoutName;
    }

    /**
     * Setter function for scout name
     *
     * @param scoutName The name of the scout who recorded the information for {@link TeamMatchData#teamNumber} in {@link TeamMatchData#matchNumber}
     */
    @Exclude
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

    //region Fouls
    //region Fouls
    int fouls;

    /**
     * Getter function for fouls
     */
    @Exclude
    @Bindable
    public int getFouls()
    {
        return fouls;
    }

    /**
     * Setter function for fouls
     *
     * @param fouls The number of normal fouls caused by this team in this match
     */
    @Exclude
    public void setFouls(int fouls)
    {
        this.fouls = fouls;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getFoulsListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setFouls(value);
            }
        };
    }
    //endregion
    //region Tech Fouls
    int techFouls;

    /**
     * Getter function for tech fouls
     */
    @Exclude
    @Bindable
    public int getTechFouls()
    {
        return techFouls;
    }

    /**
     * Setter function for tech fouls
     *
     * @param techFouls The number of tech fouls caused by this team in this match
     */
    @Exclude
    public void setTechFouls(int techFouls)
    {
        this.techFouls = techFouls;
        notifyChange();
    }

    @Exclude
    @Bindable
    public SavableCounter.CountListener getTechFoulsListener()
    {
        return new SavableCounter.CountListener()
        {
            @Override
            public void onChange(int value)
            {
                setTechFouls(value);
            }
        };
    }

    //endregion
    //region Yellow Card
    boolean yellowCard;

    /**
     * Getter function for yellow card
     *
     * @return Whether a yellow card was received in match {@link TeamMatchData#matchNumber} by team {@link TeamMatchData#teamNumber}
     */
    @Exclude
    @Bindable
    public boolean isYellowCard()
    {
        return yellowCard;
    }

    /**
     * Setter function for yellow card
     *
     * @param yellowCard Whether a yellow card was received in match {@link TeamMatchData#matchNumber} by team {@link TeamMatchData#teamNumber}
     */
    @Exclude
    public void setYellowCard(boolean yellowCard)
    {
        this.yellowCard = yellowCard;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getYellowCardListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setYellowCard(isChecked);
            }
        };
    }

    //endregion
    //region Red Card
    boolean redCard;

    /**
     * Getter function for red card
     *
     * @return Whether a red card was received in match {@link TeamMatchData#matchNumber} by team {@link TeamMatchData#teamNumber}
     */
    @Exclude
    @Bindable
    public boolean isRedCard()
    {
        return redCard;
    }

    /**
     * Setter function for red card
     *
     * @param redCard Whether a red card was received in match {@link TeamMatchData#matchNumber} by team {@link TeamMatchData#teamNumber}
     */
    @Exclude
    public void setRedCard(boolean redCard)
    {
        this.redCard = redCard;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getRedCardListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setRedCard(isChecked);
            }
        };
    }

    //endregion
    //endregion

    //region Misc
    //region DQ
    boolean dq;

    /**
     * Getter function for whether team {@link TeamMatchData#teamNumber} was disqualified in match {@link TeamMatchData#matchNumber}
     */
    @Exclude
    @Bindable
    public boolean isDq()
    {
        return dq;
    }

    /**
     * Setter function for whether team {@link TeamMatchData#teamNumber} was disqualified in match {@link TeamMatchData#matchNumber}
     *
     * @param dq Whether team {@link TeamMatchData#teamNumber} was disqualified in match {@link TeamMatchData#matchNumber}
     */
    @Exclude
    public void setDq(boolean dq)
    {
        this.dq = dq;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getDqListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setDq(isChecked);
            }
        };
    }

    //endregion
    //region No Show
    boolean noShow;

    /**
     * Getter function for whether team {@link TeamMatchData#teamNumber} did not show up to match {@link TeamMatchData#matchNumber}
     */
    @Exclude
    @Bindable
    public boolean isNoShow()
    {
        return noShow;
    }

    /**
     * Setter function for whether team {@link TeamMatchData#teamNumber} did not show up to match {@link TeamMatchData#matchNumber}
     *
     * @param noShow Whether team {@link TeamMatchData#teamNumber} did not show up to match {@link TeamMatchData#matchNumber}
     */
    @Exclude
    public void setNoShow(boolean noShow)
    {
        this.noShow = noShow;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getNoShowListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setNoShow(isChecked);
            }
        };
    }
    //endregion
    //region Notes
    String notes = "";

    /**
     * Getter function for notes
     */
    @Exclude
    @Bindable
    public String getNotes()
    {
        return notes;
    }

    /**
     * Setter function for notes
     *
     * @param notes The notes taken on team {@link TeamMatchData#teamNumber} in match {@link TeamMatchData#matchNumber}
     */
    @Exclude
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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

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

    //region Game Specific
    //region Autonomous
    //region Started with Cube
    boolean startedWithCube;

    @Exclude
    @Bindable
    public boolean getStartedWithCube()
    {
        return startedWithCube;
    }

    @Exclude
    public void setStartedWithCube(boolean startedWithCube)
    {
        this.startedWithCube = startedWithCube;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getStartedWithCubeListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setStartedWithCube(isChecked);
            }
        };
    }
    //endregion

    //region Knox started with hatch
    boolean startedWithHatch;

    @Exclude
    @Bindable
    public boolean getStartedWithHatch()
    {
        return startedWithHatch;
    }

    @Exclude
    public void setStartedWithHatch(boolean startedWithHatch)
    {
        this.startedWithHatch = startedWithHatch;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getStartedWithHatchListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setStartedWithHatch(isChecked);
            }
        };
    }
    //endregion
    //region Crossed Auto Line
    boolean crossedAutoLine;

    /**
     * Returns whether the team crossed the auto line
     *
     * @return
     */
    @Exclude
    @Bindable
    public boolean getCrossedAutoLine()
    {
        return crossedAutoLine;
    }

    /**
     * Sets whether the team crossed the auto line
     */
    @Exclude
    public void setCrossedAutoLine(boolean crossedAutoLine)
    {
        this.crossedAutoLine = crossedAutoLine;
        notifyChange();
    }

    @Exclude
    @Bindable
    public CompoundButton.OnCheckedChangeListener getCrossedAutoLineListener()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                setCrossedAutoLine(isChecked);
            }
        };
    }

    //endregion
    //region Start Location X
    double startLocationX = -1;

    /**
     * Returns the start location x as a percentage of the width of the field
     */
    @Exclude
    @Bindable
    public double getStartLocationX()
    {
        return startLocationX;
    }

    /**
     * Sets the start location x as a percentage of the width of the field
     */
    @Exclude
    public void setStartLocationX(float startLocationX)
    {
        this.startLocationX = startLocationX;
        notifyChange();
    }

    //endregion
    //region Start Location Y
    double startLocationY = -1;

    /**
     * Returns the start location y as a percentage of the depth of the field
     */
    @Exclude
    @Bindable
    public double getStartLocationY()
    {
        return startLocationY;
    }

    /**
     * Sets the start location y as a percentage of the depth of the field
     */
    @Exclude
    public void setStartLocationY(float startLocationY)
    {
        this.startLocationY = startLocationY;
        notifyChange();
    }

    //endregion
    //region Cube Events
    ArrayList<CubeEvent> autoCubeEvents = new ArrayList<>();

    @Exclude
    @Bindable
    public ArrayList<CubeEvent> getAutoCubeEvents()
    {
        return autoCubeEvents;
    }

    @Exclude
    public void setAutoCubeEvents(ArrayList<CubeEvent> autoCubeEvents)
    {
        this.autoCubeEvents = autoCubeEvents;
        notifyChange();
    }

    //endregion
    //endregion
    //region Teleop
    //region Cube Events
    ArrayList<CubeEvent> teleopCubeEvents = new ArrayList<>();

    @Exclude
    @Bindable
    public ArrayList<CubeEvent> getTeleopCubeEvents()
    {
        return teleopCubeEvents;
    }

    @Exclude
    public void setTeleopCubeEvents(ArrayList<CubeEvent> teleopCubeEvents)
    {
        this.teleopCubeEvents = teleopCubeEvents;
        notifyChange();
    }

    //endregion
    //endregion

    //region hatchEvent
    ArrayList<HatchEvent> autoHatchEvents = new ArrayList<>();
    ArrayList<HatchEvent> teleopHatchEvents = new ArrayList<>();

    @Exclude
    @Bindable
    public ArrayList<HatchEvent> getAutoHatchEvents(){return autoHatchEvents;}
    public void setAutoHatchEvents(ArrayList<HatchEvent> autoHatchEvents){this.autoHatchEvents= autoHatchEvents;notifyChange();}
    @Exclude
    @Bindable
    public ArrayList<HatchEvent> getTeleopHatchEvents(){return teleopHatchEvents;}
    public void setTeleopHatchEvents(ArrayList<HatchEvent> teleopHatchEvents){this.teleopHatchEvents=teleopHatchEvents;notifyChange();}
    //endregion

    //region Endgame
    //region Climb Time
    long climbTime;

    @Exclude
    @Bindable
    public long getClimbTime()
    {
        return climbTime;
    }

    @Exclude
    public void setClimbTime(long climbTime)
    {
        this.climbTime = climbTime;
        notifyChange();
    }
    //endregion
    //region Climb Status
    String climbStatus = "";

    /**
     * Returns the status of the climb (whether or not
     * it was successful and if not then why)
     */
    @Exclude
    @Bindable
    public String getClimbStatus()
    {
        return climbStatus;
    }

    /**
     * Set the status of the climb (whether or not
     * it was successful and if not then why)
     */
    @Exclude
    public void setClimbStatus(String climbStatus)
    {
        this.climbStatus = climbStatus;
        notifyChange();
    }

    //endregion
    //region Climb Method
    String climbMethod = "";

    /**
     * Returns the method of the climb upon a
     * successful climb
     */
    @Exclude
    @Bindable
    public String getClimbMethod()
    {
        return climbMethod;
    }

    /**
     * Set the method of the climb upon a
     * successful climb
     */
    @Exclude
    public void setClimbMethod(String climbMethod)
    {
        this.climbMethod = climbMethod;
        notifyChange();
    }
    //endregion
    //endregion
    //endregion

    //region Constructors
    public TeamMatchData(int teamNumber, int matchNumber)
    {
        this.teamNumber = teamNumber;
        this.matchNumber = matchNumber;
        mDirty = false;
    }

    public TeamMatchData()
    {}
    //endregion

    @Exclude
    public String error()
    {
        if(scoutName.isEmpty())
        {
            return "No scout name";
        }

        if(climbStatus.equals(Constants.MatchScouting.Climb.Status.CLIMB) && climbMethod.isEmpty())
        {
            return "Climb status set to 'Climb', but no method is selected";
        }

        if(!noShow && startLocationX == 0 && startLocationY == 0)
        {
            return "Start location not set, but robot not marked as no show";
        }

        return "";
    }

}
