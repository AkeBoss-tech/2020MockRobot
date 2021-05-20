package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;

public class Arm extends SnailSubsystem {
    // Declaring some variables
    private CANSparkMax armMotor;

    public enum States {
        Manual,
        Autonomous
    }

    public States state;
    private double speed;

    public Arm() {
        // Setting the motor
        armMotor = new CANSparkMax(Constants.Arm.ARM_MOTOR_ID, MotorType.kBrushless);
        armMotor.restoreFactoryDefaults();
        armMotor.setIdleMode(IdleMode.kBrake);
        armMotor.setSmartCurrentLimit(Constants.Arm.CURRENT_LIMIT);

        // Setting the variables
        state = States.Manual; // Might wanna change this later to start on autonomous
        speed = 0;
    }

    @Override
    public void update() {
        switch (state) {
            case Manual:
                armMotor.set(speed);
                break;
            case Autonomous:
                break;
        }
    }

    public void setSpeed(double _speed) {
        speed = _speed;
        state = States.Manual;
    }

    public States getState() {
        return state;
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
