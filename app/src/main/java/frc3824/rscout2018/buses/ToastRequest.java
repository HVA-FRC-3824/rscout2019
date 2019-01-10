package frc3824.rscout2018.buses;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by frc3824
 */
public class ToastRequest
{
    final String message;
    final int duration;
    final int type;

    public ToastRequest(String message)
    {
        this(message, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public ToastRequest(String message, int duration)
    {
        this(message, duration, TastyToast.DEFAULT);
    }

    public ToastRequest(String message, int duration, int type)
    {
        this.message = message;
        this.duration = duration;
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getType()
    {
        return type;
    }
}
