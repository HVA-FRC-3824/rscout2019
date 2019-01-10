package frc3824.rscout2018.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import frc3824.rscout2018.R;
import frc3824.rscout2018.services.CommunicationService;
import frc3824.rscout2018.utilities.Constants;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onCreate(final Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.preferences);

        // Registers this fragment as the OnSharedPreferenceChangeListener
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * If the Ip Address or Port number for the server changes update the background thread
     * @param sharedPreferences
     * @param key Key of the preference that changed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals(Constants.Settings.SERVER_IP) || key.equals(Constants.Settings.SERVER_IP))
        {
            Context content = getActivity();
            Intent intent = new Intent(content, CommunicationService.class);
            intent.putExtra(Constants.IntentExtras.IP_MODIFIED, true);
            content.startService(intent);
        }
    }

    /**
     * Reregister when resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Deregister when paused
     */
    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
