package frc3824.rscout2018.database.data_models;

import android.util.SparseArray;

import frc3824.rscout2018.database.Database;

/**
 * Created by frc3824.
 */
public class Team
{
    // region Team Number
    int teamNumber;

    public int getTeamNumber()
    {
        return teamNumber;
    }
    //endregion

    //region Logistics
    TeamLogistics mLogistics;

    public TeamLogistics getLogistics()
    {
        return mLogistics;
    }

    public void setLogistics(TeamLogistics logistics)
    {
        mLogistics = logistics;
    }
    //endregion
    //region Matches
    SparseArray<TeamMatchData> mMatches = new SparseArray<>();

    public SparseArray<TeamMatchData> getMatches()
    {
        return mMatches;
    }

    public TeamMatchData getMatch(int matchNumber)
    {
        return mMatches.get(matchNumber);
    }

    public void addMatch(TeamMatchData teamMatchData)
    {
        mMatches.put(teamMatchData.getMatchNumber(), teamMatchData);
    }
    //endregion
    //region Super Matches
    SparseArray<SuperMatchData> mSuperMatches = new SparseArray<>();

    public SparseArray<SuperMatchData> getSuperMatches()
    {
        return mSuperMatches;
    }

    public SuperMatchData getSuperMatch(int matchNumber)
    {
        return mSuperMatches.get(matchNumber);
    }

    public void addSuperMatch(SuperMatchData superMatchData)
    {
        mSuperMatches.put(superMatchData.getMatchNumber(), superMatchData);
    }
    //endregion
    //region Pit
    TeamPitData mPit;

    public TeamPitData getPit()
    {
        return mPit;
    }

    public void setPit(TeamPitData pit)
    {
        mPit = pit;
    }
    //endregion

    public Team(int teamNumber)
    {
        this.teamNumber = teamNumber;

        mLogistics = Database.getInstance().getTeamLogistics(teamNumber);
        mPit = Database.getInstance().getTeamPitData(teamNumber);
        mMatches = new SparseArray<>();
        mSuperMatches = new SparseArray<>();

        if(mLogistics != null)
        {
            for (int matchNumber : mLogistics.getMatchNumbers())
            {
                TeamMatchData tmd = Database.getInstance()
                                            .getTeamMatchData(teamNumber, matchNumber);
                if (tmd != null)
                {
                    addMatch(tmd);
                }

                SuperMatchData smd = Database.getInstance().getSuperMatchData(matchNumber);
                if(smd != null)
                {
                    addSuperMatch(smd);
                }
            }
        }
    }
}
