package frc3824.rscout2018.activities;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.sdsmdg.tastytoast.TastyToast;

import frc3824.rscout2018.buses.ToastRequest;
import io.reactivex.functions.Consumer;

/**
 * Created by frc2834
 */

public class RScoutActivity extends Activity implements Consumer<ToastRequest>
{
    private final Handler mToastMainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void accept(final ToastRequest request) throws Exception
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            TastyToast.makeText(this,
                                request.getMessage(),
                                request.getDuration(),
                                request.getType());
        }
        else
        {
            mToastMainThreadHandler.post(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TastyToast.makeText(RScoutActivity.this,
                                                request.getMessage(),
                                                request.getDuration(),
                                                request.getType());
                        }
                    }
            );
        }
    }
}
