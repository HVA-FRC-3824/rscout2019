package frc3824.rscout2018.activities;

import android.os.Bundle;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import activitystarter.MakeActivityStarter;
import frc3824.rscout2018.custom_charts.MainOption;
import frc3824.rscout2018.custom_charts.SecondaryOption;
import frc3824.rscout2018.database.data_models.LowLevelStats;
import frc3824.rscout2018.database.data_models.TeamMatchData;
import frc3824.rscout2018.database.data_models.powered_up.CubeEvent;
import frc3824.rscout2018.utilities.Constants;
import frc3824.rscout2018.utilities.Utilities;

/**
 * Created by frc3824
 */
@MakeActivityStarter
public class EventChartsActivity extends EventChartsActivityBase
{
    @Override
    public void onCreate(Bundle savedInstance)
    {

        ArrayList<SecondaryOption> secondaryOptions = new ArrayList<>();
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.ALL,
                                                 false,
                                                 new CubeAllOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.SWITCH,
                             false,
                             new CubeSwitchOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.AUTO_SWITCH,
                                                 false,
                                                 new CubeAutoSwitchOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.TELEOP_SWITCH,
                                                 false,
                                                 new CubeTeleopSwitchOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.TELEOP_SWITCH_TIME,
                                                  false,
                                                 new CubeTeleopSwitchTimeOptionFilter()));

        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.SCALE,
                                                 false,
                                                 new CubeScaleOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.AUTO_SCALE,
                                                 false,
                                                 new CubeAutoScaleOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.TELEOP_SCALE,
                                                 false,
                                                 new CubeTeleopScaleOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.TELEOP_SCALE_TIME,
                                                 false,
                                                 new CubeTeleopScaleTimeOptionFilter()));
