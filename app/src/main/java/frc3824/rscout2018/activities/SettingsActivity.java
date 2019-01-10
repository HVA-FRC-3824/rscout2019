package frc3824.rscout2018.activities;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.fragments.settings.SettingsFragment;
import frc3824.rscout2018.utilities.Constants;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
@MakeActivityStarter
public class SettingsActivity extends AppCompatPreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public void onBackPressed()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getString(Constants.Settings.EVENT_KEY, "").isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                                   .setTitle("Error")
                                                    .setMessage("You have not set an event id.");
            builder.create().show();

            return;
        }

        if(sharedPreferences.getBoolean(Constants.Settings.ENABLE_MATCH_SCOUT, false)
                && sharedPreferences.getString(Constants.Settings.MATCH_SCOUT_POSITION, "").isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("You have not set which match scout position.");
            builder.create().show();
            return;
        }

        if(sharedPreferences.getBoolean(Constants.Settings.ENABLE_PIT_SCOUT, false)
                && sharedPreferences.getString(Constants.Settings.PIT_SCOUT_POSITION, "").isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("You have not set which pit scout position.");
            builder.create().show();
            return;
        }

        if(sharedPreferences.getBoolean(Constants.Settings.ENABLE_SERVER, false))
        {
            if(sharedPreferences.getString(Constants.Settings.SERVER_IP, "").isEmpty())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("You have not set the server's ip address.");
                builder.create().show();
                return;
            }

            if(sharedPreferences.getString(Constants.Settings.SERVER_PORT, "").isEmpty())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("You have not set the server's port number.");
                builder.create().show();
                return;
            }
        }

        super.onBackPressed();
    }
}
