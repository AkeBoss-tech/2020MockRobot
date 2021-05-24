package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class Elevator extends SnailSubsystem {

    public enum States {
        Manual,
        PID
    }

    private CANSparkMax elevatorMotor;
    private CANEncoder elevatorEncoder;
    private CANPIDController elevatorPID;

    private States state;
    private double speed = 0;
    private double setpoint;

    public Elevator() {
        // Setting up the motor
        // Our Elevator has only one motor i think
        elevatorMotor = new CANSparkMax(Constants.Elevator.ELEVATOR_MOTOR_ID, MotorType.kBrushless);
        elevatorMotor.restoreFactoryDefaults();
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        elevatorMotor.setSmartCurrentLimit(Constants.Elevator.CURRENT_LIMIT);

        elevatorEncoder = elevatorMotor.getEncoder();
        elevatorEncoder.setPositionConversionFactor(Constants.Elevator.POSITION_CONVERSION_FACTOR); // The constant that you multiply that will tell you the distance the elevator goes up
        elevatorEncoder.setVelocityConversionFactor(Constants.Elevator.VELOCITY_CONVERSION_FACTOR); // The constant that you multiply that will tell you the velocity the elevator has in relation to the motor

        elevatorPID = elevatorMotor.getPIDController();
        elevatorPID.setP(Constants.Elevator.ELEVATOR_PID[0]);
        elevatorPID.setI(Constants.Elevator.ELEVATOR_PID[1]);
        elevatorPID.setD(Constants.Elevator.ELEVATOR_PID[2]);
        elevatorPID.setOutputRange(-Constants.Elevator.ELEVATOR_PID_MAX_OUTPUT, Constants.Elevator.ELEVATOR_PID_MAX_OUTPUT);

        state = States.Manual; // Probably should change to Autonomous later
    }

    @Override
    public void update() {
        switch (state) {
            case Manual:
                elevatorMotor.set(speed);
                break;
            case PID:
                elevatorPID.setReference(setpoint, ControlType.kPosition);

                // check our error and update the state if we finish
                if(Math.abs(elevatorEncoder.getPosition() - setpoint) < Constants.Elevator.ELEVATOR_PID_TOLERANCE) {
                    state = States.Manual;
                }
            
                break;
        }
    }

    public void setSpeed(double _speed) {
        speed = _speed;
        state = States.Manual;
    }

    public void setPosition(double setpoint) {
        state = States.PID;
        this.setpoint = setpoint;
    }

    public States getState() {
        return state;
    }

    public void endPID() {
        state = States.Manual;
    }

    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumberArray("Elevator Dist PID", new double[] 
        {elevatorEncoder.getPosition(), setpoint});
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
