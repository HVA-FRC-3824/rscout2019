package frc3824.rscout2018.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import frc3824.rscout2018.buses.ToastBus;
import frc3824.rscout2018.buses.ToastRequest;
import frc3824.rscout2018.database.Database;
import frc3824.rscout2018.database.data_models.DataModel;
import frc3824.rscout2018.database.data_models.MatchData;
import frc3824.rscout2018.database.data_models.MatchLogistics;
import frc3824.rscout2018.database.data_models.SuperMatchData;
import frc3824.rscout2018.database.data_models.TeamLogistics;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.TeamPitData;
import frc3824.rscout2018.utilities.Constants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.lang.String.format;

/**
 * @author frc3824
 */

public class CommunicationService extends IntentService
{
    OkHttpClient mClient;
    String mUrl;
    private static final MediaType kTXT = MediaType.parse("application/txt; charset=utf-8");
    private static final MediaType kIMAGE = MediaType.parse("image/png; charset=utf-8");
    private static final MediaType kJSON = MediaType.parse("application/json; charset=utf-8");
    static Set<TeamMatchData> mTmdQueue = null;
    static Set<SuperMatchData> mSmdQueue;
    static Set<TeamPitData> mTpdQueue;
    static Gson mGson;

    /**
     * Constructor
     */
    public CommunicationService()
    {
        super("Comms Service");
        mClient = new OkHttpClient();
        if(mTmdQueue == null)
        {
            mTmdQueue = new HashSet<>();
            mSmdQueue = new HashSet<>();
            mTpdQueue = new HashSet<>();
            mGson = new GsonBuilder()
                    .setExclusionStrategies(new DataModel.DataModelExclusionStrategy())
                    .create();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        updateUrl();
    }

    private void updateUrl()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = sharedPreferences.getString(Constants.Settings.SERVER_IP, "127.0.0.1");
        String port = sharedPreferences.getString(Constants.Settings.SERVER_PORT, "38241");
        if(!url.isEmpty() && !port.isEmpty())
        {
            mUrl = format(Locale.US, "http://%s:%s", url, port);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if (intent.hasExtra(Constants.IntentExtras.NextPageOptions.MATCH_SCOUTING))
        {
            putMatchData(intent.getIntExtra(Constants.IntentExtras.MATCH_NUMBER, -1),
                         intent.getIntExtra(Constants.IntentExtras.TEAM_NUMBER, -1));
        }
        else if (intent.hasExtra(Constants.IntentExtras.NextPageOptions.PIT_SCOUTING))
        {
            //
        }
        else if (intent.hasExtra(Constants.IntentExtras.NextPageOptions.SUPER_SCOUTING))
        {
            putSuperData(intent.getIntExtra(Constants.IntentExtras.MATCH_NUMBER, -1));
        }
        else if (intent.hasExtra(Constants.IntentExtras.DOWNLOAD_SCHEDULE))
        {
            getSchedule();
        }
        else if (intent.hasExtra(Constants.IntentExtras.UPLOAD_PIT_DATA))
        {
            putAllPitData();
        }
        else if (intent.hasExtra(Constants.IntentExtras.UPLOAD_SUPER_DATA))
        {
            putAllSuperData();
        }
        else if (intent.hasExtra(Constants.IntentExtras.UPLOAD_MATCH_DATA))
        {
            putAllMatchData();
        }
        else if (intent.hasExtra(Constants.IntentExtras.DOWNLOAD_TEAMS))
        {
            getTeams();
        }
        else if (intent.hasExtra(Constants.IntentExtras.DOWNLOAD_PIT_DATA))
        {
            getPitData();
        }
        else if(intent.hasExtra(Constants.IntentExtras.DOWNLOAD_PICTURES))
        {
            getPictures();
        }
        else if (intent.hasExtra(Constants.IntentExtras.DOWNLOAD_MATCH_DATA))
        {
            getMatchData();
        }
        else if (intent.hasExtra(Constants.IntentExtras.IP_MODIFIED))
        {
            updateUrl();
        }
        else if( intent.hasExtra(Constants.IntentExtras.PING))
        {
            getPing();
        }
    }

    private void getPing()
    {
        Request request = new Request.Builder()
                .url(mUrl + "/ping")
                .get()
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Pong",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS));
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Ping failed",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
        }
    }

