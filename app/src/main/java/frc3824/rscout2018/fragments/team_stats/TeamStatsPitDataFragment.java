package frc3824.rscout2018.fragments.team_stats;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import frc3824.rscout2018.R;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.TeamPitData;
import frc3824.rscout2018.databinding.FragmentTeamStatsPitDataBinding;

/**
 * A fragment that displays the data collected during pit scouting
 */
public class TeamStatsPitDataFragment extends TeamStatsFragment implements ImageListener
{
    FragmentTeamStatsPitDataBinding mBinding = null;
    TeamPitData mTeamPitData = null;
    ArrayList<String> mPictureFilepaths = null;
    CarouselView mCarouselView = null;

    protected void bind()
    {
        mTeamPitData = Database.getInstance().getTeamPitData(mTeamNumber);
        if(mTeamPitData != null && mCarouselView != null)
        {
            mPictureFilepaths = mTeamPitData.getPictureFilepaths();

            if(mCarouselView != null)
            {
                // If there are pictures then display the default image
                if (mTeamPitData.numberOfPictures() > 0)
                {
                    String defaultFilepath = mTeamPitData.getDefaultPictureFilepath();
                    mCarouselView.setPageCount(mPictureFilepaths.size());

                    // If there is a default image
                    if (!defaultFilepath.isEmpty())
                    {
                        int index = mPictureFilepaths.indexOf(defaultFilepath);
                        // Show the default image
                        if (index > -1)
                        {
                            mCarouselView.setCurrentItem(index);
                        }
                        // if the default picture isn't in the list then show the first one
                        else
                        {
                            mCarouselView.setCurrentItem(0);
                        }
                    }
                    // if there is no default picture then show the first one
                    else
                    {
                        mCarouselView.setCurrentItem(0);
                    }
                }
                // Otherwise hide the carousel and N/A buttons
                else
                {
                    mCarouselView.setVisibility(View.GONE);
                }
            }
        }

        if(mBinding != null)
        {
            mBinding.setTpd(mTeamPitData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_team_stats_pit_data, container, false);
        View view = mBinding.getRoot();

        // Inflate the carousel
        mCarouselView = view.findViewById(R.id.carousel);
        mCarouselView.setImageListener(this);

        bind();

        return view;
    }

    @Override
    public void setImageForPosition(int position, ImageView imageView)
    {
        if(position >= 0 && position < mPictureFilepaths.size())
        {
            Glide.with(this).load(mPictureFilepaths.get(position)).into(imageView);
        }
    }
}
