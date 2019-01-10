package frc3824.rscout2018.buses;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by frc3824
 */
public class ToastBus
{
    static ToastBus mSingleton = null;
    PublishSubject<ToastRequest> mBus;

    static public ToastBus getInstance()
    {
        if(mSingleton == null)
        {
            mSingleton = new ToastBus();
        }
        return mSingleton;
    }

    private ToastBus()
    {
        mBus = PublishSubject.create();
    }

    public void publish(ToastRequest request)
    {
        mBus.onNext(request);
    }

    public void subscribe(Consumer<ToastRequest> consumer)
    {
        mBus.subscribe(consumer);
    }
}
