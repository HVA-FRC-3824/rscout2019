package frc3824.rscout2018.utilities;

import android.graphics.Color;

/**
 * @author frc3824
 */
public interface Constants
{

    String APP_DATA = "appData";

    int OUR_TEAM_NUMBER = 3824;

    interface Settings
    {
        String ENABLE_MATCH_SCOUT = "enable_match_scout";
        String MATCH_SCOUT_POSITION = "match_scout_position";
        String BLUE_LEFT = "blue_left";

        String ENABLE_PIT_SCOUT = "enable_pit_scout";
        String PIT_SCOUT_POSITION = "pit_scout_position";

        String ENABLE_SUPER_SCOUT = "enable_super_scout";

        String ENABLE_STRATEGIST = "enable_strategist";

        String ENABLE_SERVER = "enable_server";
        String SERVER_IP = "server_ip";
        String SERVER_PORT = "server_port";

        String ENABLE_ADMIN = "enable_admin";

        String EVENT_KEY = "event_key";

        String[] SUPER_SCOUTS_LIST = {
                "Abigail Bradfield",
                "Steven Busby"
        };

        String[] MATCH_SCOUTS_LIST = {};

        String[] PIT_SCOUTS_LIST = {};
    }

    interface IntentExtras
    {
        interface NextPageOptions
        {
            String MATCH_SCOUTING = "match_scouting";
            String PIT_SCOUTING = "pit_scouting";
            String SUPER_SCOUTING = "super_scouting";
            String TEAM_STATS = "team_stats";
            String MATCH_PREVIEW = "match_preview";
        }

        String TEAM_NUMBER = "team_number";
        String MATCH_NUMBER = "match_number";
        String LAST_MODIFIED = "last_modified";
        String NEXT_PAGE = "next_page";
        String DRIVE_TEAM_FEEDBACK = "drive_team_feedback";
        String PIT_SCOUTING = "pit_scouting";
        String MATCH_VIEWING = "match_viewing";
        String TEAM_VIEWING = "team_viewing";
        String MATCH_PLAN_NAME = "match_plan_name";
        String SCOUTER = "scouter";

        String IP_MODIFIED = "ip_modified";
        String DOWNLOAD_FULL_UPDATE = "load_data";
        String DOWNLOAD_SCHEDULE = "pull_matches";
        String DOWNLOAD_TEAMS = "pull_teams";
        String DOWNLOAD_PIT_DATA = "pull_pit_data";
        String DOWNLOAD_MATCH_DATA = "pull_match_data";
        String UPLOAD_PIT_DATA = "upload_pit_data";
        String UPLOAD_MATCH_DATA = "upload_match_data";
        String UPLOAD_SUPER_DATA = "upload_super_data";
        String DOWNLOAD_PICTURES = "download_pictures";
        String PING = "ping";
    }

    interface Database
    {
        interface PrimaryKeys
        {
            String MATCH_LOGISTICS = "match_number";
            String TEAM_LOGISTICS = "team_number";
            String TEAM_MATCH_DATA = "id";
            String TEAM_PIT_DATA = "team_number";
            String SUPER_MATCH_DATA = "match_number";
        }
    }


    interface MatchScouting
    {
        String[] TABS = {"Start", "Auto", "Teleop", "Endgame", "Fouls", "Misc"};

        interface CubeEvents
        {
            String PICK_UP = "Picked Up";
            String PLACED = "Placed";
            String DROPPED = "Dropped";
            String LAUNCH_SUCCESS = "Launch Success";
            String LAUNCH_FAILURE = "Launch Failure";

            String[] EVENT_OPTIONS = {
                    // PICK_UP,
                    PLACED,
                    DROPPED,
                    // LAUNCH_SUCCESS,
                    LAUNCH_FAILURE
            };
        }

        interface Climb
        {
            interface Status
            {
                String NO_CLIMB_ATTEMPT = "No climb attempt";
                String PARKED_ON_PLATFORM = "Parked on platform";
                String DID_NOT_FINISH_IN_TIME = "Did not finish in time";
                String ROBOT_FELL = "Robot fell";
                String CLIMB = "Climb";

                String[] OPTIONS = {NO_CLIMB_ATTEMPT,
                                    PARKED_ON_PLATFORM,
                                    DID_NOT_FINISH_IN_TIME,
                                    ROBOT_FELL,
                                    CLIMB};
            }

            interface Method
            {
                String CLIMB_PLATFORM_LOWEST = "Parked on the platform";
                String CLIMB_PLATFORM_MEDIUM = "Climbed on the middle platform";
                String CLIMB_PLATFORM_HIGHEST = "Climbed on a highest platform";
                String CLIMB_NADA = "Did not climb on the lowest platform";
                String FOUL = "Was given a climb due to the opposing team's foul";

