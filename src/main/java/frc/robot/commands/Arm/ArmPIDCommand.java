package frc.robot.commands.Arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class ArmPIDCommand extends CommandBase {
    private final Arm arm;
    private final double setpoint;

    public ArmPIDCommand(Arm subsystem, double ControllerInput) {
        arm = subsystem;
        setpoint = ControllerInput;
        addRequirements(arm);
    }

    @Override
    public void execute() {}

    // Extra Commands
    @Override
    public void initialize() {
        arm.setPosition(setpoint);
    }

    @Override
    public void end(boolean interrupted) {
        arm.endPID();
    }

    @Override
    public boolean isFinished() {
        return arm.getState() != Arm.State.PID;
    }

}
