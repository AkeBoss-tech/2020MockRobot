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
// TODO Tune Constants

public final class Constants {

    public static class ElectricalLayout {
        // Controller IDs
        public final static int CONTROLLER_DRIVER_ID = 0;
        public final static int CONTROLLER_OPERATOR_ID = 1;

        public final static int ARM_MOTOR_ID = 0;
        public final static int ARM_LIMIT_SWITCH_PORT_ID = 0;

        public final static int INTAKE_MOTOR_ID = 0;
        
        public final static int ELEVATOR_MOTOR_ID = 0;
    }

    public static class Autonomous {
        
    }

    public static class Intake {
        // Motor Speeds
        public final static double INTAKE_SPEED = 1.0;
        public final static double EJECT_SPEED = -1.0;
        public final static double NEUTRAL_SPEED = 0.0;
    }

    public static class Arm {       
        // PID
        public static double[] ARM_PID = new double[] {0.1, 0, 0.01};
        public static double ARM_PID_TOLERANCE = 0.1;
        public static double ARM_PID_MAX_OUTPUT = 0.7;

        // Encoder
        public static double POSITION_CONVERSION_FACTOR = 48.0 * Math.PI * 6;
        public static double VELOCITY_CONVERSION_FACTOR = 48.0 * Math.PI * 6 / 60;

        // Positions
        public static double TOP = 12; // Represents the top of the arm
        public static double DOWN = 12; // Represents the bottom of the arm
    }
    
    public static class Elevator {
        // PID
        public static double[] ELEVATOR_PID = new double[] {0.1, 0, 0.01};
        public static double ELEVATOR_PID_TOLERANCE = 0.1;
        public static double ELEVATOR_PID_MAX_OUTPUT = 0.7;

        // Encoder
        public static double POSITION_CONVERSION_FACTOR = 48.0 * Math.PI * 6;
        public static double VELOCITY_CONVERSION_FACTOR = 48.0 * Math.PI * 6 / 60;

        // Positions
        public static double TOP = 12; // Represents the top of the elevator
        public static double DOWN = 12; // Represents the bottom of the elevator
    }

    // Miscellaneous
    public static double PI = 3.14159265;
    public static double UPDATE_PERIOD = 0.010; // seconds

    public final static int NEO_CURRENT_LIMIT = 25;
}
