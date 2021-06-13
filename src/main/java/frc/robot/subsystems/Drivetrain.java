package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.util.Gyro;

import static frc.robot.Constants.*;
import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Drivetrain.*;

public class Drivetrain extends SnailSubsystem {

    private CANSparkMax frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    private CANPIDController leftPID, rightPID;
    private PIDController distancePID, anglePID;
    private CANEncoder leftEncoder, rightEncoder;

    private DifferentialDriveKinematics driveKinematics;

    public enum State {
        MANUAL,
        VELOCITY,
        DRIVE_DIST,
        TURN
    }
    private State defaultState = State.MANUAL;
    private State state = defaultState;

    private double distSetpoint; // current distance setpoint in meters
    private double angleSetpoint; // current angle setpoint in deg

    // default value for when the setpoint is not set. Deliberately set low to avoid skewing graphs
    private final double defaultSetpoint = -1.257;
    
    private double speedForward;
    private double speedTurn;

    private boolean reversed;
    private boolean slowTurn;

    public Drivetrain() {
        // Motors
        frontLeftMotor = new CANSparkMax(DRIVE_FRONT_LEFT, MotorType.kBrushless);
        frontRightMotor = new CANSparkMax(DRIVE_FRONT_RIGHT, MotorType.kBrushless);
        backLeftMotor = new CANSparkMax(DRIVE_BACK_LEFT, MotorType.kBrushless);
        backRightMotor = new CANSparkMax(DRIVE_BACK_RIGHT, MotorType.kBrushless);

        frontLeftMotor.restoreFactoryDefaults();
        frontRightMotor.restoreFactoryDefaults();
        backLeftMotor.restoreFactoryDefaults();
        backRightMotor.restoreFactoryDefaults();

        frontLeftMotor.setIdleMode(IdleMode.kBrake);
        frontRightMotor.setIdleMode(IdleMode.kBrake);
        backLeftMotor.setIdleMode(IdleMode.kCoast);
        backRightMotor.setIdleMode(IdleMode.kCoast);

        frontLeftMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);
        frontRightMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);
        backLeftMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);
        backRightMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);

        backLeftMotor.follow(frontLeftMotor);
        backRightMotor.follow(frontRightMotor);

        // Encoders
        leftEncoder = frontLeftMotor.getEncoder();
        rightEncoder = frontRightMotor.getEncoder();

        leftEncoder.setPositionConversionFactor(Math.PI * DRIVE_WHEEL_DIAM_M / DRIVE_GEARBOX_REDUCTION);
        rightEncoder.setPositionConversionFactor(Math.PI * DRIVE_WHEEL_DIAM_M / DRIVE_GEARBOX_REDUCTION);
        leftEncoder.setVelocityConversionFactor(Math.PI * DRIVE_WHEEL_DIAM_M / DRIVE_GEARBOX_REDUCTION / 60.0);
        rightEncoder.setVelocityConversionFactor(Math.PI * DRIVE_WHEEL_DIAM_M / DRIVE_GEARBOX_REDUCTION / 60.0);
        
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
        
        // Controllers
        leftPID = frontLeftMotor.getPIDController();
        rightPID = frontRightMotor.getPIDController();

        leftPID.setP(DRIVE_VEL_LEFT_P, DRIVE_VEL_SLOT);
        leftPID.setFF(DRIVE_VEL_LEFT_F, DRIVE_VEL_SLOT);
        rightPID.setP(DRIVE_VEL_RIGHT_P, DRIVE_VEL_SLOT);
        rightPID.setFF(DRIVE_VEL_RIGHT_F, DRIVE_VEL_SLOT);
    }

    private void reset() {
        state = defaultState;
        reversed = false;
        slowTurn = false;
    }

    public void update() {
        switch (state) {
            case MANUAL: {
                double adjustedSpeedForward = reversed ? -speedForward : speedForward;
                double adjustedSpeedTurn = slowTurn ? speedTurn * DRIVE_SLOW_TURN_MULT : speedTurn;
              
                double[] arcadeSpeeds = arcadeDrive(adjustedSpeedForward, adjustedSpeedTurn);
                frontLeftMotor.set(arcadeSpeeds[0]);
                frontRightMotor.set(arcadeSpeeds[1]);
                break;
            }
            case VELOCITY: {
                double adjustedSpeedForward = reversed ? -speedForward : speedForward;
                double adjustedSpeedTurn = slowTurn ? speedTurn * DRIVE_SLOW_TURN_MULT : speedTurn;

                // apply negative sign to turn speed because WPILib uses left as positive
                ChassisSpeeds chassisSpeeds = new ChassisSpeeds(adjustedSpeedForward, 0, Math.toRadians(-adjustedSpeedTurn));
                DifferentialDriveWheelSpeeds wheelSpeeds = driveKinematics.toWheelSpeeds(chassisSpeeds);

                leftPID.setReference(wheelSpeeds.leftMetersPerSecond, ControlType.kVelocity, DRIVE_VEL_SLOT);
                rightPID.setReference(wheelSpeeds.rightMetersPerSecond, ControlType.kVelocity, DRIVE_VEL_SLOT);
                break;
            }
            case DRIVE_DIST: {
                if(distSetpoint == defaultSetpoint || angleSetpoint == defaultSetpoint) {
                    state = defaultState;
                    break;
                }

                // comment this out while initially tuning
                if(distancePID.atSetpoint()) {
                    state = defaultState;
                    distSetpoint = defaultSetpoint;
                    angleSetpoint = defaultSetpoint;
                    break;
                }

                double forwardOutput = distancePID.calculate(leftEncoder.getPosition(), distSetpoint);
                forwardOutput = MathUtil.clamp(forwardOutput, -DRIVE_DIST_MAX_OUTPUT, DRIVE_DIST_MAX_OUTPUT);
                double turnOutput = (angleSetpoint - Gyro.getInstance().getRobotAngle()) * DRIVE_DIST_ANGLE_P;

                double[] arcadeSpeeds = arcadeDrive(forwardOutput, turnOutput);
                frontLeftMotor.set(arcadeSpeeds[0]);
                frontRightMotor.set(arcadeSpeeds[1]);
                break;
            }
            case TURN: {
                if(angleSetpoint == defaultSetpoint) {
                    state = defaultState;
                    break;
                }

                // comment this out while initially tuning
                if(anglePID.atSetpoint()) {
                    state = defaultState;
                    angleSetpoint = defaultSetpoint;
                    break;
                }

                double turnOutput = anglePID.calculate(Gyro.getInstance().getRobotAngle(), angleSetpoint);
                turnOutput = MathUtil.clamp(turnOutput, -DRIVE_ANGLE_MAX_OUTPUT, DRIVE_ANGLE_MAX_OUTPUT);

                double[] arcadeSpeeds = arcadeDrive(0, turnOutput);
                frontLeftMotor.set(arcadeSpeeds[0]);
                frontRightMotor.set(arcadeSpeeds[1]);
                break;
            }
        }
    }

    public double[] arcadeDrive(double speedForward, double speedTurn) {
        double forward = Math.copySign(speedForward * speedForward, speedForward);
        double turn = Math.copySign(speedTurn * speedTurn, speedTurn);

        if (Math.abs(forward) < 0.02) forward = 0.0;
        if (Math.abs(turn) < 0.02) turn = 0.0;

        double maxInput = Math.copySign(Math.max(Math.abs(forward), Math.abs(turn)), forward);
        
        double speedLeft;
        double speedRight;

        if (forward >= 0.0) {
            if (turn >= 0.0) {
                speedLeft = maxInput;
                speedRight = forward - turn;
            }
            else {
                speedLeft = forward + turn;
                speedRight = maxInput;
            }
        } 
        else {
            if (turn >= 0.0) {
                speedLeft = forward + turn;
                speedRight = maxInput;
            }
            else {
                speedLeft = maxInput;
                speedRight = forward - turn;
            }
        }

        return new double[] {speedLeft, speedRight};
    }

    public void manualDrive(double speedForward, double speedTurn) {
        this.speedForward = speedForward;
        this.speedTurn = speedTurn;

        state = State.MANUAL;
	}

    public void velocityDrive(double speedForward, double speedTurn) {
        this.speedForward = speedForward;
        this.speedTurn = speedTurn;

        defaultState = State.VELOCITY;
        state = State.VELOCITY;
    }

    public void driveDistance(double distance) {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
        Gyro.getInstance().zeroRobotAngle();
        
        distSetpoint = distance;
        angleSetpoint = 0;
        distancePID.reset();

        state = State.DRIVE_DIST;
    }

    public void turnAngle(double angle) {
        Gyro.getInstance().zeroRobotAngle();

        angleSetpoint = angle;
        anglePID.reset();

        state = State.TURN;
    }

    public void toggleReverse() {
        reversed = !reversed;
    }

    public void toggleSlowTurn() {
        slowTurn = !slowTurn;
    }

    public void endPID() {
        distSetpoint = defaultSetpoint;
        angleSetpoint = defaultSetpoint;
        state = defaultState;
    }

    public State getState() {
        return state;
    }

    @Override
    public void displayShuffleboard() {

    }

    @Override
    public void tuningInit() {

    }

    @Override
    public void tuningPeriodic() {

    }
}
