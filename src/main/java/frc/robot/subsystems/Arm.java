package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Arm.*;
import static frc.robot.Constants.*;

public class Arm extends SnailSubsystem {
    // Declaring Motor related things
    private CANSparkMax armMotor;
    private CANEncoder armEncoder;
    private CANPIDController armPID;

    public enum State {
        MANUAL,
        AUTO,
        PID
    }
    public State state = State.MANUAL; // Might wanna change this later to start on autonomous

    // Limit switch
    private DigitalInput limitSwitchBottom;

    private double speed;
    private double setpoint;

    public Arm() {
        // Setting the motor
        armMotor = new CANSparkMax(ARM_MOTOR_ID, MotorType.kBrushless);
        armMotor.restoreFactoryDefaults();
        armMotor.setIdleMode(IdleMode.kBrake);
        armMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);

        // Setting the encoder
        armEncoder = armMotor.getEncoder();
        armEncoder.setPositionConversionFactor(POSITION_CONVERSION_FACTOR); // The constant that you multiply that will tell you the distance the elevator goes up
        armEncoder.setVelocityConversionFactor(VELOCITY_CONVERSION_FACTOR); // The constant that you multiply that will tell you the velocity the elevator has in relation to the motor

        // Arm PID
        armPID = armMotor.getPIDController();
        armPID.setP(ARM_PID[0]);
        armPID.setI(ARM_PID[1]);
        armPID.setD(ARM_PID[2]);
        armPID.setOutputRange(-ARM_PID_MAX_OUTPUT, ARM_PID_MAX_OUTPUT);

        // Limit switch
        limitSwitchBottom = new DigitalInput(ARM_LIMIT_SWITCH_PORT_ID); // port defined in Constants

        // Setting the variables
        speed = 0;
    }

    @Override
    public void update() {
        if (speed > 0 && limitSwitchBottom.get()) {
            speed = 0;
        }

        switch (state) {
            case MANUAL:
                armMotor.set(speed);
                break;
            case AUTO:
                break;
            case PID:
                armPID.setReference(setpoint, ControlType.kPosition);

                // check our error and update the state if we finish
                if(Math.abs(armEncoder.getPosition() - setpoint) < ARM_PID_TOLERANCE) {
                    state = State.MANUAL;
                }
                break;
            default:
                break;
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        state = State.MANUAL;
    }

    public State getState() {
        return state;
    }


    public void setPosition(double setpoint) {
        state = State.PID;
        this.setpoint = setpoint;
    }

    public void endPID() {
        state = State.MANUAL;
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
