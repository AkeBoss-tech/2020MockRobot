package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.controller.PIDController;

import static frc.robot.Constants.*;
import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Drivetrain.*;

public class Drivetrain extends SnailSubsystem {

    private CANSparkMax frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    private CANPIDController leftPID, rightPID;
    private PIDController distancePID, anglePID;

    public enum State {
        MANUAL,
        VELOCITY,
        DRIVE_DIST,
        TURN
    }
    private State defaultState = State.MANUAL;
    private State state = defaultState;
    
    private double speedForward;
    private double speedTurn;

    private boolean reversed;
    private boolean slowTurn;

    public Drivetrain() {
        // motors
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
            case VELOCITY:

                break;
            case DRIVE_DIST:

                break;
            case TURN:

                break;
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