    private void putAllPitData()
    {
        ArrayList<TeamPitData> teams = Database.getInstance().getAllTeamPitData();
        RequestBody body = RequestBody.create(kJSON, mGson.toJson(teams));

        Request request = new Request.Builder()
                .url(mUrl + "/updateTeamPitDataList")
                .post(body)
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("All Pit Data Saved",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS));
                // If success then start unloading the queue
                unloadQueue();
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR));
        }
    }

    private void putMatchData(int matchNumber, int teamNumber)
    {
        if(matchNumber == -1 || teamNumber == -1)
        {
            ToastBus.getInstance().publish(new ToastRequest("Incorrect intent parameters",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
            return;
        }

        TeamMatchData teamMatchData = Database.getInstance().getTeamMatchData(teamNumber, matchNumber);
        String test = mGson.toJson(teamMatchData);
        RequestBody body = RequestBody.create(kJSON, mGson.toJson(teamMatchData));

        Request request = new Request.Builder()
                .url(mUrl + "/updateTeamMatchData")
                .post(body)
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Team Match Data Saved",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS));
                // If success then start unloading the queue
                unloadQueue();
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
                // If failure then add to the queue for when connection is back
                mTmdQueue.add(teamMatchData);
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
            // If failure then add to the queue for when connection is back
            mTmdQueue.add(teamMatchData);
        }
    }

    private void putAllMatchData()
    {
        ArrayList<TeamMatchData> allTeamMatchData = Database.getInstance().getAllTeamMatchData();
        RequestBody body = RequestBody.create(kJSON, mGson.toJson(allTeamMatchData));

        Request request = new Request.Builder()
                .url(mUrl + "/updateTeamMatchDataList")
                .post(body)
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("All Team Match Data Saved",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS));
                // If success then start unloading the queue
                unloadQueue();
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR));
        }
    }

    private void putAllSuperData()
    {
        ArrayList<SuperMatchData> allSuperMatchData = Database.getInstance().getAllSuperMatchData();
        RequestBody body = RequestBody.create(kJSON, mGson.toJson(allSuperMatchData));

        Request request = new Request.Builder()
                .url(mUrl + "/updateSuperMatchDataList")
                .post(body)
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("All Super Match Data Saved",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS));
                // If success then start unloading the queue
                unloadQueue();
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR));
        }
    }

    private void putSuperData(int matchNumber)
    {
        if(matchNumber == -1)
        {
            ToastBus.getInstance().publish(new ToastRequest("Incorrect intent parameters",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
            return;
        }

        SuperMatchData superMatchData = Database.getInstance().getSuperMatchData(matchNumber);
        RequestBody body = RequestBody.create(kJSON, mGson.toJson(superMatchData));

        Request request = new Request.Builder()
                .url(mUrl + "/updateSuperMatchData")
                .post(body)
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Super Match Data Saved",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS));
                // If success then start unloading the queue
                unloadQueue();
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
                // If failure then add to the queue for when connection is back
                mSmdQueue.add(superMatchData);
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
            // If failure then add to the queue for when connection is back
            mSmdQueue.add(superMatchData);
        }
    }

    private void unloadQueue()
    {
        // If nothing in either queue then nothing to send
        if (mTmdQueue.isEmpty() && mSmdQueue.isEmpty() && mTpdQueue.isEmpty())
        {
            return;
        }
        ToastBus.getInstance().publish(new ToastRequest("Unloading queued data",
                                                        TastyToast.LENGTH_SHORT,
                                                        TastyToast.SUCCESS));

        if (!mTmdQueue.isEmpty())
        {
            RequestBody body = RequestBody.create(kJSON, mGson.toJson(mTmdQueue));

            Request request = new Request.Builder()
                    .url(mUrl + "/updateTeamMatchDataList")
                    .post(body)
                    .build();

            try
            {
                Response response = mClient.newCall(request).execute();
                if (response.code() == 200)
                {
                    ToastBus.getInstance()
                            .publish(new ToastRequest("Team Match Data from queue Saved",
                                                      TastyToast.LENGTH_LONG,
                                                      TastyToast.SUCCESS));
                    mTmdQueue.clear();
                }
                else
                {
                    ToastBus.getInstance()
                            .publish(new ToastRequest(format(Locale.US, "Error: Response code: %d",
                                                             response.code()),
                                                      TastyToast.LENGTH_LONG,
                                                      TastyToast.ERROR));
                }
            }
            catch (IOException e)
            {
                ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }


        if (!mSmdQueue.isEmpty())
        {
            RequestBody body = RequestBody.create(kJSON, mGson.toJson(mSmdQueue));
            Request request = new Request.Builder()
                    .url(mUrl + "/updateSuperMatchDataList")
                    .post(body)
                    .build();

            try
            {
                Response response = mClient.newCall(request).execute();
                if (response.code() == 200)
                {
                    ToastBus.getInstance()
                            .publish(new ToastRequest("Super Match Data from queue Saved",
                                                      TastyToast.LENGTH_LONG,
                                                      TastyToast.SUCCESS));
                    mSmdQueue.clear();
                }
                else
                {
                    ToastBus.getInstance()
                            .publish(new ToastRequest(format(Locale.US, "Error: Response code: %d",
                                                             response.code()),
                                                      TastyToast.LENGTH_LONG,
                                                      TastyToast.ERROR));
                }
            }
            catch (IOException e)
            {
                ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }

        if (!mTpdQueue.isEmpty())
        {
            RequestBody body = RequestBody.create(kJSON, mGson.toJson(mTpdQueue));
            Request request = new Request.Builder()
                    .url(mUrl + "/updateTeamPitDataList")
                    .post(body)
                    .build();

            try
            {
                Response response = mClient.newCall(request).execute();
                if (response.code() == 200)
                {
                    ToastBus.getInstance()
                            .publish(new ToastRequest("Team Pit Data from queue Saved",
                                    TastyToast.LENGTH_LONG,
                                    TastyToast.SUCCESS));
                    mTpdQueue.clear();
                }
                else
                {
                    ToastBus.getInstance()
                            .publish(new ToastRequest(format(Locale.US, "Error: Response code: %d",
                                                             response.code()),
                                                      TastyToast.LENGTH_LONG,
                                                      TastyToast.ERROR));
                }
            }
            catch (IOException e)
            {
                ToastBus.getInstance().publish(new ToastRequest("Save Failure",
                        TastyToast.LENGTH_LONG,
                        TastyToast.ERROR));
            }
        }
    }

    private void getSchedule()
    {
        Request request = new Request.Builder()
                .url(mUrl + "/schedule")
                .get()
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Schedule Received",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS));
                ResponseBody body = response.body();
                ArrayList<MatchLogistics> matches = mGson.fromJson(body.string(), new TypeToken<ArrayList<MatchLogistics>>(){}.getType());
                for(MatchLogistics match : matches)
                {
                    Database.getInstance().updateMatchLogistics(match);
                }
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Schedule request failed",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
        }
    }

    private void getTeams()
    {
        Request request = new Request.Builder()
                .url(mUrl + "/teams")
                .get()
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Teams Received",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS));
                ResponseBody body = response.body();
                ArrayList<TeamLogistics> teams = mGson.fromJson(body.string(), new TypeToken<ArrayList<TeamLogistics>>(){}.getType());
                for(TeamLogistics team : teams)
                {
                    Database.getInstance().updateTeamLogistics(team);
                }
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Teams request failed",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
        }
    }

    private void getPitData()
    {
        Request request = new Request.Builder()
                .url(mUrl + "/pitData")
                .get()
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Pit Data Received",
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS));
                ResponseBody body = response.body();
                ArrayList<TeamLogistics> teams = mGson.fromJson(body.string(), new TypeToken<ArrayList<TeamLogistics>>(){}.getType());
                for(TeamLogistics team : teams)
                {
                    Database.getInstance().updateTeamLogistics(team);
                }
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Pit Data request failed",
                                                            TastyToast.LENGTH_LONG,
                                                            TastyToast.ERROR));
        }
    }

    private void getMatchData()
    {
        Request request = new Request.Builder()
                .url(mUrl + "/matchData")
                .get()
                .build();

        try
        {
            Response response = mClient.newCall(request).execute();
            if (response.code() == 200)
            {
                ToastBus.getInstance().publish(new ToastRequest("Match Data Received",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS));
                ResponseBody body = response.body();
                String data = body.string();
                MatchData matchData = mGson.fromJson(data, new TypeToken<MatchData>(){}.getType());
                ArrayList<TeamMatchData> teamMatchData = matchData.teamMatchData;
                ArrayList<SuperMatchData> superMatchData = matchData.superMatchData;
                for(TeamMatchData team : teamMatchData)
                {
                    Database.getInstance().updateTeamMatchData(team);
                }
                for(SuperMatchData match : superMatchData)
                {
                    Database.getInstance().updateSuperMatchData(match);
                }
            }
            else
            {
                ToastBus.getInstance().publish(new ToastRequest(format(Locale.US, "Error: Response code: %d", response.code()),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR));
            }
        }
        catch (IOException e)
        {
            ToastBus.getInstance().publish(new ToastRequest("Match Data request failed",
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR));
        }
    }

    private void getPictures()
    {
        // todo
    }
}
