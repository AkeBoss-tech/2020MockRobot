package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ManualDriveCommand extends CommandBase {

    private Drivetrain drivetrain;
    private DoubleSupplier speedForwardSupplier;
    private DoubleSupplier speedTurnSupplier;

    public ManualDriveCommand(Drivetrain drivetrain, DoubleSupplier speedForwardSupplier,
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
        drivetrain.manualDrive(speedForwardSupplier.getAsDouble(), speedTurnSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}