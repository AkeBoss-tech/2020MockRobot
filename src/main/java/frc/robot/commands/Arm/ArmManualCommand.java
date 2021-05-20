package frc.robot.commands.Arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

import java.util.function.DoubleSupplier;

public class ArmManualCommand extends CommandBase {
    // Some variables
    private final Arm arm;
    private final DoubleSupplier speedSupplier;

    public ArmManualCommand(Arm subsystem, DoubleSupplier ControllerInput) {
        this.arm = subsystem;
        this.speedSupplier = ControllerInput;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        arm.setSpeed(speedSupplier.getAsDouble());
    }

    // Extra Commands
    @Override
    public void initialize() {}

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
