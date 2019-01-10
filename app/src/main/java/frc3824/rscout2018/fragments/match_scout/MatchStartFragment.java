package frc3824.rscout2018.fragments.match_scout;

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
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.TeamPitData;
import frc3824.rscout2018.databinding.FragmentMatchStartBinding;
import frc3824.rscout2018.utilities.Utilities;

/**
 * @author frc3824
 */
public class MatchStartFragment extends MatchScoutFragment implements ImageListener
{
    private FragmentMatchStartBinding mBinding = null;
    private CarouselView mCarouselView;
    private ArrayList<String> mPictureFilepaths;

    @Override
    public void setTeamMatchData(TeamMatchData teamMatchData)
    {
        super.setTeamMatchData(teamMatchData);
        TeamPitData tpd = Database.getInstance().getTeamPitData(teamMatchData.getTeamNumber());
        if(tpd != null)
        {
            mPictureFilepaths = tpd.getPictureFilepaths();
        }
    }

    @Override
    protected void bind()
    {
        if(mTeamMatchData != null && mBinding != null)
        {
            mBinding.setTmd(mTeamMatchData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate layout and bind the realm object
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_start, container, false);
        bind();
        View view = mBinding.getRoot();

        // Inflate the carousel
        mCarouselView = view.findViewById(R.id.carousel);
        mCarouselView.setImageListener(this);

        // Add touch listeners
        Utilities.setupUi(getActivity(), view);

        return view;
    }

    @Override
    public void setImageForPosition(int position, ImageView imageView)
    {
        if(position > 0 && position < mPictureFilepaths.size())
        {
            Glide.with(this).load(mPictureFilepaths.get(position)).into(imageView);
        }
        // todo(Andrew): error
    }
}