                String[] OPTIONS = {
                        CLIMB_PLATFORM_LOWEST,
                        CLIMB_PLATFORM_MEDIUM,
                        CLIMB_PLATFORM_HIGHEST,
                        CLIMB_NADA,
                        FOUL
                };
            }

        }
    }

    interface PitScouting
    {
        String[] TABS = {"Robot Pic", "Dimensions", "Misc"};
    }

    interface SuperScouting
    {
        String[] TABS = {"Power Ups", "Notes"};
    }

    interface TeamStats
    {
        String[] TABS = {"Charts", "Match Data", "Pit Data", "Notes", "Schedule"};

        interface Cubes
        {
            //double SHORT_DISTANCE = 0.3;
            //double MEDIUM_DISTANCE = 0.7;

            double EXCHANGE_THESHOLD = 0.10;
            double SWITCH_THRESHOlD = 0.35;
        }

        interface Climb
        {
            int[] STATUS_COLORS = new int[]{
                    Color.BLACK,
                    Color.BLUE,
                    Color.YELLOW,
                    Color.RED,
                    Color.GREEN
            };

            int[] METHOD_COLORS = new int[]{
                    Color.YELLOW,
                    Color.GRAY,
                    Color.BLUE,
                    Color.GREEN,
                    Color.RED,
                    Color.BLACK,
                    Color.CYAN,
                    Color.WHITE,
                    Color.MAGENTA
            };
        }
    }

    interface MatchPreview
    {
        String[] TABS = {"Blue", "Red"};
    }

    interface Notifications
    {

    }

    interface PickList
    {
        interface MainDropdown
        {
            String POWER_CUBES = "Power Cubes";
            String CLIMB = "Climb";
            String FOULS = "Fouls";
            String MISC = "Misc";
            String[] OPTIONS = {
                    POWER_CUBES,
                    CLIMB,
                    FOULS,
                    MISC
            };
        }

        interface SecondaryDropdown
        {

            interface PowerCubes
            {
                String ALL = "All";
                String SWITCH = "Switch";
                String AUTO_SWITCH = "Auto Switch";
                String TELEOP_SWITCH = "Teleop Switch";
                String TELEOP_SWITCH_TIME = "Teleop Switch Time";
                String SCALE = "Scale";
                String AUTO_SCALE = "Auto Scale";
                String TELEOP_SCALE = "Teleop Scale";
                String TELEOP_SCALE_TIME = "Teleop Scale Time";
                String EXCHANGE_STATION = "Exchange Station";
                String EXCHANGE_STATION_TIME = "Exchange Station Time";
                String DROP = "Drop";
                String[] OPTIONS = {
                        ALL,
                        SWITCH,
                        AUTO_SWITCH,
                        TELEOP_SWITCH,
                        TELEOP_SWITCH_TIME,
                        SCALE,
                        AUTO_SCALE,
                        TELEOP_SCALE,
                        TELEOP_SCALE_TIME,
                        EXCHANGE_STATION,
                        EXCHANGE_STATION_TIME,
                        DROP,
                };
            }

            interface Climb
            {
                String CLIMB = "Climb";
                String CLIMB_ONE = "Climb while supporting 1";
                String CLIMB_TWO = "Climb while supporting 2";
                String CLIMB_ON_OTHER = "Climb on other robot";
                String RAMP = "Ramp";
                String RAMP_ONE = "Ramp while supporting 1";
                String RAMP_TWO = "Ramp while supporting 2";
                String RAMP_ON_OTHER = "Got on other's ramp";
                String FELL = "Fell";
                String NOT_IN_TIME = "Did not make it";
                String PARKED_ON_PLATFORM = "Parked on the platform";

                String[] OPTIONS = {
                        CLIMB,
                        CLIMB_ONE,
                        CLIMB_TWO,
                        CLIMB_ON_OTHER,
                        RAMP,
                        RAMP_ONE,
                        RAMP_TWO,
                        RAMP_ON_OTHER,
                        FELL,
                        NOT_IN_TIME,
                        PARKED_ON_PLATFORM
                };
            }

            interface Fouls
            {
                String FOUL = "Normal";
                String TECH_FOUL = "Tech Fouls";
                String YELLOW_CARD = "Yellow Card";
                String RED_CARD = "Red Card";

                String[] OPTIONS = {
                        FOUL,
                        TECH_FOUL,
                        YELLOW_CARD,
                        RED_CARD
                };
            }

            interface Misc
            {
                String AUTO_CROSS = "Auto Cross";
            }
        }
    }
}
