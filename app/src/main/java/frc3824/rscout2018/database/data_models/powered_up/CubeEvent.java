package frc3824.rscout2018.database.data_models.powered_up;

/**
 * Created by frc3824.
 */
public class CubeEvent {
    //region Time
    long time;

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }
    //endregion
    //region Event
    String event;

    public String getEvent()
    {
        return event;
    }

    public void setEvent(String event)
    {
        this.event = event;
    }
    //endregion
    //region Location X
    float locationX;

    /**
     * Returns the location x of the event as a percent of the width of the field
     */
    public float getLocationX()
    {
        return locationX;
    }

    /**
     * Sets the location x of the event as a percent of the width of the field
     */
    public void setLocationX(float locationX)
    {
        this.locationX = locationX;
    }
    //endregion
    //region Location Y
    float locationY;

    /**
     * Returns the location y of the event as a percent of the depth of the field
     */
    public float getLocationY()
    {
        return locationY;
    }

    /**
     * Sets the location of y of the event as a percent of the depth of the field
     */
    public void setLocationY(float locationY)
    {
        this.locationY = locationY;
    }
    //endregion
}
