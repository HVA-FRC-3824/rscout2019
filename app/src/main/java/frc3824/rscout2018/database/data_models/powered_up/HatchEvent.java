package frc3824.rscout2018.database.data_models.powered_up;

public class HatchEvent {
    long hatchTime;
    public long getTime(){return hatchTime;}
    public void setTime(long sTime){this.hatchTime = sTime;}
    String hatchEvent;
    public String getHatchEvent(){return hatchEvent;}
    public void setHatchEvent(String sEvent){this.hatchEvent = sEvent;}
    //Migrated from CubeEvent
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
