package frc3824.rscout2018.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import frc3824.rscout2018.R;
import frc3824.rscout2018.database.data_models.TeamPickListItem;

public class TeamPickListItemView extends RelativeLayout implements View.OnClickListener
{
    TeamPickListItem mData;
    Button mPickedButton;

    public TeamPickListItemView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item_pick_list, this);

        mPickedButton = findViewById(R.id.picked_button);
        mPickedButton.setOnClickListener(this);
    }

    public void setData(TeamPickListItem data)
    {
        mData = data;
    }

    public TeamPickListItem getData()
    {
        return mData;
    }

    @Override
    public void onClick(View v)
    {
        if(mData.isPicked())
        {
            mData.setPicked(false);
            setBackgroundResource(R.color.Green);
        }
        else
        {
            mData.setPicked(true);
            setBackgroundResource(R.color.Red);
        }
    }
}
