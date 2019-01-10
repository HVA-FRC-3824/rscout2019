package frc3824.rscout2018.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import frc3824.rscout2018.views.SavableEditText;
import frc3824.rscout2018.views.SavableNumeric;

/**
 * @class Utilities
 */
public class Utilities {
    /**
     *
     * @param activity
     * @param view
     */
    public static void setupUi(final Activity activity, View view)
    {
        // Setup touch listener for non-textbox views to hide the keyboard
        if(!(view instanceof SavableEditText) && !(view instanceof SavableNumeric) && !(view instanceof EditText))
        {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        // If layout is a container, iterate over children and seed recursion
        if(view instanceof ViewGroup)
        {
            int childCount = ((ViewGroup)view).getChildCount();
            for(int i = 0; i < childCount; i++)
            {
                View innerView = ((ViewGroup)view).getChildAt(i);
                setupUi(activity, innerView);
            }
        }
    }

    /**
     *
     * @param activity
     */
    private static void hideSoftKeyboard(Activity activity)
    {
        if(activity.getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static int dpToPixels(Context context, float dp)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap temp = BitmapFactory.decodeResource(res, resId, options);
        Bitmap scaled = Bitmap.createScaledBitmap(temp, reqWidth, reqHeight, true);
        temp.recycle();
        return scaled;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static boolean isExchange(float x)
    {
        return x < Constants.TeamStats.Cubes.EXCHANGE_THESHOLD || x > 1.0 - Constants.TeamStats.Cubes.EXCHANGE_THESHOLD;
    }

    public static boolean isScale(float x)
    {
        return x > Constants.TeamStats.Cubes.SWITCH_THRESHOlD && x < 1.0 - Constants.TeamStats.Cubes.SWITCH_THRESHOlD;
    }

    public static boolean isSwitch(float x)
    {
        return (x < Constants.TeamStats.Cubes.SWITCH_THRESHOlD || x > 1.0 - Constants.TeamStats.Cubes.SWITCH_THRESHOlD) &&
                !isExchange(x);
    }

    public static boolean isAllianceSwitch(float start_x, float x)
    {
        return isSwitch(x) && Math.abs(x - start_x) < Constants.TeamStats.Cubes.SWITCH_THRESHOlD;
    }

    public static boolean isOppSwitch(float start_x, float x)
    {
        return isSwitch(x) && Math.abs(x - start_x) > Constants.TeamStats.Cubes.SWITCH_THRESHOlD;
    }
}
