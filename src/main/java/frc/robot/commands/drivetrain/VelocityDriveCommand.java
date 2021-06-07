package frc.robot.commands.drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class VelocityDriveCommand extends CommandBase {
    
    private final Drivetrain drivetrain;
    private final DoubleSupplier speedForwardSupplier;
    private final DoubleSupplier speedTurnSupplier;

    public VelocityDriveCommand(Drivetrain drivetrain, DoubleSupplier speedForwardSupplier,
        DoubleSupplier speedTurnSupplier) {

        this.drivetrain = drivetrain;
        this.speedForwardSupplier = speedForwardSupplier;
        this.speedTurnSupplier = speedTurnSupplier;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        drivetrain.velocityDrive(speedForwardSupplier.getAsDouble(), speedTurnSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}