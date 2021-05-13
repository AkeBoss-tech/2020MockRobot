package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;

public class RollerIntake extends SnailSubsystem {

    public enum State {
        INTAKING,
        EJECTING,
        NEUTRAL
    }

    State state = State.NEUTRAL;

    private CANSparkMax intakeMotor;

    public RollerIntake() {
        intakeMotor = new CANSparkMax(Constants.Intake.INTAKE_MOTOR_ID, MotorType.kBrushless);
        intakeMotor.restoreFactoryDefaults();
        intakeMotor.setIdleMode(IdleMode.kBrake);
        intakeMotor.setSmartCurrentLimit(Constants.Intake.CURRENT_LIMIT);
    }

    @Override
    public void update() {
        switch (state) {
            case INTAKING:
                intakeMotor.set(Constants.Intake.INTAKE_SPEED);
                break;
            case EJECTING:
                intakeMotor.set(-1.0);
                break;
            case NEUTRAL:
                intakeMotor.set(0.0);    
                break;
        }
    }

    // State Changers
    public void intake() {
        state = State.INTAKING;
    }

    public void eject() {
        state = State.EJECTING;
    }

    public void neutral() {
        state = State.NEUTRAL;
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

    public State getState() {
        return state;
    }
}