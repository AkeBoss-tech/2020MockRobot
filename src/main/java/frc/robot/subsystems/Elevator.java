package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;

public class Elevator extends SnailSubsystem {

    public enum States {
        Manual,
        Autonomous
    }

    private CANSparkMax elevatorMotor;
    private States state;
    private double speed = 0;

    public Elevator() {
        // Setting up the motor
        // Our Elevator has only one motor i think
        elevatorMotor = new CANSparkMax(Constants.Elevator.ELEVATOR_MOTOR_ID, MotorType.kBrushless);
        elevatorMotor.restoreFactoryDefaults();
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        elevatorMotor.setSmartCurrentLimit(Constants.Elevator.CURRENT_LIMIT);

        state = States.Manual; // Probably should change to Autonomous later
    }

    @Override
    public void update() {
        switch (state) {
            case Manual:
                elevatorMotor.set(speed);
                break;
            case Autonomous:
                break;
        }
    }

    public void setSpeed(double _speed) {
        speed = _speed;
        state = States.Manual;
    }

    @Override
    public void displayShuffleboard() {
        // TODO Auto-generated method stub

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
