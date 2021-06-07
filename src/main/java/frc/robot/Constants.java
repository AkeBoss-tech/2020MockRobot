package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be
 * declared globally (i.e. public static). Do not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 * 
 * Each subsystem should have its own static inner class to hold its constants
 */
public final class Constants {

    public static class ElectricalLayout {
        public final static int CONTROLLER_DRIVER_ID = 0;
        public final static int CONTROLLER_OPERATOR_ID = 1;

        public final static int DRIVE_FRONT_LEFT = 1;
        public final static int DRIVE_FRONT_RIGHT = 2;
        public final static int DRIVE_BACK_LEFT = 3;
        public final static int DRIVE_BACK_RIGHT = 4;
        
    }

    public static class Autonomous {
        
    }

    public static class Drivetrain {
        public static double DRIVE_SLOW_TURN_MULT = 0.45;

        // linear position PID
        public static double[] DRIVE_DIST_PID = {3.50, 0.0, 0.0};
        public static double DRIVE_DIST_ANGLE_P = 0.1;
        public static double DRIVE_DIST_TOLERANCE = 0.01;
        public static double DRIVE_DIST_MAX_OUTPUT = 0.6;

        // angular position PID
        public static double[] DRIVE_ANGLE_PID = {0.1, 0.0, 0.006};
        public static double DRIVE_ANGLE_TOLERANCE = 0.075;
        public static double DRIVE_ANGLE_MAX_OUTPUT = 0.5;

        // Velocity PID
        public static int DRIVE_VEL_SLOT = 0;
        public static double DRIVE_VEL_LEFT_P = 0.0;
        public static double DRIVE_VEL_LEFT_F = 0.25;
        public static double DRIVE_VEL_RIGHT_P = 0.25;
        public static double DRIVE_VEL_RIGHT_F = 0.25;

        // Conversion stuff
        public static double DRIVE_WHEEL_DIAM_M = 0.1524; // meters
        public static double DRIVE_GEARBOX_REDUCTION = 10.71;
    }

    public static double PI = 3.14159265;
    public static double UPDATE_PERIOD = 0.010; // seconds

    public static int NEO_CURRENT_LIMIT = 80; // amps

}
