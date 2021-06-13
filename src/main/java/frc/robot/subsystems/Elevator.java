package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Elevator.*;
import static frc.robot.Constants.*;

public class Elevator extends SnailSubsystem {

    public enum State {
        MANUAL,
        PID
    }

    private CANSparkMax elevatorMotor;
    private CANEncoder elevatorEncoder;
    private CANPIDController elevatorPID;

    private State state;
    private double speed = 0;
    private double setpoint;

    public Elevator() {
        // Setting up the motor
        // Our Elevator has only one motor i think
        elevatorMotor = new CANSparkMax(ELEVATOR_MOTOR_ID, MotorType.kBrushless);
        elevatorMotor.restoreFactoryDefaults();
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        elevatorMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);

        elevatorEncoder = elevatorMotor.getEncoder();
        elevatorEncoder.setPositionConversionFactor(POSITION_CONVERSION_FACTOR); // The constant that you multiply that will tell you the distance the elevator goes up
        elevatorEncoder.setVelocityConversionFactor(VELOCITY_CONVERSION_FACTOR); // The constant that you multiply that will tell you the velocity the elevator has in relation to the motor

        elevatorPID = elevatorMotor.getPIDController();
        elevatorPID.setP(ELEVATOR_PID[0]);
        elevatorPID.setI(ELEVATOR_PID[1]);
        elevatorPID.setD(ELEVATOR_PID[2]);
        elevatorPID.setOutputRange(-ELEVATOR_PID_MAX_OUTPUT, ELEVATOR_PID_MAX_OUTPUT);

        state = State.MANUAL; // Probably should change to Autonomous later
    }

    @Override
    public void update() {
        // special cases
        if ((speed > 0 && elevatorEncoder.getPosition() >= TOP) || 
                (speed < 0 && elevatorEncoder.getPosition() <= 0)) {
                speed = 0;
        }

        switch (state) {
            case MANUAL:
                elevatorMotor.set(speed);
                break;
            case PID:
                elevatorPID.setReference(setpoint, ControlType.kPosition);

                // check our error and update the state if we finish
                if(Math.abs(elevatorEncoder.getPosition() - setpoint) < ELEVATOR_PID_TOLERANCE) {
                    state = State.MANUAL;
                }
            
                break;
        }

        speed = 0;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        state = State.MANUAL;
    }

    public void setPosition(double setpoint) {
        state = State.PID;
        this.setpoint = setpoint;
    }

    public State getState() {
        return state;
    }

    public void endPID() {
        state = State.MANUAL;
    }

    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumberArray("Elevator Dist PID", new double[] {elevatorEncoder.getPosition(), setpoint});
    }

    @Override
    public void tuningInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void tuningPeriodic() {
        // TODO Auto-generated method stub

    }
    
}
