package frc3824.rscout2018.database.data_models;

import android.databinding.BaseObservable;
import android.databinding.Observable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by frc3824
 */
@IgnoreExtraProperties
public class DataModel extends BaseObservable
{
    @Exclude
    protected boolean mDirty = false;

    public static class DataModelExclusionStrategy implements ExclusionStrategy
    {

        @Override
        public boolean shouldSkipField(FieldAttributes f)
        {
            return f.getName().equals("mDirty");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz)
        {
            return false;
        }
    }

    /**
     * @returns Whether the model is different from the one in the database
     */
    @Exclude
    public boolean isDirty()
    {
        return mDirty;
    }

    /**
     * Handles when a property has been changed
     */
    class DataModelPropertyChangedCallback extends OnPropertyChangedCallback
    {

        @Override
        public void onPropertyChanged(Observable observable, int i)
        {
            mDirty = true;
        }

    }

    /**
     * Constructor
     */
    protected DataModel()
    {
        mDirty = false;
        addOnPropertyChangedCallback(new DataModelPropertyChangedCallback());
    }
}
