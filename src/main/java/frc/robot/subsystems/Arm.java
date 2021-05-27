package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class Arm extends SnailSubsystem {
    // Declaring Motor related things
    private CANSparkMax armMotor;
    private CANEncoder armEncoder;
    private CANPIDController armPID;

    public enum States {
        Manual,
        Autonomous,
        PID
    }

    // Limit switch
    DigitalInput limitSwitchBottom;

    public States state;
    private double speed;
    private double setpoint;

    public Arm() {
        // Setting the motor
        armMotor = new CANSparkMax(Constants.Arm.ARM_MOTOR_ID, MotorType.kBrushless);
        armMotor.restoreFactoryDefaults();
        armMotor.setIdleMode(IdleMode.kBrake);
        armMotor.setSmartCurrentLimit(Constants.Arm.CURRENT_LIMIT);

        // Setting the encoder
        armEncoder = armMotor.getEncoder();
        armEncoder.setPositionConversionFactor(Constants.Arm.POSITION_CONVERSION_FACTOR); // The constant that you multiply that will tell you the distance the elevator goes up
        armEncoder.setVelocityConversionFactor(Constants.Arm.VELOCITY_CONVERSION_FACTOR); // The constant that you multiply that will tell you the velocity the elevator has in relation to the motor

        // Arm PID
        armPID = armMotor.getPIDController();
        armPID.setP(Constants.Arm.ARM_PID[0]);
        armPID.setI(Constants.Arm.ARM_PID[1]);
        armPID.setD(Constants.Arm.ARM_PID[2]);
        armPID.setOutputRange(-Constants.Arm.ARM_PID_MAX_OUTPUT, Constants.Arm.ARM_PID_MAX_OUTPUT);


        // Limit switch
        limitSwitchBottom = new DigitalInput(Constants.Arm.ARM_LIMIT_SWITCH_PORT_ID); // port defined in Constants

        // Setting the variables
        state = States.Manual; // Might wanna change this later to start on autonomous
        speed = 0;
    }

    @Override
    public void update() {
        if (speed > 0 && limitSwitchBottom.get()) {
            speed = 0;
        }

        switch (state) {
            case Manual:
                armMotor.set(speed);
                break;
            case Autonomous:
                break;
            case PID:
                armPID.setReference(setpoint, ControlType.kPosition);

                // check our error and update the state if we finish
                if(Math.abs(armEncoder.getPosition() - setpoint) < Constants.Arm.ARM_PID_TOLERANCE) {
                    state = States.Manual;
                }
            
                break;
                
            default:
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


    public void setPosition(double setpoint) {
        state = States.PID;
        this.setpoint = setpoint;
    }

    public void endPID() {
        state = States.Manual;
    }

	@Override
	public void displayShuffleboard() {
        SmartDashboard.putBoolean("Arm Limit Switch Bottom", limitSwitchBottom.get());
        SmartDashboard.putNumberArray("Arm Dist PID", new double[] 
        {armEncoder.getPosition(), setpoint});
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
