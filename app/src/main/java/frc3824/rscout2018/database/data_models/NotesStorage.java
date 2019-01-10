package frc3824.rscout2018.database.data_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by frc3824
 */
public class NotesStorage extends BaseObservable
{
    //region Match Notes
    String matchNotes;

    @Bindable public String getMatchNotes()
    {
        return matchNotes;
    }

    public void setMatchNotes(String matchNotes)
    {
        this.matchNotes = matchNotes;
        notifyChange();
    }
    //endregion
    //region Super Match Notes
    String superNotes;

    @Bindable public String getSuperNotes()
    {
        return superNotes;
    }

    public void setSuperNotes(String superNotes)
    {
        this.superNotes = superNotes;
        notifyChange();
    }
    //endregion

    public NotesStorage(){}

    public NotesStorage(String matchNotes, String superNotes)
    {
        this.matchNotes = matchNotes;
        this.superNotes = superNotes;
    }

}
