package frc3824.rscout2018.fragments.pit_scout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.flurgle.camerakit.CameraView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import frc3824.rscout2018.R;
import info.hoang8f.widget.FButton;

import static java.lang.String.format;

/**
 * @class PitPictureFragment
 * @brief Fragment for taking pictures of a team's robot
 */
public class PitPictureFragment extends PitScoutFragment implements View.OnClickListener, ImageListener
{
    ArrayList<String> mPictureFilepaths;

    View mView;
    FButton mTakePicture;
    FButton mSetDefault;
    FButton mDelete;
    FButton mCancel;
    //CameraView mCameraView;
    CarouselView mCarouselView;
    boolean mTakingPicture = false;
    String mDir;

    protected void bind()
    {
        if(mTeamPitData != null)
        {
            mPictureFilepaths = mTeamPitData.getPictureFilepaths();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_pit_picture, container, false);

        // Inflate the "Set Default" button
        mSetDefault = mView.findViewById(R.id.set_default);
        mSetDefault.setOnClickListener(this);

        // Inflate the "Take Picture" button
        mTakePicture = mView.findViewById(R.id.take_picture);
        mTakePicture.setOnClickListener(this);

        // Inflate the "Delete" button
        mDelete = mView.findViewById(R.id.delete);
        mDelete.setOnClickListener(this);

        // Inflate the "Cancel" button
        mCancel = mView.findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);
        mCancel.setVisibility(View.GONE);

        // Inflate the Camera view
        //mCameraView = mView.findViewById(R.id.camera);
        //mCameraView.setCameraListener(new CameraListener());
        //mCameraView.setVisibility(View.GONE);

        // Inflate the carousel
        mCarouselView = mView.findViewById(R.id.carousel);
        mCarouselView.setImageListener(this);

        mDir = format(Locale.US, "data/data/%s/files/robots/%d/", getContext().getPackageName(), mTeamPitData.getTeamNumber() > 0 ? mTeamPitData.getTeamNumber() : 0);

        // If there are pictures then display the default image
        if(mTeamPitData.numberOfPictures() > 0)
        {
            mPictureFilepaths = mTeamPitData.getPictureFilepaths();
            String defaultFilepath = mTeamPitData.getDefaultPictureFilepath();
            mCarouselView.setPageCount(mPictureFilepaths.size());

            // If there is a default image
            if(!defaultFilepath.isEmpty())
            {
                int index = mPictureFilepaths.indexOf(defaultFilepath);
                // Show the default image
                if(index > -1)
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
            mSetDefault.setVisibility(View.GONE);
            mDelete.setVisibility(View.GONE);
        }

        //mCameraView.start();
        return mView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        //mCameraView.stop();
    }

    @Override
    public void onClick(View view)
    {
        /*
        switch (view.getId())
        {
            // Start the camera and  hide the carousel when taking a picture
            case R.id.take_picture:
                if(mTakingPicture)
                {
                    mCameraView.captureImage();
                    mTakingPicture = false;
                }
                else
                {
                    mCancel.setVisibility(View.VISIBLE);
                    mCameraView.setVisibility(View.VISIBLE);
                    mCarouselView.setVisibility(View.GONE);
                    mSetDefault.setVisibility(View.GONE);
                    mDelete.setVisibility(View.GONE);
                    mTakingPicture = true;
                }
                break;
            // Cancel taking a picture
            case R.id.cancel:
                mCancel.setVisibility(View.GONE);
                mCameraView.setVisibility(View.GONE);
                if(mPictureFilepaths.size() > 0)
                {
                    mCarouselView.setVisibility(View.VISIBLE);
                    mSetDefault.setVisibility(View.VISIBLE);
                    mDelete.setVisibility(View.VISIBLE);
                }
                mTakingPicture = false;
                break;
            // Set the current image as the default image
            case R.id.set_default:
                mTeamPitData.setDefaultPictureFilepath(mPictureFilepaths.get(mCarouselView.getCurrentItem()));
                break;
            // Delete the current image
            case R.id.delete:

                File file = new File(mPictureFilepaths.get(mCarouselView.getCurrentItem()));
                if(file.exists())
                {
                    file.delete();
                }
                mPictureFilepaths.remove(mCarouselView.getCurrentItem());
                mCarouselView.setPageCount(mCarouselView.getPageCount() - 1);

                // Hide the carousel if there are no images
                if(mPictureFilepaths.size() == 0)
                {
                    mCarouselView.setVisibility(View.GONE);
                    mDelete.setVisibility(View.GONE);
                    mSetDefault.setVisibility(View.GONE);
                }
                break;
        }
        */
    }

    /**
     * todo Document
     * @param position
     * @param imageView
     */
    @Override
    public void setImageForPosition(int position, ImageView imageView)
    {
        if(position >= 0 && position < mPictureFilepaths.size())
        {
            Glide.with(this).load(mPictureFilepaths.get(position)).into(imageView);
        }
        // todo(Andrew): error
    }
    /*
    // todo Document
    private class CameraListener extends com.flurgle.camerakit.CameraListener
    {
        @Override
        public void onPictureTaken(byte[] picture)
        {
            super.onPictureTaken(picture);

            Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            FileOutputStream output = null;
            File directory = new File(mDir);
            if(!directory.exists())
            {
                directory.mkdirs();
            }

            File file = new File(directory, format(Locale.US, "%d.png", System.currentTimeMillis()));

            // todo: Replace all the print stack traces with something useful

            try
            {

                file.createNewFile();
                output = new FileOutputStream(file);

                result = Bitmap.createScaledBitmap(result, 640, 480, false);
                result.compress(Bitmap.CompressFormat.PNG, 100, output);
            }
            catch(Exception e)
            {
                file.delete();
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if(output != null)
                    {
                        output.flush();
                        output.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            mPictureFilepaths.add(file.getAbsolutePath());
            mCarouselView.setPageCount(mPictureFilepaths.size());
            mCarouselView.setCurrentItem(mPictureFilepaths.size() - 1);

            mCancel.setVisibility(View.GONE);
            mCameraView.setVisibility(View.GONE);
            mCarouselView.setVisibility(View.VISIBLE);
            mSetDefault.setVisibility(View.VISIBLE);
            mDelete.setVisibility(View.VISIBLE);
        }
    }
    */
}