//        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.EXCHANGE_STATION,
//                                                 false,
//                                                 new CubeExchangeStationOptionFilter()));
//        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.EXCHANGE_STATION_TIME,
//                                                 false,
//                                                 new CubeTeleopExchangeStationTimeOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.PowerCubes.DROP,
                                                 false,
                                                 new CubeDropOptionFilter()));
        mOptions.put(Constants.PickList.MainDropdown.CARGO,
                     new MainOption(this,
                                    Constants.PickList.MainDropdown.CARGO,
                                    secondaryOptions));

        secondaryOptions = new ArrayList<>();
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.Climb.CLIMB,
                             true,
                             new ClimbOptionFilter()));
        mOptions.put(Constants.PickList.MainDropdown.CLIMB, new MainOption(this, Constants.PickList.MainDropdown.CLIMB, secondaryOptions));

        secondaryOptions = new ArrayList<>();
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.Fouls.FOUL,
                                                 true,
                                                 new FoulOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.Fouls.TECH_FOUL,
                                                 true,
                                                 new TechFoulOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.Fouls.YELLOW_CARD,
                                                 true,
                                                 new YellowCardOptionFilter()));
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.Fouls.RED_CARD,
                                                 true,
                                                 new RedCardOptionFilter()));
        mOptions.put(Constants.PickList.MainDropdown.FOULS, new MainOption(this, Constants.PickList.MainDropdown.FOULS, secondaryOptions));

        secondaryOptions = new ArrayList<>();
        secondaryOptions.add(new SecondaryOption(Constants.PickList.SecondaryDropdown.Misc.AUTO_CROSS,
                                                 true,
                                                 new AutoCrossOptionFilter()));

        mOptions.put(Constants.PickList.MainDropdown.MISC, new MainOption(this, Constants.PickList.MainDropdown.MISC, secondaryOptions));

        super.onCreate(savedInstance);
    }

    //region Cube Filters
    //region Cube All Filter
    private class CubeAllOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;
                for (CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    if (cubeEvent.getEvent()
                                 .equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        total++;
                    }
                }

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (cubeEvent.getEvent()
                            .equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        total++;
                    }
                }
                list.add(total);
            }

            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Switch Filter
    // region now used for cargo switch
    private class CubeSwitchOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;
                for (CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    if (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        total++;
                    }
                }

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        total++;
                    }
                }
                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Auto Switch Filter
    private class CubeAutoSwitchOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;
                for (CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    if (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        total++;
                    }
                }

                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Teleop Switch Filter
    private class CubeTeleopSwitchOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        total++;
                    }
                }
                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Teleop Switch Time Filter
    private class CubeTeleopSwitchTimeOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Double> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                CubeEvent pickup = null;
                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (pickup != null && cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_CS))
                    {
                        double time = cubeEvent.getTime() - pickup.getTime();
                        time /= 1000.0;
                        list.add(time);
                        pickup = null;
                    }
                    else if(pickup == null && (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PICK_UP_HATCH) || cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PICK_UP_CARGO)))
                    {
                        pickup = cubeEvent;
                    }
                }
            }
            return LowLevelStats.fromDouble(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Scale Filter
    private class CubeScaleOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;
                for (CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    if ((cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW)))
                    {
                        total++;
                    }
                }

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if ((cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW)))
                    {
                        total++;
                    }
                }
                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Auto Scale Filter
    private class CubeAutoScaleOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;
                for (CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    if ((cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW)))
                    {
                        total++;
                    }
                }

                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Teleop Scale Filter
    private class CubeTeleopScaleOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if ((cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                            cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW)))
                    {
                        total++;
                    }
                }
                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Teleop Scale Time Filter
    private class CubeTeleopScaleTimeOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Double> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                CubeEvent pickup = null;
                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (pickup != null &&
                            (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW)))
                    {
                            double time = cubeEvent.getTime() - pickup.getTime();
                            time /= 1000.0;
                            list.add(time);
                        pickup = null;
                    }
                    else if(pickup == null && (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PICK_UP_HATCH) || cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PICK_UP_CARGO)))
                    {
                        pickup = cubeEvent;
                    }
                }
            }
            return LowLevelStats.fromDouble(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Teleop Exchange Station Filter
    private class CubeExchangeStationOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (((cubeEvent.getLocationX() < Constants.TeamStats.Cubes.EXCHANGE_THESHOLD ||
                            cubeEvent.getLocationX() > 1.0 - Constants.TeamStats.Cubes.EXCHANGE_THESHOLD) &&
                            (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW))))
                    {
                        total++;
                    }
                }
                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Teleop Exchange Station Time Filter
    private class CubeTeleopExchangeStationTimeOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Double> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                CubeEvent pickup = null;
                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (pickup != null &&
                            (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_HIGH) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_MEDIUM) ||
                                    cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PLACED_LOW)))
                    {
                        if(Utilities.isExchange(cubeEvent.getLocationX()))
                        {
                            double time = cubeEvent.getTime() - pickup.getTime();
                            time /= 1000.0;
                            list.add(time);
                        }
                        pickup = null;
                    }
                    else if(pickup == null && (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PICK_UP_HATCH) || cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.PICK_UP_CARGO)))
                    {
                        pickup = cubeEvent;
                    }
                }
            }
            return LowLevelStats.fromDouble(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Cube Drop Filter
    private class CubeDropOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            return 0;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (TeamMatchData tmd : tmds)
            {
                int total = 0;

                for(CubeEvent cubeEvent : tmd.getAutoCubeEvents())
                {
                    if (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.DROPPED))
                    {
                        total++;
                    }
                }

                for (CubeEvent cubeEvent : tmd.getTeleopCubeEvents())
                {
                    if (cubeEvent.getEvent().equals(Constants.MatchScouting.CubeEvents.DROPPED))
                    {
                        total++;
                    }
                }
                list.add(total);
            }
            return LowLevelStats.fromInt(list);
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Double>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                LowLevelStats lls = createLlsValue(map.get(teamNumber));
                sortValues.put(teamNumber, lls.getAverage());
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion
    // endregion

    //region Climb Filters
    //region Climb Filter
    private class ClimbOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            int total = 0;
            for(TeamMatchData tmd : tmds)
            {
                total += tmd.getClimbStatus().equals(Constants.MatchScouting.Climb.Status.CLIMB) ? 1 : 0;
            }
            return total;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            return null;
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Float>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                sortValues.put(teamNumber, createBarValue(map.get(teamNumber)));
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Float.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion
    //endregion

    //region Foul Filters
    //region Normal Foul Filter
    private class FoulOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            int total = 0;
            for(TeamMatchData tmd : tmds)
            {
                total += tmd.getFouls();
            }
            return total;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            return null;
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Float>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                sortValues.put(teamNumber, createBarValue(map.get(teamNumber)));
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Float.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Tech Foul Filter
    private class TechFoulOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            int total = 0;
            for(TeamMatchData tmd : tmds)
            {
                total += tmd.getTechFouls();
            }
            return total;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            return null;
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Float>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                sortValues.put(teamNumber, createBarValue(map.get(teamNumber)));
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Yellow Card Filter
    private class YellowCardOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            int total = 0;
            for(TeamMatchData tmd : tmds)
            {
                total += tmd.isYellowCard() ? 1 : 0;
            }
            return total;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            return null;
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Float>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                sortValues.put(teamNumber, createBarValue(map.get(teamNumber)));
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion

    //region Red Card Filter
    private class RedCardOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            int total = 0;
            for (TeamMatchData tmd : tmds)
            {
                total += tmd.isRedCard() ? 1 : 0;
            }
            return total;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            return null;
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Float>sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                sortValues.put(teamNumber, createBarValue(map.get(teamNumber)));
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion
    //endregion

    //region Misc Filters
    //region Auto Cross Filter
    private class AutoCrossOptionFilter implements SecondaryOption.Filter
    {

        @Override
        public float createBarValue(ArrayList<TeamMatchData> tmds)
        {
            int total = 0;
            for (TeamMatchData tmd : tmds)
            {
                total += tmd.getCrossedAutoLine() ? 1 : 0;
            }
            return total;
        }

        @Override
        public LowLevelStats createLlsValue(ArrayList<TeamMatchData> tmds)
        {
            return null;
        }

        @Override
        public ArrayList<Integer> sort(SparseArray<ArrayList<TeamMatchData>> map)
        {
            ArrayList<Integer> sortTeams = new ArrayList();
            for(int i = 0, end = map.size(); i < end; i++)
            {
                sortTeams.add(map.keyAt(i));
            }

            final SparseArray<Float> sortValues = new SparseArray<>();
            for (int teamNumber : sortTeams)
            {
                sortValues.put(teamNumber, createBarValue(map.get(teamNumber)));
            }

            Collections.sort(sortTeams, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return -Double.compare(sortValues.get(o1), sortValues.get(o2));
                }
            });

            return sortTeams;
        }
    }
    //endregion
    //endregion
}
